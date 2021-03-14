package org.seasar.extension.jdbc.types;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;

import org.seasar.extension.jdbc.util.BindVariableUtil;

public class Jdbc42LocalDateType extends AbstractValueType {

    /**
     * インスタンスを構築します。
     */
    public Jdbc42LocalDateType() {
        super(Types.DATE);
    }

    @Override
    public LocalDate getValue(ResultSet resultSet, int index) throws SQLException {
        return resultSet.getObject(index, LocalDate.class);
    }

    @Override
    public LocalDate getValue(ResultSet resultSet, String columnName)
            throws SQLException {
        return resultSet.getObject(columnName, LocalDate.class);
    }

    @Override
    public LocalDate getValue(CallableStatement cs, int index) throws SQLException {
        return cs.getObject(index, LocalDate.class);
    }

    @Override
    public LocalDate getValue(CallableStatement cs, String parameterName)
            throws SQLException {
        return cs.getObject(parameterName, LocalDate.class);
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