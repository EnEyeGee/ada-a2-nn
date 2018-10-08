import java.util.ArrayList;

public class Value {

	String value;
	int count;
	ArrayList<String> targetlist;
	
	public Value () {
		this.value = "";
		this.count = 1;
		this.targetlist = new ArrayList<String>();
	}
	
	public void setValue(String v) {
		this.value = v;
	}
	
	public String getValue() {
		return this.value;
	}
	
	public int getCount() {
		return this.count;
	}
	
	public void incCount() {
		this.count += 1;
	}
	
	public ArrayList<String> gettargetlist() {
		return this.targetlist;
	}
	
}
