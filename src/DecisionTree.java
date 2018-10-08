import java.util.ArrayList;
import java.util.List;

/*
 * DecisionTree class that creates a decision tree based on the selected attribute.
 * The tree is made up of tree nodes that keeps track of a parent and list of children nodes.
 */
public class DecisionTree{

	class TreeNode {

		private String attribute = "";
		private TreeNode parent;
		private List<TreeNode> children = new ArrayList<TreeNode>();
		private String value = "";
		private String label = "";
		
		public TreeNode(String v) {
			this.value = v;
		}
		
		public TreeNode() {
			
		}
		
		public void setAttribute(String a) {
			this.attribute = a;
		}
		
		public String getAttribute() {
			return this.attribute;
		}
		
		public void setValue(String v) {
			this.value = v;
		}
		
		public void setLabel(String l) {
			this.label = l;
		}
		
		public String getLabel() {
			return this.label;
		}
		
		public String getValue() {
			return this.value;
		}

		public TreeNode getParent() {
			return this.parent;
		}
		
		public void setParent(TreeNode pn) {
			this.parent = pn;
		}
		
		public void addChild(TreeNode child) {
			children.add(child);
		}
		
		public List<TreeNode> getChildren() {
			return this.children;
		}

	}

	private TreeNode root;

	public DecisionTree() {
	}
	
	public void setRoot(TreeNode root) {
		this.root = root;
	}

	public TreeNode getRoot() {
		return this.root;
	}

	
}
