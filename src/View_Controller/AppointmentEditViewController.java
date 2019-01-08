/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View_Controller;

import Models.Address;
import Models.Appointment;
import Models.City;
import Models.Country;
import Models.Customer;
import Models.CustomerConverter;
import Models.User;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.DayOfWeek;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * FXML Controller class
 *
 * @author Nick
 */
public class AppointmentEditViewController implements Initializable {

    @FXML
    private TextField TitleTxt;

    @FXML
    private TextField DescriptionTxt;

    @FXML
    private TextField LocationTxt;

    @FXML
    private TextField TypeTxt;

    @FXML
    private TextField ContactTxt;

    @FXML
    private DatePicker StartDatePicker;

    @FXML
    private DatePicker EndDatePicker;

    @FXML
    private ComboBox<Customer> CustomerCmbBx;

    @FXML
    private Button DeleteBtn;

    @FXML
    private Button SaveBtn;

    @FXML
    private Button CancelBtn;

    @FXML
    private TableView<Appointment> appointmentTableView;

    @FXML
    private TableColumn<?, ?> apptTitle;

    @FXML
    private Button NewApptBtn;
    
    @FXML
    private ComboBox<String> cboHourStart;

    @FXML
    private ComboBox<String> cboMinStart;
    
    @FXML
    private ComboBox<String> cboHourEnd;

    @FXML
    private ComboBox<String> cboMinEnd;
    
    private Boolean newAppt = false;
    private ObservableList<Customer> availableCustomers;
    private ObservableList<Appointment> availableAppointments;
    private Connection conn = null;
    private Appointment selectedAppointment;
    private final String driver = "com.mysql.jdbc.Driver";
    private final String db = "U04Goc";
    private final String conUrl = "jdbc:mysql://52.206.157.109/" + db;
    private final String user = "U04Goc";
    private final String pass = "53688232399"; 
    private User currentUser;
    private DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss.S");
    private DateTimeFormatter formatDt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
    ObservableList<String> hours = FXCollections.observableArrayList();
    ObservableList<String> minutes = FXCollections.observableArrayList();

    
    @FXML
    void AddAppt(ActionEvent event) 
    {
        selectedAppointment = new Appointment();
        Reset();
    }

    @FXML
    void Cancel(ActionEvent event) 
    {
        CloseWindow();
    }

