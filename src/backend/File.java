package backend;

import java.io.*;


public class File { //for writing to files and stuff

	private String filename;
	private BufferedReader r;
	private BufferedWriter w;
	private boolean append;

	public File(String filename, boolean append) {
		this.filename = filename;
		this.append = append;
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
		File file = new File(System.getProperty("user.dir")+"/src/backend/Database.ini", true);


		//file.clearFile();
		//		for (int i = 0; i < 10; i++) {
		//			file.writeLine("Dette er linje nr: " + i);
		//
		//		}

		/*for (int i = 0; i < 5; i++) {
			file.writeObject(new Date(1234567L)):
		}*/

		System.out.println(file.readLines(0, 10)); // Reads from line n to m, including m
		System.out.println(file.readLine(10)); // Reads the nth line

	}
}