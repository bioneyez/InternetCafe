/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package internetcafe.database;

import internetcafe.database.ComputerDataSource;
import internetcafe.entity.Computer;
import internetcafe.entity.Member;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Lorand
 */
public class ComputerDataSourceImpl implements ComputerDataSource {

    private static final String TABLE_NAME = "Computers";
    protected final ConnectionFactory connectionFactory;
    
    public ComputerDataSourceImpl(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    
    
    @Override
    public List<Computer> getAllComputers() {
        List<Computer> computerList = new ArrayList<>();
        computerList.addAll(processQuery(findAllSql()));
        return computerList;
    }

    @Override
    public Computer getComputerById(int id) {
         return findById(id);
    }

    @Override
    public List<Computer> findNewComputersAfter() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    
    
    
    
    
    
    protected Set<Computer> processQuery(String sql, Object... args) {
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            Set<Computer> result = new HashSet<>();
            ResultSet rs = executeSelect(stmt, args);
            while (rs.next()) {
                result.add(computerFromResult(rs));
            }
            return result;
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    
    protected ResultSet executeSelect(PreparedStatement stmt, Object... args) throws SQLException {
        for (int i = 0; i < args.length; ++i) {
            stmt.setObject(i + 1, args[i]);
        }
        return stmt.executeQuery();
    }
    
    
    public Computer findById(int id) {
        Set<Computer> result = processQuery(findAllByIdSql(), id);
        return result.iterator().next();
    }
    
    
    
    
    
    
    
    protected Computer computerFromResult(ResultSet rs) throws SQLException {
        Computer e = new Computer();
        e.setId(rs.getInt("ID"));
        e.setOperationSystem(rs.getString("OPERATIONSYSTEM"));
        e.setSpecification(rs.getString("SPECIFICATION"));
        //e.setDeleted(rs.getBoolean("DELETED"));
        return e;
    }
    
    
    
    
    protected String findAllByIdSql() {
        return findAllSql() + " where id = ?";// + NOT_DELETED_WHERE;
    }
    
    protected String findAllSql() {
        return String.format("select * from %s", getTableName());
    }
    
    protected String findAllSinceSql() {
        return findAllSql() + " where id > ?"; // + NOT_DELETED_WHERE;
    }
    
    
    
    
    
    protected String getTableName() {
        return TABLE_NAME;
    }
    
    
    
}