    @FXML
    void DeleteAppointment(ActionEvent event) throws ClassNotFoundException
    {
        if(selectedAppointment == null || selectedAppointment.getId() == 0)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("No appointment selected.");
            alert.showAndWait();
        }
        else
        {
            try
            {
                Class.forName(driver);
                conn = DriverManager.getConnection(conUrl,user,pass);
                int deleteCount = 0;

                PreparedStatement st = conn.prepareStatement("Delete from appointment where appointmentid = ?");
                st.setInt(1,selectedAppointment.getId());
                deleteCount = st.executeUpdate();
                
                if(deleteCount > 0)
                {
                    UpdateAppointmentList();
                    Reset(); 
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.initStyle(StageStyle.UTILITY);
                    alert.setTitle("Success!");
                    alert.setHeaderText(null);
                    alert.setGraphic(null);
                    alert.setContentText("Record deleted succesfully");

                    alert.showAndWait();
                }
            }
            catch(SQLException e)
            {

            }
        }
    }

    @FXML
    void SaveAppointment(ActionEvent event) throws ClassNotFoundException
    {
        if(checkChange())
        {
            if(newAppt == null || newAppt == false )
            {
                UpdateAppt();
            }
            else if(newAppt == true)
            {
                AddNewAppt();   
            }
        }
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       try
       {
           hours.addAll("08", "09", "10", "11", "12", "13", "14", "15", "16", "17");
            minutes.addAll("00", "15", "30", "45");
            cboHourStart.setItems(hours);
            cboMinStart.setItems(minutes);
            cboHourEnd.setItems(hours);
            cboMinEnd.setItems(minutes);
            PopulateCustomers();
            PopulateAppointments();
            CustomerCmbBx.setConverter(new CustomerConverter());
            CustomerCmbBx.setItems(availableCustomers);
            apptTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
            appointmentTableView.getItems().setAll(availableAppointments);
            
            if(selectedAppointment != null)
            {
                populateSelectedAppointment();
            }
       }
       catch(Exception e)
       {
           
       }
    }    
    
    private void PopulateCustomers ()throws ClassNotFoundException
    {
        try (Connection conn = DriverManager.getConnection(conUrl,user,pass))
        {
            Class.forName(driver);
            //conn = DriverManager.getConnection(conUrl,user,pass);
            
            Statement st = conn.createStatement(
            ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            String sql = "Select c.customerid, c.customerName, c.active, c.addressId, " +
                        "a.address, a.address2, a.postalCode, a.phone, a.cityid, " +
                        "ct.city, ct.countryId, cntr.country " +
                        "from customer c " +
                        "join address a  on c.addressId = a.addressId " +
                        "join city ct on  a.cityid = ct.cityid " +
                        "join country cntr on ct.countryId = cntr.countryId " +
                        "ORDER BY c.customerName";

            ResultSet r = st.executeQuery(sql);
            availableCustomers = null;
            availableCustomers = FXCollections.observableArrayList();
            while(r.next())
            {
                Customer c = new Customer();
                c.setName(r.getString("customerName"));
                c.setActive(r.getBoolean("active"));
                c.setId(r.getInt("customerId"));
                c.setAddress(new Address());
                c.getAddress().setAddress(r.getString("address"));
                c.getAddress().setAddress2(r.getString("address2"));
                c.getAddress().setPostalCode(r.getString("postalCode"));
                c.getAddress().setPhone(r.getString("phone")); 
                c.getAddress().setAddressId(r.getInt("addressId"));
                c.getAddress().setCity(new City());
              
                c.getAddress().getCity().setCityId(r.getInt("cityid"));
                c.getAddress().getCity().setCity(r.getString("city"));
                c.getAddress().getCity().setCountryId(r.getInt("countryId"));
                c.getAddress().getCity().setCountry(new Country());
                c.getAddress().getCity().getCountry().setCountryId(r.getInt("countryId"));
                c.getAddress().getCity().getCountry().setCountry(r.getString("country"));

                availableCustomers.add(c);
            }
        } 
        catch (SQLException e) {
            System.out.println("SQLException: "+e.getMessage());
            System.out.println("SQLState: "+e.getSQLState());
            System.out.println("VendorError: "+e.getErrorCode());
        }
    }
    
    private void PopulateAppointments ()throws ClassNotFoundException
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
    
    private void populateSelectedAppointment()
    {
        TitleTxt.setText(selectedAppointment.getTitle());
        DescriptionTxt.setText(selectedAppointment.getDescription());
        LocationTxt.setText(selectedAppointment.getLocation());
        CustomerCmbBx.getSelectionModel().select(selectedAppointment.getCustomerId());
        TypeTxt.setText(selectedAppointment.getType());
        ContactTxt.setText(selectedAppointment.getContact());
        try
        {
            StartDatePicker.setValue(LocalDate.parse(selectedAppointment.getStartDate(), formatDt));
            EndDatePicker.setValue(LocalDate.parse(selectedAppointment.getEndDate(), formatDt));
            cboHourStart.setValue(String.format("%02d",LocalDateTime.parse(selectedAppointment.getStartDate(), formatDt).getHour()));
            cboMinStart.setValue(String.format("%02d",LocalDateTime.parse(selectedAppointment.getStartDate(), formatDt).getMinute()));
            cboHourEnd.setValue(String.format("%02d",LocalDateTime.parse(selectedAppointment.getEndDate(), formatDt).getHour()));
            cboMinEnd.setValue(String.format("%02d",LocalDateTime.parse(selectedAppointment.getEndDate(), formatDt).getMinute()));
        }
        catch (Exception e)
        {
           String test = selectedAppointment.getStartDate();
           test += " ";
        }
    }
    
    public void AppointmentSelection()
    {
       selectedAppointment =  appointmentTableView.getSelectionModel().getSelectedItem();
       populateSelectedAppointment(); 
    }
    
    public void AddNewAppt() throws ClassNotFoundException
    {
        try(Connection conn = DriverManager.getConnection(conUrl,user,pass))
        {
            Class.forName(driver);
            //conn = DriverManager.getConnection(conUrl,user,pass);
            int addAppt = 0;
            
            String startHour = cboHourStart.getValue();
            String startMin = cboMinStart.getValue();
            String endHour = cboHourEnd.getValue();
            String endMin = cboMinEnd.getValue();
            LocalDate startDate = StartDatePicker.getValue();
            LocalDate endDate = EndDatePicker.getValue();

            String sql = "INSERT INTO appointment " +
                    "(customerId, title, description, location, contact, start, end, type, userId, createDate, createdBy, lastUpdate, lastUpdateBy, url)" +
                    "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            PreparedStatement st = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            st.setInt(1, CustomerCmbBx.getSelectionModel().getSelectedItem().getId());
            st.setString(2, TitleTxt.getText());
            st.setString(3,DescriptionTxt.getText());
            st.setString(4, LocationTxt.getText());
            st.setString(5, ContactTxt.getText());
            LocalDateTime sdt = LocalDateTime.of(startDate.getYear(), startDate.getMonthValue(),
                startDate.getDayOfMonth(), Integer.parseInt(startHour), Integer.parseInt(startMin));
            LocalDateTime edt = LocalDateTime.of(endDate.getYear(), endDate.getMonthValue(),
                endDate.getDayOfMonth(), Integer.parseInt(endHour), Integer.parseInt(endMin));
            //st.setDate(6,java.sql.Timestamp.valueOf(sdt));
            st.setTimestamp(6,java.sql.Timestamp.valueOf(sdt));
            st.setTimestamp(7, java.sql.Timestamp.valueOf(edt));
            st.setString(8, TypeTxt.getText());
            st.setInt(9, currentUser.getId());
            st.setTimestamp(10, new Timestamp(System.currentTimeMillis()));
            st.setString(11, currentUser.getName());
            st.setTimestamp(12, new Timestamp(System.currentTimeMillis()));
            st.setString(13, currentUser.getName());
            st.setString(14, "");

            st.execute();
            
            ResultSet rs = st.getGeneratedKeys();
            if(rs.next())
            {
                //populateSelectedAppointment();
                UpdateAppointmentList();
            
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.initStyle(StageStyle.UTILITY);
                alert.setTitle("Success!");
                alert.setHeaderText(null);
                alert.setGraphic(null);
                alert.setContentText("Customer updated successfully. ");

                alert.showAndWait();
            }  
        }
        catch(SQLException e)
        {
            String ex;
            ex = e.getMessage();
        }
    }
    
    public void UpdateAppt()throws ClassNotFoundException
    {
        try(Connection conn = DriverManager.getConnection(conUrl,user,pass))
        {
            Class.forName(driver);
            //conn = DriverManager.getConnection(conUrl,user,pass);
            int addAppt = 0;
            String startHour = cboHourStart.getValue();
            String startMin = cboMinStart.getValue();
            String endHour = cboHourEnd.getValue();
            String endMin = cboMinEnd.getValue();
            LocalDate startDate = StartDatePicker.getValue();
            LocalDate endDate = EndDatePicker.getValue();

            String sql = "update appointment " +
                         "set customerId = ?, title = ?, description = ?, location = ?, contact = ? , start = ?, end = ?, type = ?, UserId = ?, lastUpdate = ?, lastUpdateBy = ? " +
                         "where appointmentid = ?";
            PreparedStatement st = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            st.setInt(1, CustomerCmbBx.getSelectionModel().getSelectedItem().getId());
            st.setString(2, TitleTxt.getText());
            st.setString(3,DescriptionTxt.getText());
            st.setString(4, LocationTxt.getText());
            st.setString(5, ContactTxt.getText());
            LocalDateTime sdt = LocalDateTime.of(startDate.getYear(), startDate.getMonthValue(),
                startDate.getDayOfMonth(), Integer.parseInt(startHour), Integer.parseInt(startMin));
            LocalDateTime edt = LocalDateTime.of(endDate.getYear(), endDate.getMonthValue(),
                endDate.getDayOfMonth(), Integer.parseInt(endHour), Integer.parseInt(endMin));
            //st.setDate(6,java.sql.Timestamp.valueOf(sdt));
            st.setTimestamp(6,java.sql.Timestamp.valueOf(sdt));
            st.setTimestamp(7, java.sql.Timestamp.valueOf(edt));
            st.setString(8, TypeTxt.getText());
            st.setInt(9, currentUser.getId());
            st.setDate(10, new java.sql.Date(Calendar.getInstance().getTime().getTime()));
            st.setString(11, currentUser.getName());
            st.setInt(12, selectedAppointment.getId());

            addAppt = st.executeUpdate();
            
            if(addAppt > 0)
            {
                //populateSelectedAppointment();
                UpdateAppointmentList();
            
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.initStyle(StageStyle.UTILITY);
                alert.setTitle("Success!");
                alert.setHeaderText(null);
                alert.setGraphic(null);
                alert.setContentText("Customer updated successfully. ");

                alert.showAndWait();
            }           
        }
        catch(SQLException e)
        {
            String ex;
            ex = e.getMessage();
        }
    }
    
    public void setUser(User u)
    {
        currentUser = u;
    }
    
    public void setSelectedAppointment(Appointment app)
    {
        selectedAppointment = app;
        populateSelectedAppointment();
    }
    
    private void CloseWindow()
    {
       Stage stage = (Stage) CancelBtn.getScene().getWindow();
       Stage parent = (Stage) CancelBtn.getParent().getScene().getWindow();
       stage.close();
       parent.toFront();
    }
    
    private void Reset()
    {
        TitleTxt.setText("");
        DescriptionTxt.setText("");
        LocationTxt.setText("");
        TypeTxt.setText("");
        ContactTxt.setText("");
        StartDatePicker.setValue(LocalDate.now());
        CustomerCmbBx.getSelectionModel().clearSelection(); 
        EndDatePicker.setValue(LocalDate.now());
    }
    
    private void UpdateAppointmentList() throws ClassNotFoundException
    {
        PopulateAppointments();
        appointmentTableView.getItems().setAll(availableAppointments);
    }  
    
    private boolean checkChange()
    {
        boolean dirty = false; 
        
        if(selectedAppointment == null || selectedAppointment.getId() == 0)
        {
            dirty = true;
            newAppt = true; 
        }
        else
        {
            if(!selectedAppointment.getTitle().equals(TitleTxt.getText()))
            {
                dirty = true;
            }
            if(!selectedAppointment.getDescription().equals(DescriptionTxt.getText()))
            {
                dirty = true;
            }
            if(!selectedAppointment.getLocation().equals(LocationTxt.getText()))
            {
                dirty = true;
            }
            if(!selectedAppointment.getType().equals(TypeTxt.getText()))
            {
                dirty = true;
            }
            if(!selectedAppointment.getContact().equals(ContactTxt.getText()))
            {
                dirty = true;
            }
            if(selectedAppointment.getCustomerId() != CustomerCmbBx.getSelectionModel().getSelectedItem().getId());
            {
                dirty = true;
            }
            
            if(LocalDate.parse(selectedAppointment.getStartDate(), format) != StartDatePicker.getValue())
            {
                dirty = true;
            }
            
            if(LocalDate.parse(selectedAppointment.getEndDate(), formatDt) != EndDatePicker.getValue())
            {
                dirty = true;
            }
        }
        
        return dirty;
    }
    
    private boolean Validate()
    {
        boolean valid = true; 
        String message = "";
        String startHour = cboHourStart.getValue();
        String startMin = cboMinStart.getValue();
        String endHour = cboHourEnd.getValue();
        String endMin = cboMinEnd.getValue();
        int strHr;
        int strMin;
        
        LocalDate startDate = StartDatePicker.getValue();
        LocalDate endDate = EndDatePicker.getValue();
        LocalDateTime sdt = LocalDateTime.of(startDate.getYear(), startDate.getMonthValue(),
                startDate.getDayOfMonth(), Integer.parseInt(startHour), Integer.parseInt(startMin));
        LocalDateTime edt = LocalDateTime.of(endDate.getYear(), endDate.getMonthValue(),
                endDate.getDayOfMonth(), Integer.parseInt(endHour), Integer.parseInt(endMin));
        
        if(CustomerCmbBx.getSelectionModel().getSelectedItem() == null)
        {
            message += "A customer must be selected.\n";
            valid = false; 
        }
        else
        {
            checkOverlap(sdt, edt);
        }
        
        if(TitleTxt.getText().isEmpty())
        {
            message += "A title must be entered.\n";
            valid = false; 
        }
        
        if(DescriptionTxt.getText().isEmpty())
        {
            message += "A description must be entered.\n";
            valid = false; 
        }
        
        if(LocationTxt.getText().isEmpty())
        {
            message += "A location must be entered.\n";
            valid = false; 
        }
        
        if(TypeTxt.getText().isEmpty())
        {
            message += "An appointment type must be entered.\n";
            valid = false; 
        }

        if(ContactTxt.getText().isEmpty())
        {
            message += "An appointment contact must be entered.\n";
            valid = false; 
        }
        
        if(sdt.getDayOfWeek() == DayOfWeek.SUNDAY || sdt.getDayOfWeek() == DayOfWeek.SATURDAY)
        {
            message += "Appointment is outside of bussiness hours.\n";
            valid = false; 
        }
        
        if(Integer.parseInt(startHour) > 17)
        {
            message += "Appointment is outside of bussiness hours.\n";
            valid = false;             
        }
        if(Integer.parseInt(startHour) < 8)
        {
            message += "Appointment is outside of bussiness hours.\n";
            valid = false;             
        }
        
       
        return valid;
    }
    
    private boolean checkOverlap(LocalDateTime start, LocalDateTime end)
    {
        boolean overlap = false;
        
        availableAppointments.stream().
                filter(x -> x.getCustomerId() == CustomerCmbBx.getSelectionModel().getSelectedItem().getId()).
                forEach(x -> 
                {

                    
                });



        return overlap;
    }
}
