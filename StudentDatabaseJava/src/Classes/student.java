/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes;

import java.sql.Date;

/**
 *
 * @author harryfresco
 */
public class student {
    public int StudentID;
    public String StudentFirstName;
    public String StudentLastName;
    public Date StudentDOB;
    public int StudentAttendance;
    public int StudentNumOfClasses;
    public int ModuleID;
    public int Classes_present;
    public String StudentPassword;
    
    public student(int studentID, String studentFirstName, String studentLastName,
            Date studentDOB, int studentAttendance, int studentNumOfClasses, int moduleID, int classes_present){
        StudentID = studentID;
        StudentFirstName = studentFirstName;
        StudentLastName = studentLastName;
        StudentDOB = studentDOB;
        StudentAttendance = studentAttendance;
        StudentNumOfClasses = studentNumOfClasses;
        ModuleID = moduleID;
        Classes_present = classes_present;
    }

    public student(String firstNameVar, String lastNameVar, Date dateVar, String passwordVar, int moduleVar) {
        
        StudentFirstName = firstNameVar;
        StudentLastName = lastNameVar;
        StudentDOB = dateVar;
        StudentPassword = passwordVar;
        StudentAttendance = 0;
        StudentNumOfClasses = 0;
        ModuleID = moduleVar;
        Classes_present = 0;
    }
    
    public String displayStudents(){
        return StudentFirstName;
    }
    
    
}
