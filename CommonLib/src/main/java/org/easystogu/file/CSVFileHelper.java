package org.easystogu.file;

import java.io.IOException;
import java.util.List;

import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;
import com.google.common.base.Charsets;

public class CSVFileHelper {
	public static void read(String fileName) {
		try {
			CsvReader csvReader = new CsvReader(fileName);
			csvReader.readHeaders();
			while (csvReader.readRecord()) {
				// read first line
				System.out.println(csvReader.getRawRecord());
				// read column
				System.out.println(csvReader.get("Link"));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void write(String fileName, String[] headers, List<String[]> contents) {
		try {
			CsvWriter csvWriter = new CsvWriter(fileName, ',', Charsets.UTF_8);
			csvWriter.writeRecord(headers);
			for (String[] str : contents) {
				csvWriter.writeRecord(str);
			}
			csvWriter.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
}
