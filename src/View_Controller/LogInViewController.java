/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View_Controller;


import Models.User;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Optional;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
//import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
//import javafx.event.*;
//import javafx.event.ActionEvent;

//import nickloresoftware2.NickLoreSoftware2;


public class LogInViewController {

    @FXML
    private TextField UserNameText;

    @FXML
    private TextField PasswordText;

    @FXML
    private Button LogInButton;

    @FXML
    private Button CancelButton;
    
    public void OnLogin(ActionEvent event) throws ClassNotFoundException, IOException
    {
        CheckLogin(event);
    }
    
    public void OnCancel()
    { 
       Stage stage = (Stage) CancelButton.getScene().getWindow();
       Stage parent = (Stage) CancelButton.getParent().getScene().getWindow();
       stage.close();
       parent.toFront();
    }

    public void CheckLogin(ActionEvent event) throws ClassNotFoundException, IOException   {
        //Connection conn = null;
        String driver = "com.mysql.jdbc.Driver";
        String db = "U04Goc";
        String url = "jdbc:mysql://52.206.157.109/" + db;
        String user = "U04Goc";
        String pass = "53688232399";
        
        try(Connection conn = DriverManager.getConnection(url,user,pass)) 
        {
            Class.forName(driver);
            //conn = DriverManager.getConnection(url,user,pass);
            
            Statement st = conn.createStatement(
            ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            
            ResultSet r = st.executeQuery(String.format("SELECT * FROM user where userName = '%1$s' and password = '%2$s' and active = 1", UserNameText.getText(), PasswordText.getText() ));

            if(r.next())
            {
                User currentUser = new User();
                currentUser.setName(r.getString("userName"));
                currentUser.setId(r.getInt("userid"));
                currentUser.setActive(r.getBoolean("active"));
                               
                Stage stg; 
                Parent rt; 
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/View_Controller/Main.fxml"));

                stg = new Stage(); 
                rt = loader.load();
        
                MainController controller = loader.getController();
                controller.setUser(currentUser);
                //controller.setParentController(this);
        
                Scene scene = new Scene(rt);
                stg.setScene(scene);
                stg.setTitle("Appointment Scheduler");

                //stg.initOwner(((Node)event.getTarget()).getScene().getWindow());
                stg.show();
                
                Stage stage = (Stage) LogInButton.getScene().getWindow();
                stage.close();               
            }
            else
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.initStyle(StageStyle.UTILITY);
                alert.setTitle("Invalid Log In");
                alert.setHeaderText(null);
                alert.setGraphic(null);
                alert.setContentText("Invalid user name or password");
                
                alert.showAndWait();
            }
        } 
        catch (SQLException e) {
            System.out.println("SQLException: "+e.getMessage());
            System.out.println("SQLState: "+e.getSQLState());
            System.out.println("VendorError: "+e.getErrorCode());
 
        }
    }
}

