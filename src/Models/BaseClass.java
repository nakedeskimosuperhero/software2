/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Nick
 */
public class BaseClass {
    
    private StringProperty createDate = new SimpleStringProperty(); 
    public final String getCreateDate(){return createDate.get();}
    public final void setCreateDate(String value){createDate.set(value);} 
    public StringProperty getCreateDateProperty(){return createDate;}
    
    private StringProperty createdBy = new SimpleStringProperty(); 
    public final String getCreatedBy(){return createdBy.get();}
    public final void setCreatedBy(String value){createdBy.set(value);} 
    public StringProperty getCreatedByProperty(){return createdBy;}
    
    private StringProperty lastUpdate = new SimpleStringProperty(); 
    public final String getLastUpdate(){return lastUpdate.get();}
    public final void setLastUpdate(String value){lastUpdate.set(value);} 
    public StringProperty getLastUpdateProperty(){return lastUpdate;}
    
    private StringProperty lastUpdateBy = new SimpleStringProperty(); 
    public final String getLastUpdateBy(){return lastUpdateBy.get();}
    public final void setLastUpdateBy(String value){lastUpdateBy.set(value);} 
    public StringProperty getLastUpdateByProperty(){return lastUpdateBy;}
    
}
