import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TransactionLogAnalyse {
	public DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public List<BuildRecord> records = new ArrayList<BuildRecord>();
	public Map<String, Recorder> mapRecorders = new HashMap<String, Recorder>();

	public void analyse(String folder, String configFile) {

		loadConfig(configFile);

		File dir = new File(folder);
		if (dir.isDirectory()) {
			File[] files = dir.listFiles();
			for (File file : files) {
				analyseFile(file);
			}
		}
	}

	public void loadConfig(String configFile) {
		BufferedReader bufferedReader = null;
		try {
			String line;
			InputStreamReader read = new InputStreamReader(new FileInputStream(configFile));
			bufferedReader = new BufferedReader(read);
			while ((line = bufferedReader.readLine()) != null) {
				String[] item = line.trim().split(" ");// info,startTime,endTime
				records.add(new BuildRecord(item[0], item[1], item[2]));
				mapRecorders.put(item[0], new Recorder(item[0]));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void analyseFile(File file) {
		String lineTxt = null;
		BufferedReader bufferedReader = null;
		try {
			InputStreamReader read = new InputStreamReader(new FileInputStream(file));
			bufferedReader = new BufferedReader(read);
			while ((lineTxt = bufferedReader.readLine()) != null) {
				String lineTime = lineTxt.trim().split(",")[0];
				Recorder recorder = findRecorder(lineTime);
				if (recorder != null) {
					String lastPart = lineTxt.trim().split(",2001,")[1];
					// System.out.println(lastPart);
					String[] items = lastPart.split(",");
					if (lineTxt.contains("BDT_CRT@BDT")) {
						recorder.crtResult[0] += Integer.valueOf(items[0]);// total delay
						recorder.crtResult[1] += Integer.valueOf(items[1]);// diameter grpc delay
						recorder.crtResult[2] += Integer.valueOf(items[2]);// db insert delay

						recorder.crtResult[3] += (Integer.valueOf(items[0]) - Integer.valueOf(items[1])
								- Integer.valueOf(items[2]));// other delay

						recorder.crtResult[4]++;// count

					} else if (lineTxt.contains("BDT_NOTIFICATION@BDT")) {
						recorder.nofResult[0] += Integer.valueOf(items[0]);// total delay
						recorder.nofResult[1] += Integer.valueOf(items[1]);// diameter grpc delay

						recorder.nofResult[2] += Integer.valueOf(items[3]);// db query delay
						recorder.nofResult[3] += Integer.valueOf(items[4]);// db update delay

						recorder.nofResult[4] += (Integer.valueOf(items[0]) - Integer.valueOf(items[1])
								- Integer.valueOf(items[3]) - Integer.valueOf(items[4]));// other delay

						recorder.nofResult[5]++;// count
					}
				}
			}
			read.close();
		} catch (Exception e) {
			// e.printStackTrace();
			System.out.println(lineTxt);
		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public Recorder findRecorder(String lineTime) {
		for (BuildRecord record : records) {
			if (isTimeInRange(lineTime, record)) {
				return mapRecorders.get(record.info);
			}
		}
		return null;
	}

	public boolean isTimeInRange(String lineTime, BuildRecord record) {
		try {
			long dateLineTime = sdf.parse(lineTime).getTime();

			String startTime = record.startTime.replaceAll("_", " ");
			long startTimeD = sdf.parse(startTime).getTime();

			String endTime = record.endTime.replaceAll("_", " ");
			long endTimeD = sdf.parse(endTime).getTime();

			return (dateLineTime >= startTimeD) && (dateLineTime <= endTimeD);

		} catch (Exception e) {
			// e.printStackTrace();
		}
		return false;
	}

	public void displayResult() {
		Set<String> keys = mapRecorders.keySet();
		for (String key : keys) {
			Recorder recorder = mapRecorders.get(key);

			if (recorder.crtResult[4] > 0)
				System.out.println(recorder.info + "  Number:" + recorder.crtResult[4] + ", Total Delay:"
						+ recorder.crtResult[0] / recorder.crtResult[4] + ", Diameter Grpc Delay:"
						+ recorder.crtResult[1] / recorder.crtResult[4] + ", DB Insert Delay:"
						+ recorder.crtResult[2] / recorder.crtResult[4] + ", Other Delay:"
						+ recorder.crtResult[3] / recorder.crtResult[4]);

			if (recorder.nofResult[4] > 0)
				System.out.println(recorder.info + "  Number:" + recorder.nofResult[5] + ", Total Delay:"
						+ recorder.nofResult[0] / recorder.nofResult[5] + ", Diameter Grpc Delay:"
						+ recorder.nofResult[1] / recorder.nofResult[5] + ", DB Query Delay:"
						+ recorder.nofResult[2] / recorder.nofResult[5] + ", DB Update Delay:"
						+ recorder.nofResult[3] / recorder.nofResult[5] + ", Other Delay:"
						+ recorder.nofResult[4] / recorder.nofResult[5]);

		}
	}

	//java TransactionLogAnalyse /cluster/home/transaction/node_11/extract ./buildConfig.txt
	//example buildConfig.txt 
	//#01 2019-02-22_14:42:50 2019-02-22_14:43:50
	//#02 2019-02-22_14:43:50 2019-02-22_14:44:50
	//#03 2019-02-22_14:44:50 2019-02-22_14:45:50
	public static void main(String[] args) {
		TransactionLogAnalyse ins = new TransactionLogAnalyse();
		// folder, configFile
		ins.analyse(args[0], args[1]);
		ins.displayResult();
	}
}
