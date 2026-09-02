package com.gneworks.common.utils.handler;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import java.lang.reflect.Type;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class StringListTypeHandler implements TypeHandler<List<String>> {

    private final Gson gson = new Gson();

    @Override
    public void setParameter(PreparedStatement preparedStatement, int i, List<String> list, JdbcType jdbcType) throws SQLException {
        if (list != null) {
            preparedStatement.setString(i, gson.toJson(list));
        } else {
            preparedStatement.setNull(i, jdbcType.TYPE_CODE);
        }
    }

    @Override
    public List<String> getResult(ResultSet resultSet, String s) throws SQLException {
        String value = resultSet.getString(s);
        if (value != null) {
            Type listType = new TypeToken<List<String>>() {}.getType();
            return gson.fromJson(value, listType);
        } else {
            return null;
        }
    }

    @Override
    public List<String> getResult(ResultSet resultSet, int i) throws SQLException {
        String value = resultSet.getString(i);
        if (value != null) {
            Type listType = new TypeToken<List<String>>() {}.getType();
            return gson.fromJson(value, listType);
        } else {
            return null;
        }
    }

    @Override
    public List<String> getResult(CallableStatement callableStatement, int i) throws SQLException {
        String value = callableStatement.getString(i);
        if (value != null) {
            Type listType = new TypeToken<List<String>>() {}.getType();
            return gson.fromJson(value, listType);
        } else {
            return null;
        }
    }
}