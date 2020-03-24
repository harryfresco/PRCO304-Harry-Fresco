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
public class teacher {
    private String teacherIDCurrent;
    private String teacherNameCurrent;

    /**
     *
     * @param teacherID
     */
    public teacher(String teacherID){
        teacherIDCurrent = teacherID;
    }

    /**
     *
     * @param teacherID
     * @param string
     */
    public teacher(String teacherID, String string) {
         teacherIDCurrent = teacherID;
         teacherNameCurrent = string;
    }
    
    /**
     *
     * @return
     */
    public String displayID(){
        return teacherIDCurrent;
    }

    /**
     *
     * @return
     */
    public String displayName(){
        return teacherNameCurrent;
    }

    
   
}
