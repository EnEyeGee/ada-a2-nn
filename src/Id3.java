
import java.io.FileNotFoundException;

public class Id3 {

	/*
	 * 1. read training data set and test data set (in form of text files)
	 * 2. Build model using training data as input
	 * 3. Print out representation of model (the tree)
	 * 4. Run test against the model
	 * work out accuracy of model - how many correct and confusion matrix
	 * 
	 */
	
	public static void main (String[] args) throws FileNotFoundException {
		
		//Printout accuracy and confusion matrix
		Model model = new Model();
		model.readFile();
		String target = model.header.get(0);
		model.mctv();
		model.makeListofAttributes(model.header);
		DecisionTree dt = model.makeModel(model.trainset, model.aList);
		
		//traverse tree to printout
		String s= "";
		model.displayTree(dt.getRoot(), s);
		String t = model.testModel(dt.getRoot());
		model.accuracy(t);
	}
	
		
}
