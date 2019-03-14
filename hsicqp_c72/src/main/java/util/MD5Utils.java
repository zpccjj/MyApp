package util;

import java.security.MessageDigest;

public class MD5Utils {

	public static String MD5(String logPwd) {
		return MD5Utils.toMD5(logPwd, "32");
	}

	public static String toMD5(String plainText, String flag) {
		String result = "";
		try {
			// ����ʵ��ָ��ժҪ�㷨�� MessageDigest ����
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			// ʹ��ָ�����ֽ��������ժҪ��
			md5.update(plainText.getBytes());
			// ͨ��ִ���������֮������ղ�����ɹ�ϣ���㡣
			byte md5Byte[] = md5.digest();
			// ���ɾ����md5���뵽buf����
			int i;
			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < md5Byte.length; offset++) {
				i = md5Byte[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			if (flag.equals("32")) {// 32λ�ļ���
				result = buf.toString();
			} else if (flag.equals("16")) {// 16λ�ļ��ܣ���ʵ����32λ���ܺ�Ľ�ȡ
				result = buf.toString().substring(8, 24);
			} else {// ������32λ�ļ���
				result = buf.toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = "";
		}
		return result;
	}
}
