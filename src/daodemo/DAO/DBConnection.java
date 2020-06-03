package daodemo.DAO;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author carolyn.sher
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;




/**
 *
 * @author carolyn.sher
 */
public class DBConnection {
    private static final String databaseName="U03pGb";
    private static final String DB_URL="jdbc:mysql://3.227.166.251/"+databaseName;
    private static final String username="U03pGb";
    private static final String password="53688046989";
    private static final String driver="com.mysql.jdbc.Driver";
    static Connection conn;
    public static void makeConnection()throws ClassNotFoundException, SQLException, Exception
    {
        Class.forName(driver);
        conn=(Connection) DriverManager.getConnection(DB_URL,username,password);
        System.out.println("Connection Successful");
    }
    public static void closeConnection()throws ClassNotFoundException, SQLException, Exception{
        conn.close();
        System.out.println("Connection Closed");
    }
}
