/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author hrant
 */
public class LocaleData {
    public static User currUser = null;
    private static ArrayList<User> userList;
    private static ArrayList<Customer> custList;
    private static ArrayList<Appointment> apptList;
    
    public static void updateLocalUsers(){
        userList = Database.getUserList();
    }
    public static void updateLocalCustomers(){
        custList = Database.getCustList();
    }
    public static void updateLocalAppts() throws SQLException{
        apptList = Database.getApptList(1);
    }
    public static void setCurrUser(User currUser) {
        LocaleData.currUser = currUser;
    }
    public static ArrayList<User> getUserList() {
        return userList;
    }

    public static User getCurrUser() {
        return currUser;
    }

    public static ArrayList<Customer> getCustList() {
        return custList;
    }

    public static ArrayList<Appointment> getApptList() {
        return apptList;
    }

    
    
}
