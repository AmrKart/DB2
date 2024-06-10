/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package homework;

/**
 *
 * @author Amr
 */
public class inputValidation {
    
    static public boolean isValidNumber(String number){
        
        String regex = "[0-9]+";    
        
        return number.matches(regex);                
    }
    
    static public boolean isValidPhoneNumber(String phoneNumber){
        
        String regex = "\\d{10}";
                
        return phoneNumber.matches(regex);
    }
    
    static public boolean isValidEmail (String email) {
        
        String regex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        
        return email.matches(regex);
    }
    
    
    static public boolean isValidDate (String date){
        
        String regex = "^\\d{4}-\\d{2}-\\d{2}$";
                
        return date.matches(regex);
    }
}
