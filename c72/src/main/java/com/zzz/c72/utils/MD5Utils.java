package com.zzz.c72.utils;

import java.security.MessageDigest;

public class MD5Utils {

	public static String MD5(String logPwd) {
		return MD5Utils.toMD5(logPwd, "32");
	}

	public static String toMD5(String plainText, String flag) {
		String result = "";
		try {
			// 生成实现指定摘要算法的 MessageDigest 对象。
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			// 使用指定的字节数组更新摘要。
			md5.update(plainText.getBytes());
			// 通过执行诸如填充之类的最终操作完成哈希计算。
			byte md5Byte[] = md5.digest();
			// 生成具体的md5密码到buf数组
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
			if (flag.equals("32")) {// 32位的加密
				result = buf.toString();
			} else if (flag.equals("16")) {// 16位的加密，其实就是32位加密后的截取
				result = buf.toString().substring(8, 24);
			} else {// 其他：32位的加密
				result = buf.toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = "";
		}
		return result;
	}
}
