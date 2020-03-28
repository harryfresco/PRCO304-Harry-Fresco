/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package guis;

import Classes.newclass;
import Classes.module;
import Classes.student;


import java.sql.Array;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;


import java.sql.DriverManager;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import static JarFiles.studentAttendanceSystem.*;
import static JarFiles.updateAttendance.*;
import static JarFiles.getDatabase.*;
import static JarFiles.addDatabase.*;
import static JarFiles.referrals.*;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.time.LocalDateTime;
/**
 *
 * @author harryfresco
 */
public class MainPage extends javax.swing.JFrame {
Connection con=null;
    ResultSet rs=null;
    PreparedStatement pst=null;

    /**
     * Creates new form MainPage
     */
    
    // Initialises lists and listModels
    List<student> list = new ArrayList<>();
    List<String> enrolledList = new ArrayList<>();
    List<module> moduleList = new ArrayList<>();
    DefaultListModel studentListModel = new DefaultListModel();
    JComboBox moduleListModel = new JComboBox();
    
    /**
     *
     * @throws SQLException
     */
    public MainPage() throws SQLException {
        initComponents();
        
        // Fills list with students in class using getStudents() function
        list = getStudents();
        
        // Fills list with modules using getModules() function
        moduleList = getModules();
        
        fillBoxesAndLabels();
    }
   
/**
* The function fillBoxesAndLabels fills the combo boxes.
* 
*/    
private void fillBoxesAndLabels() {
    // Loops through list of students in class and adds full name to list
        for (int i = 0; i<list.size(); i++){
            studentListModel.addElement(list.get(i).StudentFirstName + " " + 
                    list.get(i).StudentLastName);  
        }
        
        // Loops through modules and adds to the module dropDown box
        for (int i = 0; i<moduleList.size(); i++){
            moduleBox.addItem(moduleList.get(i).ModuleTitle);          
        }
        
        // Loops through modules and adds to the module dropDown box
        for (int i = 0; i<moduleList.size(); i++){
            newModuleBox.addItem(moduleList.get(i).ModuleTitle);
        }
        
        // Connects List Model to the jList of students
        studentList.setModel(studentListModel);
   
    try {
        // Shows the selected student attendance
        attendanceLabel.setText(Integer.toString(getAttendance()) + "%");
        
    } catch (SQLException ex) {
        Logger.getLogger(MainPage.class.getName()).log(Level.SEVERE, null, ex);
    }
 
}

/**
* The function searchButton gets the entered value and searches database.
* 
*/  
private void searchButton() {
    String nameEntered = jTextField1.getText();
        String[] splited;
        // Split name by space
        splited = nameEntered.split("\\s+");
        // Loop through list of students in class
        for (int i = 0; i<list.size(); i++){
            // If it is a match, display details via labels
            if((list.get(i).StudentFirstName == null ? splited[0] == null : list.get(i).StudentFirstName.equals(splited[0])) && (list.get(i).StudentLastName == null ? splited[1] == null : list.get(i).StudentLastName.equals(splited[1]))){
                firstLabel.setText(list.get(i).StudentFirstName);
                lastLabel.setText(list.get(i).StudentLastName);
                attendanceIndvLabel.setText(String.valueOf(list.get(i).StudentAttendance) + "%");
                studentIDIndvLabel.setText(String.valueOf(list.get(i).StudentID));
                
            }

        }
}

/**
* Gets the selected students and passes it to updateAttendance().
* 
*/  
private void signInButton() {
     // Initialise list
        List<String> list2 = new ArrayList<>();
        
        // Fill list2 with selected students
        list2 = studentList.getSelectedValuesList();
        int selectedIndex = studentList.getSelectedIndex();
        int[] arrayID = new int[30];

        // Go through selected students, check against list of students, add their 
        // studentID to a list, remove them from the jList
        
        for(int i = 0; i<list2.size(); i++){

            for(int j= 0; j<list.size(); j++){

                if(list2.get(i).equals(list.get(j).StudentFirstName + " " + list.get(j).StudentLastName)){                   
                    arrayID[i] = list.get(j).StudentID;
                    
                    if (selectedIndex != -1) {
                        studentListModel.remove(selectedIndex);
                        if (studentListModel.isEmpty()) {
                            JOptionPane.showMessageDialog(null, "Register Complete");
                        }
                    }
                }
            }

        }
       
        // Send students to updateAttendance() function to change attendance value
        try {
            updateAttendance(arrayID);
        } catch (SQLException ex) {
            Logger.getLogger(MainPage.class.getName()).log(Level.SEVERE, null, ex);
        }
}

/**
* Gets the selected students and passes it to updateAttendanceAbsent().
* 
*/ 
private void signInAbsentButton() {
    // Initialise variables
        List<String> list3 = new ArrayList<>();
        int selectedIndex = studentList.getSelectedIndex();
        
        // Fill list3 with selected students
        list3 = studentList.getSelectedValuesList();
        int[] arrayID2 = new int[30];

        // Go through selected students, check against list of students, add their 
        // studentID to a list, remove them from the jList
        for(int i = 0; i<list3.size(); i++){

            for(int j= 0; j<list.size(); j++){

                if(list3.get(i).equals(list.get(j).StudentFirstName + " " + list.get(j).StudentLastName)){                   
                    arrayID2[i] = list.get(j).StudentID;
                    
                    if (selectedIndex != -1) {
                        studentListModel.remove(selectedIndex);
                        if (studentListModel.isEmpty()) {
                            JOptionPane.showMessageDialog(null, "Register Complete");
                        }
                    }
                }
            }

        }
        
        // Send students to updateAttendanceAbsent() function to change attendance value
        try {
            updateAttendanceAbsent(arrayID2);
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(MainPage.class.getName()).log(Level.SEVERE, null, ex);
        }
}

/**
* Gets the field values and adds a student using addStudent() function.
* 
*/ 
private void addStudentButton() {
    // Put field values into variables
    try {
        String firstNameVar = firstName.getText();
        String lastNameVar = lastName.getText();
        Date dateVar = Date.valueOf(date.getText());
        String passwordVar = password.getText();
        String moduleVar = (String) moduleBox.getSelectedItem();

        int selectedModuleID = 0;
        
        // Convert the Module Title to it's ModuleID using moduleList
        for (int i = 0; i<moduleList.size(); i++){
            if(moduleVar == moduleList.get(i).ModuleTitle){
                selectedModuleID = moduleList.get(i).ModuleID;
            }
        }
        
        // Create new student object
        student newStudent = new student(firstNameVar, lastNameVar, dateVar, passwordVar, selectedModuleID);
        
        // Use addStudent() function to add student object to database
        if(addStudent(newStudent) == true){
            JOptionPane.showMessageDialog(null, "Student Added successfully");
        }
    } catch (ClassNotFoundException ex) {
        Logger.getLogger(MainPage.class.getName()).log(Level.SEVERE, null, ex);
    } catch (SQLException ex) {
        Logger.getLogger(MainPage.class.getName()).log(Level.SEVERE, null, ex);
    }
}

/**
* Gets the field values and adds a class using addNewClass() function.
* 
*/ 
private void addClassButton() {
    // Put field values into variables
        String location = newLocation.getText();
        String date = newDate.getText();
        String moduleVar = (String) newModuleBox.getSelectedItem();

        int selectedModuleID = 0;
        
        // Convert the Module Title to it's ModuleID using moduleList
        for (int i = 0; i<moduleList.size(); i++){
            if(moduleVar == moduleList.get(i).ModuleTitle){
                selectedModuleID = moduleList.get(i).ModuleID;
            }
        }
        // Create new class object
        newclass addClass = new newclass(location, date, selectedModuleID);
    
    // Use addNewClass() function to add class object to database
    try {
        if(addNewClass(addClass) == true){
            JOptionPane.showMessageDialog(null, "Class Added successfully");
        }
    } catch (ClassNotFoundException ex) {
        Logger.getLogger(MainPage.class.getName()).log(Level.SEVERE, null, ex);
    } catch (SQLException ex) {
        Logger.getLogger(MainPage.class.getName()).log(Level.SEVERE, null, ex);
    }
}
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTextField2 = new javax.swing.JTextField();
        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        signOutButton = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        studentList = new javax.swing.JList<>();
        signInButton = new javax.swing.JButton();
        absentButton = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        attendanceRegisterLabel = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        studentIDLabel = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        referralLabel = new javax.swing.JLabel();
        referralButton = new javax.swing.JButton();
        negativeReferralButton = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        studentIDRadio = new javax.swing.JRadioButton();
        rfidRadio = new javax.swing.JRadioButton();
        studentIDInput = new javax.swing.JTextField();
        loadingBar = new javax.swing.JProgressBar();
        jPanel4 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        attendanceLabel = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        attendanceIndvLabel = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        firstLabel = new javax.swing.JLabel();
        lastLabel = new javax.swing.JLabel();
        searchStudent = new javax.swing.JButton();
        jLabel16 = new javax.swing.JLabel();
        studentIDIndvLabel = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        firstName = new javax.swing.JTextField();
        lastName = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        date = new javax.swing.JFormattedTextField();
        jLabel7 = new javax.swing.JLabel();
        password = new javax.swing.JTextField();
        moduleBox = new javax.swing.JComboBox<>();
        jLabel8 = new javax.swing.JLabel();
        addStudentButton = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        newModuleBox = new javax.swing.JComboBox<>();
        newDate = new javax.swing.JFormattedTextField();
        newLocation = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        addClassButton = new javax.swing.JButton();

