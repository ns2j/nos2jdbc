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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.seasar.extension.jdbc.DbmsDialect;
import org.seasar.extension.jdbc.EntityMetaFactory;
import org.seasar.extension.jdbc.MappingContext;
import org.seasar.extension.jdbc.ResultSetHandler;

/**
 * Beanのリストを返す {@link ResultSetHandler}です。
 */
public class BeanListNonAutoResultSetHandler extends AbstractBeanNonAutoResultSetHandler {
    protected int limit;
    
    public BeanListNonAutoResultSetHandler(Class<?> beanClass, EntityMetaFactory entityMetaFactory, DbmsDialect dialect,
	    String sql, boolean shouldSetInverseField) {

        this(beanClass, entityMetaFactory, dialect, sql, 0, shouldSetInverseField);
    }

    public BeanListNonAutoResultSetHandler(Class<?> beanClass, EntityMetaFactory entityMetaFactory, DbmsDialect dialect,
	    String sql, int limit, boolean shouldSetInverseField) {
	super(beanClass, entityMetaFactory, dialect, sql, shouldSetInverseField);
	this.limit = limit;
    }

    @Override
    public Object handle(ResultSet rs) throws SQLException {
	if (entityMapper == null)
	    prepare(rs.getMetaData());
	
        List<Object> list = new ArrayList<Object>(100);
        MappingContext mappingContext = new MappingContext();
        for (int i = 0; (limit <= 0 || i < limit) && rs.next(); i++) {
            Object entity = createEntity(rs, mappingContext);
            if (entity != null) {
                list.add(entity);
            }
        }
        return list;
    }

}
