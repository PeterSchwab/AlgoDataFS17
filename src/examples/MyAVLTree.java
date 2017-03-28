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
		}
	}
	
	// instance variables
	
	private AVLNode root = new AVLNode();
	private int size;
	
	@Override
	public int size() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Locator<K, E> find(K key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Locator<K, E>[] findAll(K key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Locator<K, E> insert(K key, E o) {
		// TODO Auto-generated method stub
		return null;
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

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
