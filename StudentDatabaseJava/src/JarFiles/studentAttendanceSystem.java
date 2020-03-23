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
     * @param args the command line arguments
     */
    public static teacher t;
    public static lesson currentClass;
    public static student student;
    public static void main(String[] args) {
        new login().setVisible(true); 
       
    }
    public static boolean login(String username, String password) {
        try{
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            String url="jdbc:sqlserver://socem1.uopnet.plymouth.ac.uk;databaseName=PRCO304_HFresco;user=HFresco;password=PRCO304!";
            Connection con = DriverManager.getConnection(url);
            String sql = "Select * from dbo.teacher_table where TeacherID=? and TeacherPassword = ?";
            //String sql2 = "SELECT TeacherFirstName FROM dbo.teacher_table WHERE TeacherID = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, username);
            pst.setString(2, password);
            ResultSet rs = pst.executeQuery();
           if(rs.next()){
               
                //JOptionPane.showMessageDialog(null, "Hello, "+ rs.getString("TeacherFirstName"));

                String teacherID = username;
                studentAttendanceSystem.t = new teacher(teacherID, rs.getString("TeacherFirstName"));
         
             try {     
            new selectClass().setVisible(true);
        } catch (SQLException ex) {
            Logger.getLogger(login.class.getName()).log(Level.SEVERE, null, ex);
        }
          
           }
           else{
                JOptionPane.showMessageDialog(null, "Username and password not Correct");
                return false;
            }
            con.close();
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(null, e);
            
        }
        return true;
    }
    
    public static List<lesson> getClasses() throws SQLException{
        ResultSet rs = null;
         List<lesson> lessonList = new ArrayList<>();

          try{
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            String url="jdbc:sqlserver://socem1.uopnet.plymouth.ac.uk;databaseName=PRCO304_HFresco;user=HFresco;password=PRCO304!";
            Connection con = DriverManager.getConnection(url);
            
            String sql = "Select * FROM dbo.lesson_table "
                    + "JOIN dbo.module_table ON dbo.lesson_table.ModuleID = dbo.module_table.ModuleID "
                    + "JOIN dbo.teacher_table ON dbo.module_table.TeacherID = dbo.teacher_table.TeacherID "
                    
                    + "where teacher_table.TeacherID = ?";
        
            PreparedStatement pst = con.prepareStatement(sql);
           
            System.out.println(studentAttendanceSystem.t.displayID());
            pst.setString(1, studentAttendanceSystem.t.displayID());
           
             rs = pst.executeQuery();
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
    
    public static List<student> getStudents() throws SQLException{
        ResultSet rs = null;
         List<student> studentList = new ArrayList<>();

          try{
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            String url="jdbc:sqlserver://socem1.uopnet.plymouth.ac.uk;databaseName=PRCO304_HFresco;user=HFresco;password=PRCO304!";
            Connection con = DriverManager.getConnection(url);
            
            String sql = "Select * FROM dbo.student_table "
                    + "JOIN dbo.enrolled_modules_table ON dbo.student_table.StudentID = dbo.enrolled_modules_table.StudentID "
              
                    
                    + "where enrolled_modules_table.ModuleID = ?";
        
            PreparedStatement pst = con.prepareStatement(sql);
           
            System.out.println(studentAttendanceSystem.currentClass.displayModuleID());
            
            pst.setString(1, valueOf(studentAttendanceSystem.currentClass.displayModuleID()));
           
             rs = pst.executeQuery();
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
         
    
    public static void updateAttendance(int arrayID[]) throws SQLException, ClassNotFoundException {
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            String url="jdbc:sqlserver://socem1.uopnet.plymouth.ac.uk;databaseName=PRCO304_HFresco;user=HFresco;password=PRCO304!";
            Connection con = DriverManager.getConnection(url);
            
            String sql = "UPDATE dbo.student_table SET StudentNumOfClasses = StudentNumOfClasses + 1 "
         
                    + ", classes_present = classes_present + 1 "
                    + "where dbo.student_table.studentID = ?";
        
            PreparedStatement pst = con.prepareStatement(sql);

            for (int i = 0; i<arrayID.length; i++){
                pst.setString(1, String.valueOf(arrayID[i]));
           
                pst.executeUpdate();
            }
         
             List<student> list = new ArrayList<>();
          
          list = getStudents();
    
          float attended;
          float present;
          float attendance;
          for (int i = 0; i <list.size(); i++){
              present = (float)list.get(i).Classes_present;
              
              attended = (float)list.get(i).StudentNumOfClasses;
  
              attendance = (present / attended) * 100;
              list.get(i).StudentAttendance = (int)attendance;
              
              
          }
           sql = "UPDATE dbo.student_table SET StudentAttendance = ? "
                    + "where dbo.student_table.studentID = ?";
        
            pst = con.prepareStatement(sql);
            
            for (int i = 0; i<list.size(); i++){
                pst.setInt(1, list.get(i).StudentAttendance);
                pst.setInt(2, list.get(i).StudentID);
                
                pst.executeUpdate();
            }
           
    }   

      public static void updateAttendanceAbsent(int arrayID[]) throws SQLException, ClassNotFoundException {
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            String url="jdbc:sqlserver://socem1.uopnet.plymouth.ac.uk;databaseName=PRCO304_HFresco;user=HFresco;password=PRCO304!";
            Connection con = DriverManager.getConnection(url);
            
            String sql = "UPDATE dbo.student_table SET StudentNumOfClasses = StudentNumOfClasses + 1 "
                    + "where dbo.student_table.studentID = ?";
        
            PreparedStatement pst = con.prepareStatement(sql);

            for (int i = 0; i<arrayID.length; i++){
                pst.setString(1, String.valueOf(arrayID[i]));
           
                pst.executeUpdate();
            }
      
            //calculateAttendance();
            
              List<student> list = new ArrayList<>();
          
          list = getStudents();
    
          float attended;
          float present;
          float attendance;
          for (int i = 0; i <list.size(); i++){
              present = (float)list.get(i).Classes_present;
              
              attended = (float)list.get(i).StudentNumOfClasses;
  
              attendance = (present / attended) * 100;
              list.get(i).StudentAttendance = (int)attendance;
              
              
          }
           sql = "UPDATE dbo.student_table SET StudentAttendance = ? "
                    + "where dbo.student_table.studentID = ?";
        
            pst = con.prepareStatement(sql);
            
            for (int i = 0; i<list.size(); i++){
                pst.setInt(1, list.get(i).StudentAttendance);
                pst.setInt(2, list.get(i).StudentID);
                
                pst.executeUpdate();
            }
     
    }     
      
      public static void calculateAttendance() throws ClassNotFoundException, SQLException{
          
          List<student> list = new ArrayList<>();
          
          list = getStudents();
    
          int attended;
          int present;
          
          for (int i = 0; i <list.size(); i++){
              present = list.get(i).Classes_present;
              attended = list.get(i).StudentNumOfClasses;
              list.get(i).StudentAttendance = (attended / present) * 100;
              System.out.println(list.get(i).StudentAttendance);
              
          }
          Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            String url="jdbc:sqlserver://socem1.uopnet.plymouth.ac.uk;databaseName=PRCO304_HFresco;user=HFresco;password=PRCO304!";
            Connection con = DriverManager.getConnection(url);
            
            String sql = "UPDATE dbo.student_table SET StudentAttendance = ? "
                    + "where dbo.student_table.studentID = ?";
        
            PreparedStatement pst = con.prepareStatement(sql);
            
            for (int i = 0; i<list.size(); i++){
                pst.setInt(1, list.get(i).StudentAttendance);
                pst.setInt(2, list.get(i).StudentID);
                
                pst.executeUpdate();
            }
        
      }
      
      public static List<module> getModules() {
          ResultSet rs = null;
         List<module> moduleList = new ArrayList<>();

          try{
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            String url="jdbc:sqlserver://socem1.uopnet.plymouth.ac.uk;databaseName=PRCO304_HFresco;user=HFresco;password=PRCO304!";
            Connection con = DriverManager.getConnection(url);
            
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
      
      public static boolean addStudent(student newStudent) throws ClassNotFoundException, SQLException{
          Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            String url="jdbc:sqlserver://socem1.uopnet.plymouth.ac.uk;databaseName=PRCO304_HFresco;user=HFresco;password=PRCO304!";
            Connection con = null;
        try {
            con = DriverManager.getConnection(url);
        } catch (SQLException ex) {
            Logger.getLogger(studentAttendanceSystem.class.getName()).log(Level.SEVERE, null, ex);
        }

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
                sql = "INSERT INTO dbo.enrolled_modules_table (StudentID, ModuleID) "
                    + "VALUES (?, ?)";
        pst = null;
        pst = con.prepareStatement(sql);
        pst.setInt(1, studentID);
        pst.setInt(2, newStudent.ModuleID);
        pst.executeUpdate();
        return true;
      }
      
      public static boolean addNewClass(newclass newClass) throws ClassNotFoundException, SQLException{
          
          Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            String url="jdbc:sqlserver://socem1.uopnet.plymouth.ac.uk;databaseName=PRCO304_HFresco;user=HFresco;password=PRCO304!";
            Connection con = null;
        try {
            con = DriverManager.getConnection(url);
        } catch (SQLException ex) {
            Logger.getLogger(studentAttendanceSystem.class.getName()).log(Level.SEVERE, null, ex);
        }

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
    
    

