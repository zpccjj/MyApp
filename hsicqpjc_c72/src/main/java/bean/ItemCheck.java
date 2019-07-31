package bean;

public class ItemCheck {
	private String Key;
	private String Text;
	private int ViewType=0;//0-CheckBox 1-EditText
	private boolean isCheck = false;//ViewType==0 สนำร
	private String Value;//ViewType==1 สนำร

	public ItemCheck(){}
	public ItemCheck(String key, String txt, int type, boolean ischeck, String value){
		this.Key = key;
		this.Text = txt;
		this.ViewType = type;
		this.isCheck = ischeck;
		this.Value = value;
	}

	public String getKey() {
		return Key;
	}
	public void setKey(String key) {
		Key = key;
	}
	public String getText() {
		return Text;
	}
	public void setText(String text) {
		Text = text;
	}
	public int getViewType() {
		return ViewType;
	}
	public void setViewType(int viewType) {
		ViewType = viewType;
	}
	public boolean isCheck() {
		return isCheck;
	}
	public void setCheck(boolean isCheck) {
		this.isCheck = isCheck;
	}
	public String getValue() {
		return Value;
	}
	public void setValue(String value) {
		Value = value;
	}
}
