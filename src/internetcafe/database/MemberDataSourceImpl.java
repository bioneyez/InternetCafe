/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package internetcafe.database;

import internetcafe.database.MemberDataSource;
import internetcafe.entity.Member;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Lorand
 */
public class MemberDataSourceImpl implements MemberDataSource {

    private static final String TABLE_NAME = "Members";
    //protected static final String NOT_DELETED_WHERE = " and deleted = FALSE";
    protected final ConnectionFactory connectionFactory;
    
    public MemberDataSourceImpl(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }


    @Override
    public List<Member> getAllMembers() {
        List<Member> memberList = new ArrayList<>();
        memberList.addAll(processQuery(findAllSql()));
        return memberList;
/*
        List<Member> result = new ArrayList<>();
                    for(int i = 0; i < 10; i++) {
                        Member m = new Member();
                        m.setUsername("Username " + i);
                        m.setAddress("Address" + i);
                        m.setId(i);
                        result.add(m);
                    }
                    return result;
*/
    }

    @Override
    public Member getMemberById(int id) {
        
        return findById(id);
    }


    @Override
    public List<Member> findNewMembersAfter(int id) {
        List<Member> memberList = new ArrayList<>();
        memberList.addAll(processQuery(findAllSinceSql(),id));
        return memberList;
    }
    
    
    
    
    protected Set<Member> processQuery(String sql, Object... args) {
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            Set<Member> result = new HashSet<>();
            ResultSet rs = executeSelect(stmt, args);
            while (rs.next()) {
                result.add(memberFromResult(rs));
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
    
    
    public Member findById(int id) {
        Set<Member> result = processQuery(findAllByIdSql(), id);
        return result.iterator().next();
    }
    
    
    protected void update(Member m) throws SQLException {
        //log.info("Updating entity in DB...{}", t.getId());
        try (Connection conn = connectionFactory.getConnection()) {
            executeUpdate(conn, m);
        }
    }
    
    protected void insert(Member m) throws SQLException {
        //log.info("Inserting entity to DB...{}", t.getId());
        try (Connection conn = connectionFactory.getConnection()) {
            executeInsert(conn, m);
        }
    }
    
    
     protected void executeInsert(Connection conn, Member m) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(getInsertSql(), Statement.RETURN_GENERATED_KEYS);
        fillInsertParamsForEntity(stmt, m);

        int affectedRows = stmt.executeUpdate();

        if (affectedRows == 0) {
            throw new SQLException("Creating entity failed, no rows affected.");
        }
        fillId(stmt, m);
    }
     
    protected void executeUpdate(Connection conn, Member m) throws SQLException {
        try {
            conn.setAutoCommit(false);
            //int updateNo = nextUpdateNo(conn);

            PreparedStatement stmt = conn.prepareStatement(getUpdateSql());
            fillUpdateParamsForEntity(stmt, m);

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("update entity failed, no rows affected.");
            }
            //m.setUpdateNo(updateNo);
            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        }
    }
    
    
    protected Member memberFromResult(ResultSet rs) throws SQLException {
        Member e = new Member();
        e.setId(rs.getInt("ID"));
        e.setAddress(rs.getString("ADDRESS"));
        e.setName(rs.getString("NAME"));
        e.setUsername(rs.getString("USERNAME"));
        e.setPassword(rs.getString("PASSWORD"));
        e.setBalance(rs.getInt("MONEY"));
        e.setIdNumber(rs.getInt("IDNUMBER"));
        e.setPoints(rs.getInt("POINTS"));
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
    
    
     
    protected String getInsertSql() {
        return String.format("INSERT INTO %s (NAME, ADDRESS, IDNUMBER,USERNAME,PASSWORD,MONEY,POINTS) VALUES(?,?,?,?,?,?,?)", TABLE_NAME);
    }

    
    protected String getUpdateSql() {
        return String.format("UPDATE %s SET name=?, address=?, idNumber=?, username=?, password=?, money=?, points=? WHERE ID=?", TABLE_NAME);
    }
    
    protected void fillInsertParamsForEntity(PreparedStatement stmt, Member m) throws SQLException {
        stmt.setString(1, m.getName());
        stmt.setString(2, m.getAddress());
        stmt.setInt(3, m.getIdNumber());
        stmt.setString(4, m.getUsername());
        stmt.setString(5, m.getPassword());
        stmt.setInt(6, m.getBalance());
        stmt.setInt(7,m.getPoints());
    }

    protected void fillUpdateParamsForEntity(PreparedStatement stmt, Member m) throws SQLException {
        stmt.setString(1, m.getName());
        stmt.setString(2, m.getAddress());
        stmt.setInt(3, m.getIdNumber());
        stmt.setString(4, m.getUsername());
        stmt.setString(5,m.getPassword());
        stmt.setInt(6,m.getBalance());
        stmt.setInt(7, m.getId());
        stmt.setInt(8,m.getPoints());
    }
    
    
    protected String getTableName() {
        return TABLE_NAME;
    }
    
    public void save(Member m) {
        doSave(m);
    }

    private void doSave(Member m) {
        try {
            if (m.getId() == 0) {
                insert(m);
            } else {
                update(m);
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    protected void fillId(PreparedStatement stmt, Member m) throws SQLException {
        try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                m.setId(generatedKeys.getInt(1));
            } else {
                throw new SQLException("Creating entity failed, no ID obtained.");
            }
        }
    }
    
}
