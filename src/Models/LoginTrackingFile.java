/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 *
 * @author hrant
 */
public class LoginTrackingFile {

    private static final String logName = "/antwineSoftware2TrackingLog.txt";
    private static String logPath = "";

    public static void logInformation(String trackLogin) {
        getUserDesktopPath();
        try {
            File file = new File(logPath.concat(logName));
            //Check to see if file exists first
            if (file.exists()) {
                trackLogin = getLogInfo(logPath + logName).concat(trackLogin);
            }
            //Write the information to the text file - https://www.tutorialspoint.com/java/java_filewriter_class.htm
            FileWriter writer = new FileWriter(file);
            // https://www.javatpoint.com/java-bufferedwriter-class
            BufferedWriter buffer = new BufferedWriter(writer);  //TODO - lookup Try-with-resources
            buffer.write(trackLogin);
            buffer.close();
        } catch (IOException e) {
            System.out.println("ERROR: loginInformation " + e);
        }

    }

    public static void getUserDesktopPath() {
        String desktopPath = System.getProperty("user.home") + "/Desktop";
        desktopPath.replace("\\", "//");
        logPath = desktopPath;
    }

    public static String getLogInfo(String filePath) throws IOException {
        String info;
        info = new String(Files.readAllBytes(Paths.get(filePath)));
        return info;
    }

    public static String getFilePath() {
        return logPath.concat(logName);
    }

}
