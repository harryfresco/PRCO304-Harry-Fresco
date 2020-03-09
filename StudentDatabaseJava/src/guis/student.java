/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package guis;

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
    
    public student(int studentID, String studentFirstName, String studentLastName,
            Date studentDOB, int studentAttendance, int studentNumOfClasses){
        StudentID = studentID;
        StudentFirstName = studentFirstName;
        StudentLastName = studentLastName;
        StudentDOB = studentDOB;
        StudentAttendance = studentAttendance;
        StudentNumOfClasses = studentNumOfClasses;
    }
    
    public String displayStudents(){
        return StudentFirstName;
    }
    
    
}
