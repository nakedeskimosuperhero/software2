/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

import java.time.LocalDateTime;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Nick
 */
public class Appointment extends BaseClass {
    
    private  IntegerProperty id = new SimpleIntegerProperty(); 
    public final int getId(){return id.get();}
    public final void setId(int value){id.set(value);}   
    public IntegerProperty getIdProperty(){return id;}
    
    private StringProperty consultant = new SimpleStringProperty(); 
    public final String getConsultant(){return consultant.get();}
    public final void setConsultant(String value){consultant.set(value);} 
    public StringProperty getConsultantProperty(){return consultant;}
    
    private  IntegerProperty customerId = new SimpleIntegerProperty(); 
    public final int getCustomerId(){return customerId.get();}
    public final void setCustomerId(int value){customerId.set(value);}   
    public IntegerProperty getCustomerIdProperty(){return customerId;}
    
    private  IntegerProperty userId = new SimpleIntegerProperty(); 
    public final int getUserId(){return userId.get();}
    public final void setUserId(int value){userId.set(value);}   
    public IntegerProperty getUserIdProperty(){return userId;}
    
    private StringProperty title = new SimpleStringProperty(); 
    public final String getTitle(){return title.get();}
    public final void setTitle(String value){title.set(value);} 
    public StringProperty getTitleProperty(){return title;}
    
    private StringProperty customerName = new SimpleStringProperty(); 
    public final String getCustomerName(){return customerName.get();}
    public final void setCustomerName(String value){customerName.set(value);} 
    public StringProperty getCustomerNameProperty(){return customerName;}
    
    private StringProperty description = new SimpleStringProperty(); 
    public final String getDescription(){return description.get();}
    public final void setDescription(String value){description.set(value);} 
    public StringProperty getDescriptionProperty(){return description;}
    
    private StringProperty location = new SimpleStringProperty(); 
    public final String getLocation(){return location.get();}
    public final void setLocation(String value){location.set(value);} 
    public StringProperty getLocationProperty(){return location;}
    
    private StringProperty contact = new SimpleStringProperty(); 
    public final String getContact(){return contact.get();}
    public final void setContact(String value){contact.set(value);} 
    public StringProperty getContactProperty(){return contact;}
    
    private StringProperty type = new SimpleStringProperty(); 
    public final String getType(){return type.get();}
    public final void setType(String value){type.set(value);} 
    public StringProperty getTypeProperty(){return type;}
    
    private StringProperty url = new SimpleStringProperty(); 
    public final String getUrl(){return url.get();}
    public final void setUrl(String value){url.set(value);} 
    public StringProperty getUrlProperty(){return url;}
    
    private StringProperty startDate = new SimpleStringProperty(); 
    public final String getStartDate(){return startDate.get();}
    public final void setStartDate(String value){startDate.set(value);} 
    public StringProperty getStartDateProperty(){return startDate;}
    
    private StringProperty endDate = new SimpleStringProperty(); 
    public final String getEndDate(){return endDate.get();}
    public final void setEndDate(String value){endDate.set(value);} 
    public StringProperty getEndDateProperty(){return endDate;}
    
}
