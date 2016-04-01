package backend;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import java.io.*;
import java.util.Properties;

public class SettingsFile {
    private FileInputStream fileInn;
    private FileOutputStream fileOut;
    private String propPath = "./db.properties";

    public SettingsFile() {
        java.io.File f = new java.io.File(propPath);
        if(!f.exists() || f.isDirectory()){
            try{
                    //Get default template from resources folder
                    InputStream in = this.getClass().getClassLoader().getResourceAsStream("default.properties");
                    Properties prop = new Properties();
                    prop.load(in);
                    in.close();

                    //write new config file at root
                    fileOut = new FileOutputStream(propPath);
                    prop.store(fileOut, "test");

            }catch(IOException e){
                System.out.println("Default file not found \n"+ e);
            }finally {
                try {
                    fileOut.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Returns the value of provided property - host,database,user,password
     * Returns null if not found
     * TODO: Clean and Errorhandling
     */
    String getPropValue(String key){
        try {

            Properties prop = new Properties();
            fileInn = new FileInputStream(propPath);
            prop.load(fileInn);
            return readLineAsBase64(prop.getProperty(key));
        } catch (IOException e) {
            System.out.println("Property file '" + propPath + "' not found at root folder \n" + " Exception: " + e);
        } finally {
            try {
                fileInn.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * Saves the value of provided property - host,database,user,password
     * Returns false if not saved
     * TODO: Clean and Errorhandling
     */
    boolean setPropValue(String key, String value)  {

        try {
            Properties prop = new Properties();
            fileInn = new FileInputStream(propPath);
            prop.load(fileInn);

            fileOut = new FileOutputStream(propPath);
            prop.setProperty(key, writeLineAsBase64(value));
            prop.store(fileOut, null);

            return true;
        } catch (IOException e) {
            System.out.println("Property file '" + propPath + "' not found at root folder \n" + " Exception: " + e);
        } finally {
            try {
                fileInn.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                fileOut.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return false;
    }


    /**
     * Technically it reads *reverse* Base64, so it's actually useless for reading Base64 V(^.^)V
     */
    private String readLineAsBase64(String line){
        String read = new StringBuilder(line).reverse().toString();
        byte[] decoded = Base64.decode(read);
        return new String(decoded);
    }

    /**
     * Because plaintext 2utrygt4me
     */
    private String writeLineAsBase64(String line){
        byte[] bytes = line.getBytes();
        String base64 = Base64.encode(bytes);
        return new StringBuilder(base64).reverse().toString();//In reverse 'cause that's much safer they'll never know
    }

    public static void main(String[] args) throws Exception {

        System.out.println("---ORIGINAL---");
        SettingsFile test = new SettingsFile();
        System.out.println(test.getPropValue("database"));
        System.out.println(test.getPropValue("user"));
        System.out.println(test.getPropValue("password"));

        test.setPropValue("database", "jdbc:mysql://mysql.stud.iie.ntnu.no:3306/");
        test.setPropValue("user", "olavhus");
        test.setPropValue("password", "CmrXjoQn");

        System.out.println("---CHANGED---");
        System.out.println(test.getPropValue("database"));
        System.out.println(test.getPropValue("user"));
        System.out.println(test.getPropValue("password"));

        System.out.println("---KRYPTCHECK---");
        String inn = "test";
        String out = test.writeLineAsBase64(inn);
        System.out.println(test.writeLineAsBase64(inn));
        System.out.println(test.readLineAsBase64(out));





    }
}