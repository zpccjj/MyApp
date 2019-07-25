package com.hsic.version;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;

import java.io.InputStream;

public class XmlParser {
	/**
	 * 
	 * @param in
	 * @return
	 * @throws Exception
	 */
	public static String[] getVersionInfo(InputStream in) throws Exception {
		String[] versionInfos = null;
		if (in != null) {
			XmlPullParser xpp = Xml.newPullParser();
			xpp.setInput(in, "gb2312");
			int type = xpp.getEventType();

			while (type != XmlPullParser.END_DOCUMENT) {

				switch (type) {

				case XmlPullParser.START_TAG:
					if ("update".equals(xpp.getName())) {
						versionInfos = new String[5];
					} else if ("version".equals(xpp.getName())) {
						String version = xpp.nextText();
						versionInfos[0] = version;
					} else if ("file_real_path".equals(xpp.getName())) {
						String file_real_path = xpp.nextText();
						versionInfos[1] = file_real_path;

					} else if ("file_MD5".equals(xpp.getName())) {
						String file_MD5 = xpp.nextText();
						versionInfos[2] = file_MD5;
					} 
					if(xpp.getName()!=null){
						if ("min_version".equals(xpp.getName())) {
							String min_version = xpp.nextText();
							versionInfos[3] = min_version;						
						}
					}else{
						versionInfos[3] = "";		
					}
					if(xpp.getName()!=null){
						if ("version_explain".equals(xpp.getName())) {
							String version_explain = xpp.nextText();
							versionInfos[4] = version_explain;
						} 
					}else{
						versionInfos[4] = "";
					}
									
					break;

				case XmlPullParser.END_TAG:
					break;
				}

				type = xpp.next();
			}

		}

		return versionInfos;
	}
}
