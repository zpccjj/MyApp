package util;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;

import android.util.Log;
import bean.Rfid;

public class RfidUtils {
	public static boolean isBound(String EPC){
		if(EPC.substring(16, 24).equals("FFFFFFFF"))
			return false;
		else return true;
	}

	public static String getDataFromEPC(String epc){
		if(epc==null || epc.length()!=24) return null;
		try {
			String epcX34 = xorHex(epc, "34");

			byte[] Data = hexStringToBytes(epcX34);
			String bitString = "";
			for (int i = 0; i < Data.length; i++) {
				bitString+=byteToBit(Data[i]);
			}

			Rfid rfid = new Rfid();
			rfid.setEPC(epc);
			rfid.setVersion(bitString.substring(0, 4));//0101 标签类别 + 规范版本
			rfid.setCQDW(String.format("%04d", binaryToDecimal(bitString.substring(4, 18))));//4位单位代码
			rfid.setLabelNo(String.format("%08d", binaryToDecimal(bitString.substring(18, 45))));//8位追溯码
			rfid.setCZJZCode(String.format("%05d", binaryToDecimal(bitString.substring(48, 64))));//5位充装介质
			rfid.setNextCheckDate(String.format("%04d", binaryToDecimal(bitString.substring(64, 78))));//4位下次检验日期
			rfid.setState(bitString.substring(78, 80)); //钢瓶状态 00合格 01 报废 10 停用
			rfid.setType(bitString.substring(80, 82));//气瓶种类 00散瓶01集格10集格内瓶
			if(rfid.getType().equals("01")) rfid.setIsJG(1);
			else rfid.setIsJG(0);
			rfid.setQPDJCode(rfid.getCQDW() + rfid.getLabelNo());
			Log.e("="+rfid.getCQDW()+rfid.getLabelNo(), "rfid.getType()="+rfid.getType());
			return util.json.JSONUtils.toJsonWithGson(rfid);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
	}

	public static void getDataFromUser(Rfid rfid, String user){
		if(user==null || user.length()!=52) return ;
		String userX34 = xorHex(user, "34");
		Log.e("userX34", userX34);
		byte[] Data = hexStringToBytes(userX34);
		String bitString = "";
		for (int i = 0; i < Data.length; i++) {
			bitString+=byteToBit(Data[i]);
		}

		rfid.setCheckDate(String.format("%04d", binaryToDecimal(bitString.substring(24, 44))));
		String qp = "";
		try {
			qp = new String(RfidUtils.hexStringToBytes(userX34.substring(14, 38)), "UTF-8").replace(" ", "");
			Log.e("气瓶号", qp);
			rfid.setQPNO(qp);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		rfid.setQPNO(qp);
	}



	public static String xorHex(String EPC, String xor) {
		String ret = "";
		for (int i = 0; i < EPC.length()/2; i++) {
			String data = EPC.substring(i*2, i*2+2);
			char[] chars = new char[data.length()];
			for (int j = 0; j < chars.length; j++) {
				chars[j] = toHex(fromHex(data.charAt(j)) ^ fromHex(xor.charAt(j)));
			}
			ret += new String(chars);
		}

		return ret;
	}

	public static int fromHex(char c) {
		if (c >= '0' && c <= '9') {
			return c - '0';
		}
		if (c >= 'A' && c <= 'F') {
			return c - 'A' + 10;
		}
		if (c >= 'a' && c <= 'f') {
			return c - 'a' + 10;
		}
		throw new IllegalArgumentException();
	}

	public static char toHex(int nybble) {
		if (nybble < 0 || nybble > 15) {
			throw new IllegalArgumentException();
		}
		return "0123456789ABCDEF".charAt(nybble);
	}

	public static String byteToBit(byte b) {
		return ""
				+ (byte) ((b >> 7) & 0x1) + (byte) ((b >> 6) & 0x1)
				+ (byte) ((b >> 5) & 0x1) + (byte) ((b >> 4) & 0x1)
				+ (byte) ((b >> 3) & 0x1) + (byte) ((b >> 2) & 0x1)
				+ (byte) ((b >> 1) & 0x1) + (byte) ((b >> 0) & 0x1);
	}

	/*
	 * Convert byte[] to hex
	 * string.这里我们可以将byte转换成int，然后利用Integer.toHexString(int)来转换成16进制字符串。
	 *
	 * @param src byte[] data
	 *
	 * @return hex string
	 */
	public static String bytesToHexString(byte[] src) {
		StringBuilder stringBuilder = new StringBuilder("");
		if (src == null || src.length <= 0) {
			return null;
		}
		for (int i = 0; i < src.length; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		return stringBuilder.toString();
	}

	/**
	 * Convert hex string to byte[]
	 *
	 * @param hexString
	 *            the hex string
	 * @return byte[]
	 */
	public static byte[] hexStringToBytes(String hexString) {
		if (hexString == null || hexString.equals("")) {
			return null;
		}
		hexString = hexString.toUpperCase();
		int length = hexString.length() / 2;
		char[] hexChars = hexString.toCharArray();
		byte[] d = new byte[length];
		for (int i = 0; i < length; i++) {
			int pos = i * 2;
			d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
		}
		return d;
	}

	/**
	 * Convert char to byte
	 *
	 * @param c
	 *            char
	 * @return byte
	 */
	private static byte charToByte(char c) {
		return (byte) "0123456789ABCDEF".indexOf(c);
	}


	public static int binaryToDecimal(String binarySource) {
		BigInteger bi = new BigInteger(binarySource, 2);	//转换为BigInteger类型
		return Integer.parseInt(bi.toString());		//转换成十进制
	}

	public static int BinaryToDecimal(int binaryNumber){
		int decimal = 0;
		int p = 0;
		while(true){
			if(binaryNumber == 0){
				break;
			} else {
				int temp = binaryNumber%10;
				decimal += temp*Math.pow(2, p);
				binaryNumber = binaryNumber/10;
				p++;
			}
		}
		return decimal;
	}

	public static String LeftAddString(String data, int len, String AddString){
		String ret = "";
		if(data.length()>len) return "";
		else if(data.length()==len) return data;
		else{
			for (int i = 0; i < (len - data.length()); i++) {
				ret += AddString;
			}
		}
		return ret + data;
	}


}
