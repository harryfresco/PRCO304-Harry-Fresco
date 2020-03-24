/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes;

/**
 *
 * @author harryfresco
 */

public class lesson {

    /**
     *
     */
    public int LessonID;

    /**
     *
     */
    public int ModuleID;

    /**
     *
     */
    public String LessonDate;

    /**
     *
     */
    public String LessonLocation;

    /**
     *
     * @param lessonID
     * @param moduleID
     * @param lessonDate
     * @param lessonLocation
     */
    public lesson(int lessonID, int moduleID, String lessonDate, String lessonLocation){
        LessonID = lessonID;
        ModuleID = moduleID;
        LessonDate = lessonDate;
        LessonLocation = lessonLocation;
        
    }

    /**
     *
     * @return
     */
    public int displayModuleID(){
        return ModuleID;
    }
    lesson(boolean next) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}   
    
