/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package studentprogram;

import Classes.lesson;
import Classes.student;

import guis.*;
import java.sql.Connection;
import java.sql.Date;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author harryfresco
 */
public class studentAttendanceSystem {

    /**
     */
    
    public static student s;

    /**
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
    public static void main(String[] args) throws ClassNotFoundException, SQLException{
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
     * @throws java.sql.SQLException
     */
    public static boolean login(String username, String password) throws SQLException {
      
            // Selects all from database that matches the input credentials
            String sql = "Select * from dbo.student_table JOIN dbo.enrolled_modules_table "
                    + "ON dbo.student_table.StudentID = dbo.enrolled_modules_table.StudentID "
                    + "where student_table.StudentID=? and student_table.StudentPassword = ?";
            //String sql2 = "SELECT TeacherFirstName FROM dbo.teacher_table WHERE TeacherID = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, username);
            pst.setString(2, password);
            ResultSet rs = pst.executeQuery();
           
           // Goes through results and creates a teacher object using teacher class
           if(rs.next()){

                s = new student(rs.getInt("StudentID"), rs.getString("StudentFirstName"), rs.getString("StudentLastName"),
                rs.getDate("studentDOB"), rs.getInt("StudentAttendance"), rs.getInt("StudentNumOfClasses"), rs.getInt("ModuleID"),
                        rs.getString("StudentPassword"), rs.getInt("classes_present"), rs.getInt("StudentReferral"));
           }
           else{
                JOptionPane.showMessageDialog(null, "Username and password not Correct");
                return false;
            }
            
        new MainPage().setVisible(true);
       
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
