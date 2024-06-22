/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package homework;

import java.util.ArrayList;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import javax.swing.JOptionPane;
/**
 *
 * @author Amr
 */
public class DataBaseHelper {
    
    private static String url= "jdbc:oracle:thin:@localhost:1521:XE";
    private static String userName = "system";
    private static String password = "admin";       
    
    public static Connection getConnection(){
        try {
            return (Connection) DriverManager.getConnection(url,userName,password);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.WARNING_MESSAGE);
            return null;
        }        
    }    
    
    public static JTable getTableByName(String tableName) {       
    ArrayList<String> columnNames = new ArrayList<>();
    ArrayList<ArrayList<Object>> data = new ArrayList<>();

    try (Connection conn = getConnection();
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(OracleScripts.getTableScript(tableName))) {

        ResultSetMetaData md = rs.getMetaData();
        int columns = md.getColumnCount();

        // Get column names
        for (int i = 1; i <= columns; i++) {
            columnNames.add(md.getColumnName(i));
        }        
        // Get row data        
        while (rs.next()) {
            ArrayList<Object> row = new ArrayList<>(columns);
            for (int i = 1; i <= columns; i++) {
                row.add(rs.getObject(i));
            }            
            data.add(row);
        }
        
        Object[][] dataArray = new Object[data.size()][];
        for (int i = 0; i < data.size(); i++) {
            ArrayList<Object> row = data.get(i);
            dataArray[i] = row.toArray(new Object[row.size()]);
        }
       
        DefaultTableModel model = new DefaultTableModel(dataArray, columnNames.toArray());
        return new JTable(model);

    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.WARNING_MESSAGE);
        return new JTable();
    }
}

    
    public static ArrayList<String> getColumnsNames (String tableName) {
        ArrayList<String> columnNames = new ArrayList<>();                
        
         try(
            Connection conn = getConnection();

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(OracleScripts.getTableScript(tableName))) {

            ResultSetMetaData md = rs.getMetaData();
            int columns = md.getColumnCount();

            // Get column names
            for (int i = 1; i <= columns; i++) {
                columnNames.add(md.getColumnName(i));
            }
            
            conn.close();
            
            return columnNames;
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.WARNING_MESSAGE);
            return new ArrayList<String>();
        }
    }
    
    public static boolean validateInput (String tableName, ArrayList<String> columnsValue){                
        
       try(
            Connection conn = getConnection();

            Statement stmt = conn.createStatement();               
            ResultSet rs = stmt.executeQuery(OracleScripts.getTableScript(tableName))) {

            ResultSetMetaData md = rs.getMetaData();
            int columns = md.getColumnCount();
            
            int indexOfColumnsValue = 0;
            
            for (int i = 1; i <= columns; i++) {
                if(i==1)
                    continue;                
                String colName = md.getColumnName(i);
                String colType = md.getColumnTypeName(i);                                            
                
                switch (colType){
                    case "NUMBER":
                        if(!inputValidation.isValidNumber(columnsValue.get(indexOfColumnsValue)))                        
                            throw new SQLException(colName+" should be Number");                                                   
                        break;
                    case "DATE":                        
                        if(!inputValidation.isValidDate(columnsValue.get(indexOfColumnsValue)))
                            throw new SQLException(colName+" should be Date of format YYYY-MM-DD");                           
                        
                        break;
                    case "VARCHAR2":
                        if(colName.equals("PHONENUMBER")){
                            if(!inputValidation.isValidPhoneNumber(columnsValue.get(indexOfColumnsValue)))
                                throw new SQLException(colName+ " should be valid phone Number");
                        }
                        
                        if(colName.equals("EMAIL")){
                            if(!inputValidation.isValidEmail(columnsValue.get(indexOfColumnsValue)))
                                throw new SQLException(colName+ " should be valid email");
                        }
                        break;
                }
                
                indexOfColumnsValue++;
            }
            
            conn.close();
            
            return true;
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.WARNING_MESSAGE);
            return false;
        }
    }
    
    public static void insert(String tableName, ArrayList<String> columnsValue){
                
        
         try{
             Connection conn = getConnection();
             Statement stmt = conn.createStatement();                             
             
             int result = stmt.executeUpdate(OracleScripts.getInsertScript(tableName, columnsValue));
             
             if(result> 0){
                 JOptionPane.showMessageDialog(null, "row inserted successfully", null, JOptionPane.INFORMATION_MESSAGE);                 
             }
             else{
                 throw new SQLException("something went wrong.. try agian");
             }
             
            conn.close();
            return;             
         }catch (SQLException e){
            String message = e.getMessage();
            String errorCode = e.getSQLState();

            if ("23000".equals(errorCode)) { // ORA-02291 corresponds to error code 23000
                message = "Enter Valid Foreign Key";
            }
            
            JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.WARNING_MESSAGE);
            return;
         }
    }
    
    public static void createView()
    {
        try{
            Connection conn = getConnection();
            Statement stmt = conn.createStatement();   
            
            
            stmt.executeUpdate(OracleScripts.getCreateViewScript());                        
            JOptionPane.showMessageDialog(null, "view created successfully", null, JOptionPane.INFORMATION_MESSAGE);                                                                       
            conn.close();             
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.WARNING_MESSAGE);            
        }        
    }
    
    public static void createTrigger(){
        
        try{
            Connection conn = getConnection();
            Statement stmt = conn.createStatement();   
            
            stmt.executeUpdate(OracleScripts.getCreateTriggerScript());                        
            
            JOptionPane.showMessageDialog(null, "trigger created successfully", null, JOptionPane.INFORMATION_MESSAGE);                                                                       
            conn.close();             
            
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.WARNING_MESSAGE);            
        }
        
    }
    
}
