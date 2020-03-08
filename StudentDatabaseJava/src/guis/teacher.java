/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package guis;

/**
 *
 * @author harryfresco
 */
public class teacher {
    private String teacherIDCurrent;
    public teacher(String teacherID){
        teacherIDCurrent = teacherID;
    }
    
    public String displayName(){
        return teacherIDCurrent;
    }


    
   
}
