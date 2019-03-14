package data;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class ConfigData {
	public static int OVERDUE = 2 ;//超期
	public static int FORTHCOMING = 1 ; //即将超期
	public static int NORMAL = 0 ;//正常
	
	public static int IsOverdue(String nextCheck){
		
		SimpleDateFormat sdf=new SimpleDateFormat("yyMM");
		
		try {
            Date currdate = sdf.parse(nextCheck);
            Calendar   calendar= Calendar.getInstance();
            calendar.setTime(currdate);
            calendar.set(Calendar.MONTH,calendar.get(Calendar.MONTH)-1);
 
            int next = Integer.valueOf(nextCheck);
    		int before = Integer.valueOf(sdf.format(calendar.getTime()));
    		int now = Integer.valueOf(sdf.format(new Date()));
    		
    		if(now>=next) return ConfigData.OVERDUE;
    		else if(now<next && now>=before) return ConfigData.FORTHCOMING;
    		
        } catch (Exception e) {
            e.printStackTrace();
        }
		
		return ConfigData.NORMAL;
	}
	
	
	public static String[][] CZDW= {
		{"0301","江南造船(集团)有限责任公司"},
		{"0401","液化空气上海有限公司"},
		{"0701","上海申佳工业气体有限公司"},
		{"0702","上海化工院青磊特种气体有限公司"},
		{"0801","液化空气（上海）气体有限公司"},
		{"0901","上海立新气体有限公司"},
		{"0902","上海华星气体有限责任公司"},
		{"1201","上海氯碱化工股份有限公司"},
		{"1203","上海焦化有限公司"},
		{"1204","上海林德二氧化碳有限公司"},
		{"1207","上海重型机器厂"},
		{"1208","上海上农气体有限公司"},
		{"1210","上海亚太酿酒有限公司"},
		{"1211","上海液化石油气经营有限公司"},
		{"1212","上海科举化工有限公司"},
		{"1213","上海瑞利化工气体有限公司"},
		{"1215","上海氯碱创业有限公司"},
		{"1217","上海宝闵工业气体有限公司"},
		{"1301","上海五钢气体有限责任公司"},
		{"1302","上海宝氢气体工业有限公司"},
		{"1303","上海宝氢气体工业有限公司"},
		{"1304","上海宝钢普莱克斯实用气体有限公司"},
		{"1305","上海弘益冶金材料有限公司"},
		{"1306","上海凇化气体化工有限公司"},
		{"1307","上海盖斯工业气体有限公司"},
		{"1309","上海比欧西气体工业有限公司"},
		{"1311","上海振信新帅气体有限公司"},
		{"1312","上海旗辉科技有限公司"},
		{"1315","上海豪杰特种气体有限公司"},
		{"1316","上海济阳科技发展有限公司"},
		{"1317","上海闸殷气体厂"},
		{"1318","广东杜然贸易有限公司上海分公司"},
		{"1319","BP(上海)液化石油气有限公司"},
		{"1320","上海百斯特能源发展有限公司(杨行储备站)"},
		{"1323","上海杰盟化工有限公司"},
		{"1324","上海酷奥制冷有限公司"},
		{"1325","上海天翼气体有限公司"},
		{"1328","上海万事红燃气技术发展有限公司"},
		{"1329","喜威（上海）液化石油气有限公司"},
		{"1401","上海娄氧气体罐装有限公司"},
		{"1402","上海成功气体工业有限公司"},
		{"1403","上海申威气体罐装有限公司"},
		{"1404","上海庆达工业气体充装有限公司"},
		{"1405","上海富翔气体有限公司"},
		{"1407","上海富祥液态气体有限公司"},
		{"1410","上海都茂爱净化气有限公司"},
		{"1411","上海尤嘉利液氦有限公司"},
		{"1412","上海澳宏化学品有限公司"},
		{"1414","上海嘉定燃气有限公司"},
		{"1415","上海伟良浦江特种气体有限公司"},
		{"1416","上海宝来特气体有限公司"},
		{"1419","上海伟良浦江气体有限公司"},
		{"1501","上海孙桥隆申制气有限公司"},
		{"1502","上海新沪化学气体厂"},
		{"1503","上海三钢-梅塞尔气体产品有限公司"},
		{"1504","上海金明工业气体有限公司"},
		{"1505","上海浦东高桥制氧有限公司"},
		{"1507","上海申明制氧有限公司"},
		{"1508","上海振信盖斯实业有限公司"},
		{"1509","上海西西艾尔气雾推进剂制造与罐装有限公司"},
		{"1510","上海普莱克斯仪电实用气体有限公司"},
		{"1511","上海东方能源股份有限公司"},
		{"1512","上海百斯特能源发展有限公司"},
		{"1513","上海浦东海光燃气有限公司"},
		{"1515","上海神开气体技术有限公司"},
		{"1602","中国石化上海石化股份公用事业公司"},
		{"1603","上海石化鑫源化工实业有限公司"},
		{"1604","上海金地石化有限公司"},
		{"1606","上海金山区山阳打火机厂"},
		{"1607","上海金山液化气公司"},
		{"1608","中天科盛(上海)企业发展股份有限公司"},
		{"1609","上海海洲特种气体有限公司"},
		{"1610","上海万时红管道燃气经营有限公司"},
		{"1611","上海和立工业气体有限公司"},
		{"1612","上海盛瀛化工有限公司"},
		{"1613","上海康文气体有限公司"},
		{"1614","顺天（上海）能源技术发展有限公司"},
		{"1701","上海申港乙炔气厂"},
		{"1702","上海申中工业气体有限公司"},
		{"1703","上海松闵气体技术有限公司"},
		{"1704","上海松引气体有限公司"},
		{"1705","上海松江精心气体站"},
		{"1706","上海松江燃气有限公司"},
		{"1707","上海中信燃气有限公司"},
		{"1708","丰罗绝缘材料（上海）有限公司"},
		{"1799","上海申港乙炔气厂(申佳乙炔气瓶产权转移)"},
		{"1801","上海桑达工业气体公司"},
		{"1802","上海九安工业气体有限公司"},
		{"1803","上海光林气体公司"},
		{"1804","上海吉福液态气体分装厂"},
		{"1805","上海爱沃特医疗气体有限公司"},
		{"1807","霍尼韦尔特殊材料(中国)有限公司"},
		{"1808","上海青浦区煤气管理所"},
		{"1809","上海磊诺安防技术股份有限公司"},
		{"1810","上海基量标准气体有限公司"},
		{"1901","上海滨海溶解乙炔气厂"},
		{"1902","上海沪康工业气体有限公司"},
		{"1903","上海南汇化工轻工有限公司"},
		{"1906","上海市南汇液化气公司"},
		{"1907","上海百斯思特能源发展有限公司  南汇东海储备站"},
		{"1909","上海东海液化气有限公司"},
		{"1910","上海恒申燃气发展有限公司"},
		{"1911","上海利旦工业气体有限公司"},
		{"1912","上海沧海工业气体有限公司"},
		{"1913","上海鸿盛工业气体有限公司东海农场灌装厂"},
		{"2001","上海欣头桥隆申制气有限公司"},
		{"2002","上海南化工业气体有限公司"},
		{"2003","上海金明制氧厂"},
		{"2004","上海京丰化工有限公司"},
		{"2005","上海奉贤燃气股份有限公司"},
		{"2007","上海南耀气体有限公司"},
		{"2008","斯堪的亚电子(上海)有限公司"},
		{"2009","上海化学工业区浦江特种气体有限公司"},
		{"2010","上海奉贤交通液化气有限公司"},
		{"2011","上海昭和电子化学材料有限公司"},
		{"3001","上海市星盛精细化工厂"},
		{"3003","上海市崇明氧气厂"},
		{"3004","上海瀛西制氧厂"},
		{"3005","上海市宝山区长兴制氧厂"},
		{"3006","上海宝冶奇成气体有限公司"},
		{"3007","上海燃气崇明有限公司"},
		{"3008","上海长泰气体有限公司"},
		{"3009","上海振华精密铸造有限公司"},
		{"3010","上海威奥气体有限公司"},
		{"3011","上海英尔实业有限公司"},
		{"3012","上海市申江燃气有限公司"},
		{"3013","上海瀛海燃气有限责任公司"}
	};
	
	public static String[][] MediaInfo = {
		{"00010","混合气"},
		{"10010","溶解乙炔"},
		{"10020","压缩空气"},
		{"10050","氨"},
		{"10060","氩(纯氩)"},
		{"10061","氩混合（混合氩）"},
		{"10062","高纯氩"},
		{"10063","灯泡氩"},
		{"10110","丁烷"},
		{"10130","二氧化碳"},
		{"10160","压缩一氧化碳"},
		{"10180","R22（二氟一氯甲烷）"},
		{"10300","制冷气体 二氟乙烷（ R152A）"},
		{"10400","环氧乙烷"},
		{"10460","氦"},
		{"10490","氢"},
		{"10650","液氩"},
		{"10660","氮"},
		{"10661","纯氮"},
		{"10662","高纯氮"},
		{"10720","氧（工业氧）"},
		{"10721","医用氧"},
		{"10722","高纯氧"},
		{"10730","液氧"},
		{"10750","液化石油气"},
		{"10770","丙烯"},
		{"10780","液化氟氯烷（未列明制冷气体）"},
		{"10781","R507"},
		{"10783","LBA"},
		{"10800","六氟化硫SF6"},
		{"19510","液氩"},
		{"19620","乙烯"},
		{"19690","异丁烷"},
		{"19710","压缩天然气"},
		{"19720","液化天然气"},
		{"19760","八氟环丁烷(RC318)"},
		{"19770","液氮"},
		{"19780","丙烷"},
		{"20350","1,1,1-三氟乙烷（制冷气体R143a）"},
		{"24510","三氟化氮NF3"},
		{"31590","1,1,1,2-四氟乙烷(制冷气体R134a）"},
		{"32960","七氟丙烷（制冷气体R227）"},
		{"32961","IG541（烟烙尽）"},
		{"32962","消防气（混合气杂项）"},
		{"32963","五氟丙烷（HFC-245fa）"},
		{"33370","制冷气体R404A"},
		{"33400","制冷气体R407C"}
	};
	
	public static String[][] GPLX = {
		{"01","钢质无缝气瓶"},
		{"02","钢质焊接气瓶"},
		{"03","液化石油气瓶"},
		{"04","溶解乙炔气瓶"},
		{"05","车用气瓶"},
		{"06","低温绝热气瓶"},
		{"07","缠绕气瓶 "}
	};
	
	public static String getMediaName(String Code){
		
		for (int i = 0; i < MediaInfo.length; i++) {
			if(Code.equals(MediaInfo[i][0])){
				return MediaInfo[i][1];
			}
		}
		return "";
	}
}
