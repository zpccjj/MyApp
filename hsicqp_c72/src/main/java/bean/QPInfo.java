package bean;

public class QPInfo {
	private String Bot_type;//气瓶类别代码 2位
	private String CQDW;//产权单位代码 4位
	private String LabelNo;//电子标签号7位
	private String MediumCode;//介质代码
	private String QPType ;//商品CODE

	private String UserID ;//客户号
	private String Saleid ;//销售单号
	private int OpType ;//操作；类型 0表示回收气瓶 1表示发送气瓶 9表示其他气瓶

	private int IsByHand;//是否手输 0表示自动扫描 1表示手输电子标签号
	private String Msg;

	public String getBot_type() {
		return Bot_type;
	}

	public void setBot_type(String bot_type) {
		Bot_type = bot_type;
	}

	public String getCQDW() {
		return CQDW;
	}

	public void setCQDW(String cQDW) {
		CQDW = cQDW;
	}

	public String getLabelNo() {
		return LabelNo;
	}

	public void setLabelNo(String labelNo) {
		LabelNo = labelNo;
	}

	public String getQPType() {
		return QPType;
	}

	public void setQPType(String qPType) {
		QPType = qPType;
	}

	public String getUserID() {
		return UserID;
	}

	public void setUserID(String userID) {
		UserID = userID;
	}

	public String getSaleid() {
		return Saleid;
	}

	public void setSaleid(String saleid) {
		Saleid = saleid;
	}

	public int getOpType() {
		return OpType;
	}

	public void setOpType(int opType) {
		OpType = opType;
	}

	public int getIsByHand() {
		return IsByHand;
	}

	public void setIsByHand(int isByHand) {
		IsByHand = isByHand;
	}

	public String getMsg() {
		return Msg;
	}

	public void setMsg(String msg) {
		Msg = msg;
	}

	public String getMediumCode() {
		return MediumCode;
	}

	public void setMediumCode(String mediumCode) {
		MediumCode = mediumCode;
	}


}
