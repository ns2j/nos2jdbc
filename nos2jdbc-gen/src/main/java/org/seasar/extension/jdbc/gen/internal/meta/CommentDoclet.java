/*
 * Copyright 2004-2015 the Seasar Foundation avnd the Others.
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
package org.seasar.extension.jdbc.gen.internal.meta;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FilterReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.util.ElementFilter;
import javax.persistence.MappedSuperclass;
import javax.tools.Diagnostic.Kind;

import org.seasar.extension.jdbc.EntityMeta;
import org.seasar.extension.jdbc.PropertyMeta;
import org.seasar.extension.jdbc.gen.internal.util.EntityMetaUtil;
import org.seasar.extension.jdbc.gen.internal.util.PropertyMetaUtil;
import org.seasar.framework.log.Logger;

import com.sun.source.doctree.DocCommentTree;
import com.sun.source.util.DocTrees;

import jdk.javadoc.doclet.Doclet;
import jdk.javadoc.doclet.DocletEnvironment;
import jdk.javadoc.doclet.Reporter;


/**
 * エンティティとプロパティのコメントを抽出する{@link Doclet}です。
 * 
 * @author taedium
 */
public class CommentDoclet implements Doclet {
    protected static Logger logger = Logger
            .getLogger(CommentDoclet.class);

    protected DocletEnvironment env;
    
    protected DocTrees docTrees;
	
	protected String charset;
    
	@Override
    public boolean run(DocletEnvironment env) {
		this.env = env;
        List<EntityMeta> entityMetaList = CommentDocletContext
                .getEntityMetaList();
        if (entityMetaList == null) {
            throw new NullPointerException("entityMetaList");
        }
        docTrees = env.getDocTrees();
        for (EntityMeta entityMeta : entityMetaList) {
            TypeElement classDoc = env.getElementUtils().getTypeElement(entityMeta.getEntityClass()
                    .getName());
            if (classDoc == null) {
                continue;
            }
    		try {
            doEntityComment(classDoc, entityMeta);
    		} catch (Exception ex) {
    			logger.warn(ex);
    			ex.printStackTrace();
    		}
        }
        return true;
    }
	
	protected String getBody(Element e) {
		if (e == null) {
			logger.warn("getBody(): Element is null");
			return "";
		}
		DocCommentTree docCommentTree = docTrees.getDocCommentTree(e);
		if (docCommentTree == null) {
			logger.debug("getBody(): DocElementTree is null: kind: " + e.getKind() + ": " + e);
			return "";
		}

		String fullBodyUnicode = "" + docCommentTree.getFullBody();
		String fullBody = null;
		try {
			fullBody = convertToOiginal(fullBodyUnicode);
		} catch (IOException e1) {
			logger.warn(e1);
			e1.printStackTrace();
		}
		logger.debug(fullBodyUnicode);
		logger.debug(fullBody);
		return fullBody;
	}

    /**
     * エンティティクラスのコメントを処理します。
     * 
     * @param classDoc TypeElement
     * @param entityMeta
     *            エンティティメタデータ
     */
    protected void doEntityComment(TypeElement classDoc,
            EntityMeta entityMeta) {
        EntityMetaUtil.setComment(entityMeta, getBody(classDoc));

        Map<String, VariableElement> fieldDocMap = getFieldDocMap(classDoc);
        for (PropertyMeta propertyMeta : entityMeta.getAllPropertyMeta()) {
            if (fieldDocMap.containsKey(propertyMeta.getName())) {
                VariableElement fieldDoc = fieldDocMap.get(propertyMeta.getName());
                doPropertyComment(fieldDoc, propertyMeta);
            }
        }
    }

    /**
     * プロパティのコメントを処理します。
     * 
     * @param fieldDoc VariableElement
     * @param propertyMeta
     *            プロパティメタデータ
     */
    protected void doPropertyComment(VariableElement fieldDoc,
            PropertyMeta propertyMeta) {
        PropertyMetaUtil.setComment(propertyMeta, getBody(fieldDoc));
    }

    /**
     * フィールド名をキー、 {@link VariableElement}を値とするマップを返します。
     * 
     * @param classDoc TypeElement
     * @return フィールド名をキー、 {@link VariableElement}を値とするマップ
     */
    protected Map<String, VariableElement> getFieldDocMap(TypeElement classDoc) {
        Map<String, VariableElement> fieldMap = new HashMap<String, VariableElement>();
        ElementFilter.fieldsIn(env.getElementUtils().getAllMembers(classDoc))
        .forEach(e -> fieldMap.put(e.getSimpleName().toString(), e));

        for (TypeElement superclassDoc = (TypeElement)((DeclaredType)classDoc.getSuperclass()).asElement();
        		!Object.class.getName().equals(superclassDoc.getQualifiedName().toString());
        		superclassDoc = (TypeElement)((DeclaredType)superclassDoc.getSuperclass()).asElement()) {
            if (isMappedSuperclass(superclassDoc)) {
                for (VariableElement fieldDoc : ElementFilter.fieldsIn(env.getElementUtils().getAllMembers(superclassDoc))) {
                    if (!fieldMap.containsKey(fieldDoc.getSimpleName().toString())) {
                        fieldMap.put(fieldDoc.getSimpleName().toString(), fieldDoc);
                    }
                }
            }
        }
        return fieldMap;
    }

