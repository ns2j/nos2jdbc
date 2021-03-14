package org.seasar.extension.jdbc.types;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDateTime;

import org.seasar.extension.jdbc.util.BindVariableUtil;

public class LocalDateTimeType extends AbstractValueType {

    /**
     * インスタンスを構築します。
     */
    public LocalDateTimeType() {
        super(Types.TIMESTAMP);
    }
    
    protected LocalDateTime toLocalDateTime(Timestamp ts) {
        return ts == null ? null : ts.toLocalDateTime();
    }

    @Override
    public LocalDateTime getValue(ResultSet resultSet, int index) throws SQLException {
        return toLocalDateTime(resultSet.getTimestamp(index));
    }

    @Override
    public LocalDateTime getValue(ResultSet resultSet, String columnName)
            throws SQLException {
        return toLocalDateTime(resultSet.getTimestamp(columnName));
    }

    @Override
    public LocalDateTime getValue(CallableStatement cs, int index) throws SQLException {
        return toLocalDateTime(cs.getTimestamp(index));
    }

    @Override
    public LocalDateTime getValue(CallableStatement cs, String parameterName)
            throws SQLException {
        return toLocalDateTime(cs.getTimestamp(parameterName));
    }

    @Override
    public void bindValue(PreparedStatement ps, int index, Object value)
            throws SQLException {
        if (value == null) {
            setNull(ps, index);
        } else {
            LocalDateTime ldt = (LocalDateTime)value;
            ps.setTimestamp(index, Timestamp.valueOf(ldt));
        }
    }

    @Override
    public void bindValue(CallableStatement cs, String parameterName,
            Object value) throws SQLException {
        if (value == null) {
            setNull(cs, parameterName);
        } else {
            LocalDateTime ldt = (LocalDateTime)value;
            cs.setTimestamp(parameterName, Timestamp.valueOf(ldt));
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