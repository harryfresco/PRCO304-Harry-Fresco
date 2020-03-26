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
import java.applet.Applet;

/**
 *
 * @author harryfresco
 */
public class studentAttendanceSystem  extends Applet{

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
    
    

