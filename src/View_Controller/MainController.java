/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View_Controller;

import Models.Appointment;
import Models.User;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * FXML Controller class
 *
 * @author Nick
 */
public class MainController implements Initializable {

    /**
     * Initializes the controller class.
     */ 
    @FXML
    private Button customerBtn;
    
    private User currentUser;
    private Connection conn = null;
    private Appointment selectedAppointment;
    private final String driver = "com.mysql.jdbc.Driver";
    private final String db = "U04Goc";
    private final String conUrl = "jdbc:mysql://52.206.157.109/" + db;
    private final String user = "U04Goc";
    private final String pass = "53688232399";    
    private ObservableList<Appointment> availableAppointments;
    private FilteredList<Appointment> filteredAppointments;
    private DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss.S");
    private DateTimeFormatter formatDt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
    private DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm");
    
    @FXML
    private Button apptBtn;

    @FXML
    private TableView<Appointment> appointmentsTblView;
    
    @FXML
    private TableColumn<?, ?> apptCust;

    @FXML
    private TableColumn<?, ?> apptTitle;

    @FXML
    private TableColumn<?, ?> apptDate;

    @FXML
    private TableColumn<?, ?> apptTime;
    
    @FXML
    private Button ViewBtn;
    
    @FXML
    private ToggleGroup filterGroup;

    @FXML
    private RadioButton weekRDO;

    @FXML
    private RadioButton monthRDO;
    
    @FXML
    private RadioButton allRDO;
    
    public MainController()
    {
        
    }
    
