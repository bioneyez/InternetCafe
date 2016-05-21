package internetcafe.database;



import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.derby.jdbc.ClientConnectionPoolDataSource;

/**
 *
 * @author alexb
 */
public class ConnectionFactory {
    
    private final String dbName = "kavezo";
    private final String user = "kavezo";
    private final String pwd = "kavezo";
    private final String host = "localhost";
    
    private final int port = 1527;
    private final ClientConnectionPoolDataSource dataSource;

    public ConnectionFactory() {
        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ConnectionFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
        dataSource = new ClientConnectionPoolDataSource();
        dataSource.setDatabaseName(dbName);
        dataSource.setUser(user);  
        dataSource.setPassword(pwd); 
        dataSource.setServerName(host);
        dataSource.setPortNumber(port);
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getPooledConnection().getConnection();
    }
    
    
    
}
