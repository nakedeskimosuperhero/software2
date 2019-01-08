/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

import java.time.LocalDateTime;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Nick
 */
public class Address extends BaseClass {
    //public int addressId;
    //public String address2;
    
    private IntegerProperty addressId = new SimpleIntegerProperty();
    public final int getAddressId(){return addressId.get();}
    public final void setAddressId(int value){addressId.set(value);}
    public IntegerProperty getAddressIdProperty(){return addressId;}
    
    private StringProperty address = new SimpleStringProperty(); 
    public final String getAddress(){return address.get();}
    public final void setAddress(String value){address.set(value);} 
    public StringProperty getAddressProperty(){return address;}

    private StringProperty address2 = new SimpleStringProperty(); 
    public final String getAddress2(){return address2.get();}
    public final void setAddress2(String value){address2.set(value);} 
    public StringProperty getAddress2Property(){return address2;}

    private ObjectProperty<City> city = new SimpleObjectProperty();
    public final City getCity(){return city.get();}
    public final void setCity(City value){city.set(value);}
    
    private StringProperty postalCode = new SimpleStringProperty(); 
    public final String getPostalCode(){return postalCode.get();}
    public final void setPostalCode(String value){postalCode.set(value);} 
    
    private StringProperty phone = new SimpleStringProperty(); 
    public final String getPhone(){return phone.get();}
    public final void setPhone(String value){phone.set(value);} 

}
