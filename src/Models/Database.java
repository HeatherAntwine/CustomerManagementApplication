/*
 * To change this license header, choose License HeaderesultSet in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.ZoneId;
import java.util.ArrayList;

/**
 *
 * @author hrant
 */
public class Database {

    static Connection conn = null;
    private static final String driver = "com.mysql.jdbc.Driver";
    private static final String dbName = "U04Dtg";
    private static final String dbUsername = "U04Dtg";
    private static final String dbPassword = "53688208471";
    private static final String dbURL = "jdbc:mysql://52.206.157.109/" + dbName;
    private static final String defaultUsername = "admin";
    private static final ZoneId currLocale = ZoneId.systemDefault();
    private static final int phxOffset = 2;
    private static final int nyOffset = 1;
    private static final int londOffset = 6;

    public static void makeConnection() throws ClassNotFoundException, SQLException {
        Class.forName(driver);
        conn = (Connection) DriverManager.getConnection(dbURL, dbUsername, dbPassword);
        System.out.println("Connection successful");
    }

    public static void closeConnection() throws SQLException {
        conn.close();
        System.out.println("Connection closed");
    }

    public static ArrayList<User> getUserList() {
        ArrayList<User> userList = new ArrayList<>();
        try {
            Statement stmt = conn.createStatement();
            ResultSet resultSet = stmt.executeQuery("SELECT userName FROM user;");
            while (resultSet.next()) {
                int userId = resultSet.getInt("userId");
                String userName = resultSet.getString("userName");
                String password = resultSet.getString("password");
                String active = resultSet.getString("active");
                String createdDate = resultSet.getString("createDate");
                String createBy = resultSet.getString("createBy");
                String lastUpdate = resultSet.getString("lastUpdate");
                String lastUpdatedBy = resultSet.getString("lastUpdatedBy");
                User user = new User(userId, userName, password, active, createBy, createdDate, lastUpdate, lastUpdatedBy);
                userList.add(user);
            }
        } catch (SQLException e) {
            System.out.println("ERROR: " + e);
        }
        return userList;
       
    }

    public static ArrayList<Customer> getCustList() {

        ArrayList<Customer> custList = new ArrayList<>();
        try {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM customer");
            while (resultSet.next()) {
                String name = resultSet.getString("customerName");
                int addressId = resultSet.getInt("addressId");
                String address = getAddress(addressId) + (", " + getAddress2(addressId) + ", " + getCity(getCityId(addressId)) + ", " + getPostalCode(addressId) + ", "
                        + getCountry(getCountryId((getCityId(addressId)))) + ".");
                String phoneNumber = getPhone(addressId);
                int id = resultSet.getInt("customerId");
                Customer customer = new Customer(id, name, phoneNumber, address, addressId);
                custList.add(customer);
            }
        } catch (SQLException e) {
            System.out.println("ERROR: " + e);
        }
        return custList;
    }
    
    public static ArrayList<Appointment> getApptList(int userId) throws SQLException{

        ArrayList<Appointment> apptList = new ArrayList<>();
        try {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM appointment WHERE createdBy = \'" + userId + "\';");
            while (resultSet.next()) {
                int appointmentId = resultSet.getInt("appointmentId");
                int customerId = resultSet.getInt("customerId");
                String customerName = getCustomerName(customerId);
                String title = resultSet.getString("title");
                String description = resultSet.getString("description");
                String location = resultSet.getString("location");
                String contact = resultSet.getString("contact");
                String url = resultSet.getString("url");
                String startTime = resultSet.getString("start");
                String endTime = resultSet.getString("end");
                int day = getDay(startTime);
                String startHour = getStartHour(startTime);
                String startMinute = getStartMinute(startTime);
                String endHour = getEndHour(endTime);
                String endMinute = getEndMinute(endTime);
                
                Appointment appointment = new Appointment(appointmentId, customerId, customerName, userId, title, description, location, contact, url, startTime, endTime,  offsetTime(startHour),  startMinute,  offsetTime(endHour),  endMinute, day);
                apptList.add(appointment);
            }

        } catch (SQLException e) {
            System.out.println("ERROR: " + e);
        }
        return apptList;
    }
    
