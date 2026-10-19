package org.seasar.extension.jdbc.gen.model;

import java.util.Map;

public class KotlinUtil {
    static Map<String, String> map = Map.of(
            "int", "Int",
            "Integer", "Int",
            "long", "Long",
            "boolean", "Boolean",
            "float", "Float",
            "double", "Double",
            "char", "Char"
            );

    public static String getTypeName(String javaClassSimpleName) {
        String type = map.get(javaClassSimpleName);
        return type == null ? javaClassSimpleName: type;
    }
}
