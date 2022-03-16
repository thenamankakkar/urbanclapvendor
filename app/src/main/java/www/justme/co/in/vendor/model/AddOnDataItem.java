package www.justme.co.in.vendor.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class AddOnDataItem implements Parcelable {

	@SerializedName("price")
	private final String price;

	@SerializedName("title")
	private final String title;

	protected AddOnDataItem(Parcel in) {
		price = in.readString();
		title = in.readString();
	}

	public static final Creator<AddOnDataItem> CREATOR = new Creator<AddOnDataItem>() {
		@Override
		public AddOnDataItem createFromParcel(Parcel in) {
			return new AddOnDataItem(in);
		}

		@Override
		public AddOnDataItem[] newArray(int size) {
			return new AddOnDataItem[size];
		}
	};

	public String getPrice(){
		return price;
	}

	public String getTitle(){
		return title;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(price);
		dest.writeString(title);
	}
}