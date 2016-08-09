package org.easystogu.runner;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Test {

	public static void main(String[] args) throws JsonProcessingException {
		DeliverSmsResponse dr = new DeliverSmsResponse();
		List<DeliveryInfo> deliveryInfoList = new ArrayList<DeliveryInfo>();
		DeliveryInfo deliveryInfo = new DeliveryInfo();
		deliveryInfo.setAddress("tel:10086");
		deliveryInfo.setDeliveryStatus(DeliveryStatus.DELIVERED_TO_NETWORK);
		deliveryInfoList.add(deliveryInfo);

		dr.setSmsId("smsId");
		dr.setDeliveryInfo(deliveryInfoList);

		ObjectMapper mapper = new ObjectMapper();
		String str = mapper.writeValueAsString(dr);

		System.out.println("dr json=\n" + str);
	}

}

@JsonRootName("deliverSmsResponse")
class DeliverSmsResponse {
    private String smsId;
    private List<DeliveryInfo> deliveryInfo;

    public List<DeliveryInfo> getDeliveryInfo() {
        return deliveryInfo;
    }

    public void setDeliveryInfo(List<DeliveryInfo> deliveryInfo) {
        this.deliveryInfo = deliveryInfo;
    }

    public DeliverSmsResponse() {

    }

    public DeliverSmsResponse(String smsId, List<DeliveryInfo> deliveryInfo) {
        this.smsId = smsId;
        this.deliveryInfo = deliveryInfo;
    }

    public String getSmsId() {
        return smsId;
    }

    public void setSmsId(String smsId) {
        this.smsId = smsId;
    }
}

@JsonRootName("deliveryInfo")
class DeliveryInfo{
    private String address;

    private DeliveryStatus deliveryStatus;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public DeliveryStatus getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(DeliveryStatus deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }
}
enum DeliveryStatus {

	DELIVERED(0), NOT_DELIVERED(1), DELIVERY_UNCERTAIN(3), EXPIRED(2), DELIVERED_TO_NETWORK(
			4), MESSAGE_WAITING(5), PARTIAL_SUCCESS_GROUP(6), MO_MESSAGE_WAITING(
			7), MO_DELIVERED(8);

	private int value;

	DeliveryStatus(int value) {
		this.value = value;
	}

	/**
	 * @return
	 */
	public int getValue() {
		return value;
	}
	
	/**
	 * getDeliveryStatus by value
	 * 
	 * @param value
	 * @return
	 */
	public static DeliveryStatus getDeliveryStatusByValue(int value) {
		for (DeliveryStatus deliveryStatus : DeliveryStatus.values()) {
			if (deliveryStatus.getValue() == value) {
				return deliveryStatus;
			}
		}
		return null;
	}
}
