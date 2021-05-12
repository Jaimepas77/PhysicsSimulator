package view;

public class LawRowInfo {
	private String key, value, desc;
	
	public LawRowInfo(String key, String value, String desc) {
		this.key = key;
		this.value = value;
		this.desc = desc;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getKey() {
		return key;
	}

	public String getValue() {
		return value;
	}

	public String getDesc() {
		return desc;
	}

}