    private static String offsetTime(String time){
        int timeConvert = Integer.parseInt(time);
        if (currLocale.equals(ZoneId.of("America/New_York"))) {
            timeConvert += nyOffset;
        } else if (currLocale.equals(ZoneId.of("America/Phoenix"))){
            timeConvert -= phxOffset;
        } else if (currLocale.equals(ZoneId.of("Europe/London"))) {
            timeConvert += londOffset;
        }
        String timeConverted = Integer.toString(timeConvert);
        return timeConverted;
    }

    public static String getCustomerName(int customerId) throws SQLException {
        String customerName = "";
        Statement statement = conn.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM customer WHERE customerId = " + customerId + ";");
        while (resultSet.next()) {
            customerName = resultSet.getString("customerName");
        }
        return customerName;
    }
    public static int getDay(String startTime){
        return Integer.parseInt(startTime.substring(8,10));
    }
    public static String getStartHour(String startTime) {
        return (startTime.substring(11, 13));
    }

    public static String getStartMinute(String startTime) {
        return (startTime.substring(14, 16));
    }

    public static String getEndHour(String endTime) {
        return (endTime.substring(11, 13));
    }

    public static String getEndMinute(String endTime) {
        return (endTime.substring(14, 16));
    }

    public static String getPhone(int addressId) {
        String phoneNumber = null;
        try {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM address WHERE addressId = " + addressId + ";");
            while (resultSet.next()) {
                phoneNumber = resultSet.getString("phone");
            }
        } catch (SQLException e) {
            System.out.println("ERROR: " + e);
        }
        return phoneNumber;
    }

    public static String getAddress2(int addressId) {
        String address2 = null;
        try {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM address WHERE addressId = " + addressId + ";");
            while (resultSet.next()) {
                address2 = resultSet.getString("address2");
            }
        } catch (SQLException e) {
            System.out.println("ERROR: " + e);
        }
        return address2;
    }

    public static String getPostalCode(int addressId) {
        String postalCode = null;
        try {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM address WHERE addressId = " + addressId + ";");
            while (resultSet.next()) {
                postalCode = resultSet.getString("postalCode");
            }
        } catch (SQLException e) {
            System.out.println("ERROR: " + e);
        }
        return postalCode;
    }

    public static String getAddress(int addressId) {
        String address = null;
        try {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM address WHERE addressId = " + addressId + ";");
            while (resultSet.next()) {
                address = resultSet.getString("address");
            }
        } catch (SQLException e) {
            System.out.println("ERROR: " + e);
        }
        return address;
    }

    public static int getCityId(int addressId) {
        int cityId = 0;
        try {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM address WHERE addressId = " + addressId + ";");
            while (resultSet.next()) {
                cityId = resultSet.getInt("cityId");
            }
        } catch (SQLException e) {
            System.out.println("ERROR: " + e);
        }
        return cityId;
    }

    public static String getCity(int cityId) {
        String city = null;
        try {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM city WHERE cityId = " + cityId + ";");
            while (resultSet.next()) {
                city = resultSet.getString("city");
            }
        } catch (SQLException e) {
            System.out.println("ERROR: " + e);
        }
        return city;
    }

    public static int getCountryId(int cityId) {
        int countryId = 0;
        try {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM city WHERE cityId = " + cityId + ";");
            while (resultSet.next()) {
                countryId = resultSet.getInt("countryId");
            }
        } catch (SQLException e) {
            System.out.println("ERROR: " + e);
        }
        return countryId;
    }

    public static String getCountry(int countryId) {
        String country = null;
        try {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM country WHERE countryId = " + countryId + ";");
            while (resultSet.next()) {
                country = resultSet.getString("country");
            }
        } catch (SQLException e) {
            System.out.println("ERROR: " + e);
        }
        return country;
    }

    public static int getCustomerId(String customerName) throws SQLException {
        int customerId = 0;
        Statement statement = conn.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM customer WHERE customerName = \'" + customerName + "\';");
        while (resultSet.next()) {
            customerId = resultSet.getInt("customerId");
        }
        return customerId;
    }

    public static int getNextCustomerId() throws SQLException {
        int nextCustId = 0;

        Statement statement = conn.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT COUNT(customerId) FROM customer;");
        while (resultSet.next()) {
            nextCustId = resultSet.getInt(1);
        }
        nextCustId++;
        return nextCustId;
    }

    public static int getNextAddressId() throws SQLException {
        int nextAddId = 0;

        Statement statement = conn.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT COUNT(addressId) FROM address;");
        while (resultSet.next()) {
            nextAddId = resultSet.getInt(1);
        }
        nextAddId++;
        return nextAddId;
    }

