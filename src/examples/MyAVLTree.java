package examples;

import java.util.Iterator;

public class MyAVLTree<K extends Comparable<? super K>, E> implements
		OrderedDictionary<K, E> {

	// auxiliary class for the nodes
	class AVLNode implements Locator<K,E>{
		
		K key;
		E elem;
		AVLNode parent,left,right;
		Object creator = MyAVLTree.this;
		int height;
		
		@Override
		public E element() {
			return elem;
		}

		@Override
		public K key() {
			return key;
		}

		boolean isExternal(){
			return key==null;
		}
		
		boolean isLeftChild(){
			if (parent==null) return false;
			return parent.left==this;
		}
		
		boolean isrightChild(){
			if (parent==null) return false;
			return parent.right==this;
		}
		
		void expand(K key, E elem){
			this.key = key;
			this.elem = elem;
			height=1;
			left = new AVLNode();
			right = new AVLNode();
			// backwards:
			left.parent=right.parent=this;
		}
	}
	
	// instance variables
	
	private AVLNode root = new AVLNode();
	private int size;
	

	private AVLNode checkAndCast(Locator<K,E> p) {
		AVLNode n;
		try {
			n = (AVLNode) p;
		} catch (ClassCastException e) {
			throw new RuntimeException("This is not a Locator belonging to MyLinkedList"); 
		}
		if (n.creator == null) throw new RuntimeException("locator was allready deleted!");
		if (n.creator != this) throw new RuntimeException("locator belongs to another MyLinkedList instance!");			
		return n;
	}

	
	@Override
	public int size() {
		return size;
	}

	@Override
	public Locator<K, E> find(K key) {
		// return the "leftmost" locator with key 'key' (or null)
		AVLNode n = root;
		AVLNode ret = null;
		while (! n.isExternal()){
			int comp = key.compareTo(n.key);
			if (comp<0) n=n.left;
			else if (comp>0) n=n.right;
			else {
				ret = n;
				n=n.left;
			}
		}
		return ret;
	}

	@Override
	public Locator<K, E>[] findAll(K key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Locator<K, E> insert(K key, E o) {
		AVLNode n=root;
		while( ! n.isExternal()){
			if (key.compareTo(n.key)<0) n=n.left;
			else n=n.right;
		}
		n.expand(key,o);
		// now we should adjust the height of all ancestors of n
		adjustHeightAboveAndRebalance(n);
		size++;
		return n;
	}

	private void adjustHeightAboveAndRebalance(AVLNode n) {
		n=n.parent;
		while (n!=null){
			int newHeight = 1+Math.max(n.left.height,n.right.height);
			if (n.height==newHeight) return;
			n.height=newHeight;
			n=n.parent;
		}
	}


	@Override
	public void remove(Locator<K, E> loc) {
		// TODO Auto-generated method stub

	}

	@Override
	public Locator<K, E> closestBefore(K key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Locator<K, E> closestAfter(K key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Locator<K, E> next(Locator<K, E> loc) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Locator<K, E> previous(Locator<K, E> loc) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Locator<K, E> min() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Locator<K, E> max() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterator<Locator<K, E>> sortedLocators() {
		// TODO Auto-generated method stub
		return null;
	}

	public String toString(){
		StringBuilder sb = new StringBuilder();
		buildString(root,"", sb, true);
		return sb.toString();
	}
	
	private void buildString(AVLNode r, String ind,
			StringBuilder sb, boolean indent) {

		if (r.isExternal()) return;
		String add ="";
		if (indent) add="-"; 
		buildString(r.left, ind+add, sb, indent);
		sb.append(ind+"key: "+r.key+" element: "+r.elem+" ,height="+r.height+"\n");
		buildString(r.right, ind+add, sb, indent);	
	}

	public static void main(String[] args) {
		MyAVLTree<Integer,String> t = new MyAVLTree<>();
		t.insert(10,"elem of key 10");
		t.insert(5,"first elem of key 5");
		t.insert(1,"elem of key 1");
		t.insert(20,"elem of key 20");
		t.insert(5,"second elem of key 5");
		t.insert(3,"elem of key 3");
		System.out.println(t.find(5).element());
		System.out.println(t);
	}

}
