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
package org.seasar.framework.convention.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.seasar.framework.convention.NamingConvention;
import org.seasar.framework.exception.EmptyRuntimeException;
import org.seasar.framework.util.ArrayUtil;
import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.Disposable;
import org.seasar.framework.util.DisposableUtil;
import org.seasar.framework.util.MapUtil;
import org.seasar.framework.util.ResourceUtil;
import org.seasar.framework.util.ResourcesUtil;
import org.seasar.framework.util.StringUtil;
import org.seasar.framework.util.ResourcesUtil.Resources;

/**
 * {@link NamingConvention}の実装クラスです。
 * 
 * @author higa
 * @author shot
 */
public class NamingConventionImpl implements NamingConvention, Disposable {

    private static final char PACKAGE_SEPARATOR = '_';

    private static final String PACKAGE_SEPARATOR_STR = "_";

    private boolean initialized;

    private String viewRootPath = "/view";

    private String viewExtension = ".html";

    private String implementationSuffix = "Impl";

    private String pageSuffix = "Page";

    private String actionSuffix = "Action";

    private String serviceSuffix = "Service";

    private String dxoSuffix = "Dxo";

    private String logicSuffix = "Logic";

    private String daoSuffix = "Dao";

    private String helperSuffix = "Helper";

    private String interceptorSuffix = "Interceptor";

    private String validatorSuffix = "Validator";

    private String converterSuffix = "Converter";

    private String dtoSuffix = "Dto";

    private String connectorSuffix = "Connector";

    private String subApplicationRootPackageName = "web";

    private String entityPackageName = "entity";

    private String[] rootPackageNames = new String[0];

    private Set hotdeployRootPackageNames = new HashSet();

    private String[] ignorePackageNames = new String[0];

    private Map existCheckerArrays = MapUtil.createHashMap();

    private Map interfaceToImplementationMap = new HashMap();

    private Map implementationToInterfaceMap = new HashMap();

    /**
     * {@link NamingConventionImpl}を作成します。
     */
    public NamingConventionImpl() {
        initialize();
    }

    /**
     * 初期化します。
     */
    public void initialize() {
        if (!initialized) {
            for (int i = 0; i < rootPackageNames.length; ++i) {
                addExistChecker(rootPackageNames[i]);
            }
            DisposableUtil.add(this);
            initialized = true;
        }
    }

    @Override
    public void dispose() {
        for (final Iterator it = existCheckerArrays.values().iterator(); it
                .hasNext();) {
            final Resources[] array = (Resources[]) it.next();
            for (int i = 0; i < array.length; ++i) {
                array[i].close();
            }
        }
        existCheckerArrays.clear();
        initialized = false;
    }

    @Override
    public String getPageSuffix() {
        return pageSuffix;
    }

    /**
     * <code>Page</code>の<code>suffix</code>を設定します。
     * 
     * @param pageSuffix pageSuffix
     */
    public void setPageSuffix(final String pageSuffix) {
        this.pageSuffix = pageSuffix;
    }

    @Override
    public String getActionSuffix() {
        return actionSuffix;
    }

    /**
     * <code>Action</code>の<code>suffix</code>を設定します。
     * 
     * @param actionSuffix actionSuffix
     */
    public void setActionSuffix(final String actionSuffix) {
        this.actionSuffix = actionSuffix;
    }

    @Override
    public String getConnectorSuffix() {
        return connectorSuffix;
    }

    /**
     * <code>Connector</code>の<code>suffix</code>を設定します。
     * 
     * @param connectorSuffix connectorSuffix
     */
    public void setConnectorSuffix(final String connectorSuffix) {
        this.connectorSuffix = connectorSuffix;
    }

    @Override
    public String getDaoSuffix() {
        return daoSuffix;
    }

    /**
     * <code>Dao</code>の<code>suffix</code>を設定します。
     * 
     * @param daoSuffix daoSuffix
     */
    public void setDaoSuffix(final String daoSuffix) {
        this.daoSuffix = daoSuffix;
    }

    @Override
    public String getDtoSuffix() {
        return dtoSuffix;
    }

