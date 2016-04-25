package backend;


import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import java.io.*;
import java.util.Properties;
/**
 * Class for loading, writing and reading from a properties file, this means data is stored in a "key-value" format.
 * The "value" field will be obscured as long the value is not '1' or '0' (this is to support boolean values).
 * The obscurity is not supposed to prevent resourceful attacker, but will at least hide it from plaintext view.
 */
public class SettingsFile {
    // TODO: change to config.properties
    private final String outPath = "./config.properties";

    /**
     * Checks if file exists. If not it calls the createDefaultFile().
     * Throws exception if default file can't be created. This can happen because of ownership problems,
     * filename already taken or a directory with the same name is in the install folder.
     * @throws FileNotFoundException
     */
    public SettingsFile() throws FileNotFoundException {
        java.io.File f = new java.io.File(outPath);
        if(!f.exists() || f.isDirectory()){
            if(!createDefaultFile()) throw new FileNotFoundException();
        }
    }

    /**
     * Creates a prop file from default.properties in resources folder, and writes a copy called config.properties
     * to the installed dir.
     * Using helpmethod readDefaultTemplate to read the default file.
     * Returns false for any IO-error reading from resources (unlikely) or writing the copy (more likely).
     * @return boolean value, describes whether file creation was successful or not
     */
    private boolean createDefaultFile(){
        try(FileOutputStream fileOut = new FileOutputStream(outPath)) {
            Properties prop = readDefaultTemplate();
            prop.store(fileOut, null);
            return true;
        }catch (IOException e){return false;}
    }

    private Properties readDefaultTemplate() throws IOException{
        //TODO: change to default.properties
        final String defaultPath = "defaults/default.properties";
        try(InputStream in = this.getClass().getClassLoader().getResourceAsStream(defaultPath)){
            Properties prop = new Properties();
            prop.load(in);
            return prop;
        }
    }

    /**
     * Returns the value of provided key in the outPath.
     * If key is empty or null, returns "Invalid Input".
     * Returns "Key not found!", if the key can't be located.
     * For any IO-error it returns "Could not access " + outPath
     * Decrypts with readLineAsBase64();
     * @param key String value matching the key in the config.properties file.
     * @return value for specified key, can be a empty string.
     */
    public String getPropValue(String key){
        if (key == null || key.trim().equals("")) return "Invalid Input";
        try (FileInputStream fileInn = new FileInputStream(outPath)){
            Properties prop = new Properties();
            prop.load(fileInn);
            String value = prop.getProperty(key);
            if (value == null){
                return "Key not found!";
            }else{
                return readLineAsBase64(value);
            }
        } catch (IOException e) {return "Could not access " + outPath;}
    }

    /**
     * Will create or overwrite existing key and value.
     * Can save boolean with string "1" or "0", can be used as pseudo-boolean values, saved in plaintext.
     * Sending inn empty value will clear field.
     * If it's unable to find key, it will make that key.
     * Encrypts with writeLineAsBase64();
     * @param key : Set a String key. Can't be empty or null.
     * @param value : Set connected String value. "1" or "0" can will be saved in plaintext. Can be empty, but not null.
     * @return boolean value : describes whether key creation was successful or not
     */
    public boolean setPropValue(String key, String value)  {
        if(key == null || key.trim().equals("") || value == null) return false;
        try (FileInputStream fileInn = new FileInputStream(outPath)) {
            Properties prop = new Properties();
            prop.load(fileInn);
            fileInn.close();
            try (FileOutputStream fileOut = new FileOutputStream(outPath)) {
                prop.setProperty(key, writeLineAsBase64(value));
                prop.store(fileOut, null);
                return true;
            }
        } catch (IOException e) {return false;}
    }

    /**
     * Technically it reads *reverse* Base64.
     * Support for boolean values, which will be read as plaintext!
     * @param line String to be decrypted.
     * @return Decrypted String. Will return immediately if String is "0" or "1".
     */
    private String readLineAsBase64(String line){
        if(line.trim().equals("1") || line.trim().equals("0")) return line;
        String read = new StringBuilder(line).reverse().toString();
        byte[] decoded = Base64.decode(read);
        return new String(decoded);
    }

    /**
     * Technically it writes *reverse* Base64.
     * Boolean will not be encrypted!
     * @param line String to be encrypted.
     * @return Encrypted String. Will return immediately if String is "0" or "1".
     */
    private String writeLineAsBase64(String line){
        if(line.trim().equals("1") || line.trim().equals("0")) return line;
        byte[] bytes = line.getBytes();
        String base64 = Base64.encode(bytes);
        return new StringBuilder(base64).reverse().toString();
    }

    public static void main(String[] args) throws Exception {

        System.out.println("---ORIGINAL---");
        SettingsFile test = new SettingsFile();
        System.out.println(test.getPropValue("host"));
        System.out.println(test.getPropValue("database"));
        System.out.println(test.getPropValue("user"));
        System.out.println(test.getPropValue("password"));
        System.out.println(test.getPropValue("firsttime"));

        System.out.println(test.getPropValue("this is a new setting"));

        System.out.println("---CHANGED---");
        System.out.println(test.getPropValue("database"));
        System.out.println(test.getPropValue("user"));
        System.out.println(test.getPropValue("password"));

      /*  System.out.println("---CRYPT-CHECK---");
        String inn = "test";
        String out = test.writeLineAsBase64(inn);
        System.out.println(test.writeLineAsBase64(inn));
        System.out.println(test.readLineAsBase64(out));*/
    }
}