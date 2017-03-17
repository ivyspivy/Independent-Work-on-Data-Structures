public class Node {
	/*
	 * Doubly linked list
	 */
	Node before = null;
	Node after = null;
	int value;
	public Node(int v){
		this.value = v;
	}
	
	/*
	 * adds a node to the end of a list
	 */
	public void addNode(Node n){
		Node current = this;
		while(current.after != null){
			current = this.after;
		}
		current.after = n;
		n.before = current;
	}
	
	/*
	 * returns true if remove
	 * if not in list, returns false
	 */
	public boolean removeNode(int elem){
		Node current = this;
		
		if(current.after == null){ //elem is not in list
			return false;
		}
		while(current.after.value != elem){
			current = current.after.after;
		}
		//current.after is now the value to remove, shift all elements over
		if(current.after.after == null){ //node to remove is in the end of the list
			current.after = null;
		}
		while(current.after.after != null){
			current.after = current.after.after;
			current.after.before = current;
		}
		return true;
	}
	
	/*
	 * Moves down the linked list to the node after.
	 * Returns the new "current" node
	 */
	public Node moveForward(){
		return this.after;
	}
	
	/*
	 * Moves up the linked list to the node before.
	 * Returns the new "current" node.
	 */
	public Node moveBackward(){
		return this.before;
	}
}
