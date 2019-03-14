package bean;

public class InfoItem {
	private String Key;//打印时-1：--------，0：换行，1：单列左对齐， 2：双列Name左对齐 Value右对齐，3：三列 Name Value Value2
	private String Name;
	private String Value;
	private String Value2;


	public String getKey() {
		return Key;
	}
	public void setKey(String key) {
		Key = key;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public String getValue() {
		return Value;
	}
	public void setValue(String value) {
		Value = value;
	}
	public String getValue2() {
		return Value2;
	}
	public void setValue2(String value2) {
		Value2 = value2;
	}
	@Override
	public String toString() {
		return Name;
	}
}
