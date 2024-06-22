/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package homework;

import static homework.DataBaseHelper.getColumnsNames;
import java.util.ArrayList;

/**
 *
 * @author Amr
 */
public class OracleScripts {
        
    public static String getTableScript(String tableName){
        String sql = "SELECT * FROM " + tableName;
        return sql;
    }
    public static String getInsertScript (String tableName, ArrayList<String> columnsValue){
        
        ArrayList<String> columnsNames = DataBaseHelper.getColumnsNames(tableName);
        columnsNames.remove(0);
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("INSERT INTO ").append(tableName).append(" (");
        for (int i = 0; i < columnsNames.size(); i++) {
            queryBuilder.append(columnsNames.get(i));
            if (i < columnsNames.size() - 1) {
                queryBuilder.append(", ");
            }
        }
        queryBuilder.append(") VALUES (");
        for (int i = 0; i < columnsValue.size(); i++) {
            if(inputValidation.isValidDate(columnsValue.get(i))){
                queryBuilder.append("TO_DATE('").append(columnsValue.get(i)).append("', 'YYYY-MM-DD')");
            }
            else{
                queryBuilder.append("'").append(columnsValue.get(i)).append("'");   
            }                 
            if (i < columnsValue.size() - 1) {
                queryBuilder.append(", ");
            }
        }
        queryBuilder.append(")");

        String sql = queryBuilder.toString();
        
        return sql;
    }
    
    public static String getCreateViewScript ()
    {
        String sql = "CREATE OR REPLACE VIEW Most_Selling_Medications AS\n" +
                        "\n" +
                        "SELECT oi.MedicationId,o.OrderDate,ms.Name,ms.Price,\n" +
                        "       mc.DiseaseName AS MedicationCategory,\n" +
                        "       ms.ProductionDate,\n" +
                        "       ms.ExpirationDate,\n" +
                        "       ms.ChemicalComposition,\n" +
                        "       ms.ProductionLineId,\n" +
                        "       COUNT(*) AS totalSells\n" +
                        "\n" +
                        "FROM orderItems oi\n" +
                        "\n" +
                        "JOIN medications ms ON oi.medicationid = ms.id\n" +
                        "\n" +
                        "JOIN medicationCategory mc ON ms.medicationCategoryId = mc.id\n" +
                        "\n" +
                        "JOIN orders o ON oi.orderid = o.id \n" +
                        "\n" +
                        "WHERE EXTRACT (YEAR FROM o.orderdate) = 2024\n" +
                        "\n" +
                        "GROUP BY oi.medicationid,\n" +
                        "         o.orderdate,\n" +
                        "         ms.name,\n" +
                        "         ms.price,\n" +
                        "         mc.diseasename,\n" +
                        "         ms.productiondate,\n" +
                        "         ms.expirationdate,\n" +
                        "         ms.chemicalcomposition,\n" +
                        "         ms.productionlineid\n" +
                        "\n" +
                        "ORDER BY totalSells DESC";
        return sql;
    }
    
    public static String getCreateTriggerScript(){
        
        String sql = "CREATE OR REPLACE NONEDITIONABLE TRIGGER medication_expiry_date_check\n" +
                     "BEFORE INSERT ON medications\n" +
                     "FOR EACH ROW\n" +
                     "BEGIN\n" +
                     "    IF :NEW.EXPIRATIONDATE < SYSDATE THEN\n" +
                     "        RAISE_APPLICATION_ERROR(-20001, 'Medication expiry date is not valid.');\n" +
                     "    END IF;\n" +
                     "END;";
        return sql;        
    }
    
}
