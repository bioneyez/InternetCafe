/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package internetcafe.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Lorand
 * @param <T>
 */

public abstract class AbstractAccess<T> {
    protected final ConnectionFactory connectionFactory;
    protected static final String NOT_DELETED_WHERE = " and deleted = FALSE";
    
    public AbstractAccess(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }
    protected Set<T> processQuery(String sql, Object... args) {
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            Set<T> result = new HashSet<>();
            ResultSet rs = executeSelect(stmt, args);
            while (rs.next()) {
                result.add(entityFromResult(rs));
            }
            return result;
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    public T findById(int id) {
        Set<T> result = processQuery(findAllByIdSql(), id);
        return result.iterator().next();
    }
    
    
    protected String findAllByIdSql() {
        return findAllSql() + " where id = ?" + NOT_DELETED_WHERE;
    }
    
    protected String findAllSql() {
        return String.format("select * from %s", getTableName());
    }
    
    protected ResultSet executeSelect(PreparedStatement stmt, Object... args) throws SQLException {
        for (int i = 0; i < args.length; ++i) {
            stmt.setObject(i + 1, args[i]);
        }
        return stmt.executeQuery();
    }
    
    
    
    protected abstract T entityFromResult(ResultSet rs) throws SQLException;
    protected abstract String getTableName();
}
