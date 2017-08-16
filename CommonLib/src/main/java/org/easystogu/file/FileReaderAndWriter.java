package org.easystogu.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class FileReaderAndWriter {
	public static String readFromFile(String filePath) {
		StringBuffer content = new StringBuffer();
		try {
			String encoding = "UTF-8";
			File file = new File(filePath);
			if (file.isFile() && file.exists()) {
				InputStreamReader read = new InputStreamReader(new FileInputStream(file), encoding);
				BufferedReader bufferedReader = new BufferedReader(read);
				String lineTxt = null;
				while ((lineTxt = bufferedReader.readLine()) != null) {
					content.append(lineTxt + "\n");
				}
				read.close();
			} else {
				System.out.println("fie not found:" + filePath);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return content.toString();
	}

	public static void writeToFile(String content, String filePath) {
		try {
			File f = new File(filePath);
			FileWriter fw;
			fw = new FileWriter(f);
			fw.write(content);
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String argv[]) {
		String filePath = "C:/Users/source.txt";
		String content = readFromFile(filePath);
		System.out.println(content);
		writeToFile(content, "C:/Users/target.txt");
	}
}
