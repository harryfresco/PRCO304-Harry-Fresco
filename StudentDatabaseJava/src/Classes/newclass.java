/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes;

import java.time.LocalDateTime;

/**
 *
 * @author harryfresco
 */
public class newclass {
//location, date, selectedModuleID

    /**
     *
     */
    public String Location;

    /**
     *
     */
    public String Date;

    /**
     *
     */
    public int ModuleID;
    
    /**
     *
     * @param location
     * @param date
     * @param selectedModuleID
     */
    public newclass(String location, String date, int selectedModuleID) {
         Location = location;
         Date = date;
         ModuleID = selectedModuleID;
    }
    
}
