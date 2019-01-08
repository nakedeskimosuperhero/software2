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
public class CityConverter extends StringConverter<City> {
    
    public String toString(City cty)
    {
        return cty.getCity();
    }
    
    public City fromString(String str)
    {
        return new City();
    }
}
