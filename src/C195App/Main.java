/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package C195App;

import com.mysql.cj.protocol.Resultset;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import java.util.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import utils.DBConnection;
import utils.Query;

/**
 *
 * @author carolyn.sher
 */
public class Main extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/view_controller/LogIn.fxml"));
        Scene LogInScene = new Scene(root);
        stage.setScene(LogInScene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SQLException {
        launch(args);
        DBConnection.closeConnection();
    }
    
}

/*//String insertStatement = "INSERT INTO country(country, createdate, createdBy, lastUpdateBy) VALUES ('US', '2020-02-22 00:00:00', 'admin', 'admin')";
        
        // variable Insert
        String countryName = "Canada";
        String createDate = "2020-06-04 00:00:00";
        String createdBy = "admin";
        String lastUpdateBy = "admin";
        
        String insertStatement  = "INSERT INTO country(country, createDate, createdBy, lastupdateBy)" +
                                  "VALUES(" +
                                  "'" + countryName +"'," +
                                  "'" + createDate +"'," +
                                  "'" + createdBy +"'," +
                                  "'" + lastUpdateBy +"'," +
                                  ")";
        
        //String updateStatement = "UPDATE country SET country = 'Japan' WHERE country = 'Canada'";
        
        //execute SQL statement
        //statement.execute(insertStatement);
        (if(statement.getUpdateCount() > 0)
        {
            System.out.println(statement.getUpdateCount() + " row(s) affected!");
        }
        else
        {
            System.out.println("No changes were made");
        }*/