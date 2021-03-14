package org.seasar.extension.jdbc.types;

import java.sql.CallableStatement;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import org.seasar.extension.jdbc.util.BindVariableUtil;

public class LocalDateType extends AbstractValueType {

    /**
     * インスタンスを構築します。
     */
    public LocalDateType() {
        super(Types.DATE);
    }
    
    protected LocalDate toLocalDate(Date ld) {
        return ld == null ? null : ld.toLocalDate();
    }

    @Override
    public LocalDate getValue(ResultSet resultSet, int index) throws SQLException {
        return toLocalDate(resultSet.getDate(index));
    }

    @Override
    public LocalDate getValue(ResultSet resultSet, String columnName)
            throws SQLException {
        return toLocalDate(resultSet.getDate(columnName));
    }

    @Override
    public LocalDate getValue(CallableStatement cs, int index) throws SQLException {
        return toLocalDate(cs.getDate(index));
    }

    @Override
    public LocalDate getValue(CallableStatement cs, String parameterName)
            throws SQLException {
        return toLocalDate(cs.getDate(parameterName));
    }

    @Override
    public void bindValue(PreparedStatement ps, int index, Object value)
            throws SQLException {
        if (value == null) {
            setNull(ps, index);
        } else {
            LocalDate ld = (LocalDate)value;
            ps.setDate(index, Date.valueOf(ld));
        }
    }

    @Override
    public void bindValue(CallableStatement cs, String parameterName,
            Object value) throws SQLException {
        if (value == null) {
            setNull(cs, parameterName);
        } else {
            LocalDate ld = (LocalDate)value;
            cs.setDate(parameterName, Date.valueOf(ld));
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