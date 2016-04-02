package backend;


import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import java.io.*;
import java.util.Properties;
/**
 * Created by bahafeld on 01.04.2016.
 */
public class SettingsFile {
    private final String propPath = "./db.properties";

    public SettingsFile() {
        java.io.File f = new java.io.File(propPath);
        if(!f.exists() || f.isDirectory()){
            createDefaultFile();
        }
    }
    /**
     * Creates a prop file from default.prop in resources
     * Returns false for any IOerror reading from resources (unlikely) or writing the copy (more likely)
     * or can't close streams.
     * TODO: Clean and Errorhandling #DONE
     */
    private boolean createDefaultFile(){
        try(FileOutputStream fileOut = new FileOutputStream(propPath)) {
            //Get default template from resources folder
            Properties prop = readDefaultTemplate();
            //write new config file at root
            prop.store(fileOut, null);
            return true;
        }catch (IOException e){
            return false;
        }
    }

    private Properties readDefaultTemplate() throws IOException{
        try(InputStream in = this.getClass().getClassLoader().getResourceAsStream("default.properties")){
            Properties prop = new Properties();
            prop.load(in);
            return prop;
        }
    }

    /**
     * Returns the value of provided property - host,database,user,password
     * Returns null if not found or IOexception happens
     * TODO: Clean and Errorhandling #DONE
     */
    String getPropValue(String key){
        try (FileInputStream fileInn = new FileInputStream(propPath)){
            Properties prop = new Properties();
            prop.load(fileInn);
            return readLineAsBase64(prop.getProperty(key));
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Saves the value of provided property - ex host,database,user,password
     * Returns false if not saved
     * !!!!if values set to null it will delete all info!!!!
     * TODO: Clean and Errorhandling
     */
    boolean setPropValue(String key, String value)  {
        if(value == null || key == null) return false;
        try (FileInputStream fileInn = new FileInputStream(propPath)
            ) {
            Properties prop = new Properties();
            prop.load(fileInn);
            fileInn.close();
            try (FileOutputStream fileOut = new FileOutputStream(propPath)){
                prop.setProperty(key, writeLineAsBase64(value));
                prop.store(fileOut, null);
                return true;
            }
        } catch (IOException e) {
            return false;
        }
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