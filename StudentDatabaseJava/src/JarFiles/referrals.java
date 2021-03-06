/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JarFiles;

import Classes.student;
import static JarFiles.getDatabase.getStudents;
import static JarFiles.studentAttendanceSystem.con;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author harryfresco
 */
public class referrals {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
    }

    /**
     *
     * @param arrayID
     * @throws SQLException
     */
    public static boolean addReferral(int arrayID[]) throws SQLException{
            // Adds 1 to the Number of Classes and Number of Classes present
            String sql = "UPDATE dbo.student_table SET StudentReferral = StudentReferral + 1 "
                    + "where dbo.student_table.studentID = ?";
        
            PreparedStatement pst = con.prepareStatement(sql);
            
            // Each value in array is the studentID
            for (int i = 0; i<arrayID.length; i++){
                pst.setString(1, String.valueOf(arrayID[i]));
                pst.executeUpdate();
            }
            return true;
    }
    
    /**
     *
     * @param arrayID
     * @throws SQLException
     */
    public static boolean negativeReferral(int arrayID[]) throws SQLException{
            // Adds 1 to the Number of Classes and Number of Classes present
            String sql = "UPDATE dbo.student_table SET StudentReferral = StudentReferral - 1 "
                    + "where dbo.student_table.studentID = ?";
        
            PreparedStatement pst = con.prepareStatement(sql);
            
            // Each value in array is the studentID
            for (int i = 0; i<arrayID.length; i++){
                pst.setString(1, String.valueOf(arrayID[i]));
                pst.executeUpdate();
            }
            return true;
    }
}
