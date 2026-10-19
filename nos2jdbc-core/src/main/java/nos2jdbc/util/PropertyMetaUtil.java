package nos2jdbc.util;

import nos2jdbc.NoS2JdbcConstants;

/**
 * PropertyMetaのためのユーティリティクラスです。
 */
public class PropertyMetaUtil {

    /**
     * プロパティ名をベース名と残りの名前に分割します。
     * 
     * @param propertyName プロパティ名
     * @return 分割された名前の配列。分割できない場合は要素数1の配列。
     */
    public static String[] split(String propertyName) {
        int index = propertyName.indexOf(NoS2JdbcConstants.EMBEDDED_PROPERTY_NAME_SEPARATOR);
        if (index < 0) {
            return new String[] { propertyName };
        }
        return new String[] {
            propertyName.substring(0, index),
            propertyName.substring(index + NoS2JdbcConstants.EMBEDDED_PROPERTY_NAME_SEPARATOR.length())
        };
    }
}
