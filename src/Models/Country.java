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
public class Country extends BaseClass{
        
    private StringProperty country = new SimpleStringProperty(); 
    public final String getCountry(){return country.get();}
    public final void setCountry(String value){country.set(value);} 
    public StringProperty getCountryProperty(){return country;}
    
    private IntegerProperty countryId = new SimpleIntegerProperty();
    public final int getCountryId(){return countryId.get();}
    public final void setCountryId(int value){countryId.set(value);}
    public IntegerProperty getCountryIdProperty(){return countryId;}
    
    
}
