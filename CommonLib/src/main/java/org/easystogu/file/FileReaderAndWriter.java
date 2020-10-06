package org.easystogu.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import org.easystogu.log.LogHelper;
import org.slf4j.Logger;

public class FileReaderAndWriter {
	private static Logger logger = LogHelper.getLogger(FileReaderAndWriter.class);

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
				logger.debug("file not found:" + filePath);
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

	// tail -100 file
	public static String tailFile(String filePath, int tailLen) {
		String[] contents = readFromFile(filePath).split("\n");
		if (tailLen >= contents.length) {
			return contents.toString();
		}
		//
		StringBuffer rtns = new StringBuffer();
		for (int index = contents.length - tailLen; index < contents.length; index++) {
			rtns.append(contents[index] + "\n");
		}
		return rtns.toString();
	}
}
