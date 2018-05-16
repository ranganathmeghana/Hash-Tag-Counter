import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FibonacciHeap {

	private int heapCount=0;
	private Node maxNode=null;;
	Map<String, Node> heapMap = new HashMap<String, Node>();

	class Node {

		Node leftSibling;
		Node rightSibling;
		Node parent;
		Node child;
		int degree;
		int value;
		boolean mark;
		String word;

		// changes made here
		public Node() {
			// TODO Auto-generated constructor stub

			this.leftSibling = null;
			this.rightSibling = null;
			this.parent = null;
			this.child = null;
		}

	}

	public void insertIntoHeap(String word, int value) {

		if (heapMap.containsKey(word)) {

			Node node = heapMap.get(word);
			increaseKey(node, value);

		} else {
			Node node = new Node();
			node.word = word;
			node.value = value;
			node.degree = 0;
			
			// made change, removed child=null
			// node.child=null;
			// made changes here
			insertNode(node);
			heapCount++;
			heapMap.put(word, node);

		}

	}

	private void insertNode(Node node) {

		if (maxNode == null) {
			maxNode = node;
			node.leftSibling = node;
			node.rightSibling = node;
		} else {

			// made change, node.rightSibling
			node.leftSibling = maxNode;
			node.rightSibling = maxNode.rightSibling;
			maxNode.rightSibling = node;
			node.rightSibling.leftSibling = node;

			if (node.value > maxNode.value) {
				maxNode = node;
			}

		}
		node.mark = false;
		node.parent = null;

	}

	public void increaseKey(Node node, int value) {

		node.value = node.value + value;

		if (node.parent == null) {

			if (node.value > maxNode.value) {
				maxNode = node;
			}
			return;
		}

		else if (node.parent != null && node.value < node.parent.value) {
			return;
		}

		else {

			Node current = node;
			while (true) {
				Node parent = current.parent;
				boolean parentChildCut = parent.mark;
				parent.degree--;

				if (parent.parent != null) {
					parent.mark = true;
				}
				if (parent.child == current) {
					if (current.rightSibling == current) {
						parent.child = null;
					} else {
						parent.child = current.rightSibling;
					}
				}
				if (current.rightSibling != current) {
					current.rightSibling.leftSibling = current.leftSibling;
					current.leftSibling.rightSibling = current.rightSibling;
				}

				insertNode(current);

				if (!parentChildCut) {
					break;

				}

				current = parent;
			}
		}
	}

	public Node removeMax() {
		Node initialMax = maxNode;

		if (initialMax == null) {
			return null;
		}

		// change here, leftSibling
		else if (initialMax.leftSibling == initialMax) {
			maxNode = null;
		}

		else {
			initialMax.rightSibling.leftSibling = initialMax.leftSibling;
			initialMax.leftSibling.rightSibling = initialMax.rightSibling;
			maxNode = initialMax.rightSibling;
		}

		Node nodeChild = initialMax.child;
		consolidate(nodeChild);
		heapCount--;
		return initialMax;
	}

	private void consolidate(Node nodeChild) {
		// TODO Auto-generated method stub

		Node last = nodeChild;
		Node max = nodeChild;

		if (nodeChild != null) {
			while (last.rightSibling != nodeChild) {

				last.parent = null;
				last.mark = false;

				if (last.value > max.value) {

					max = last;
				}

				last = last.rightSibling;
			}

		}

		if (maxNode == null) {

			maxNode = max;
			meldEqualDegree();
			return;
		}

		else if (nodeChild != null) {

			nodeChild.leftSibling = maxNode;
			last.rightSibling = maxNode.rightSibling;
			maxNode.rightSibling = nodeChild;
			last.rightSibling.leftSibling = last;
		}

		Node node1 = maxNode;
		Node node2 = maxNode;

		while (maxNode != node1.rightSibling) {

			if (node2.value < node1.value) {
				node2 = node1;
			}
			node1 = node1.rightSibling;
		}

		if (node1.value > node2.value) {
			node2 = node1;
		}
		maxNode = node2;
		meldEqualDegree();

	}

	private void meldEqualDegree() {

		boolean control = true;

		if (maxNode == null) {
			return;
		}

		while (control) {
			control = false;

			Map<Integer, Node> nodeMap = new HashMap<>();
			nodeMap.put(maxNode.degree, maxNode);
			Node temp = maxNode.rightSibling;

			while (temp != maxNode) {
				Node next = temp.rightSibling;
				Node joinedNodes = null;
				int d = temp.degree;
				// meld the two trees of equal degree
				if (nodeMap.containsKey(temp.degree)) {
					Node node1 = nodeMap.get(temp.degree);
					control = true;
					if (node1.value >= temp.value) {
						joinedNodes = meld(node1, temp);
					} else {
						joinedNodes = meld(temp, node1);
					}

					// Remove the old entry from the table
					nodeMap.remove(d);
					if (!nodeMap.containsKey(joinedNodes.degree)) {
						nodeMap.put(joinedNodes.degree, joinedNodes);
					}

				}

				/*
				 * If the current node's degree doesn't have a corresponding
				 * entry in the, then insert the node into the table
				 */
				else {
					nodeMap.put(temp.degree, temp);
				}
				temp = next;
			}
		}
	}

	/*
	 * Method that makes node2 a subtree of node1 and returns the resulting tree
	 */
	private Node meld(Node n1, Node n2) {
		if (n1 == n2) {
			System.out.println("unexpected");
		}
		// Remove node 2 from the circular list
		n2.rightSibling.leftSibling = n2.leftSibling;
		n2.leftSibling.rightSibling = n2.rightSibling;
		// make node2 a child of node1
		n1.degree++;
		n2.parent = n1;

		// add node2 to the siblings list of node1. 2 cases:
		// If node1 has no children
		if (n1.child == null) {
			n1.child = n2;
			n2.leftSibling = n2;
			n2.rightSibling = n2;
		}

		// Else if node1 has children
		else {
			n2.leftSibling = n1.child;
			n2.rightSibling = n1.child.rightSibling;
			n1.child.rightSibling = n2;
			n2.rightSibling.leftSibling = n2;
		}
		return n1;
	}

	public List<String> maxWords(int n) {

		List<String> maxWords = new ArrayList<>();
		List<Node> removed = new ArrayList<>();
		for (int i = 0; i < n; i++) {

			Node temp = removeMax();

			String hashWords = temp.word;

			heapMap.remove(hashWords);

			maxWords.add(hashWords);

			removed.add(temp);
		}

		for (int i = 0; i < removed.size(); i++) {
			insertIntoHeap(removed.get(i).word, removed.get(i).value);
		}
		return maxWords;

	}

}