    /**
     * <code>Dto</code>の<code>suffix</code>を設定します。
     * 
     * @param dtoSuffix dtoSuffix
     */
    public void setDtoSuffix(final String dtoSuffix) {
        this.dtoSuffix = dtoSuffix;
    }

    @Override
    public String getDxoSuffix() {
        return dxoSuffix;
    }

    /**
     * <code>Dxo</code>の<code>suffix</code>を設定します。
     * 
     * @param dxoSuffix dxoSuffix
     */
    public void setDxoSuffix(final String dxoSuffix) {
        this.dxoSuffix = dxoSuffix;
    }

    @Override
    public String getHelperSuffix() {
        return helperSuffix;
    }

    /**
     * <code>Helper</code>の<code>suffix</code>を設定します。
     * 
     * @param helperSuffix helperSuffix
     */
    public void setHelperSuffix(final String helperSuffix) {
        this.helperSuffix = helperSuffix;
    }

    @Override
    public String getInterceptorSuffix() {
        return interceptorSuffix;
    }

    /**
     * <code>Interceptor</code>の<code>suffix</code>を設定します。
     * 
     * @param interceptorSuffix interceptorSuffix
     */
    public void setInterceptorSuffix(final String interceptorSuffix) {
        this.interceptorSuffix = interceptorSuffix;
    }

    @Override
    public String getLogicSuffix() {
        return logicSuffix;
    }

    /**
     * <code>Logic</code>の<code>suffix</code>を設定します。
     * 
     * @param logicSuffix logicSuffix
     */
    public void setLogicSuffix(final String logicSuffix) {
        this.logicSuffix = logicSuffix;
    }

    @Override
    public String getServiceSuffix() {
        return serviceSuffix;
    }

    /**
     * <code>Service</code>の<code>suffix</code>を設定します。
     * 
     * @param serviceSuffix serviceSuffix
     */
    public void setServiceSuffix(final String serviceSuffix) {
        this.serviceSuffix = serviceSuffix;
    }

    @Override
    public String getValidatorSuffix() {
        return validatorSuffix;
    }

    /**
     * <code>Validator</code>の<code>suffix</code>を設定します。
     * 
     * @param validatorSuffix validatorSuffix
     */
    public void setValidatorSuffix(final String validatorSuffix) {
        this.validatorSuffix = validatorSuffix;
    }

    @Override
    public String getConverterSuffix() {
        return converterSuffix;
    }

    /**
     * <code>Converter</code>の<code>suffix</code>を設定します。
     * 
     * @param converterSuffix converterSuffix
     */
    public void setConverterSuffix(final String converterSuffix) {
        this.converterSuffix = converterSuffix;
    }

    @Override
    public String getImplementationSuffix() {
        return implementationSuffix;
    }

    /**
     * 実装クラスの<code>suffix</code>を設定します。
     * 
     * @param implementationSuffix implementationSuffix
     */
    public void setImplementationSuffix(final String implementationSuffix) {
        this.implementationSuffix = implementationSuffix;
    }

    @Override
    public String getDaoPackageName() {
        return fromSuffixToPackageName(daoSuffix);
    }

    @Override
    public String getDtoPackageName() {
        return fromSuffixToPackageName(dtoSuffix);
    }

    @Override
    public String getDxoPackageName() {
        return fromSuffixToPackageName(dxoSuffix);
    }

    @Override
    public String getHelperPackageName() {
        return fromSuffixToPackageName(helperSuffix);
    }

    @Override
    public String getInterceptorPackageName() {
        return fromSuffixToPackageName(interceptorSuffix);
    }

    @Override
    public String getLogicPackageName() {
        return fromSuffixToPackageName(logicSuffix);
    }

    @Override
    public String getServicePackageName() {
        return fromSuffixToPackageName(serviceSuffix);
    }

    @Override
    public String getValidatorPackageName() {
        return fromSuffixToPackageName(validatorSuffix);
    }

    @Override
    public String getConverterPackageName() {
        return fromSuffixToPackageName(converterSuffix);
    }

    @Override
    public String getConnectorPackageName() {
        return fromSuffixToPackageName(connectorSuffix);
    }

    @Override
    public String getImplementationPackageName() {
        return fromSuffixToPackageName(implementationSuffix);
    }