    public MainController(Appointment app)
    {
        selectedAppointment = app; 
        appointmentsTblView.getSelectionModel().select(app);
    }
    
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        try
        {           
            PopulateAppointments();
            checkFifteen();
            apptTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
            apptCust.setCellValueFactory(new PropertyValueFactory<>("customerName"));
            apptDate.setCellValueFactory(new PropertyValueFactory<>("startDate"));
            appointmentsTblView.getItems().setAll(availableAppointments);
        }
        catch(Exception e)
        {
            
        }
    }    
    
    @FXML
    void OpenCustomerView(ActionEvent event) throws IOException   
    {
        Stage stg; 
        Parent rt; 
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/View_Controller/CustomerEditView.fxml"));

        stg = new Stage(); 
        rt = loader.load();
        
        CustomerEditViewController controller = loader.getController();
        controller.setUser(currentUser);
        
        Scene scene = new Scene(rt);
        stg.setScene(scene);
        stg.setTitle("Customer Edit");

        stg.initOwner(((Node)event.getTarget()).getScene().getWindow());
        stg.show();  
    }
    
    @FXML
    void OpenAppointmentView(ActionEvent event) throws IOException   
    {
        Stage stg; 
        Parent rt; 
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/View_Controller/AppointmentEditView.fxml"));

        stg = new Stage(); 
        rt = loader.load();
        
        AppointmentEditViewController controller = loader.getController();
        controller.setUser(currentUser);
        if(selectedAppointment != null)
            controller.setSelectedAppointment(selectedAppointment);
        
        Scene scene = new Scene(rt);
        stg.setScene(scene);
        stg.setTitle("Appointment Edit");

        stg.initOwner(((Node)event.getTarget()).getScene().getWindow());
        stg.show();  
    }
    
    @FXML
    void OpenReportsView(ActionEvent event) throws IOException   
    {
        Stage stg; 
        Parent rt; 
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/View_Controller/ReportView.fxml"));

        stg = new Stage(); 
        rt = loader.load();
        
        ReportViewController controller = loader.getController();
        //controller.setUser(currentUser);
//        if(selectedAppointment != null)
//            controller.setSelectedAppointment(selectedAppointment);
        
        Scene scene = new Scene(rt);
        stg.setScene(scene);
        stg.setTitle("Reports");

        stg.initOwner(((Node)event.getTarget()).getScene().getWindow());
        stg.show();  
    }
    
    @FXML   
    void filterAppointments(ActionEvent event)
    {
        try
        {
            filteredAppointments = new FilteredList(availableAppointments);

            if(weekRDO.isSelected())
            {
               filteredAppointments.setPredicate(row -> {

                LocalDateTime rowDate = LocalDateTime.parse(row.getStartDate(), formatDt);
                boolean testAft = rowDate.isAfter(LocalDateTime.now());
                boolean testBfr = rowDate.isBefore(LocalDateTime.now().plusDays(7));

                return rowDate.isAfter(LocalDateTime.now()) && rowDate.isBefore(LocalDateTime.now().plusDays(7));
                });

                appointmentsTblView.getItems().setAll(filteredAppointments);
            }
            if(monthRDO.isSelected())
            {
               filteredAppointments.setPredicate(row -> {

                LocalDateTime rowDate = LocalDateTime.parse(row.getStartDate(), formatDt);
                boolean testAft = rowDate.isAfter(LocalDateTime.now());
                boolean testBfr = rowDate.isBefore(LocalDateTime.now().plusDays(7));

                return rowDate.isAfter(LocalDateTime.now()) && rowDate.getMonth() == LocalDateTime.now().getMonth();
                });

                appointmentsTblView.getItems().setAll(filteredAppointments);
            }    
            if(allRDO.isSelected())
            {
                appointmentsTblView.getItems().setAll(availableAppointments);
            } 
        }
        catch(Exception e)
        {
            String test = e.getMessage();
        }
    }
    
    public void setUser(User u)
    {
        currentUser = u;
        
        logUserActivity();
    }
    
    public void PopulateAppointments ()throws ClassNotFoundException
    {
        try (Connection conn = DriverManager.getConnection(conUrl,user,pass))
        {
            Class.forName(driver);
            //conn = DriverManager.getConnection(conUrl,user,pass);
            
            Statement st = conn.createStatement(
            ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            String sql = "Select * from appointment join customer on appointment.customerId = customer.customerid";

            ResultSet r = st.executeQuery(sql);
            availableAppointments = null;
            availableAppointments = FXCollections.observableArrayList();
            while(r.next())
            {
                Appointment a = new Appointment();
                
                a.setId(r.getInt("appointmentid"));
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
     
    public void AppointmentSelection()
    {
       selectedAppointment =  appointmentsTblView.getSelectionModel().getSelectedItem(); 
    }
    
    private void checkFifteen()
    {
        ObservableList<Appointment> alertAppointments = FXCollections.observableArrayList();
        int appCount = 0;
        String message = "You have upcoming appointments: \n";
        
        for(Appointment a : availableAppointments)
        {
            LocalDateTime appDate = LocalDateTime.parse(a.getStartDate(), formatDt);
            LocalDateTime nw = LocalDateTime.now();
            boolean test = appDate.isAfter(nw);
            if(appDate.isAfter(LocalDateTime.now()) && appDate.isBefore(LocalDateTime.now().plusMinutes(15)))
            {
                    message += a.getCustomerName() + "\t" + a.getTitle() + "\t" + appDate.format(timeFormat) + "\n";
                    appCount++;
            }           
        }
        if(appCount > 0)
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.initStyle(StageStyle.UTILITY);
            alert.setTitle("Upcoming Appointments");
            alert.setHeaderText(null);
            alert.setGraphic(null);
            alert.setContentText(message);

            alert.showAndWait();
        }
    }
    
    private void logUserActivity()
    {
        File log = new File("Login.txt");
        
        if(log.exists())
        {
            try
            {
                writeToFile(log);
            }
            catch(Exception e)
            {
               
            }
        }
        else
        {
            try
            {
                log.createNewFile();
                writeToFile(log);
            }
            catch(IOException e)
            {
                String t = e.getMessage();
            } 
            catch(Exception e)
            {
                String t = e.getMessage();
            }
        } 
    }
    
    private void writeToFile(File file)
    {
        try
        {
            FileWriter fw = new FileWriter(file.getAbsoluteFile(),true);
            try (BufferedWriter bw = new BufferedWriter(fw)) {
                LocalDateTime dt = LocalDateTime.now();

                bw.write(System.getProperty("line.separator") + currentUser.getName() + "\t" + dt);
            }
        }
        catch(IOException e)
        {
            String t = e.getMessage();
        } 
        catch(Exception e)
        {
            String t = e.getMessage();
        }
    }
}
