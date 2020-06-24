package org.easystogu.runner;

import java.util.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang.ArrayUtils;

public class Test {
  public static String concatRdsHeader(String rawData) {
    int begin = rawData.indexOf("[");
    int end = rawData.indexOf("]");
    if (begin > -1 && end > -1 && end > begin) {
      return concatRdsHeader(rawData, begin, end);
    } else {
      return Base64.getEncoder().encodeToString(rawData.getBytes());
    }
  }

  public static String concatRdsHeader(String rawData, int begin, int end) {
    String wrappedData = rawData;
    try {
      String[] headerHexString = rawData.substring(begin + 1, end).split(" ");
      byte[] headerBytes = new byte[headerHexString.length];
      for (int i = 0; i < headerHexString.length; i++) {
        int a = Integer.decode(headerHexString[i]);
        headerBytes[i] = (byte) a;
      }
      String factualPayload = rawData.substring(end + 1);
      wrappedData = Base64.getEncoder()
          .encodeToString(ArrayUtils.addAll(headerBytes, factualPayload.getBytes()));
    } catch (NumberFormatException e) {
      e.printStackTrace();
    }
    return wrappedData;
  }

  public static void main(String[] args) {
    String rdsWithHeader = concatRdsHeader("[0x78 0x07 0x11]");
    System.out.println("rds:" + rdsWithHeader);
    byte[] bytes = Base64.getDecoder().decode(rdsWithHeader);
    System.out.println("bytes:" + Hex.encodeHexString(bytes));
  }

}