    public static int getNextCityId() throws SQLException {
        int nextCityId = 0;

        Statement statement = conn.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT COUNT(cityId) FROM city;");
        while (resultSet.next()) {
            nextCityId = resultSet.getInt(1);
        }
        nextCityId++;
        return nextCityId;
    }

    public static int getNextCountryId() throws SQLException {
        int nextCountryId = 0;

        Statement statement = conn.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT COUNT(countryId) FROM country;");
        while (resultSet.next()) {
            nextCountryId = resultSet.getInt(1);
        }
        nextCountryId++;
        return nextCountryId;
    }

    public static int getNextApptId() throws SQLException {
        int nextApptId = 0;

        Statement statement = conn.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM appointment;");
        while (resultSet.next()) {
            if (resultSet.getInt("appointmentId") > nextApptId) {
                nextApptId = resultSet.getInt("appointmentId");
            }
        }
        nextApptId++;
        return nextApptId;
    }

    public static void addNewCustomer(int customerId, String customerName, String address, String address2, String city, String postalCode, String country, int addressId) {
        try {
            Statement statement = conn.createStatement();
            statement.executeUpdate("INSERT INTO customer VALUES(" + customerId + ", \'" + customerName + "\', " + addressId + ", \'1\', CURRENT_DATE, \'" + defaultUsername + "\', CURRENT_TIMESTAMP, \'" + defaultUsername + "\');");
        } catch (SQLException e) {
            System.out.println("ERROR: " + e);
        }
    }

    public static void addNewAddress(int addressId, String address, String address2, int cityId, String postalCode, String phoneNumber) {
        try {
            Statement statement = conn.createStatement();
            statement.executeUpdate("INSERT INTO address VALUES(" + addressId + ", \'" + address + "\', \'" + address2 + "\', " + cityId + ", \'" + postalCode + "\', \'" + phoneNumber + "\', CURRENT_DATE, \'" + defaultUsername + "\', CURRENT_TIMESTAMP, \'" + defaultUsername + "\');");
        } catch (SQLException e) {
            System.out.println("ERROR: " + e);
        }
    }

    public static void addNewCity(int cityId, String city, int countryId) {
        try {
            Statement statement = conn.createStatement();
            statement.executeUpdate("INSERT INTO city VALUES(" + cityId + ", \'" + city + "\', " + countryId + ", CURRENT_DATE, \'" + defaultUsername + "\', CURRENT_TIMESTAMP, \'" + defaultUsername + "\');");
        } catch (SQLException e) {
            System.out.println("ERROR: " + e);
        }
    }

    public static void addNewCountry(int countryId, String country) {
        try {
            Statement statement = conn.createStatement();
            statement.executeUpdate("INSERT INTO country VALUES(" + countryId + ", \'" + country + "\', CURRENT_DATE, \'" + defaultUsername + "\', CURRENT_TIMESTAMP, \'" + defaultUsername + "\');");
        } catch (SQLException e) {
            System.out.println("ERROR: " + e);
        }
    }

    public static void addNewAppt(String title, String customerName, String description, String location, String contact, String url, int day, int startHour, int startMin, int endHour, int endMin) throws SQLException {
        int userId = 1;
        int customerId = getCustomerId(customerName);
        int appointmentId = getNextApptId();
        try {
            Statement statement = conn.createStatement();
            statement.executeUpdate("INSERT INTO appointment VALUES(" + appointmentId + ", " + customerId + ", \'" + title + "\', \'" + description + "\', \'" + location + "\', \'" + contact + "\', \'" + url + "\', \'2018-09-" + day + " " + startHour + ":" + startMin + ":00\', \'2018-09-" + day + " " + endHour + ":" + endMin + ":00\', CURRENT_DATE , " + userId + " , CURRENT_TIMESTAMP, " + userId + ");");
                             //                                           appointmentId, customerId,                title,              description,            location,               contact,            url      ,  2018-09-day            hour:min:00                      , 2018-09-    day         hour:min:00                  ,            
        } catch (SQLException e) {
            System.out.println("ERROR: " + e);
        }
    }

