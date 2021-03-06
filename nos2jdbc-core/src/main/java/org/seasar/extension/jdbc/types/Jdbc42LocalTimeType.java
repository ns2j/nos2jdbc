package org.seasar.extension.jdbc.types;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalTime;

import org.seasar.extension.jdbc.util.BindVariableUtil;

public class Jdbc42LocalTimeType extends AbstractValueType {

    /**
     * インスタンスを構築します。
     */
    public Jdbc42LocalTimeType() {
        super(Types.TIME);
    }

    @Override
    public LocalTime getValue(ResultSet resultSet, int index) throws SQLException {
        return resultSet.getObject(index, LocalTime.class);
    }

    @Override
    public LocalTime getValue(ResultSet resultSet, String columnName)
            throws SQLException {
        return resultSet.getObject(columnName, LocalTime.class);
    }

    @Override
    public LocalTime getValue(CallableStatement cs, int index) throws SQLException {
        return cs.getObject(index, LocalTime.class);
    }

    @Override
    public LocalTime getValue(CallableStatement cs, String parameterName)
            throws SQLException {
        return cs.getObject(parameterName, LocalTime.class);
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