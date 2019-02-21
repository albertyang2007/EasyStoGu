import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class TransactionLogAnalyse {
	public DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public double[] crtResult = new double[5];
	public double[] nofResult = new double[6];

	public void analyseFolder(String folder, String startTime, String endTime) {
		File dir = new File(folder);
		if (dir.isDirectory()) {
			File[] files = dir.listFiles();
			for (File file : files) {
				analyseFile(file, startTime, endTime);
			}
		}
	}

	public void analyseFile(File file, String startTime, String endTime) {
		String lineTxt = null;
		try {
			InputStreamReader read = new InputStreamReader(new FileInputStream(file));
			BufferedReader bufferedReader = new BufferedReader(read);
			while ((lineTxt = bufferedReader.readLine()) != null) {
				String lineTime = lineTxt.trim().split(",")[0];
				if (isTimeInRange(lineTime, startTime, endTime)) {
					String lastPart = lineTxt.trim().split(",2001,")[1];
					// System.out.println(lastPart);
					String[] items = lastPart.split(",");
					if (lineTxt.contains("BDT_CRT@BDT")) {
						crtResult[0] += Integer.valueOf(items[0]);// total delay
						crtResult[1] += Integer.valueOf(items[1]);// diameter grpc delay
						crtResult[2] += Integer.valueOf(items[2]);// db insert delay

						crtResult[3] += (Integer.valueOf(items[0]) - Integer.valueOf(items[1])
								- Integer.valueOf(items[2]));// other delay

						crtResult[4]++;// count

					} else if (lineTxt.contains("BDT_NOTIFICATION@BDT")) {
						nofResult[0] += Integer.valueOf(items[0]);// total delay
						nofResult[1] += Integer.valueOf(items[1]);// diameter grpc delay

						nofResult[2] += Integer.valueOf(items[3]);// db query delay
						nofResult[3] += Integer.valueOf(items[4]);// db update delay

						nofResult[4] += (Integer.valueOf(items[0]) - Integer.valueOf(items[1])
								- Integer.valueOf(items[3]) - Integer.valueOf(items[4]));// other delay

						nofResult[5]++;// count
					}
				}
			}
			read.close();
		} catch (Exception e) {
			//e.printStackTrace();
			System.out.println(lineTxt);
		}
	}

	public boolean isTimeInRange(String lineTime, String startTime, String endTime) {
		try {
			long dateLineTime = sdf.parse(lineTime).getTime();

			startTime = startTime.replaceAll("_", " ");
			long startTimeD = sdf.parse(startTime).getTime();

			endTime = endTime.replaceAll("_", " ");
			long endTimeD = sdf.parse(endTime).getTime();

			return (dateLineTime >= startTimeD) && (dateLineTime <= endTimeD);

		} catch (Exception e) {
			//e.printStackTrace();
		}
		return false;
	}

	public void displayResult() {
		if (crtResult[4] > 0)
			System.out.println("Number:" + crtResult[4] + ", Total Delay:" + crtResult[0] / crtResult[4]
					+ ", Diameter Grpc Delay:" + crtResult[1] / crtResult[4] + ", DB Insert Delay:"
					+ crtResult[2] / crtResult[4] + ", Other Delay:" + crtResult[3] / crtResult[4]);

		if (nofResult[4] > 0)
		System.out.println("Number:" + nofResult[5] + ", Total Delay:" + nofResult[0] / nofResult[5]
				+ ", Diameter Grpc Delay:" + nofResult[1] / nofResult[5] + ", DB Query Delay:"
				+ nofResult[2] / nofResult[5] + ", DB Update Delay:" + nofResult[3] / nofResult[5] + ", Other Delay:"
				+ nofResult[4] / nofResult[5]);

	}

	public static void main(String[] args) {
		TransactionLogAnalyse ins = new TransactionLogAnalyse();
		ins.analyseFolder(args[0], args[1], args[2]);
		ins.displayResult();
	}
}