    public static void modCustomer(int customerId, int addressId, String modCustomerName, String modPhoneNumber, String modAddress, String modAddress2, String modCity, String modPostalCode, String modCountry) {
        // Update Customer class
        try {
            Statement statement = conn.createStatement();
            statement.executeUpdate("UPDATE customer SET customerName = \'" + modCustomerName + "\', lastUpdate = CURRENT_TIMESTAMP WHERE customerId = " + customerId + ";");
        } catch (SQLException e) {
            System.out.println("ERROR: " + e);
        }
        // Update Address class
        try {
            Statement statement = conn.createStatement();
            statement.executeUpdate("UPDATE address SET address = \'" + modAddress + "\', address2 = \'" + modAddress2 + "\', postalCode = \'" + modPostalCode + "\', phone = \'" + modPhoneNumber + "\', lastUpdate = CURRENT_TIMESTAMP WHERE addressId = " + addressId + ";");
        } catch (SQLException e) {
            System.out.println("ERROR: " + e);
        }
        // Update City class
        try {
            Statement statement = conn.createStatement();
            statement.executeUpdate("UPDATE city SET city = \'" + modCity + "\', lastUpdate = CURRENT_TIMESTAMP WHERE cityId = " + getCityId(addressId) + ";");
        } catch (SQLException e) {
            System.out.println("ERROR: " + e);
        }
        // Update Country class
        try {
            Statement statement = conn.createStatement();
            statement.executeUpdate("UPDATE country SET country = \'" + modCountry + "\', lastUpdate = CURRENT_TIMESTAMP WHERE countryId = " + getCountryId(getCityId(addressId)) + ";");
        } catch (SQLException e) {
            System.out.println("ERROR: " + e);
        }

        LocaleData.updateLocalCustomers();
    }
//    public static void modAddress(){}
//    public static void modCity(){}
//    public static void modCountry(){}

    public static void modAppt(int appointmentId, String modTitle, String customerName, String modDescription, String modLocation, String modContact, String modUrl, int modDay, int modStartHour, int modStartMin, int modEndHour, int modEndMin) throws SQLException {
        // Get customerId from customer name first 
        //TODO - check into having the ID included in the screen and saved as part of Appt - for portfolio project, not WGU project
        int customerId = getCustomerId(customerName);
        try {
            Statement statement = conn.createStatement();
            statement.executeUpdate("UPDATE appointment SET customerId = " + customerId + ", title = \'" + modTitle + "\', description = \'" + modDescription + "\', location = \'" + modLocation + "\', contact = \'" + modContact + "\', url = \'" + modUrl
                    + "\', start = \'2018-09-" + modDay + " " + modStartHour + ":" + modStartMin + ":00\', end = \'2018-09-" + modDay + " " + modEndHour + ":" + modEndMin + ":00\', lastUpdate = CURRENT_TIMESTAMP WHERE appointmentId = " + appointmentId);
        } catch (SQLException e) {
            System.out.println("ERROR: " + e);
        }
    }

    public static void delCustomer(Customer customer) {
        try {
            Statement statement = conn.createStatement();
            statement.executeUpdate("DELETE FROM customer WHERE customerId = " + customer.getCustomerId() + ";");
            LocaleData.updateLocalCustomers();
        } catch (SQLException e) {
            System.out.println("ERROR: " + e);
        }
    }
//    public static void delAddress(){}
//    public static void delCity(){}
//    public static void delCountry(){}

    public static void delAppt(int appointmentId) {
        try {
            Statement statement = conn.createStatement();
            statement.executeUpdate("DELETE FROM appointment WHERE appointmentId = " + appointmentId + ";");
            LocaleData.updateLocalAppts();
        } catch (SQLException e) {
            System.out.println("ERROR: " + e);
        }
    }
    
    public static String getApptCountByType(String type) throws SQLException{
        int apptCount = 0;
        Statement statement = conn.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT COUNT(appointmentId) FROM appointment WHERE description = \'" + type + "\';");
        while(resultSet.next()){
            apptCount = resultSet.getInt(1);
        }
        return String.valueOf(apptCount);
    }

    public static int getUserIdByUsername(String userName) throws SQLException{
        int userId = 0;
        Statement stmt = conn.createStatement();
        ResultSet resultSet = stmt.executeQuery("SELECT * FROM user WHERE userName = \'" + userName + "\';");
        while(resultSet.next()){
            userId = Integer.valueOf(resultSet.getString("userId"));
        }
        return userId;
    }

    
}
