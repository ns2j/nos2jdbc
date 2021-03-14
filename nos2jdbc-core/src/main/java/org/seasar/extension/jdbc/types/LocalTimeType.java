package org.seasar.extension.jdbc.types;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Types;
import java.time.LocalTime;
import org.seasar.extension.jdbc.util.BindVariableUtil;

public class LocalTimeType extends AbstractValueType {

    /**
     * インスタンスを構築します。
     */
    public LocalTimeType() {
        super(Types.TIME);
    }
    
    protected LocalTime toLocalTime(Time t) {
        return t == null ? null : t.toLocalTime();
    }

    @Override
    public LocalTime getValue(ResultSet resultSet, int index) throws SQLException {
        return toLocalTime(resultSet.getTime(index));
    }

    @Override
    public LocalTime getValue(ResultSet resultSet, String columnName)
            throws SQLException {
        return toLocalTime(resultSet.getTime(columnName));
    }

    @Override
    public LocalTime getValue(CallableStatement cs, int index) throws SQLException {
        return toLocalTime(cs.getTime(index));
    }

    @Override
    public LocalTime getValue(CallableStatement cs, String parameterName)
            throws SQLException {
        return toLocalTime(cs.getTime(parameterName));
    }

    @Override
    public void bindValue(PreparedStatement ps, int index, Object value)
            throws SQLException {
        if (value == null) {
            setNull(ps, index);
        } else {
            LocalTime lt = (LocalTime)value;
            ps.setTime(index, Time.valueOf(lt));
        }
    }

    @Override
    public void bindValue(CallableStatement cs, String parameterName,
            Object value) throws SQLException {
        if (value == null) {
            setNull(cs, parameterName);
        } else {
            LocalTime lt = (LocalTime)value;
            cs.setTime(parameterName, Time.valueOf(lt));
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