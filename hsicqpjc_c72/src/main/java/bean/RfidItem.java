package bean;

public class RfidItem {
	private String QPDJCODE;
	private String GNO;
	private String NEXTCHECKDATE;
	private String MSG;//0已检验 1超过使用年限
	private int color;//0 1red 
	public String getQPDJCODE() {
		return QPDJCODE;
	}
	public void setQPDJCODE(String qPDJCODE) {
		QPDJCODE = qPDJCODE;
	}
	public String getGNO() {
		return GNO;
	}
	public void setGNO(String gNO) {
		GNO = gNO;
	}
	public String getNEXTCHECKDATE() {
		return NEXTCHECKDATE;
	}
	public void setNEXTCHECKDATE(String nEXTCHECKDATE) {
		NEXTCHECKDATE = nEXTCHECKDATE;
	}
	public String getMSG() {
		return MSG;
	}
	public void setMSG(String mSG) {
		MSG = mSG;
	}
	public int getColor() {
		return color;
	}
	public void setColor(int color) {
		this.color = color;
	}
}
