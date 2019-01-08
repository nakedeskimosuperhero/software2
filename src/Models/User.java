/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Nick
 */
public class User {
    private  IntegerProperty id = new SimpleIntegerProperty(); 
    public final int getId(){return id.get();}
    public final void setId(int value){id.set(value);}   
    public IntegerProperty getIdProperty(){return id;}
    
    private StringProperty name = new SimpleStringProperty(); 
    public final String getName(){return name.get();}
    public final void setName(String value){name.set(value);} 
    public StringProperty getNameProperty(){return name;}
    
    private BooleanProperty active = new SimpleBooleanProperty();
    public final boolean getActive(){return active.get();}
    public final void setActive(boolean value){active.set(value);}
    
}