    /**
     * {@link MappedSuperclass}を表す場合{@code true}を返します。
     * 
     * @param classDoc TypeElement
     * @return {@link MappedSuperclass}を表す場合{@code true}
     */
    protected boolean isMappedSuperclass(TypeElement classDoc) {
        if (classDoc.getAnnotationsByType(MappedSuperclass.class).length == 0)
        	return false;
        return true;
    }

    Reporter reporter;

    @Override
    public void init(Locale locale, Reporter reporter) {
    	reporter.print(Kind.NOTE, "Doclet using locale: " + locale);
    	this.reporter = reporter;
    }

    @Override
    public String getName() {
    	return "CommentDoclet";
    }

    @Override
    public Set<? extends Option> getSupportedOptions() {
    	Option[] options = {
    			new Option() {
    				private final List<String> someOption = Arrays.asList(
    						"--charset"
    						);

    				@Override
    				public int getArgumentCount() {
    					return 1;
    				}

    				@Override
    				public String getDescription() {
    					return "charset";
    				}

    				@Override
    				public Option.Kind getKind() {
    					return Option.Kind.STANDARD;
    				}

    				@Override
    				public List<String> getNames() {
    					return someOption;
    				}

    				@Override
    				public String getParameters() {
    					return "charset";
    				}

    				@Override
    				public boolean process(String opt, List<String> arguments) {
    					logger.debug(opt);
    					arguments.forEach(logger::debug);
    					charset = arguments.get(0);
    					return true;
    				}
    			}
    	};
    	return new HashSet<>(Arrays.asList(options));
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
    	return null;
    }

    protected String convertToOiginal(String unicode) throws IOException {
    	try (StringReader sr = new StringReader(unicode);
    			BufferedReader br = new BufferedReader(new A2NFilter(sr));
    			StringWriter sw = new StringWriter();
    			BufferedWriter bw = new BufferedWriter(sw);
    			) {
    		String line;
    		while ((line = br.readLine()) != null) {
    			bw.write(line.toCharArray());
    			bw.newLine();
    		}
    		bw.flush();
    		return sw.toString().stripTrailing();
    	}
    }

    // A copy of native2ascii A2NFilter
    class A2NFilter extends FilterReader {
    	// maintain a trailing buffer to hold any incompleted
    	// unicode escaped sequences
    	private char[] trailChars = null;
    	Charset cs = Charset.forName(charset);
    	CharsetEncoder encoder;

    	public A2NFilter(Reader in) {
    		super(in);
    		encoder = cs.newEncoder();
    	}

    	@Override
        public int read(char[] buf, int off, int len) throws IOException {
    		int numChars = 0;        // how many characters have been read
    		int retChars = 0;        // how many characters we'll return

    		char[] cBuf = new char[len];
    		boolean eof = false;

    		// copy trailing chars from previous invocation to input buffer
    		if (trailChars != null) {
    			for (int i = 0; i < trailChars.length; i++)
    				cBuf[i] = trailChars[i];
    			numChars = trailChars.length;
    			trailChars = null;
    		}

    		int n = in.read(cBuf, numChars, len - numChars);
    		if (n < 0) {
    			eof = true;
    			if (numChars == 0)
    				return -1;              // EOF;
    		} else {
    			numChars += n;
    		}

    		for (int i = 0; i < numChars; ) {
    			char c = cBuf[i++];

    			if (c != '\\' || (eof && numChars <= 5)) {
    				// Not a backslash, so copy and continue
    				// Always pass non backslash chars straight thru
    				// for regular encoding. If backslash occurs in
    				// input stream at the final 5 chars then don't
    				// attempt to read-ahead and de-escape since these
    				// are literal occurrences of U+005C which need to
    				// be encoded verbatim in the target encoding.
    				buf[retChars++] = c;
    				continue;
    			}

    			int remaining = numChars - i;
    			if (remaining < 5) {
    				// Might be the first character of a unicode escape, but we
    				// don't have enough characters to tell, so save it and finish
    				trailChars = new char[1 + remaining];
    				trailChars[0] = c;
    				for (int j = 0; j < remaining; j++)
    					trailChars[1 + j] = cBuf[i + j];
    				break;
    			}
    			// At this point we have at least five characters remaining

    			c = cBuf[i++];
    			if (c != 'u') {
    				// Not a unicode escape, so copy and continue
    				buf[retChars++] = '\\';
    				buf[retChars++] = c;
    				continue;
    			}

    			// The next four characters are the hex part of a unicode escape
    			char rc = 0;
    			boolean isUE = true;
    			try {
    				rc = (char) Integer.parseInt(new String(cBuf, i, 4), 16);
    			} catch (NumberFormatException x) {
    				isUE = false;
    			}
    			if (isUE && encoder.canEncode(rc)) {
    				// We'll be able to convert this
    				buf[retChars++] = rc;
    				i += 4; // Align beyond the current uXXXX sequence
    			} else {
    				// We won't, so just retain the original sequence
    				buf[retChars++] = '\\';
    				buf[retChars++] = 'u';
    				continue;
    			}

    		}
    		return retChars;
    	}

    	@Override
        public int read() throws IOException {
    		char[] buf = new char[1];

    		if (read(buf, 0, 1) == -1)
    			return -1;
    		else
    			return buf[0];
    	}
    }

}
