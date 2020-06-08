/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Jacobi
 */
public class DBConnection {
    
    private static final String protocol = "jdbc";
    private static final String vendorName = ":mysql:";
    private static final String ipAddress = "//3.227.166.251/U06r1w";
    
    // JDBC URL
    private static final String jdcbcURL = protocol + vendorName + ipAddress;
    private static final String MYSQLJDBCDriver = "com.mysql.jdbc.Driver";
    private static Connection conn = null;  
    private static final String username = "U06r1w";
    private static final String password = "53688848503";
    
    public static Connection startConnection()
    {
        try{
            Class.forName(MYSQLJDBCDriver);
            conn = (Connection)DriverManager.getConnection(jdcbcURL, username, password);
            System.out.println("DBConnection initiated! Great success!");
        }
        catch(ClassNotFoundException e)
        {
            System.out.println(e.getMessage());
        }
        catch(SQLException e)
        {
            System.out.println("Error: " + e.getMessage());
        }
        return conn;
    }
    
    public static void closeConnection(){
        try{
            conn.close();
            System.out.println("Connection has been closed : (");
        }
        catch(SQLException e){
            System.out.println("Error" + e.getMessage());
            
        }
    }
    
    
}
