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
public class CustomerConverter extends StringConverter<Customer>  {
    
    public String toString(Customer cust)
    {
        return cust.getName();
    }
    
    public Customer fromString(String str)
    {
        return new Customer();
    }
    
}
