package View_Controller;

import Models.Address;
import Models.City;
import Models.CityConverter;
import Models.Country;
import Models.Customer;
import Models.User;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.regex.Pattern;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class CustomerEditViewController implements Initializable {

    @FXML
    private Button AddUserBtn;
    
    @FXML
    private Button DeleteBtn;
        
    @FXML
    private Button CancelBtn;
        
    @FXML
    private Button SaveUserBtn;
    
    @FXML
    private Button UserBtn;
    
    @FXML
    private CheckBox activeCkBx;
    
    @FXML
    private ComboBox<City> CityCmbBx;

    @FXML
    private ComboBox<Country> CountryCmbBx;
    
    @FXML
    private Label CountryLabel;

    @FXML
    private TableColumn<?, ?> customerName;
    
    @FXML
    private TableView<Customer> CustomerTableView;
    
    @FXML
    private TextField AddressTxt;
    
    @FXML
    private TextField Address2Txt;

    @FXML
    private TextField CityTxt;

    @FXML
    private TextField CountryTxt;
         
    @FXML
    private TextField NameTxt;
    
    @FXML
    private TextField PostalCodeTxt;
    
    @FXML
    private TextField PhoneTxt;
    
    private Boolean newCust = false;
    private Boolean updateCust = false;
    private Boolean updateAdd = false;
    private Connection conn = null;
    private City selectedCity;
    private Country selectedCountry;
    private Customer selectedCustomer;  
    private ObservableList<Customer> availableCustomers;
    private ObservableList<City> availableCities; 
    private ObservableList<Country> availableCountries; 
    private final String driver = "com.mysql.jdbc.Driver";
    private final String db = "U04Goc";
    private final String conUrl = "jdbc:mysql://52.206.157.109/" + db;
    private final String user = "U04Goc";
    private final String pass = "53688232399"; 
    private User currentUser;
    //private Boolean updateCity = false; 
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try
        {
            PopulateCustomers();
            PopulateCity();
            CityCmbBx.setConverter(new CityConverter());
            CityCmbBx.setItems(availableCities);
            CountryLabel.setText("");
            customerName.setCellValueFactory(new PropertyValueFactory<>("name"));
            CustomerTableView.getItems().setAll(availableCustomers);
        }
        catch(ClassNotFoundException e)
        {
            
        }
        catch(Exception e)
        {
            String test = e.getMessage();
        }
    }  
    
    @FXML
    void AddCustomer(ActionEvent event) 
    {
        selectedCustomer = new Customer();
        Reset();
    }
    
    @FXML       
    void Cancel(ActionEvent event) 
    {
        CloseWindow();
    }

    @FXML
    void DeleteCustomer(ActionEvent event) throws ClassNotFoundException  {
        if(selectedCustomer == null || selectedCustomer.getId() == 0)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("No customer selected.");
            alert.showAndWait();
        }
        else
        {
            try(Connection conn = DriverManager.getConnection(conUrl,user,pass))
            {
                Class.forName(driver);
                //conn = DriverManager.getConnection(conUrl,user,pass);
                int deleteCount = 0;

                PreparedStatement st = conn.prepareStatement("Delete from customer where customerid = ?");
                st.setInt(1,selectedCustomer.getId());
                deleteCount = st.executeUpdate();
                
                if(deleteCount > 0)
                {
                    UpdateCustomerList();
                    Reset(); 
                }
            }
            catch(SQLException e)
            {

            }
        }
    }

    @FXML
    void SaveCustomer(ActionEvent event)throws ClassNotFoundException  {

        if(checkChange())
        {
            if(newCust == null || newCust == false )
            {
                UpdateCustomer();
            }
            else if(newCust == true)
            {
                AddCustomer();   
            }
        }
    }
    
    @FXML
    void UpdateCountry(ActionEvent event) 
    {
        selectedCity = CityCmbBx.selectionModelProperty().getValue().getSelectedItem();
        String test = selectedCity.getCountry().getCountry();
        CountryLabel.setText(selectedCity.getCountry().getCountry());
    }
    
    public void CustomerSelection()
    {
       selectedCustomer =  CustomerTableView.getSelectionModel().getSelectedItem();
       populateSelectedCustomer();
        
    }
    
    public void setUser(User u)
    {
        currentUser = u;
    }
    
    private void PopulateCountry ()throws ClassNotFoundException
    {
        try (Connection conn = DriverManager.getConnection(conUrl,user,pass))
        {
            Class.forName(driver);
            //conn = DriverManager.getConnection(conUrl,user,pass);
            
            Statement st = conn.createStatement(
            ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            String sql = "Select c.country, c.countryId FROM country as c";

            ResultSet r = st.executeQuery(sql);
            availableCountries = FXCollections.observableArrayList();
            while(r.next())
            {
                Country c = new Country();
                c.setCountry(r.getString("country"));
                c.setCountryId(r.getInt("countryId"));
             
                availableCountries.add(c);
            }  
        } 
        catch (SQLException e) {
            System.out.println("SQLException: "+e.getMessage());
            System.out.println("SQLState: "+e.getSQLState());
            System.out.println("VendorError: "+e.getErrorCode());
 
        }
    }
    
    private void PopulateCity ()throws ClassNotFoundException
    {
        try (Connection conn = DriverManager.getConnection(conUrl,user,pass))
        {
            Class.forName(driver);
            //conn = DriverManager.getConnection(conUrl,user,pass);
            
            Statement st = conn.createStatement(
            ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            String sql = "Select c.cityId, c.city, c.countryId FROM city as c";

            ResultSet r = st.executeQuery(sql);
            availableCities = FXCollections.observableArrayList();
            PopulateCountry();
            while(r.next())
            {
                City c = new City(r.getString("city"), r.getInt("cityId"), r.getInt("countryId")) ;
                Optional<Country> cntry = availableCountries.stream().filter(p -> p.getCountryId() == c.getCountryId()).findFirst();
                c.setCountry(cntry.isPresent() ? cntry.get() : null);
             
                availableCities.add(c);
            }           
        } 
        catch (SQLException e) {
            System.out.println("SQLException: "+e.getMessage());
            System.out.println("SQLState: "+e.getSQLState());
            System.out.println("VendorError: "+e.getErrorCode());
 
        }
    }
     
    private void PopulateCustomers ()throws ClassNotFoundException
    {
        try(Connection conn = DriverManager.getConnection(conUrl,user,pass)) 
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
    
    private void populateSelectedCustomer()
    {
        NameTxt.setText(selectedCustomer.getName());
        AddressTxt.setText(selectedCustomer.getAddress().getAddress());
        Address2Txt.setText(selectedCustomer.getAddress().getAddress2());
        CityCmbBx.getSelectionModel().select(selectedCustomer.getAddress().getCity().getCityId()-1);
        //CountryCmbBx.getSelectionModel().select(selectedCustomer.getAddress().getCity().getCountryId() - 1);
        CountryLabel.setText(selectedCustomer.getAddress().getCity().getCountry().getCountry());
        PostalCodeTxt.setText(selectedCustomer.getAddress().getPostalCode());
        PhoneTxt.setText(selectedCustomer.getAddress().getPhone());
        activeCkBx.setSelected(selectedCustomer.getActive());
    }
    
    private boolean checkChange()
    {
        boolean dirty = false; 
        
        if(selectedCustomer == null || selectedCustomer.getId() == 0)
        {
            dirty = true;
            newCust = true; 
        }
        else
        {
            String selName = selectedCustomer.getName();
            String nmTxt = NameTxt.getText();
            boolean test = selectedCustomer.getName().equals(NameTxt.getText());
            if(!selectedCustomer.getName().equals(NameTxt.getText()))
            {
                dirty = true;
                updateCust = true;
            }
            if(!selectedCustomer.getAddress().getAddress().equals(AddressTxt.getText()))
            {
                dirty = true;
                updateAdd = true;
            }
            if(!selectedCustomer.getAddress().getAddress2().equals(Address2Txt.getText()))
            {
                dirty = true;
                updateAdd = true;
            }
            if(!selectedCustomer.getAddress().getCity().getCity().equals(CityCmbBx.getSelectionModel().getSelectedItem().getCity()))
            {
                dirty = true;
                updateAdd = true;
            }
//            if(!selectedCustomer.getAddress().getCity().getCountry().getCountry().equals((CountryCmbBx.getSelectionModel().getSelectedItem().getCountry())))
//            {
//                dirty = true;
//                updateCity = true;
//            }
            if(!selectedCustomer.getAddress().getPostalCode().equals(PostalCodeTxt.getText()))
            {
                dirty = true;
                updateAdd = true;
            }
            if(!selectedCustomer.getAddress().getPhone().equals(PhoneTxt.getText()))
            {
                dirty = true;
                updateAdd = true;
            }
        }
        
        return dirty;
    }
    
    private void AddCustomer() throws ClassNotFoundException 
    {
        if(Validate())
        {
            try(Connection conn = DriverManager.getConnection(conUrl,user,pass))
            {
                Class.forName(driver);
                //conn = DriverManager.getConnection(conUrl,user,pass);

                String sql = "Insert into address (address, address2, cityId, postalCode, phone, createDate, createdBy, lastUpdate, lastUpdateBy) "
                        + "Values(?,?,?,?,?,?,?,?,?)";
                PreparedStatement st = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                st.setString(1, AddressTxt.getText());
                st.setString(2, Address2Txt.getText());
                st.setInt(3,CityCmbBx.getSelectionModel().getSelectedItem().getCityId());
                st.setString(4, PostalCodeTxt.getText());
                st.setString(5, PhoneTxt.getText());
                st.setDate(6, new java.sql.Date(Calendar.getInstance().getTime().getTime()));
                st.setString(7, currentUser.getName());
                st.setDate(8, new java.sql.Date(Calendar.getInstance().getTime().getTime()));
                st.setString(9, currentUser.getName());
                st.execute();

                ResultSet rs = st.getGeneratedKeys();
                int addId = 0;
                if(rs.next())
                {
                    addId = rs.getInt(1);
                }
                else
                {

                }

                if(addId != 0)
                {
                    String custSql = "Insert into customer (customerName, addressId, active, createDate, createdBy, lastUpdate, lastUpdateBy)"
                        + "Values(?,?,?,?,?,?,?)";
                    PreparedStatement custSt = conn.prepareStatement(custSql, Statement.RETURN_GENERATED_KEYS);
                    custSt.setString(1, NameTxt.getText());
                    custSt.setInt(2, addId);
                    custSt.setBoolean(3, activeCkBx.isSelected());
                    custSt.setDate(4, new java.sql.Date(Calendar.getInstance().getTime().getTime()));
                    custSt.setString(5, "Current User");
                    custSt.setDate(6, new java.sql.Date(Calendar.getInstance().getTime().getTime()));
                    custSt.setString(7, "Current User");
                    custSt.execute();
                }

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.initStyle(StageStyle.UTILITY);
                alert.setTitle("Success!");
                alert.setHeaderText(null);
                alert.setGraphic(null);
                alert.setContentText("Customer saved successfully. ");

                alert.showAndWait();
                UpdateCustomerList();   
            }
            catch(SQLException e)
            {
                String message = e.getMessage();
            }
            catch(Exception e)
            {
                String message = e.getMessage();
            }
        }
    }
          
    private int convertBoolToInt(boolean value)
    {
        if(value == true)
            return 1; 
        else 
            return 0;
    }
    
    private void Reset()
    {
        NameTxt.setText("");
        AddressTxt.setText("");
        Address2Txt.setText("");
        PostalCodeTxt.setText("");
        PhoneTxt.setText("");
        activeCkBx.setSelected(true);
        CityCmbBx.getSelectionModel().clearSelection(); 
        CountryLabel.setText("");
    }
    
    private void CloseWindow()
    {
       Stage stage = (Stage) CancelBtn.getScene().getWindow();
       Stage parent = (Stage) CancelBtn.getParent().getScene().getWindow();
       stage.close();
       parent.toFront();
    }
    
    private void UpdateCustomer() throws ClassNotFoundException 
    {
        try(Connection conn = DriverManager.getConnection(conUrl,user,pass))
        {
            Class.forName(driver);
            //conn = DriverManager.getConnection(conUrl,user,pass);
            int custUpt = 0;
            int addUpt = 0;
            if(Validate())
            {
                if(updateAdd)
                {
                    String sql = "Update address set address = ?, address2 = ?, cityId = ?, postalCode = ?, phone = ?, lastUpdate = ?, lastUpdateBy = ? Where addressid = ?";
                    PreparedStatement st = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                    st.setString(1, AddressTxt.getText());

                    st.setString(2, Address2Txt.getText());
                    st.setInt(3,CityCmbBx.getSelectionModel().getSelectedItem().getCityId());
                    st.setString(4, PostalCodeTxt.getText());
                    st.setString(5, PhoneTxt.getText());
                    st.setDate(6, new java.sql.Date(Calendar.getInstance().getTime().getTime()));
                    st.setString(7, currentUser.getName());
                    st.setInt(8, selectedCustomer.getAddress().getAddressId());

                   addUpt = st.executeUpdate();
                }

                if(updateCust)
                {
                    String updateCust = "Update customer Set customerName = ?, active = ? Where customerid = ?";
                    PreparedStatement st = conn.prepareStatement(updateCust, Statement.RETURN_GENERATED_KEYS);
                    st.setString(1, NameTxt.getText());
                    st.setInt(2, convertBoolToInt(activeCkBx.isSelected()));
                    st.setInt(3, selectedCustomer.getId());

                    custUpt = st.executeUpdate();
                }

                if(custUpt > 0 || addUpt > 0)
                {
                    UpdateCustomerList();


                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.initStyle(StageStyle.UTILITY);
                alert.setTitle("Success!");
                alert.setHeaderText(null);
                alert.setGraphic(null);
                alert.setContentText("Customer updated successfully. ");

                alert.showAndWait();
                }
            }
        }
        catch(SQLException e)
        {
            String ex;
            ex = e.getMessage();
        }
    }
    
    private void UpdateCustomerList() throws ClassNotFoundException
    {
        PopulateCustomers();
        CustomerTableView.getItems().setAll(availableCustomers);
    } 
    
    private boolean Validate()
    {
        boolean valid = true; 
        String message = "";
        
        if(NameTxt.getText().isEmpty())
        {
            valid = false; 
            message += "Must enter a customer Name.\n";
        }
        if(AddressTxt.getText().isEmpty())
        {
            valid = false; 
            message += "Must enter a valid street address.\n";
        }
        if(selectedCity == null)
        {
            valid = false;
            message += "Must select a City.\n";
        }
        if(PostalCodeTxt.getText().isEmpty())
        {
            valid = false;
            message += "Must enter a valid postal code.\n";
        }
        else
        {
            try
            {
                Integer.parseInt(PostalCodeTxt.getText()); 
            }
            catch(NumberFormatException e)
            {
                valid = false;
                message += "Must enter a valid postal code.\n";
            }
        }
        if(PhoneTxt.getText().isEmpty())
        {
            valid = false;
            message += "Must entar a phone number.\n";
        }
        else
        {
            String regEx = "^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{4})$";//https://stackoverflow.com/questions/42104546/java-regular-expressions-to-validate-phone-numbers
            Pattern pat =  Pattern.compile(regEx);
            if(!pat.matcher(PhoneTxt.getText()).matches())
            {
                valid =false; 
                message += "Invalid phone number.\n";
            }
        }
        
        if(!message.isEmpty())
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initStyle(StageStyle.UTILITY);
            alert.setTitle("Invalid Entry");
            alert.setHeaderText(null);
            alert.setGraphic(null);
            alert.setContentText(message);

            alert.showAndWait(); 
        }
        return valid; 
    }
}