        jTextField2.setText("jTextField2");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        signOutButton.setText("Sign Out");
        signOutButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                signOutButtonActionPerformed(evt);
            }
        });

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Sign-In students"));

        studentList.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Student 1", "Student 2", "Student 3", "Student 4", "Student 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        studentList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                studentListMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(studentList);

        signInButton.setText("Sign In");
        signInButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                signInButtonActionPerformed(evt);
            }
        });

        absentButton.setText("Absent");
        absentButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                absentButtonActionPerformed(evt);
            }
        });

        jLabel9.setText("Attendance: ");

        attendanceRegisterLabel.setText("100%");

        jLabel14.setText("Student ID: ");

        studentIDLabel.setText("N/A");

        jLabel18.setText("Referrals:");

        referralLabel.setText("N/A");

        referralButton.setText("Positive Referral");
        referralButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                referralButtonActionPerformed(evt);
            }
        });

        negativeReferralButton.setText("Negative Referral");
        negativeReferralButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                negativeReferralButtonActionPerformed(evt);
            }
        });

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder("Sign-In via ID or Tag"));

        studentIDRadio.setText("Student ID");
        studentIDRadio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                studentIDRadioActionPerformed(evt);
            }
        });

        rfidRadio.setText("RFID Tag");

        studentIDInput.setForeground(new java.awt.Color(102, 102, 102));
        studentIDInput.setText("Click here to start");
        studentIDInput.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                studentIDInputMouseClicked(evt);
            }
        });
        studentIDInput.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                studentIDInputActionPerformed(evt);
            }
        });
        studentIDInput.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                studentIDInputKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(studentIDInput)
                    .addComponent(loadingBar, javax.swing.GroupLayout.DEFAULT_SIZE, 182, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(studentIDRadio)
                    .addComponent(rfidRadio))
                .addGap(31, 31, 31))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(studentIDRadio)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(rfidRadio)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(studentIDInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(loadingBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(signInButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(absentButton)
                .addGap(56, 56, 56)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel9)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(attendanceRegisterLabel))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel14)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(studentIDLabel))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel18)
                                .addGap(26, 26, 26)
                                .addComponent(referralLabel)))
                        .addContainerGap(147, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(referralButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(negativeReferralButton, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addContainerGap())))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 254, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9)
                            .addComponent(attendanceRegisterLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel14)
                            .addComponent(studentIDLabel))
                        .addGap(28, 28, 28)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel18)
                            .addComponent(referralLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(referralButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(negativeReferralButton))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(signInButton)
                    .addComponent(absentButton))
                .addGap(18, 18, 18)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(78, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Register", jPanel3);

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Analytics"));

        jLabel12.setText("Class Attendance:");

        attendanceLabel.setText("90%");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(attendanceLabel)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(attendanceLabel))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Analytics", jPanel4);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Search Student"));

        jLabel1.setText("Attendance:");

        attendanceIndvLabel.setText("0%");

        jTextField1.setText("Search by name");
        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField1KeyTyped(evt);
            }
        });

        jLabel4.setText("First Name:");

        jLabel5.setText("Surname:");

        firstLabel.setText("Fred");

        lastLabel.setText("Highmore");

        searchStudent.setText("Search");
        searchStudent.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchStudentActionPerformed(evt);
            }
        });

        jLabel16.setText("Student ID:");

        studentIDIndvLabel.setText("N/A");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addGap(58, 58, 58)
                        .addComponent(jLabel5))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(firstLabel)
                        .addGap(97, 97, 97)
                        .addComponent(lastLabel))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(searchStudent)
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 271, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(18, 18, Short.MAX_VALUE)
                                .addComponent(jLabel1)
                                .addGap(24, 24, 24)
                                .addComponent(attendanceIndvLabel))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(jLabel16)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 22, Short.MAX_VALUE)
                                .addComponent(studentIDIndvLabel)))))
                .addContainerGap(120, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(searchStudent))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(attendanceIndvLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel16)
                            .addComponent(studentIDIndvLabel))
                        .addGap(22, 22, 22)))
                .addGap(7, 7, 7)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(firstLabel)
                    .addComponent(lastLabel))
                .addContainerGap(274, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Search Student", jPanel1);

        jLabel2.setText("First Name: ");

        jLabel3.setText("Last Name: ");

        jLabel6.setText("Date of Birth:");

        date.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter(new java.text.SimpleDateFormat("yyyy-MM-dd"))));
        date.setText("yyyy-MM-dd");
        date.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dateActionPerformed(evt);
            }
        });

        jLabel7.setText("Password:");

        jLabel8.setText("Modules enrolled:");

        addStudentButton.setText("Add Student");
        addStudentButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addStudentButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(firstName)
                        .addComponent(date, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel6))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(jLabel3))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(lastName)
                        .addComponent(password, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel7))
                .addGap(26, 26, 26)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8)
                    .addComponent(moduleBox, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addStudentButton))
                .addGap(192, 192, 192))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(moduleBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(40, 40, 40)
                        .addComponent(addStudentButton))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lastName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(firstName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addComponent(jLabel6))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(password, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(date, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(286, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Add Student", jPanel2);

        newDate.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter(new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss"))));
        newDate.setText("yyyy-MM-dd HH:mm:ss");

        jLabel10.setText("Lesson Date and Time:");

        jLabel11.setText("Module Code: ");

        jLabel13.setText("Lesson Location");

        addClassButton.setText("Add New Class");
        addClassButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addClassButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel13)
                            .addComponent(jLabel11)
                            .addComponent(newDate)
                            .addComponent(newModuleBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(newLocation)))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(200, 200, 200)
                        .addComponent(addClassButton)))
                .addContainerGap(207, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(newModuleBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel13)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(newLocation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(newDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 188, Short.MAX_VALUE)
                .addComponent(addClassButton)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Add/Remove Class", jPanel5);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 566, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(225, 225, 225)
                .addComponent(signOutButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jTabbedPane1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(signOutButton)
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    // Clears class variables when Sign Out button is pressed
    private void signOutButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_signOutButtonActionPerformed
        try { 
            new selectClass().setVisible(false);
        } catch (SQLException ex) {
            Logger.getLogger(selectClass.class.getName()).log(Level.SEVERE, null, ex);
        }
             this.dispose(); 
              new login().setVisible(true);

              t = null;
              currentClass = null;
           
    }//GEN-LAST:event_signOutButtonActionPerformed

    // When search student button is pressed, splits the first and last name,
    // and gets attendance by searching against list of students
    private void searchStudentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchStudentActionPerformed
        searchButton();
    }//GEN-LAST:event_searchStudentActionPerformed


    private void jTextField1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyTyped

    }//GEN-LAST:event_jTextField1KeyTyped

    // When the Sign In button is pressed, the selected students are signed in to class
    // and removed from the list
    private void signInButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_signInButtonActionPerformed
       signInButton();
    }//GEN-LAST:event_signInButtonActionPerformed
   // When the Absent button is pressed, the selected students are registered as absent
    // and removed from the list
    private void absentButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_absentButtonActionPerformed
        
        signInAbsentButton();

    }//GEN-LAST:event_absentButtonActionPerformed

    private void dateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dateActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_dateActionPerformed

    // When Add New Student button is pressed, use fields to create new student
    private void addStudentButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addStudentButtonActionPerformed
        
    addStudentButton();
  
    }//GEN-LAST:event_addStudentButtonActionPerformed
    
    // When a student in the list is clicked, it's details are shown via labels
    private void studentListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_studentListMouseClicked
        String tmp = (String)studentList.getSelectedValue();
        
        for (int i = 0; i<list.size(); i++){
            if (tmp.equals(list.get(i).StudentFirstName + " " + list.get(i).StudentLastName)){
                attendanceRegisterLabel.setText(String.valueOf(list.get(i).StudentAttendance)+"%");
                studentIDLabel.setText(String.valueOf(list.get(i).StudentID));
               
                referralLabel.setText(Integer.toString(list.get(i).StudentReferral));
            }
        }
    }//GEN-LAST:event_studentListMouseClicked

    // When Add Class is pressed, a class object is created and added to database
    private void addClassButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addClassButtonActionPerformed
        
        addClassButton();
        
    }//GEN-LAST:event_addClassButtonActionPerformed

    private void referralButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_referralButtonActionPerformed
        
        // Initialise list
        List<String> list3 = new ArrayList<>();
        
        // Fill list2 with selected students
        list3 = studentList.getSelectedValuesList();
        int selectedIndex = studentList.getSelectedIndex();
        int[] arrayID = new int[30];

        // Go through selected students, check against list of students, add their 
        // studentID to a list, remove them from the jList
        
        for(int i = 0; i<list3.size(); i++){

            for(int j= 0; j<list.size(); j++){

                if(list3.get(i).equals(list.get(j).StudentFirstName + " " + list.get(j).StudentLastName)){                   
                    arrayID[i] = list.get(j).StudentID;
                }
            }
        }
       
        // Send students to updateAttendance() function to change attendance value
        try {
            addReferral(arrayID);
        } catch (SQLException ex) {
            Logger.getLogger(MainPage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_referralButtonActionPerformed

    private void negativeReferralButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_negativeReferralButtonActionPerformed
        
        // Initialise list
        List<String> list3 = new ArrayList<>();
        
        // Fill list2 with selected students
        list3 = studentList.getSelectedValuesList();
        int selectedIndex = studentList.getSelectedIndex();
        int[] arrayID = new int[30];

        // Go through selected students, check against list of students, add their 
        // studentID to a list, remove them from the jList
        
        for(int i = 0; i<list3.size(); i++){

            for(int j= 0; j<list.size(); j++){

                if(list3.get(i).equals(list.get(j).StudentFirstName + " " + list.get(j).StudentLastName)){                   
                    arrayID[i] = list.get(j).StudentID;
                }
            }
        }
       
        // Send students to updateAttendance() function to change attendance value
        try {
            negativeReferral(arrayID);
        } catch (SQLException ex) {
            Logger.getLogger(MainPage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_negativeReferralButtonActionPerformed

    private void studentIDRadioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_studentIDRadioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_studentIDRadioActionPerformed

    private void studentIDInputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_studentIDInputActionPerformed
        
    }//GEN-LAST:event_studentIDInputActionPerformed

    private void studentIDInputMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_studentIDInputMouseClicked
        studentIDInput.setForeground(Color.black);
        studentIDInput.setText("");
    }//GEN-LAST:event_studentIDInputMouseClicked

    private void studentIDInputKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_studentIDInputKeyPressed
        loadingBar.setValue(0);
        System.out.println("Started");
        
        if(evt.getKeyCode() == KeyEvent.VK_ENTER) {
            loadingBar.setValue(10);
            loadingBar.repaint();
            try {
                if(updateAttendanceID(studentIDInput.getText()) == true){
                        loadingBar.setValue(100);
                        loadingBar.repaint();
                        studentIDInput.setText("");
                }
            } catch (SQLException ex) {
                Logger.getLogger(MainPage.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
    }//GEN-LAST:event_studentIDInputKeyPressed
     
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new MainPage().setVisible(true);
                } catch (SQLException ex) {
                    Logger.getLogger(MainPage.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton absentButton;
    private javax.swing.JButton addClassButton;
    private javax.swing.JButton addStudentButton;
    private javax.swing.JLabel attendanceIndvLabel;
    private javax.swing.JLabel attendanceLabel;
    private javax.swing.JLabel attendanceRegisterLabel;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JFormattedTextField date;
    private javax.swing.JLabel firstLabel;
    private javax.swing.JTextField firstName;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JLabel lastLabel;
    private javax.swing.JTextField lastName;
    private javax.swing.JProgressBar loadingBar;
    private javax.swing.JComboBox<String> moduleBox;
    private javax.swing.JButton negativeReferralButton;
    private javax.swing.JFormattedTextField newDate;
    private javax.swing.JTextField newLocation;
    private javax.swing.JComboBox<String> newModuleBox;
    private javax.swing.JTextField password;
    private javax.swing.JButton referralButton;
    private javax.swing.JLabel referralLabel;
    private javax.swing.JRadioButton rfidRadio;
    private javax.swing.JButton searchStudent;
    private javax.swing.JButton signInButton;
    private javax.swing.JButton signOutButton;
    private javax.swing.JLabel studentIDIndvLabel;
    private javax.swing.JTextField studentIDInput;
    private javax.swing.JLabel studentIDLabel;
    private javax.swing.JRadioButton studentIDRadio;
    private javax.swing.JList<String> studentList;
    // End of variables declaration//GEN-END:variables
}
