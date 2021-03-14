package org.seasar.extension.jdbc.types;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import org.seasar.extension.jdbc.util.BindVariableUtil;
import org.seasar.framework.util.UtcUtil;

public class MysqlOffsetDateTimeType extends AbstractValueType {

    /**
     * インスタンスを構築します。
     */
    public MysqlOffsetDateTimeType() {
        super(Types.TIMESTAMP_WITH_TIMEZONE);
    }
    
    @Override
    public OffsetDateTime getValue(ResultSet resultSet, int index) throws SQLException {
        return UtcUtil.toOffsetDateTime(resultSet.getObject(index, LocalDateTime.class));
    }

    @Override
    public OffsetDateTime getValue(ResultSet resultSet, String columnName)
            throws SQLException {
        return UtcUtil.toOffsetDateTime(resultSet.getObject(columnName, LocalDateTime.class));
    }

    @Override
    public OffsetDateTime getValue(CallableStatement cs, int index) throws SQLException {
        return UtcUtil.toOffsetDateTime(cs.getObject(index, LocalDateTime.class));
    }

    @Override
    public OffsetDateTime getValue(CallableStatement cs, String parameterName)
            throws SQLException {
        return UtcUtil.toOffsetDateTime(cs.getObject(parameterName, LocalDateTime.class));
    }

    @Override
    public void bindValue(PreparedStatement ps, int index, Object value)
            throws SQLException {
        if (value == null) {
            setNull(ps, index);
        } else {
            ps.setObject(index, UtcUtil.toLocalDateTime((OffsetDateTime)value));
        }
    }

    @Override
    public void bindValue(CallableStatement cs, String parameterName,
            Object value) throws SQLException {
        if (value == null) {
            setNull(cs, parameterName);
        } else {
            cs.setObject(parameterName, UtcUtil.toLocalDateTime((OffsetDateTime)value));
        }
    }

    @Override
    public String toText(Object value) {
        if (value == null) {
            return BindVariableUtil.nullText();
        }
        return BindVariableUtil.toText(value);
    }
}