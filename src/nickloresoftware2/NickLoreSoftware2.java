/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nickloresoftware2;

import java.io.IOException;
import java.sql.Connection;
//import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import java.util.Date;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

/**
 *
 * @author Nick
 */
public class NickLoreSoftware2 extends Application {
    
    @Override
    public void start(Stage primaryStage) throws ClassNotFoundException, IOException {
        
        AnchorPane page = (AnchorPane) FXMLLoader.load(NickLoreSoftware2.class.getResource("/View_Controller/LogInView.fxml"));
             
             Scene scene = new Scene(page);
             primaryStage.setScene(scene);
             primaryStage.setTitle("Log IN");
             primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
        
    }
    
    public void Test() throws ClassNotFoundException
    {
        Connection conn = null;
        String driver = "com.mysql.jdbc.Driver";
        String db = "U04Goc";
        String url = "jdbc:mysql://52.206.157.109/" + db;
        String user = "U04Goc";
        String pass = "53688232399";
        
        try 
        {
            Class.forName(driver);
            conn = DriverManager.getConnection(url,user,pass);
            
            Statement st = conn.createStatement(
            ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);

            ResultSet r = st.executeQuery("SELECT * FROM user" );
            String test = "";
            if(r.next())
            {
                test  = r.getString("userName");             
            }
            
            
            System.out.println("Connected to database : " + test);
            
        } 
        catch (SQLException e) {
            System.out.println("SQLException: "+e.getMessage());
            System.out.println("SQLState: "+e.getSQLState());
            System.out.println("VendorError: "+e.getErrorCode());
 
        }
    }
    
    
    
    
}
