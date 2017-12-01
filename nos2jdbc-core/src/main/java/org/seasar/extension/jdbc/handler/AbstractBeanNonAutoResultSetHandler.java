/*
 * Copyright 2004-2015 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.seasar.extension.jdbc.handler;

import java.lang.reflect.Field;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.seasar.extension.jdbc.DbmsDialect;
import org.seasar.extension.jdbc.EntityMeta;
import org.seasar.extension.jdbc.EntityMetaFactory;
import org.seasar.extension.jdbc.PropertyMapper;
import org.seasar.extension.jdbc.PropertyMeta;
import org.seasar.extension.jdbc.ValueType;
import org.seasar.extension.jdbc.exception.EntityColumnNotFoundRuntimeException;
import org.seasar.extension.jdbc.mapper.AbstractEntityMapper;
import org.seasar.extension.jdbc.mapper.AbstractRelationshipEntityMapper;
import org.seasar.extension.jdbc.mapper.EntityMapperImpl;
import org.seasar.extension.jdbc.mapper.ManyToOneEntityMapperImpl;
import org.seasar.extension.jdbc.mapper.OneToManyEntityMapperImpl;
import org.seasar.extension.jdbc.mapper.OneToOneEntityMapperImpl;
import org.seasar.extension.jdbc.mapper.PropertyMapperImpl;
import org.seasar.framework.util.tiger.Pair;

public abstract class AbstractBeanNonAutoResultSetHandler extends AbstractBeanAutoResultSetHandler {
    protected Class<?> beanClass;
    protected EntityMetaFactory entityMetaFactory;
    protected DbmsDialect dialect;
    protected boolean shouldSetInverseField;

    protected Map<Class<?>, EntityMeta> classEntityMetaMap = new LinkedHashMap<Class<?>, EntityMeta>();
    protected Map<EntityMeta, List<PropertyMapper>> entityMetaPropertyMapperListMap = new HashMap<EntityMeta, List<PropertyMapper>>();
    protected Map<EntityMeta, List<Integer>> entityMetaIdListMap = new HashMap<EntityMeta, List<Integer>>();
    protected List<Class<?>> mappedClassList = new ArrayList<Class<?>>();
    
    public AbstractBeanNonAutoResultSetHandler(Class<?> beanClass, EntityMetaFactory entityMetaFactory,
	    DbmsDialect dialect, String sql, boolean shouldSetInverseField) {
	super(null, null, sql);
	this.beanClass = beanClass;
	this.entityMetaFactory = entityMetaFactory;
	this.dialect = dialect;
	this.shouldSetInverseField = shouldSetInverseField;
    }

    protected void prepareClassEntityMetaMap(Class<?> entityClass) {
	EntityMeta em = entityMetaFactory.getEntityMeta(entityClass);
	classEntityMetaMap.putIfAbsent(entityClass, em);
	for (PropertyMeta pm: em.getAllPropertyMeta()) {
	    if (pm.isRelationship() && !classEntityMetaMap.containsKey(pm.getRelationshipClass())) {
		prepareClassEntityMetaMap(pm.getRelationshipClass());
	    }
	}
    }
    
    protected Pair<EntityMeta, PropertyMeta> findProperty(String columnName) {
	for (EntityMeta em: classEntityMetaMap.values()) {
	    PropertyMeta pm = null;
	    try {
		pm = em.getColumnPropertyMeta(columnName);
	    } catch (EntityColumnNotFoundRuntimeException ecnfre) {
	    }
	    if (pm != null)
		return new Pair<EntityMeta, PropertyMeta>(em, pm);
	}
	return null;
    }
    
    public void resolveResultMetaData(ResultSetMetaData rsmd) throws SQLException {
	valueTypes = new ValueType[rsmd.getColumnCount()];
	for (int i = 0; i < rsmd.getColumnCount(); i++) {
	    Pair<EntityMeta, PropertyMeta> pair = findProperty(rsmd.getColumnLabel(i + 1));
	    if (pair == null) {
		valueTypes[i] = dialect.getValueType(Object.class, true, null);
	    } else {
		EntityMeta eMeta = pair.getFirst();
		PropertyMeta pMeta = pair.getSecond();

		valueTypes[i] = dialect.getValueType(pMeta);

		PropertyMapper pMapper = new PropertyMapperImpl(pMeta.getField(), i);
		List<PropertyMapper> pMapperList = entityMetaPropertyMapperListMap.get(eMeta);
		if (pMapperList == null) {
		    pMapperList = new ArrayList<PropertyMapper>();
		    entityMetaPropertyMapperListMap.putIfAbsent(eMeta, pMapperList);
		}
		pMapperList.add(pMapper);
		
		if (pMeta.isId()) {
		    List<Integer> idList = entityMetaIdListMap.get(eMeta);
		    if (idList == null) {
			idList = new ArrayList<Integer>();
			entityMetaIdListMap.putIfAbsent(eMeta, idList);
		    }
		    idList.add(i);
		}
	    }
	}
    }

    protected PropertyMapper[] getPropertyMappers(EntityMeta em) {
	List<PropertyMapper> pMapperList = entityMetaPropertyMapperListMap.get(em);
	if (pMapperList == null)
	    return new PropertyMapper[0];
	return pMapperList.toArray(new PropertyMapper[pMapperList.size()]);
    }
    
    protected int[] getIds(EntityMeta em) {
	List<Integer> idList = entityMetaIdListMap.get(em);
	if (idList == null)
	    return new int[0];
	int[] ids = new int[idList.size()];
        for (int i = 0; i < ids.length; i++) {
            ids[i] = idList.get(i).intValue();
        }
	return ids;
    }
    
    protected AbstractRelationshipEntityMapper createRelationship(EntityMeta parentEMeta, EntityMeta childEMeta, PropertyMeta pMeta) {
	PropertyMapper[] pMappers = getPropertyMappers(childEMeta);
	int[] ids = getIds(childEMeta);
	Field inverseField = null;
	if (shouldSetInverseField) {
	    if (pMeta.getMappedBy() != null) {
		PropertyMeta inversePMeta = childEMeta.getPropertyMeta(pMeta.getMappedBy());
		if (inversePMeta != null)
		    inverseField = inversePMeta.getField();
	    } else {
		PropertyMeta inversePMeta = childEMeta.getMappedByPropertyMeta(pMeta.getName(), parentEMeta.getEntityClass());
		if (inversePMeta != null)
		    inverseField = inversePMeta.getField();
	    }
	}
	AbstractRelationshipEntityMapper rem = null;
	switch(pMeta.getRelationshipType()) {
	case ONE_TO_MANY:
	    rem = new OneToManyEntityMapperImpl(childEMeta.getEntityClass(), pMappers, ids, pMeta.getField(), inverseField, true);
	    break;
	case MANY_TO_ONE:
	    rem = new ManyToOneEntityMapperImpl(childEMeta.getEntityClass(), pMappers, ids, pMeta.getField(), inverseField, true);
	    break;
	case ONE_TO_ONE:
	    rem = new OneToOneEntityMapperImpl(childEMeta.getEntityClass(), pMappers, ids, pMeta.getField(), inverseField, true);
	    break;
	}
	return rem;
    }    
    protected void resolveRelationships(EntityMeta parentEMeta, AbstractEntityMapper parentEMapper) {
	for (PropertyMeta pMeta: parentEMeta.getAllPropertyMeta()) {
	    if (!pMeta.isRelationship())
		continue;
	    Class<?> childClass = pMeta.getRelationshipClass();
	    if (mappedClassList.contains(childClass))
		continue;
	    EntityMeta childEMeta = classEntityMetaMap.get(childClass);
	    AbstractRelationshipEntityMapper rem = createRelationship(parentEMeta, childEMeta, pMeta);
	    if (rem == null)
		continue;
	    parentEMapper.addRelationshipEntityMapper(rem);
	    mappedClassList.add(childClass);
		
	    resolveRelationships(classEntityMetaMap.get(childClass), rem);
	}
    }
    
    protected void prepareEntityMapper() {
	EntityMeta eMeta = classEntityMetaMap.get(beanClass);
	EntityMapperImpl eMapper = new EntityMapperImpl(beanClass, getPropertyMappers(eMeta), getIds(eMeta), true);
	mappedClassList.add(beanClass);
	resolveRelationships(eMeta, eMapper);
	entityMapper = eMapper;
    }
    
    protected void prepare(ResultSetMetaData rsmd) throws SQLException {
	prepareClassEntityMetaMap(beanClass);
	resolveResultMetaData(rsmd);
	prepareEntityMapper();
    }
}
