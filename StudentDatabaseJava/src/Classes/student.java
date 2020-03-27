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

    /**
     *
     */
    public int StudentID;

    /**
     *
     */
    public String StudentFirstName;

    /**
     *
     */
    public String StudentLastName;

    /**
     *
     */
    public Date StudentDOB;

    /**
     *
     */
    public int StudentAttendance;

    /**
     *
     */
    public int StudentNumOfClasses;

    /**
     *
     */
    public int ModuleID;

    /**
     *
     */
    public int Classes_present;

    /**
     *
     */
    public String StudentPassword;

    /**
     *
     */
    public int StudentReferral;
    
    /**
     *
     * @param studentID
     * @param studentFirstName
     * @param studentLastName
     * @param studentDOB
     * @param studentAttendance
     * @param studentNumOfClasses
     * @param moduleID
     * @param passwordVar
     * @param classes_present
     * @param studentReferral
     */
    public student(int studentID, String studentFirstName, String studentLastName,
            Date studentDOB, int studentAttendance, int studentNumOfClasses, int moduleID,String passwordVar, int classes_present,
            int studentReferral){
        StudentID = studentID;
        StudentFirstName = studentFirstName;
        StudentLastName = studentLastName;
        StudentDOB = studentDOB;
        StudentAttendance = studentAttendance;
        StudentNumOfClasses = studentNumOfClasses;
        ModuleID = moduleID;
        Classes_present = classes_present;
        StudentPassword = passwordVar;
        StudentReferral = studentReferral;
    }

    /**
     *
     * @param firstNameVar
     * @param lastNameVar
     * @param dateVar
     * @param passwordVar
     * @param moduleVar
     */
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
    
    /**
     * Display Students, returns the student's first name
     * @return
     */
    public String displayStudents(){
        return StudentFirstName;
    }
    
    
}
