/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

import java.time.LocalDate;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Nick
 */

public class Customer {
    
    private  IntegerProperty id = new SimpleIntegerProperty(); 
    public final int getId(){return id.get();}
    public final void setId(int value){id.set(value);}   
    public IntegerProperty getIdProperty(){return id;}
    
    private StringProperty name = new SimpleStringProperty(); 
    public final String getName(){return name.get();}
    public final void setName(String value){name.set(value);} 
    public StringProperty getNameProperty(){return name;}
    
    private ObjectProperty<Address> Addr = new SimpleObjectProperty();
    public final Address getAddress(){return Addr.get();}
    public final void setAddress(Address value){Addr.set(value);}
    
    private BooleanProperty active = new SimpleBooleanProperty();
    public final boolean getActive(){return active.get();}
    public final void setActive(boolean value){active.set(value);}
    
    private ObjectProperty<LocalDate> createDate = new SimpleObjectProperty();
    public final LocalDate getCreateDate(){return createDate.get();}
    public final void setCreateDate(LocalDate value){createDate.set(value);}
    
    private StringProperty createdBy = new SimpleStringProperty(); 
    public final String getCreatedBy(){return createdBy.get();}
    public final void setCreatedBy(String value){createdBy.set(value);} 

    private ObjectProperty<LocalDate> lastUpdate = new SimpleObjectProperty();
    public final LocalDate getLastUpdate(){return lastUpdate.get();}
    public final void setLastUpdate(LocalDate value){lastUpdate.set(value);}
    
    private StringProperty lastUpdateBy = new SimpleStringProperty(); 
    public final String getLastUpdateBy(){return lastUpdateBy.get();}
    public final void setLastUpdateBy(String value){lastUpdateBy.set(value);}
    
}
