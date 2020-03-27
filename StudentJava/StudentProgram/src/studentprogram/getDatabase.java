/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package studentprogram;

import Classes.lesson;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import static studentprogram.studentAttendanceSystem.*;

/**
 *
 * @author harryfresco
 */
public class getDatabase {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
    }
     public static List<lesson> getClasses() throws SQLException{
        ResultSet rs = null;
         List<lesson> lessonList = new ArrayList<>();

          try{ 
            // Joins 3 tables to return lessons that matches the teacherID
            String sql = "Select * FROM dbo.student_table "
                    + "JOIN dbo.enrolled_modules_table ON dbo.student_table.StudentID = dbo.enrolled_modules_table.StudentID "
                    + "JOIN dbo.module_table ON dbo.enrolled_modules_table.ModuleID = dbo.module_table.ModuleID "
                    + "JOIN dbo.lesson_table ON dbo.module_table.ModuleID = dbo.lesson_table.ModuleID "
                    
                    + "where student_table.StudentID = ?";
        
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, studentAttendanceSystem.s.StudentID);
           
            rs = pst.executeQuery();
            
            // Loops through the result set and creates a list containing lesson objects
            while(rs.next()){
               
                lesson l = new lesson(rs.getInt("LessonID"), rs.getInt("ModuleID"),
                        rs.getString("LessonDate"), rs.getString("LessonLocation"), rs.getString("ModuleTitle"));

                lessonList.add(l);          
           }      
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
         return lessonList; 
    }
}
