import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Model {

	List<String[]> testset = new ArrayList<String[]>();
	List<String[]> trainset = new ArrayList<String[]>();
	List<String> valueset = new ArrayList<String>();
	List<Integer> countset = new ArrayList<Integer>();
	List<String> aList = new ArrayList<String>();
	String mctv = ""; //most common target value
	List<String> header = new ArrayList<String>();
	List<String> hset = new ArrayList<String>(); //homogenous set
	
	/*
	 * Reading csv for test and training set, and placing them into 2d arrays, as well as populating the header list.
	 */
	public void readFile () throws FileNotFoundException {
		
		try {
			String path = new File("").getAbsolutePath();
			String file = getFile();
			String npath = path + "\\src\\" + file;
			npath.replace("\\", "\\\\");
			String lines;
			Scanner scanner = new Scanner(new File(npath));
			
			String theader = scanner.nextLine();
			makeHeader(theader);
			while(scanner.hasNextLine()){
				lines = scanner.nextLine();
	        	String[] cells = lines.split(",");
	        	trainset.add(cells);
			}
	        //find size of trainset. * 0.3 = split.size
	        int a = (int) Math.round(trainset.size()*0.7);
	        for (int b = trainset.size() - 1; b > a; b--) {
	        	String[] c = trainset.get(b);
	        	testset.add(c);
	        	trainset.remove(b);
	        }
		} catch (FileNotFoundException e){
			System.err.println("FileNotFoundException: " + e.getMessage());
		}
		
	}
	
	/*
	 * asking user input for csv filename
	 */
	public String getFile() {
		System.out.println("Type the name of the csv file you'd like to train and test");
		Scanner scan = new Scanner(System.in);
		String r = scan.nextLine();
		scan.close();
		return r;
	}
	
//	/*
//	 * Getting the target class from user
//	 */
//	public String readTarget() {
//		System.out.println("What is the target class?");
//		Scanner scan = new Scanner(System.in);
//		String r = scan.nextLine();
//		scan.close();
//		return r;
//	}
	
	/*
	 * Populating the attributes list (aList)
	 */
	public void makeListofAttributes(List<String> h){
		for (int i = 1; i< h.size(); i++) {
			aList.add(h.get(i));
		}
	}
	
	/*
	 * Helper method to populate the header
	 */
	public void makeHeader(String h){
		String[] a = h.split(",");
		for (int i = 0; i<a.length; i++) {
			header.add(a[i]);
		}
	}
	
	/*
	 * Creating the id3 model using a decision tree. Parameters: attribute, value, attributes list.
	 */
	public DecisionTree makeModel(List<String[]> dataset, List<String> al) {
		
		String bestA = "";
		double pe = 100;
		double e = 0; 
		int setL = trainset.size();;
		int iot = 0;
		String newValue = "";
		int sum = dataset.size();
		double weight = 0;
		List<String> aCopy = new ArrayList<String>(al);
		List<String> bestset = new ArrayList<String>();
		List<String[]> newset = new ArrayList<String[]>();
		valueset.clear();
		countset.clear();
		
		//Create root node for the tree
		DecisionTree dt = new DecisionTree();
		DecisionTree.TreeNode tn = dt.new TreeNode();
		dt.setRoot(tn);
		
		//If all examples are the same value
		if (homogenous(dataset)) {
			String[] temp = dataset.get(0);
			tn.setLabel(temp[0]);
			return dt;
		}
		
		//If the attributes is empty
		if (aCopy.isEmpty() || dataset.isEmpty()) {
			tn.setLabel(mctv);
			return dt;
		}
		

		e = 0;
		pe = 100;
		int ioa = 0;
		//Long process of finding entropy and best attribute.
		for (String attribute: aCopy) {
			valueset.clear();
			countset.clear();
			ioa = indexofString(attribute, header); 
			//finding out how many values in the attribute
			for (int i = 0; i < dataset.size(); i++) {
				String[] tempA = dataset.get(i);
				newValue = tempA[ioa];
				if (isNew(newValue, valueset)) {
					valueset.add(newValue);
					countset.add(1);
					
				} else {
					int iov = indexofString(newValue, valueset);
					int c = countset.get(iov);
					c++;
					countset.set(iov, c);
				}
			}
			//for each value of the attribute in valueset, creating a list to store the different values of the target and a list to store the count
			for (int k = 0; k < valueset.size(); k++) {
				int c = countset.get(k);
				weight = (double)c/(double)sum;
				String voa = valueset.get(k);
				List<String> lta = new ArrayList<String>(); //creating list of the target attribute
				List<Integer> cta = new ArrayList<Integer>();
				for (int l = 0; l < dataset.size(); l++) {
					String[] ta = dataset.get(l);
					newValue = ta[iot];
					if (ta[ioa].equals(voa)) {
						if (isNew(newValue, lta)) {
							lta.add(newValue);
							cta.add(1);
						} else {
							int iov = indexofString(newValue, lta);
							int ct = cta.get(iov);
							ct++;
							cta.set(iov, ct);
						}
					}
				}
				
				for (int m = 0; m < lta.size(); m++) {
					double p = (double)cta.get(m)/(double)c;
					e += weight*(-1)*p*Math.log(p)/Math.log(2);	//sum e of whole attribute
				}
			}
			if (e <= pe) {
				pe = e;
				bestA = attribute;
			}
			e = 0;
		}//End find bestA

		
		//Best Attribute found, delete attribute from attributes list
		int z = indexofString(bestA, aList);
		aList.remove(z);
		tn.setAttribute(bestA);
		
		//creating valueset for the best attribute
		int iog = indexofString(bestA, header); 
		//finding out how many values in the attribute
		for (int i = 0; i < dataset.size(); i++) {
			String[] tempA = dataset.get(i);
			newValue = tempA[iog];
			if (isNew(newValue, bestset)) {
				bestset.add(newValue);
			}
		}
		for (String str: bestset) {
			//create new set
			//loop through dataset and populate the new set
			for (int i = 0; i<dataset.size(); i++) {
				String[] tempA = dataset.get(i);
				if (str.equals(tempA[iog])) {
					newset.add(tempA);
				}
			}
			
			if (newset.isEmpty()) {
				DecisionTree.TreeNode nn = dt.new TreeNode();
				nn.setLabel(mctv);
				tn.addChild(nn);
				nn.setParent(tn);
				return dt;
			}
			DecisionTree nt = makeModel(newset, aList);
			tn.addChild(nt.getRoot());
			nt.getRoot().setValue(str);
			nt.getRoot().setParent(tn);
		}
		
		return dt;
	}
	
	/*
	 * To check if the value is new or already accounted for in the valueset
	 */
	private boolean isNew(String newValue, List<String> vs) {
		if (vs.isEmpty()) {
			return true;
		}
		for(String str: vs) {
			if (str.equals(newValue))
				return false;
		}
		return true;
	}
	

	/*
	 * Method to find the index of the attribute or value 
	 */
	public int indexofString (String r, List<String> l) {
		for (int i = 0; i < l.size(); i++) {
			if (l.get(i).equals(r)) {
				return i;
			} 
		}
		return -1;
	}
	
	/*
	 * To populate the most common target value
	 */
	public void mctv() {
		
		valueset.clear();
		countset.clear();
		String newV = "";
		int bc = 0;
		
		for (int i = 0; i < trainset.size(); i++) {
			String[] tempA = trainset.get(i);
			newV = tempA[0];
			if (isNew(newV, valueset)) {
				valueset.add(newV);
				countset.add(1);
			} else {
				int iov = indexofString(newV, valueset);
				int c = countset.get(iov);
				c++;
				countset.set(iov, c);
			}
		}
		
		for (int i = 0; i < countset.size(); i++) {
			if (countset.get(i) >= bc) {
				bc = countset.get(i);
				mctv = valueset.get(i);
			}
		}
	}
	
	/*
	 * Checking if the dataset is homogenous, all one target value.
	 */
	public boolean homogenous(List<String[]> dataset) {
		
		hset.clear();
		
		String newV = "";
		
		for (int i = 0; i < dataset.size(); i++) {
			String[] tempA = dataset.get(i);
			newV = tempA[0];
			if (isNew(newV, hset)) {
				hset.add(newV);
			} 
		}
		if (hset.size() == 1) {
			return true;
		}
		return false;
	}
	
	public void displayTree(DecisionTree.TreeNode tn, String s) {
		String n = "";
		String m = "";
		if (tn == null)
		    return;
		n = "V: " + tn.getValue() + " |A: " + tn.getAttribute() + " L: " + tn.getLabel() + " ";
		m = s + n;
		if (!tn.getChildren().isEmpty()) {
			for (DecisionTree.TreeNode child: tn.getChildren()) {
				  displayTree(child, m);
			}
		} else {
			System.out.println(m);
		}
	}
	
	public String testModel(DecisionTree.TreeNode tn) {
		String s = "";
		//for each data point in testset, run it through the model.
		for (int i = 0; i < testset.size(); i++) {
			String[] t = testset.get(i);
			s += testTree(tn, t);
		}
		return s;
	}
	
	public String testTree(DecisionTree.TreeNode tn, String[] s) {
		String z = "";
		String w = tn.getAttribute();
		String y = tn.getLabel();
		if (w.length() > 0) {
			int ia = indexofString(w, header);
			String v = s[ia];
			for (DecisionTree.TreeNode child: tn.getChildren()) {
				if (child.getValue().equals(v)) {
					testTree(child, s);
				}
			}
		}
		if (y.length() < 1) {
			return z += mctv;
		}
		return z+= tn.getLabel();
	}
	
	public void accuracy(String s) {
		int[] c = new int[2];
		for (int i = 0; i<s.length(); i++) {
			String[] t = testset.get(i);
			char d = s.charAt(i);
			if (t[0].equals(d+"")) {
				c[0]++;
			}
		}
		System.out.println("Accuracy : " + (double)c[0]/(double)testset.size());
	}
}
