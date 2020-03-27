/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JarFiles;

import Classes.student;
import static JarFiles.studentAttendanceSystem.*;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import static JarFiles.getDatabase.*;


/**
 *
 * @author harryfresco
 */
public class updateAttendance {

    /**
     * @param args the command line arguments
     * @throws java.sql.SQLException
     */
    public static void main(String[] args) throws SQLException {
        
        }
    
      /**
     * The function updateAttendance updates the number of classes attended, then
     * calculates and updates the attendance after
     * @param arrayID An array of the Student ID's
     * @throws SQLException
     */
    public static void updateAttendance(int arrayID[]) throws SQLException{
            // Adds 1 to the Number of Classes and Number of Classes present
            String sql = "UPDATE dbo.student_table SET StudentNumOfClasses = StudentNumOfClasses + 1 "
                    + ", classes_present = classes_present + 1 "
                    + "where dbo.student_table.studentID = ?";
        
            PreparedStatement pst = con.prepareStatement(sql);
            
            // Each value in array is the studentID
            for (int i = 0; i<arrayID.length; i++){
                pst.setString(1, String.valueOf(arrayID[i]));
                pst.executeUpdate();
            }
         
            List<student> list = new ArrayList<>();
          
            // Fill list with students
            list = getStudents();
    
          float attended;
          float present;
          float attendance;
          
          // Calculate attendance
          for (int i = 0; i <list.size(); i++){
              present = (float)list.get(i).Classes_present;
              
              attended = (float)list.get(i).StudentNumOfClasses;
  
              attendance = (present / attended) * 100;
              list.get(i).StudentAttendance = (int)attendance;   
          }
          
          // Update the attendance
           sql = "UPDATE dbo.student_table SET StudentAttendance = ? "
                    + "where dbo.student_table.studentID = ?";
        
            pst = con.prepareStatement(sql);
            
            for (int i = 0; i<list.size(); i++){
                pst.setInt(1, list.get(i).StudentAttendance);
                pst.setInt(2, list.get(i).StudentID);
                pst.executeUpdate();
            }   
    }
    
     /**
     * The function updateAttendanceAbsent updates the number of classes attended, then
     * calculates and updates the attendance after for people that were registered absent
     * @param arrayID An array of student ID's
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public static void updateAttendanceAbsent(int arrayID[]) throws SQLException, ClassNotFoundException {
            // Adds 1 to the Number of Classes and Number of Classes present
            String sql = "UPDATE dbo.student_table SET StudentNumOfClasses = StudentNumOfClasses + 1 "
                    + "where dbo.student_table.studentID = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            
            // Each value in array is the studentID
            for (int i = 0; i<arrayID.length; i++){
                pst.setString(1, String.valueOf(arrayID[i]));
                pst.executeUpdate();
            }
            
            List<student> list = new ArrayList<>();
            // Fill list with students
            list = getStudents();
    
          float attended;
          float present;
          float attendance;
          
          // Calculate attendance
          for (int i = 0; i <list.size(); i++){
              present = (float)list.get(i).Classes_present;
              
              attended = (float)list.get(i).StudentNumOfClasses;
  
              attendance = (present / attended) * 100;
              list.get(i).StudentAttendance = (int)attendance;
              
              
          }
          // Update the attendance
           sql = "UPDATE dbo.student_table SET StudentAttendance = ? "
                    + "where dbo.student_table.studentID = ?";
        
            pst = con.prepareStatement(sql);
            
            for (int i = 0; i<list.size(); i++){
                pst.setInt(1, list.get(i).StudentAttendance);
                pst.setInt(2, list.get(i).StudentID);
                
                pst.executeUpdate();
            }
     
    } 
}
