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
package org.seasar.framework.util;

import java.util.Map;

import ognl.ClassResolver;
import ognl.Ognl;
import ognl.OgnlContext;
import ognl.OgnlException;
//i import org.seasar.framework.container.S2Container;
import org.seasar.framework.exception.OgnlRuntimeException;

/**
 * Ognl用のユーティリティクラスです。
 * 
 * @author higa
 * 
 */
public class OgnlUtil {

    /**
     * インスタンスを構築します。
     */
    protected OgnlUtil() {
    }

    /**
     * 値を返します。
     * 
     * @param exp exp
     * @param root root
     * @return 値
     * @see #getValue(Object, Map, Object, String, int)
     */
    public static Object getValue(Object exp, Object root) {
        return getValue(exp, root, null, 0);
    }

    /**
     * 値を返します。
     * 
     * @param exp exp
     * @param root root
     * @param path path
     * @param lineNumber lineNumber
     * @return 値
     * @see #getValue(Object, Map, Object, String, int)
     */
    public static Object getValue(Object exp, Object root, String path,
            int lineNumber) {
        return getValue(exp, null, root, path, lineNumber);
    }

    /**
     * 値を返します。
     * 
     * @param exp exp
     * @param ctx ctx
     * @param root root
     * @return 値
     * @see #getValue(Object, Map, Object, String, int)
     */
    public static Object getValue(Object exp, OgnlContext ctx, Object root) {
        return getValue(exp, ctx, root, null, 0);
    }

    /**
     * 値を返します。
     * 
     * @param exp exp
     * @param ctx ctx
     * @param root root
     * @param path path
     * @param lineNumber lineNumber
     * @return 値
     * @throws OgnlRuntimeException
     *             OgnlExceptionが発生した場合
     */
    public static Object getValue(Object exp, OgnlContext ctx, Object root,
            String path, int lineNumber) throws OgnlRuntimeException {
        try {
            OgnlContext newCtx = addClassResolverIfNecessary(ctx, root);
            if (newCtx != null) {
                if (exp instanceof String)
                    return Ognl.getValue((String)exp, newCtx, root);
                else
                    return Ognl.getValue(exp, newCtx, root);
            } else {
                return Ognl.getValue(exp, Ognl.createDefaultContext(root, new DefaultMemberAccess(true)), root);
            }
        } catch (OgnlException ex) {
            throw new OgnlRuntimeException(ex.getReason() == null ? ex : ex
                    .getReason(), path, lineNumber);
        } catch (Exception ex) {
            throw new OgnlRuntimeException(ex, path, lineNumber);
        }
    }

    /**
     * 式を解析します。
     * 
     * @param expression expression
     * @return 解析した結果
     * @see #parseExpression(String, String, int)
     */
    public static Object parseExpression(String expression) {
        return parseExpression(expression, null, 0);
    }

    /**
     * 式を解析します。
     * 
     * @param expression expression
     * @param path path
     * @param lineNumber lineNumber
     * @return 解析した結果
     * @throws OgnlRuntimeException
     *             OgnlExceptionが発生した場合
     */
    public static Object parseExpression(String expression, String path,
            int lineNumber) throws OgnlRuntimeException {
        try {
            return Ognl.parseExpression(expression);
        } catch (Exception ex) {
            throw new OgnlRuntimeException(ex, path, lineNumber);
        }
    }

    static OgnlContext addClassResolverIfNecessary(OgnlContext ctx, Object root) {
//i        if (root instanceof S2Container) {
//i            S2Container container = (S2Container) root;
//i            ClassLoader classLoader = container.getClassLoader();
//i            if (classLoader != null) {
//i                ClassResolverImpl classResolver = new ClassResolverImpl(
//i                        classLoader);
//i                if (ctx == null) {
//i                    ctx = Ognl.createDefaultContext(root, classResolver);
//i                } else {
//i                    ctx = Ognl.addDefaultContext(root, classResolver, ctx);
//i                }
//i            }
//i        }
        ClassResolverImpl classResolver = new ClassResolverImpl(Thread.currentThread().getClass().getClassLoader());
        if (ctx == null) {
                ctx = Ognl.createDefaultContext(root, new DefaultMemberAccess(false), classResolver, null);
        } else {
                ctx = Ognl.addDefaultContext(root, classResolver, ctx);
        }

        return ctx;
    }

    /**
     * ClassResolverの実装クラスです。
     * 
     */
    public static class ClassResolverImpl implements ClassResolver {
        final private ClassLoader classLoader;

        /**
         * インスタンスを作成します。
         * 
         * @param classLoader classLoader
         */
        public ClassResolverImpl(ClassLoader classLoader) {
            this.classLoader = classLoader;
        }

        @Override
        public Class<?> classForName(String className, OgnlContext ctx)
                throws ClassNotFoundException {
            try {
                return classLoader.loadClass(className);
            } catch (ClassNotFoundException ex) {
                int dot = className.indexOf('.');
                if (dot < 0) {
                    return classLoader.loadClass("java.lang." + className);
                } else {
                    throw ex;
                }
            }
        }
    }
}
