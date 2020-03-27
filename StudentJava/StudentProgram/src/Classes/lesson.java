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
    
    public String LessonTitle;

    /**
     *
     * @param lessonID
     * @param moduleID
     * @param lessonDate
     * @param lessonLocation
     */
    public lesson(int lessonID, int moduleID, String lessonDate, String lessonLocation, String lessonTitle){
        LessonID = lessonID;
        ModuleID = moduleID;
        LessonDate = lessonDate;
        LessonLocation = lessonLocation;
        LessonTitle = lessonTitle;
        
    }

    /**
     *
     * @return
     */
    public int displayModuleID(){
        return ModuleID;
    }
}
