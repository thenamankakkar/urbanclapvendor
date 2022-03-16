package www.justme.co.in.vendor.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Order{

	@SerializedName("ResponseCode")
	private String responseCode;

	@SerializedName("order_data")
	private List<OrderDataItem> orderData;

	@SerializedName("ResponseMsg")
	private String responseMsg;

	@SerializedName("Result")
	private String result;

	@SerializedName("Is_approve")
	private String isapprove;

	public String getResponseCode(){
		return responseCode;
	}

	public List<OrderDataItem> getOrderData(){
		return orderData;
	}

	public String getResponseMsg(){
		return responseMsg;
	}

	public String getResult(){
		return result;
	}

	public String getIsapprove() {
		return isapprove;
	}

	public void setIsapprove(String isapprove) {
		this.isapprove = isapprove;
	}
}