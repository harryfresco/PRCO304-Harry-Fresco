/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import guis.lesson;
import guis.login;
import guis.selectClass;
import guis.teacher;
import java.sql.Connection;
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

/**
 *
 * @author harryfresco
 */
public class Test {

    /**
     * @param args the command line arguments
     */
    public static teacher t;
    
        public static lesson currentClass;
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
               
                JOptionPane.showMessageDialog(null, "Hello, "+ rs.getString("TeacherFirstName"));

                String teacherID = username;
                Test.t = new teacher(teacherID, rs.getString("TeacherFirstName"));
                System.out.println(t.displayName());
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
           
            System.out.println(Test.t.displayID());
            pst.setString(1, Test.t.displayID());
           
             rs = pst.executeQuery();
            while(rs.next()){
               
                lesson l = new lesson(rs.getInt("LessonID"), rs.getInt("ModuleID"),
                        rs.getString("LessonDate"), rs.getString("LessonLocation"));
                System.out.println(l.LessonLocation);
                
                lessonList.add(l);
                
           }
            
              
            
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
         return lessonList; 
   

        
 
    }
}
    
    

