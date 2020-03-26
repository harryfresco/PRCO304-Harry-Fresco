/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JarFiles;

import Classes.newclass;
import Classes.student;
import static JarFiles.studentAttendanceSystem.con;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author harryfresco
 */
public class addDatabase {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
    }
    /**
     * The function addStudent gets a student object and adds it to the database
     * along with their enrolled module
     * @param newStudent
     * @return Returns true when done successfully
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public static boolean addStudent(student newStudent) throws ClassNotFoundException, SQLException{
            String sql = "INSERT INTO dbo.student_table (StudentFirstName, StudentLastName, StudentDOB,"
                    + " StudentAttendance, StudentNumOfClasses, StudentPassword, classes_present) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
            PreparedStatement pst = con.prepareStatement(sql);

                pst.setString(1, newStudent.StudentFirstName);
                pst.setString(2, newStudent.StudentLastName);    
                pst.setDate(3, newStudent.StudentDOB);      
                pst.setInt(4, newStudent.StudentAttendance);
                pst.setInt(5, newStudent.StudentNumOfClasses);  
                pst.setString(6, newStudent.StudentPassword);
                pst.setInt(7, newStudent.Classes_present);
                pst.executeUpdate();
           
            // Gets name and DOB  
            sql = "SELECT StudentID FROM dbo.student_table WHERE StudentFirstName = ? "
                        + "and StudentLastName = ? and StudentDOB = ?";
                pst = con.prepareStatement(sql);
                pst.setString(1, newStudent.StudentFirstName);
                pst.setString(2, newStudent.StudentLastName);
                pst.setDate(3, newStudent.StudentDOB);
                ResultSet rs = pst.executeQuery();
                int studentID = 0;
                while(rs.next()){
                    studentID = rs.getInt("StudentID");
                }
            // Adds enrolled modules   
            sql = "INSERT INTO dbo.enrolled_modules_table (StudentID, ModuleID) "
                    + "VALUES (?, ?)";
        pst = null;
        pst = con.prepareStatement(sql);
        pst.setInt(1, studentID);
        pst.setInt(2, newStudent.ModuleID);
        pst.executeUpdate();
        return true;
      }
      
    /**
     * The function addNewClass gets a class object and adds it to the database
     * @param newClass class object
     * @return Returns true when done successfully
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public static boolean addNewClass(newclass newClass) throws ClassNotFoundException, SQLException{
            
        String sql = "INSERT INTO dbo.lesson_table (ModuleID, LessonDate, LessonLocation) "
                    + "VALUES (?, ?, ?)";
        
            PreparedStatement pst = con.prepareStatement(sql);

                pst.setInt(1, newClass.ModuleID);
                pst.setString(2, newClass.Date);    
                pst.setString(3, newClass.Location);      
                
                pst.executeUpdate();
        return true;
          
      }
}
