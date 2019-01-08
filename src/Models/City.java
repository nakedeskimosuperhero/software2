/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

import java.time.LocalDateTime;
import java.util.HashSet;
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
public class City extends BaseClass {

    private StringProperty city = new SimpleStringProperty(); 
    public final String getCity(){return city.get();}
    public final void setCity(String value){city.set(value);} 
    public StringProperty getCityProperty(){return city;}
    
    private IntegerProperty cityId = new SimpleIntegerProperty();
    public final int getCityId(){return cityId.get();}
    public final void setCityId(int value){cityId.set(value);}
    public IntegerProperty getCityIdProperty(){return cityId;}
    
     private IntegerProperty countryId = new SimpleIntegerProperty();
    public final int getCountryId(){return countryId.get();}
    public final void setCountryId(int value){countryId.set(value);}
    public IntegerProperty getCountryIdProperty(){return countryId;}
    
    private ObjectProperty<Country> country = new SimpleObjectProperty();
    public final Country getCountry(){return country.get();}
    public final void setCountry(Country value){country.set(value);}
    
    public City()
    {
        
    }
    
    public City(String c, int id, int countryId)
    {
        this.setCity(c);
        this.setCityId(id);
        this.setCountryId(countryId);   
    }
    
}