    @Override
    public String getEntityPackageName() {
        return entityPackageName;
    }

    /**
     * <code>Entity</code>のパッケージを設定します。
     * 
     * @param entityPackage entityPackage
     */
    public void setEntityPackage(final String entityPackage) {
        this.entityPackageName = entityPackage;
    }

    @Override
    public String getViewExtension() {
        return viewExtension;
    }

    /**
     * <code>View</code>の拡張子を設定します。
     * 
     * @param viewExtension viewExtension
     */
    public void setViewExtension(final String viewExtension) {
        this.viewExtension = viewExtension;
    }

    @Override
    public String getViewRootPath() {
        return viewRootPath;
    }

    /**
     * <code>View</code>のルートパスを設定します。
     * 
     * @param viewRootPath viewRootPath
     */
    public void setViewRootPath(final String viewRootPath) {
        this.viewRootPath = viewRootPath;
    }

    @Override
    public String adjustViewRootPath() {
        return "/".equals(viewRootPath) ? "" : viewRootPath;
    }

    @Override
    public String getSubApplicationRootPackageName() {
        return subApplicationRootPackageName;
    }

    /**
     * サブアプリケーションのルートパッケージ名を設定します。
     * 
     * @param subApplicationRootPackageName subApplicationRootPackageName
     */
    public void setSubApplicationRootPackageName(
            final String subApplicationRootPackageName) {
        this.subApplicationRootPackageName = subApplicationRootPackageName;
    }

    @Override
    public String[] getRootPackageNames() {
        return rootPackageNames;
    }

    /**
     * ルートパッケージ名を追加します。
     * 
     * @param rootPackageName
     *            ルートパッケージ
     */
    public void addRootPackageName(final String rootPackageName) {
        addRootPackageName(rootPackageName, true);
    }

    /**
     * ルートパッケージ名を追加します。
     * 
     * @param rootPackageName
     *            ルートパッケージ
     * @param hotdeploy
     *            HOT deployの対象なら<code>true</code>
     */
    public void addRootPackageName(final String rootPackageName,
            final boolean hotdeploy) {
        rootPackageNames = (String[]) ArrayUtil.add(rootPackageNames,
                rootPackageName);
        if (hotdeploy) {
            hotdeployRootPackageNames.add(rootPackageName);
        }
        addExistChecker(rootPackageName);
    }

    @Override
    public String[] getIgnorePackageNames() {
        return ignorePackageNames;
    }

    /**
     * 無視するパッケージ名を追加します。
     * 
     * @param ignorePackageName ignorePackageName
     */
    public void addIgnorePackageName(final String ignorePackageName) {
        ignorePackageNames = (String[]) ArrayUtil.add(ignorePackageNames,
                ignorePackageName);
    }

    /**
     * インターフェース名と実装クラス名の関連を追加します。
     * 
     * @param interfaceName interfaceName
     * @param implementationClassName implementationClassName
     */
    public void addInterfaceToImplementationClassName(
            final String interfaceName, final String implementationClassName) {
        interfaceToImplementationMap
                .put(interfaceName, implementationClassName);
        implementationToInterfaceMap
                .put(implementationClassName, interfaceName);
    }

    @Override
    public String fromSuffixToPackageName(final String suffix) {
        if (StringUtil.isEmpty(suffix)) {
            throw new EmptyRuntimeException("suffix");
        }
        return suffix.toLowerCase();
    }

    @Override
    public String fromClassNameToShortComponentName(final String className) {
        if (StringUtil.isEmpty(className)) {
            throw new EmptyRuntimeException("className");
        }
        String s = StringUtil.decapitalize(ClassUtil
                .getShortClassName(className));
        if (s.endsWith(implementationSuffix)) {
            return s.substring(0, s.length() - implementationSuffix.length());
        }
        return s;
    }

