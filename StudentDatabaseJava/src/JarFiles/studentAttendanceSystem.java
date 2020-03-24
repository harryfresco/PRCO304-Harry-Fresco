/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JarFiles;

import Classes.lesson;
import guis.login;
import Classes.module;
import guis.selectClass;
import Classes.teacher;
import Classes.*;
import static java.lang.String.valueOf;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import static javax.swing.UIManager.getInt;

/**
 *
 * @author harryfresco
 */
public class studentAttendanceSystem {

    /**
     */
    
    // Create variables to be used through entire program, this is to keep temporary
    // data while the teacher is logged in
    public static teacher t;

    /**
     * Object to hold teachers log-in details
     */
    public static lesson currentClass;

    /**
     * Object to hold the current class
     */
    public static student student;
    
    /**
     * Object to hold a student
     * 
     */
    
    public static String url;

    /**
     *
     */
    public static Connection con;
    
    /**
     *
     * @param args
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        new login().setVisible(true); 
        connect();
    }
    
    /**
     *  The connect() function connects to the database at the beginning
     * 
     * @throws java.lang.ClassNotFoundException
     * @throws java.sql.SQLException
     */
    public static void connect() throws ClassNotFoundException, SQLException {
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        url="jdbc:sqlserver://socem1.uopnet.plymouth.ac.uk;databaseName=PRCO304_HFresco;user=HFresco;password=PRCO304!";
        con = DriverManager.getConnection(url);
    }
    /**
     *  The log-in function that takes the inputted username and password, checks it
     *  against the database and creates a teacher object
     * @param username The username field value
     * @param password The password field value
     * @return Boolean Returns true if correct credentials
     */
    public static boolean login(String username, String password) {
        try{
            // Selects all from database that matches the input credentials
            String sql = "Select * from dbo.teacher_table where TeacherID=? and TeacherPassword = ?";
            //String sql2 = "SELECT TeacherFirstName FROM dbo.teacher_table WHERE TeacherID = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, username);
            pst.setString(2, password);
            ResultSet rs = pst.executeQuery();
           
           // Goes through results and creates a teacher object using teacher class
           if(rs.next()){
               
                String teacherID = username;
                studentAttendanceSystem.t = new teacher(teacherID, rs.getString("TeacherFirstName"));
         
                
            try {   
                // Go to next page
                new selectClass().setVisible(true);
            } catch (SQLException ex) {
                Logger.getLogger(login.class.getName()).log(Level.SEVERE, null, ex);
            }
          
           }
           else{
                JOptionPane.showMessageDialog(null, "Username and password not Correct");
                return false;
            }
            
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(null, e);
            
        }
        return true;
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
                        rs.getInt("ModuleID"), rs.getInt("classes_present"));
             
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
     * The function updateAttendance updates the number of classes attended, then
     * calculates and updates the attendance after
     * @param arrayID An array of the Student ID's
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public static void updateAttendance(int arrayID[]) throws SQLException, ClassNotFoundException {
            
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
    
    /**
     * Close the connection to database
     */
    public static void closeConnection() {
        try {
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(studentAttendanceSystem.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
}
    
    

