/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View_Controller;

import Models.Appointment;
import Models.User;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * FXML Controller class
 *
 * @author Nick
 */
public class ReportViewController implements Initializable {
    
    private User currentUser;
    private Connection conn = null;
    private Appointment selectedAppointment;
    private final String driver = "com.mysql.jdbc.Driver";
    private final String db = "U04Goc";
    private final String conUrl = "jdbc:mysql://52.206.157.109/" + db;
    private final String user = "U04Goc";
    private final String pass = "53688232399";    
    private ObservableList<Appointment> availableAppointments;
    private DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss.S");
    private DateTimeFormatter formatDt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
    private DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm");

    @FXML
    private TableView<Appointment> appointmentsTblView;
    
    @FXML
    private TableColumn<?, ?> apptConsultant;
    
    @FXML
    private TableColumn<?, ?> apptCust;

    @FXML
    private TableColumn<?, ?> apptTitle;

    @FXML
    private TableColumn<?, ?> apptDate;
    
//    @FXML
//    private TableColumn<?, ?> apptTime;

    @FXML
    void AppointmentSelection(MouseEvent event) {

    }
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        try
        {
            populateConsultantReport();
            apptConsultant.setCellValueFactory(new PropertyValueFactory<>("consultant"));
            apptTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
            apptCust.setCellValueFactory(new PropertyValueFactory<>("customerName"));
            apptDate.setCellValueFactory(new PropertyValueFactory<>("startDate"));
            appointmentsTblView.getItems().setAll(availableAppointments);
        }
        catch(Exception e)
        {
            String test = e.getMessage();
        }
    }

    private void populateConsultantReport() throws ClassNotFoundException
    {
        try 
        {
            Class.forName(driver);
            conn = DriverManager.getConnection(conUrl,user,pass);
            
            Statement st = conn.createStatement(
            ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            String sql = "Select * from appointment join customer on appointment.customerId = customer.customerid Order By contact";

            ResultSet r = st.executeQuery(sql);
            availableAppointments = null;
            availableAppointments = FXCollections.observableArrayList();
            while(r.next())
            {
                Appointment a = new Appointment();
                
                a.setId(r.getInt("appointmentid"));
                a.setConsultant(r.getString("contact"));
                a.setCustomerId(r.getInt("customerId"));
                a.setTitle(r.getString("title"));
                a.setDescription(r.getString("description"));
                a.setContact(r.getString("contact"));
                a.setUrl(r.getString("url"));
                a.setLocation(r.getString("location"));
                a.setType(r.getString("type"));
                a.setUserId(r.getInt("UserId"));
                a.setStartDate(r.getString("start"));
                a.setEndDate(r.getString("end"));
                a.setCustomerName(r.getString("customerName"));

                availableAppointments.add(a);
            }
        } 
        catch (SQLException e) {
            System.out.println("SQLException: "+e.getMessage());
            System.out.println("SQLState: "+e.getSQLState());
            System.out.println("VendorError: "+e.getErrorCode());
        }
    }
    
}
