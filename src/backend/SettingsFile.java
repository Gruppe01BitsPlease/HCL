package backend;


import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import java.io.*;
import java.util.Properties;
/**
 * Created by bahafeld on 01.04.2016.
 * Every method uses try-with-resources, no need to close them manually.
 */
public class SettingsFile {
    private final String propPath = "./configdev.properties";

    /**
     * Checks if file is there, will throw Exeception if it can not be found and a default can't be created.
     * @throws FileNotFoundException
     */
    public SettingsFile() throws FileNotFoundException {
        java.io.File f = new java.io.File(propPath);
        if(!f.exists() || f.isDirectory()){
            if(!createDefaultFile()) throw new FileNotFoundException();
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
            Properties prop = readDefaultTemplate();
            prop.store(fileOut, null);
            return true;
        }catch (IOException e){return false;}
    }

    private Properties readDefaultTemplate() throws IOException{
        try(InputStream in = this.getClass().getClassLoader().getResourceAsStream("defaultdev.properties")){
            Properties prop = new Properties();
            prop.load(in);
            return prop;
        }
    }

    /**
     * Returns the value of provided property - host,database,user,password
     * This value can be an empty string!
     * Returns null if not found or IOexception happens
     * TODO: Clean and Errorhandling #DONE
     */
    public String getPropValue(String key){
        if (key == null) return "Invalid input";
        try (FileInputStream fileInn = new FileInputStream(propPath)){
            Properties prop = new Properties();
            prop.load(fileInn);
            String value = prop.getProperty(key);
            if (value == null) return null;
            return readLineAsBase64(value);
        } catch (IOException e) {return "Could not access " + propPath;}
    }

    /**
     * Saves the value of provided property - ex host,database,user,password
     * Returns false if not saved
     * Can save boolean with string "1" or "0", saved in plaintext.
     * Sending inn empty value will clear field.
     * If it's unable to find key, it will make that key.
     * TODO: Clean and Errorhandling #DONE
     */
    public boolean setPropValue(String key, String value)  {
        if(value == null || key == null) return false;
        try (FileInputStream fileInn = new FileInputStream(propPath)) {
            Properties prop = new Properties();
            prop.load(fileInn);
            fileInn.close();
            try (FileOutputStream fileOut = new FileOutputStream(propPath)){
                prop.setProperty(key, writeLineAsBase64(value));
                prop.store(fileOut, null);
                return true;
            }catch (IOException e) {return false;}
        } catch (IOException e) {return false;}
    }


    /**
     * Technically it reads *reverse* Base64, so it's actually useless for reading Base64 V(^.^)V
     * Support for boolean values, which will be saved in plaintext! Anything else should be encrypted
     */
    private String readLineAsBase64(String line){
        if(line.trim().equals("1") || line.trim().equals("0")) return line;
        String read = new StringBuilder(line).reverse().toString();
        byte[] decoded = Base64.decode(read);
        return new String(decoded);
    }

    private String writeLineAsBase64(String line){
        if(line.trim().equals("1") || line.trim().equals("0")) return line;
        byte[] bytes = line.getBytes();
        String base64 = Base64.encode(bytes);
        return new StringBuilder(base64).reverse().toString();//In reverse 'cause that's much safer they'll never know
    }

    public static void main(String[] args) throws Exception {

        System.out.println("---ORIGINAL---");
        SettingsFile test = new SettingsFile();
        System.out.println(test.getPropValue("host"));
        System.out.println(test.getPropValue("database"));
        System.out.println(test.getPropValue("user"));
        System.out.println(test.getPropValue("password"));
        System.out.println(test.getPropValue("firsttime"));

        test.setPropValue("this is a new setting", "this is a new value");
        System.out.println(test.getPropValue("this is a new setting"));


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