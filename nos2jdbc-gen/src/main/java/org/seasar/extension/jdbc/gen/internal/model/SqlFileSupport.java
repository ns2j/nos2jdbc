/*
v * Copyright 2004-2015 the Seasar Foundation and the Others.
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
package org.seasar.extension.jdbc.gen.internal.model;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.jar.JarFile;

import org.seasar.extension.jdbc.DbmsDialect;
import org.seasar.extension.jdbc.gen.internal.util.FileUtil;
import org.seasar.framework.util.ClassLoaderUtil;
//i import org.seasar.framework.container.S2Container;
//i import org.seasar.framework.container.factory.SingletonS2ContainerFactory;
import org.seasar.framework.util.ClassTraversal;
import org.seasar.framework.util.ClassTraversal.ClassHandler;

/**
 * SQLファイルのサポートクラスです。
 * 
 * @author taedium
 * 
 */
public class SqlFileSupport {
//i    
    String dialectPackageName = "org.seasar.extension.jdbc.dialect";

    /**
     * SQLファイルのパスのリストを作成します。
     * 
     * @param classpathDir
     *            クラスパスのディレクトリ
     * @param sqlFileSet
     *            SQLファイルのセット
     * @return SQLファイルのパスのリスト
     */
    public List<String> createSqlFilePathList(File classpathDir,
            Set<File> sqlFileSet) {
        List<String> sqlFilePathList = new ArrayList<String>();
        Set<String> dbmsNameSet = getDbmsNameSet();
        String basePath = FileUtil.getCanonicalPath(classpathDir)
                + File.separator;

        for (File sqlFile : sqlFileSet) {
            String path = FileUtil.getCanonicalPath(sqlFile);
            if (!path.startsWith(basePath)) {
                continue;
            }
            path = path.substring(basePath.length());
            if (path.endsWith(".sql")) {
                path = path.substring(0, path.length() - 4);
            }
            for (String dbmsName : dbmsNameSet) {
                if (path.endsWith("_" + dbmsName)) {
                    path = path.substring(0, path.length() - dbmsName.length()
                            - 1);
                    break;
                }
            }
            String resourcePath = path.replace(File.separator, "/") + ".sql";
            if (sqlFilePathList.contains(resourcePath)) {
                continue;
            }
            sqlFilePathList.add(resourcePath);
        }

        Collections.sort(sqlFilePathList);
        return sqlFilePathList;
    }

    /**
     * コンテナに登録されているすべての{@link DbmsDialect}について名前のセットを返します。
     * 
     * @return {@link DbmsDialect}の名前のセット
     */
    protected Set<String> getDbmsNameSet() {
//i        if (!SingletonS2ContainerFactory.hasContainer()) {
//i            return Collections.emptySet();
//i        }
        Set<String> dbmsNameSet = new HashSet<String>();
        //String[] names = {"db2", "derby", "firebird", "h2", "hsql", "interbase", "maxdb", "mssql", "mysql",
        //	"oracle", "postgre", "sqlite", "sybase"};
//i        
//i        S2Container container = SingletonS2ContainerFactory.getContainer();
//i        DbmsDialect[] dialects = (DbmsDialect[]) container
//i                .findAllComponents(DbmsDialect.class);
//i        for (DbmsDialect dialect : dialects) {
//i            String name = dialect.getName();
//i            if (name != null) {
//i                dbmsNameSet.add(name);
//i            }
//i        }
        URL packageUrl = null;
        Iterator itr = ClassLoaderUtil.getResources(dialectPackageName.replace(".", "/"));
        while (itr.hasNext())
            packageUrl = (URL)itr.next();
        //System.out.println(packageUrl.getProtocol());
        ClassHandler ch = new MyClassHandler(dbmsNameSet);
        if ("file".equals(packageUrl.getProtocol())) {
            File f = new File(packageUrl.getFile());
            ClassTraversal.forEach(f, ch);
        } else if ("jar".equals(packageUrl.getProtocol())) {
            JarFile jf = null;
	    try {
		jf = ((JarURLConnection)packageUrl.openConnection()).getJarFile();
		ClassTraversal.forEach(jf, dialectPackageName.replace(".",  "/") + "/", ch);
	    } catch (IOException e) {
		e.printStackTrace();
	    } finally {
		try {
		    if (jf != null)
			jf.close();
		} catch (IOException e) {
		    e.printStackTrace();
		}
	    }
        }
        //for (String name: dbmsNameSet) {
        //    System.out.println(name);
        //}
        return dbmsNameSet;
    }
    
    class MyClassHandler implements ClassHandler {
	Set<String> dbmsNameSet;

	MyClassHandler(Set<String> dbmsNameSet) {
	    this.dbmsNameSet = dbmsNameSet;
	}

	public void processClass(String pn, String shortClassName) {
	    //System.out.println(dialectPackageName + ": " + shortClassName);
	    if (shortClassName.contains("$"))
		return;
	    try {
		Class<?> cls = Class.forName(dialectPackageName + "." + shortClassName);
		Object o = cls.newInstance();
		if (o instanceof DbmsDialect) {
		    DbmsDialect dialect = (DbmsDialect)o;
		    if (dialect.getName() != null)
			dbmsNameSet.add(dialect.getName());
		}
	    } catch (ClassNotFoundException e) {
		e.printStackTrace();
	    } catch (InstantiationException e) {
		e.printStackTrace();
	    } catch (IllegalAccessException e) {
		e.printStackTrace();
	    }
	}
    }
}
