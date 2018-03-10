package diameter;

import java.util.ArrayList;
import java.util.List;

class Avp {
	int code = -99999;
	String name = "Not_Defined";
	String type = "Not_Defined";
	boolean mandatory = false;
	boolean protection = false;
	int vendorId = 0;
	String valueLength;
	String occurs;
	List<Avp> subAvp = new ArrayList<Avp>();

	public Avp(String name) {
		this.name = name;
	}

	public Avp(int code, String name, String type) {
		this.code = code;
		this.name = name;
		this.type = type;
	}

	public void copy(Avp src) {
		this.code = src.code;
		this.name = src.name;
		this.type = src.type;
		this.mandatory = src.mandatory;
		this.protection = src.protection;
		this.vendorId = src.vendorId;
		this.valueLength = src.valueLength;
		// this.occurs = src.occurs;//Don't copy it!!!
		this.subAvp = src.subAvp;
	}

	public String toString() {

		String rtn = "name=" + name + ",code=" + code + ",type=" + type + ",mandatory=" + mandatory + ",vendorId="
				+ vendorId + ",valueLength=" + valueLength + ",occurs=" + occurs;
		if ("Grouped".equalsIgnoreCase(type)) {
			rtn += ",subAvp=" + subAvp;
		}
		return rtn;
	}

	public String toAvpCommandXML() {
		StringBuffer sb = new StringBuffer();
		if ("Grouped".equalsIgnoreCase(type)) {
			sb.append("<avp name=\"" + name + "\" context=\"" + name + "\">\n");
			for (Avp aSubAvp : subAvp) {
				sb.append(aSubAvp.toAvpCommandXML());
			}
			sb.append("</avp>\n");
		} else {
			String requiredStr = "1..1".equals(occurs) ? " required=\"true\"" : "";
			sb.append("<avp name=\"" + name + "\"" + requiredStr + " />\n");
		}

		return sb.toString();
	}

	public String toAvpDictionaryXML() {
		StringBuffer sb = new StringBuffer();
		String mandatoryStr = this.mandatory ? "mandatoryBit=\"true\"" : "";
		String vendorStr = (10415 == this.vendorId) ? "vendorName=\"TGPP\"" : "";
		sb.append("<avpEntry name=\"" + name + "\" code=\"" + code + "\" type=\"" + type + "\" " + mandatoryStr + " "
				+ vendorStr + "/>\n");
		if ("Grouped".equalsIgnoreCase(type)) {
			for (Avp aSubAvp : subAvp) {
				sb.append(aSubAvp.toAvpDictionaryXML());
			}
		}
		return sb.toString();
	}
}
