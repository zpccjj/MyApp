package bean;

public class ResponseData {

	private int RespCode;//返回code iSucess = 0;  iError = 1;
	private String RespMsg;//
	public int getRespCode() {
		return RespCode;
	}
	public void setRespCode(int respCode) {
		RespCode = respCode;
	}
	public String getRespMsg() {
		return RespMsg;
	}
	public void setRespMsg(String respMsg) {
		RespMsg = respMsg;
	}





}
