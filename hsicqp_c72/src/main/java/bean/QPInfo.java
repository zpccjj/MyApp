package bean;

public class QPInfo {
	private String Bot_type;//气瓶类别代码 2位
	private String CQDW;//产权单位代码 4位
	private String LabelNo;//电子标签号8位
	private String MediumCode;//介质代码
	private String QPType ;//商品CODE

	private String UserID ;//客户号
	private String Saleid ;//销售单号
	private int OpType ;//操作；类型 0表示回收气瓶 1表示发送气瓶 9表示其他气瓶

	private int IsByHand;//是否手输 0表示自动扫描 1表示手输电子标签号
	private String Msg;

	private int Num=1;//集格瓶每组数量，默认1，散瓶1 集格>1
	private int IsJG=0;//气瓶类型 0散瓶 1集格

	private int color= 0; //0蓝色 充装商品名称不确定，1黑色 充装商品名称确定 2红色充装商品名称确定 单不在订单中

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

	public int getNum() {
		return Num;
	}

	public void setNum(int num) {
		Num = num;
	}

	public int getIsJG() {
		return IsJG;
	}

	public void setIsJG(int isJG) {
		IsJG = isJG;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}


}
