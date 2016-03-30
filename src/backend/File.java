package backend;

import java.io.*;

import com.sun.org.apache.bcel.internal.util.ClassLoader;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
//import java.util.Base64;
import java.net.URL;
import java.util.Arrays;


public class File{ //for writing to files and stuff

	private String filename;
	private BufferedReader r;
	private BufferedWriter w;
	private boolean append;

	public File(String filename, boolean append) {
		this.filename = filename;
		this.append = append;
	}
    public File() {
		try {
			this.filename = File.class.getResource("/Database.ini").toURI().getPath();
		}
		catch (Exception e) {}
        this.append = true;
    }
    public boolean fileExists(){
        java.io.File file = new java.io.File(filename);
        return file.exists() && !file.isDirectory();
    }

	private boolean rInit() {
		try {
			r = new BufferedReader(new FileReader(filename));
            return true;
        }
		catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
		}
	}

	private boolean wInit() {
		try {
			w = new BufferedWriter(new FileWriter(filename, append));
		}
		catch (IOException e) {
			return false;
		}
		return true;
	}

	private void rEnd() {
		try {
			r.close();
		}
		catch (IOException e) {
		}

	}

	private void wEnd() {
		try {
			w.close();
		}
		catch (IOException e) {
		}

	}

	public String getFilename() {
		return filename;
	}

	public void clearFile() {
		try {
			PrintWriter pw = new PrintWriter(filename);
			pw.close();
		}
		catch (FileNotFoundException e) {
		}

	}

	public boolean writeLine(String in) {
		try {
			wInit();
			w.write(in + "\n");
		}
		catch (IOException e) {
			return false;
		}
		wEnd();
		return true;
	}

	public String readLines(int start, int end) {
		String out = "";
		String temp = "";
		try {
			System.out.println(rInit());
            System.out.println(r);
			for (int i = 0; i <= end; i++) {
                temp = r.readLine();
                System.out.println(r);

				if (i >= start && i <= end) {
					out += temp + "\n";
				}
			}

		}
		catch (IOException e) {
			e.printStackTrace();
		}
		rEnd();
		return out;
	}

	public String readLine(int line) {
		String out = "";
		String temp = "";
		try {
			rInit();
			for (int i = 0; i <= line; i++) {
				temp = r.readLine();
				if (i == line) {
					out = temp;
				}
			}
		}
		catch (IOException e) {
			return out;
		}

		rEnd();
		return out;
	}

    /**
     * Technically it reads *reverse* Base64, so it's actually useless for reading Base64 V(^.^)V
     */
    public String readLineAsBase64(int line){

        String read = new StringBuilder(readLine(line)).reverse().toString();
        byte[] decoded = Base64.decode(read);
        String out = new String(decoded); //Briliant

        return out;
    }

    /**
     * Because plaintext 2utrygt4me
     */
    public boolean writeLineAsBase64(String line){
        byte[] bytes = line.getBytes();
        String base64 = Base64.encode(bytes);
        return writeLine(new StringBuilder(base64).reverse().toString());//In reverse 'cause that's much safer they'll never know
    }

	public boolean writeObject(Object o) {
		boolean ok = false;

		try {
			wInit();
			PrintWriter print = new PrintWriter(new FileWriter(filename));
			//print.wr
		}
		catch (Exception e) {
		}

		wEnd();
		return ok;
	}

	public static void main(String[] args) {
		//File file = new File(System.getProperty("user.dir")+"/src/backend/Database.ini", true);

		//file.clearFile();
		//		for (int i = 0; i < 10; i++) {
		//			file.writeLine("Dette er linje nr: " + i);
		//
		//		}

		/*for (int i = 0; i < 5; i++) {
			file.writeObject(new Date(1234567L)):
		}*/
       /* file.clearFile();
        file.writeLineAsBase64("jdbc:mysql://mysql.stud.iie.ntnu.no:3306/");
        file.writeLineAsBase64("olavhus");
        file.writeLineAsBase64("CmrXjoQn");*/
        //ImputSream url2 = User.class.getResourceAsStream("Database.ini");

        File file = null;
        try {
			file = new File(File.class.getResource("/Database.ini").toURI().getPath(), true);
            System.out.println(file.readLineAsBase64(0));
            System.out.println(file.readLineAsBase64(1));
            System.out.println(file.readLineAsBase64(2));
        }
        catch(Exception e){}

        System.out.println(file.getFilename());
        System.out.println(file.fileExists());

    }
}