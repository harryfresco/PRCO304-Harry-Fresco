/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JarFiles;

import Classes.lesson;
import Classes.module;
import Classes.student;
import static JarFiles.studentAttendanceSystem.con;
import static java.lang.String.valueOf;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

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
    /**
     * The getClasses function gets all classes that the teacher teaches from database 
     * @return A list of the lessons the teacher teaches
     * @throws SQLException
     */
    public static List<lesson> getClasses() throws SQLException{
        ResultSet rs = null;
         List<lesson> lessonList = new ArrayList<>();

          try{ 
            // Joins 3 tables to return lessons that matches the teacherID
            String sql = "Select * FROM dbo.lesson_table "
                    + "JOIN dbo.module_table ON dbo.lesson_table.ModuleID = dbo.module_table.ModuleID "
                    + "JOIN dbo.teacher_table ON dbo.module_table.TeacherID = dbo.teacher_table.TeacherID "
                    
                    + "where teacher_table.TeacherID = ?";
        
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, studentAttendanceSystem.t.displayID());
           
            rs = pst.executeQuery();
            
            // Loops through the result set and creates a list containing lesson objects
            while(rs.next()){
               
                lesson l = new lesson(rs.getInt("LessonID"), rs.getInt("ModuleID"),
                        rs.getString("LessonDate"), rs.getString("LessonLocation"));

                lessonList.add(l);          
           }      
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
         return lessonList; 
    }
    
    /**
     * The getStudents function gets all students that match the selected moduleID
     * @return A list of the students
     * @throws SQLException
     */
    public static List<student> getStudents() throws SQLException{
        ResultSet rs = null;
         List<student> studentList = new ArrayList<>();

          try{
            // Select students that match the selected ModuleID
            String sql = "Select * FROM dbo.student_table "
                    + "JOIN dbo.enrolled_modules_table ON dbo.student_table.StudentID = dbo.enrolled_modules_table.StudentID "
                    + "where enrolled_modules_table.ModuleID = ?";
        
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, valueOf(studentAttendanceSystem.currentClass.displayModuleID()));
           
            rs = pst.executeQuery();
            
            // Loop through result set and add student objects to list
            while(rs.next()){
       
                student s = new student(rs.getInt("StudentID"), rs.getString("StudentFirstName"),
                        rs.getString("StudentLastName"), rs.getDate("StudentDOB"), 
                        rs.getInt("StudentAttendance"), rs.getInt("StudentNumOfClasses"),
                        rs.getInt("ModuleID"),rs.getString("StudentPassword"), rs.getInt("classes_present"));
             
                studentList.add(s);  
            } 
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
         return studentList; 
    }
    
    /**
     * The getAttendance function gets the overall class attendance
     * @return int The average class attendance
     * @throws SQLException
     */
    public static int getAttendance() throws SQLException{
       
        List<student> list = new ArrayList<>();
        list = getStudents();
        int i = 0;
        int tempAtt = 0;
        for (i=0; i<list.size(); i++){
            tempAtt += list.get(i).StudentAttendance;
        }
        tempAtt = tempAtt / i;
        return tempAtt;
    }
         
  
   

   
        
      

    /**
     * The function getModules gets all modules and returns a list of 
     * module objects
     * @return List of module objects consisting of ModuleID and ModuleTitle
     */
    public static List<module> getModules() {
         ResultSet rs = null;
         List<module> moduleList = new ArrayList<>();

          try{
                String sql = "Select ModuleID, ModuleTitle FROM dbo.module_table";
                PreparedStatement pst = con.prepareStatement(sql);

                rs = pst.executeQuery();
                while(rs.next()){
                    module m = new module(rs.getInt("ModuleID"), rs.getString("ModuleTitle"));
                    moduleList.add(m);
                }    
            }
         catch(Exception e){
            JOptionPane.showMessageDialog(null, e);
         }
         return moduleList;    
      }
}
