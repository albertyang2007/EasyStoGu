package org.easystogu.utils;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.easystogu.log.LogHelper;
import org.slf4j.Logger;

public class FileHelper {
	private static Logger logger = LogHelper.getLogger(FileHelper.class);
	// full path file name
	public static void RenameFile(String srcFile, String destFile) {
		try {
			File source = new File(srcFile);
			File target = new File(destFile);
			logger.debug("Rename file " + source + " to " + target);
			FileUtils.copyFile(source, target);
			FileUtils.deleteQuietly(source);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		FileHelper.RenameFile("C:/myFolder/EasyStoGu/report_2015-11-23.html", "C:/myFolder/EasyStoGu/estimate_2015-11-23.html");
	}
}
