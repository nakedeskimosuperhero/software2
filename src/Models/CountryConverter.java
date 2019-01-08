/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

import javafx.util.StringConverter;

/**
 *
 * @author Nick
 */
public class CountryConverter extends StringConverter<Country>  {
     
    public String toString(Country ctry)
    {
        return ctry.getCountry();
    }
    
    public Country fromString(String str)
    {
        return new Country();
    }
}
