package org.seasar.extension.jdbc.types;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.OffsetDateTime;

import org.seasar.extension.jdbc.util.BindVariableUtil;

public class Jdbc42OffsetDateTimeType extends AbstractValueType {

    /**
     * インスタンスを構築します。
     */
    public Jdbc42OffsetDateTimeType() {
        super(Types.TIMESTAMP_WITH_TIMEZONE);
    }

    @Override
    public OffsetDateTime getValue(ResultSet resultSet, int index) throws SQLException {
        return resultSet.getObject(index, OffsetDateTime.class);
    }

    @Override
    public OffsetDateTime getValue(ResultSet resultSet, String columnName)
            throws SQLException {
        return resultSet.getObject(columnName, OffsetDateTime.class);
    }

    @Override
    public OffsetDateTime getValue(CallableStatement cs, int index) throws SQLException {
        return cs.getObject(index, OffsetDateTime.class);
    }

    @Override
    public OffsetDateTime getValue(CallableStatement cs, String parameterName)
            throws SQLException {
        return cs.getObject(parameterName, OffsetDateTime.class);
    }

    @Override
    public void bindValue(PreparedStatement ps, int index, Object value)
            throws SQLException {
        if (value == null) {
            setNull(ps, index);
        } else {
            ps.setObject(index, value);
        }
    }

    @Override
    public void bindValue(CallableStatement cs, String parameterName,
            Object value) throws SQLException {
        if (value == null) {
            setNull(cs, parameterName);
        } else {
            cs.setObject(parameterName, value);
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