/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.dal;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import java.sql.Connection;

/**
 *
 * @author Dominik
 */
public class ConnectionManager {
    private SQLServerDataSource source = new SQLServerDataSource();

    /**
     *
     */
    public ConnectionManager() {
        source.setDatabaseName("CS2017B_7_myTunes");
        source.setUser("CS2017B_7_java");
        source.setPassword("javajava");
        source.setPortNumber(1433);
        source.setServerName("10.176.111.31");
    }
    
    /**
     *
     * @return Connection
     * @throws SQLServerException
     */
    public Connection getConnection() throws SQLServerException{
        return source.getConnection();
    }
}