    @Override
    public String fromClassNameToComponentName(final String className) {
        if (StringUtil.isEmpty(className)) {
            throw new EmptyRuntimeException("className");
        }
        String cname = toInterfaceClassName(className);
        String suffix = fromClassNameToSuffix(cname);
        String middlePackageName = fromSuffixToPackageName(suffix);
        String name = null;
        for (int i = 0; i < rootPackageNames.length; ++i) {
            String prefix = rootPackageNames[i] + "." + middlePackageName + ".";
            if (cname.startsWith(prefix)) {
                name = cname.substring(prefix.length());
            }
        }
        if (StringUtil.isEmpty(name)) {
            for (int i = 0; i < rootPackageNames.length; ++i) {
                String prefix = rootPackageNames[i] + "."
                        + subApplicationRootPackageName + ".";
                if (cname.startsWith(prefix)) {
                    name = cname.substring(prefix.length());
                }
            }
            if (StringUtil.isEmpty(name)) {
                return fromClassNameToShortComponentName(className);
            }
        }
        String[] array = StringUtil.split(name, ".");
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < array.length; ++i) {
            if (i == array.length - 1) {
                buf.append(StringUtil.decapitalize(array[i]));
            } else {
                buf.append(array[i]);
                buf.append('_');
            }
        }
        return buf.toString();
    }

    @Override
    public Class fromComponentNameToClass(final String componentName) {
        if (StringUtil.isEmpty(componentName)) {
            throw new EmptyRuntimeException("componentName");
        }
        String suffix = fromComponentNameToSuffix(componentName);
        if (suffix == null) {
            return null;
        }
        String middlePackageName = fromSuffixToPackageName(suffix);
        String partOfClassName = fromComponentNameToPartOfClassName(componentName);
        boolean subAppSuffix = isSubApplicationSuffix(suffix);
        for (int i = 0; i < rootPackageNames.length; ++i) {
            String rootPackageName = rootPackageNames[i];
            if (subAppSuffix) {
                Class clazz = findClass(rootPackageName,
                        subApplicationRootPackageName, partOfClassName);
                if (clazz != null) {
                    return clazz;
                }
                clazz = findClass(rootPackageName, middlePackageName,
                        partOfClassName);
                if (clazz != null) {
                    return clazz;
                }
            } else {
                Class clazz = findClass(rootPackageName, middlePackageName,
                        partOfClassName);
                if (clazz != null) {
                    return clazz;
                }
                clazz = findClass(rootPackageName,
                        subApplicationRootPackageName, partOfClassName);
                if (clazz != null) {
                    return clazz;
                }
            }
        }
        return null;
    }

    /**
     * サブアプリケーションサフィックスかどうかを返します。
     * 
     * @param suffix
     *            サフィックス
     * @return サブアプリケーションサフィックスかどうか
     */
    protected boolean isSubApplicationSuffix(final String suffix) {
        if (pageSuffix.equals(suffix)) {
            return true;
        }
        if (dxoSuffix.equals(suffix)) {
            return true;
        }
        if (actionSuffix.equals(suffix)) {
            return true;
        }
        if (serviceSuffix.equals(suffix)) {
            return true;
        }
        return false;
    }

    /**
     * クラスを探します。
     * 
     * @param rootPackageName
     *            ルートパッケージ名
     * @param middlePackageName
     *            ミドルパッケージ名
     * @param partOfClassName
     *            クラス名の一部
     * @return クラス
     */
    protected Class findClass(final String rootPackageName,
            final String middlePackageName, final String partOfClassName) {
        initialize();

        final String backPartOfClassName = ClassUtil.concatName(
                middlePackageName, partOfClassName);
        final String className = ClassUtil.concatName(rootPackageName,
                backPartOfClassName);
        final String backPartOfImplClassName = toImplementationClassName(backPartOfClassName);
        final String implClassName = ClassUtil.concatName(rootPackageName,
                backPartOfImplClassName);

        if (!isIgnoreClassName(implClassName)
                && isExist(rootPackageName, backPartOfImplClassName)) {
            return ClassUtil.forName(implClassName);
        }

        if (!isIgnoreClassName(className)
                && isExist(rootPackageName, backPartOfClassName)) {
            return ClassUtil.forName(className);
        }
        return null;
    }

    @Override
    public String toImplementationClassName(final String className) {
        String implementationClassName = (String) interfaceToImplementationMap
                .get(className);
        if (implementationClassName != null) {
            return implementationClassName;
        }
        int index = className.lastIndexOf('.');
        if (index < 0) {
            return getImplementationPackageName() + "." + className
                    + implementationSuffix;
        }
        return className.substring(0, index) + "."
                + getImplementationPackageName() + "."
                + className.substring(index + 1) + implementationSuffix;
    }

    @Override
    public String toInterfaceClassName(final String className) {
        String interfaceClassName = (String) implementationToInterfaceMap
                .get(className);
        if (interfaceClassName != null) {
            return interfaceClassName;
        }
        if (!className.endsWith(implementationSuffix)) {
            return className;
        }
        String key = "." + getImplementationPackageName() + ".";
        int index = className.lastIndexOf(key);
        if (index < 0) {
            throw new IllegalArgumentException(className);
        }
        return className.substring(0, index)
                + "."
                + className.substring(index + key.length(), className.length()
                        - implementationSuffix.length());
    }

    @Override
    public boolean isSkipClass(final Class clazz) {
        if (clazz.isInterface()) {
            return false;
        }
        for (final Iterator it = interfaceToImplementationMap.entrySet()
                .iterator(); it.hasNext();) {
            final Entry entry = (Entry) it.next();
            final Class interfaceClass = ClassUtil.forName((String) entry
                    .getKey());
            if (interfaceClass.isAssignableFrom(clazz)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Class toCompleteClass(final Class clazz) {
        if (!clazz.isInterface()) {
            return clazz;
        }
        String className = toImplementationClassName(clazz.getName());
        if (ResourceUtil.isExist(ClassUtil.getResourcePath(className))) {
            return ClassUtil.forName(className);
        }
        return clazz;
    }

    @Override
    public String fromComponentNameToPartOfClassName(final String componentName) {
        if (componentName == null) {
            throw new EmptyRuntimeException("componentName");
        }
        String[] names = StringUtil.split(componentName, PACKAGE_SEPARATOR_STR);
        StringBuffer buf = new StringBuffer(50);
        for (int i = 0; i < names.length; ++i) {
            if (i == names.length - 1) {
                buf.append(StringUtil.capitalize(names[i]));
            } else {
                buf.append(names[i]).append(".");
            }
        }
        return buf.toString();
    }

    @Override
    public String fromComponentNameToSuffix(final String componentName) {
        return fromNameToSuffix(componentName);
    }

    @Override
    public String fromClassNameToSuffix(final String componentName) {
        return fromNameToSuffix(componentName);
    }

    /**
     * 名前をサフィックスに変換します。
     * 
     * @param name
     *            名前
     * @return サフィックス
     */
    protected String fromNameToSuffix(final String name) {
        if (StringUtil.isEmpty(name)) {
            throw new EmptyRuntimeException("name");
        }
        for (int i = name.length() - 1; i >= 0; --i) {
            if (Character.isUpperCase(name.charAt(i))) {
                return name.substring(i);
            }
        }
        return null;
    }

    @Override
    public boolean isValidViewRootPath(final String path) {
        if (!path.startsWith(viewRootPath) || !path.endsWith(viewExtension)) {
            return false;
        }
        return true;
    }

    @Override
    public String fromPathToPageName(final String path) {
        return fromPathToComponentName(path, pageSuffix);
    }

    /**
     * パスをコンポーネント名に変換します。
     * 
     * @param path
     *            パス
     * @param nameSuffix
     *            サフィックス
     * @return コンポーネント名
     */
    protected String fromPathToComponentName(final String path,
            final String nameSuffix) {
        if (!path.startsWith(viewRootPath) || !path.endsWith(viewExtension)) {
            throw new IllegalArgumentException(path);
        }
        String componentName = (path.substring(
                adjustViewRootPath().length() + 1, path.length()
                        - viewExtension.length()) + nameSuffix).replace('/',
                '_');
        int pos = componentName.lastIndexOf('_');
        if (pos == -1) {
            return StringUtil.decapitalize(componentName);
        }
        return componentName.substring(0, pos + 1)
                + StringUtil.decapitalize(componentName.substring(pos + 1));
    }

    @Override
    public String fromPathToActionName(final String path) {
        return fromPathToComponentName(path, actionSuffix);
    }

    @Override
    public String fromPageNameToPath(final String pageName) {
        if (!pageName.endsWith(pageSuffix)) {
            throw new IllegalArgumentException(pageName);
        }
        String name = pageName.substring(0, pageName.length()
                - pageSuffix.length());
        return adjustViewRootPath() + "/"
                + name.replace(PACKAGE_SEPARATOR, '/') + viewExtension;
    }

    @Override
    public String fromPageClassToPath(final Class pageClass) {
        String componentName = fromClassNameToComponentName(pageClass.getName());
        return fromPageNameToPath(componentName);
    }

    @Override
    public String fromActionNameToPath(final String actionName) {
        if (!actionName.endsWith(actionSuffix)) {
            throw new IllegalArgumentException(actionName);
        }
        String name = actionName.substring(0, actionName.length()
                - actionSuffix.length());
        return adjustViewRootPath() + "/"
                + name.replace(PACKAGE_SEPARATOR, '/') + viewExtension;
    }

    @Override
    public String fromActionNameToPageName(final String actionName) {
        if (!actionName.endsWith(actionSuffix)) {
            throw new IllegalArgumentException(actionName);
        }
        return actionName.substring(0, actionName.length()
                - actionSuffix.length())
                + pageSuffix;
    }

    @Override
    public boolean isTargetClassName(final String className, final String suffix) {
        if (isIgnoreClassName(className)) {
            return false;
        }
        if (!StringUtil.trimSuffix(className, implementationSuffix).endsWith(
                suffix)) {
            return false;
        }
        final String shortClassName = ClassUtil.getShortClassName(className);
        if (className.endsWith(implementationSuffix)
                && !className.endsWith("." + getImplementationPackageName()
                        + "." + shortClassName)) {
            return false;
        }
        final String middlePkgName = fromSuffixToPackageName(suffix);
        for (int i = 0; i < rootPackageNames.length; ++i) {
            if (className.startsWith(rootPackageNames[i] + "." + middlePkgName
                    + ".")) {
                return true;
            }
            if (className.startsWith(rootPackageNames[i] + "."
                    + subApplicationRootPackageName + ".")) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isTargetClassName(final String className) {
        if (isIgnoreClassName(className)) {
            return false;
        }
        for (int i = 0; i < rootPackageNames.length; ++i) {
            if (className.startsWith(rootPackageNames[i] + ".")) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isHotdeployTargetClassName(final String className) {
        if (isIgnoreClassName(className)) {
            return false;
        }
        for (int i = 0; i < rootPackageNames.length; ++i) {
            if (className.startsWith(rootPackageNames[i] + ".")) {
                return hotdeployRootPackageNames.contains(rootPackageNames[i]);
            }
        }
        return false;
    }

    @Override
    public boolean isIgnoreClassName(final String className) {
        for (int i = 0; i < ignorePackageNames.length; ++i) {
            if (className.startsWith(ignorePackageNames[i] + ".")) {
                return true;
            }
        }
        return false;
    }

    /**
     * 存在するかどうかを返します。
     * 
     * @param rootPackageName
     *            ルートパッケージ名
     * @param lastClassName
     *            クラス名の最後
     * @return 存在するかどうか
     */
    protected boolean isExist(final String rootPackageName,
            final String lastClassName) {
        final Resources[] checkerArray = getExistCheckerArray(rootPackageName);
        for (int i = 0; i < checkerArray.length; ++i) {
            if (checkerArray[i].isExistClass(lastClassName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 存在チェッカの配列を返します。
     * 
     * @param rootPackageName
     *            ルートパッケージ名
     * @return 存在チェッカの配列
     */
    protected Resources[] getExistCheckerArray(final String rootPackageName) {
        return (Resources[]) existCheckerArrays.get(rootPackageName);
    }

    /**
     * 存在チェッカを追加します。
     * 
     * @param rootPackageName
     *            ルートパッケージ名
     */
    protected void addExistChecker(final String rootPackageName) {
        Resources[] checkerArray = ResourcesUtil
                .getResourcesTypes(rootPackageName);
        existCheckerArrays.put(rootPackageName, checkerArray);
    }

}
