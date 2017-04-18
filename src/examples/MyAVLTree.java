package examples;

import java.util.Iterator;
import java.util.Random;

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
		
		boolean isRightChild(){
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
		List<Locator<K,E>> list = new MyLinkedList<>();
		if (size!=0) findAll(key,root,list);
		Iterator<Locator<K,E>> it = list.elements();
		Locator[]ret = new Locator[list.size()];
		int i = 0;
		while (it.hasNext()) ret[i++]=it.next();
		return ret;
	}

	private void findAll(K key,AVLNode p,List<Locator<K, E>> list) {
		if (p.isExternal()) return;
		int comp = p.key.compareTo(key);
		if (comp<0) findAll(key,p.right,list);
		else if (comp>0) findAll(key,p.left,list);
		else {
			findAll(key,p.left,list);
			list.insertLast(p);
			findAll(key,p.right,list);
		}
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
			boolean balanced = Math.abs(n.left.height-n.right.height)<2; 
			if (n.height==newHeight && balanced) return;
			n.height=newHeight;
			if ( ! balanced) n = restructure(n);
			n=n.parent;
		}
	}


	@Override
	public void remove(Locator<K, E> loc) {
		AVLNode n = checkAndCast(loc);
		AVLNode w = null; // this will be the node which
						  // replaced the really removed node
		
		if ( ! n.left.isExternal() && ! n.right.isExternal()){
			// we must replace
			// lets take the rightmost node in the left 
			// subtree of n:
			AVLNode v = n.left;
			while ( ! v.right.isExternal()) v = v.right;
			w = removeAboveExternal(v);
			// now replace n by v:
			v.parent = n.parent;
			if (n.isLeftChild()) v.parent.left = v;
			else if (n.isRightChild()) v.parent.right = v;
			else root = v;
			// height:
			v.height = n.height;
			// children:
			v.left = n.left;
			v.left.parent = v;
			v.right = n.right;
			v.right.parent = v;
			
		}
		else {
			w = removeAboveExternal(n);
		}
		n.creator = null;
		size--;
		adjustHeightAboveAndRebalance(w);

	}

	private AVLNode removeAboveExternal( AVLNode n) {
		// remove n and return the node which takes the place of n
		AVLNode w;
		if (n.left.isExternal()){
			w = n.right;
		}
		else w = n.left;
		// chaining
		w.parent = n.parent;
		if (n.isLeftChild()) w.parent.left = w;
		else if (n.isRightChild()) w.parent.right = w;
		else root = w;			
		return w;
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
		AVLNode n=checkAndCast(loc);
		if (n.right.isExternal()){
			while (n.isRightChild()) n=n.parent;
			return n.parent;
		}
		else {
			n=n.right;
			while (! n.left.isExternal()) n=n.left;
			return n;
		}
	}

	@Override
	public Locator<K, E> previous(Locator<K, E> loc) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Locator<K, E> min() {
		if (size==0) return null;
		AVLNode n = root;
		while ( ! n.left.isExternal()) n=n.left;
		return n;
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

	private AVLNode restructure(AVLNode z) {
		// z is unbalanced
		// returns the node that takes the position of z (i.e. b)
		AVLNode p=z.parent,x=null,y=null,
		a=null,b=null,c=null, t1=null,t2=null; 
		// t0 and t3 never change their parent, 
		// that's why we don't need them 
		if (z.left.height > z.right.height){
			//   z
			//  /
			// y
			c=z;
			y=z.left;
			if (y.left.height >= y.right.height){
				// in case we have two equal branches
				// concidering the length we take always the single
				// rotation
				//     z
				//    /
				//   y
				//  /
				// x
				x=y.left;
				t1=x.right;
				t2=y.right;
				b=y;
				a=x;
			}
			else {
				//     z
				//    /
				//   y
				//   \  
				//    x
				x=y.right;
				t1=x.left;
				t2=x.right;
				a=y;
				b=x;
			}
		}
		else{
			// z
			//   \
			//    y
			a=z;
			y=z.right;
			if (y.right.height >= y.left.height){
				//  z
				//   \
				//    y
				//     \  
				//      x
				x=y.right;
				b=y;
				c=x;
				t1=y.left;
				t2=x.left;
			}
			else {
				//  z
				//   \
				//    y
				//    /  
				//   x
				x=y.left;
				b=x;
				c=y;
				t1=x.left;
				t2=x.right;
			}
		}		
		// umhaengen
		b.parent = p;
		if (p != null){
			if (p.left == z) {
				p.left=b;
			}
			else p.right=b;
		}
		else {
			root=b;
		}
		b.right = c;
		b.left = a;
		// ..and reverse:
		a.parent = b;
		c.parent = b;

		// subtrees: (t0 and t3 are already  at its position)
		a.right = t1;
		t1.parent = a;
		c.left = t2;
		t2.parent = c;
		
		
		a.height = Math.max(a.left.height, a.right.height)+1;
		c.height = Math.max(c.left.height, c.right.height)+1;
		// now we can calculate the height of b
		b.height = Math.max(b.left.height, b.right.height)+1;
		return b;
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

	public void test(){
		if (root.parent!=null) throw new RuntimeException("root has parent!");
		if (size>0) test(root);
	}


	private void test(AVLNode n) {
		if (n.isExternal()) return;
		test(n.left);
		test(n.right);
		if (n.left.parent != n) throw new RuntimeException("chaining incorrect"+n.key);
		if (n.right.parent != n) throw new RuntimeException("chaining incorrec"+n.key);
		if (Math.max(n.left.height,n.right.height)+1!=n.height)
			throw new RuntimeException("Height wrong"+n.key);
		if (n.left.key !=null &&  n.left.key.compareTo(n.key)>0) throw new RuntimeException("order wrong "+n.key); 
		if (n.right.key !=null &&  n.right.key.compareTo(n.key)<0) throw new RuntimeException("order wrong "+n.key);
		if (Math.abs(n.left.height-n.right.height)> 1) throw new RuntimeException("unbalanced "+n.key);
		if (n.creator != this) throw new RuntimeException("invalid node: "+n.key);
	}

	public static void main(String[] args) {
		MyAVLTree<Integer,String> t = new MyAVLTree<>();
		int n = 1000000;
		Random rand = new Random();
//		Locator<Integer,String>[] locs = new Locator[n];
		int [] keys = new int[n];
		long t1 = System.nanoTime();
//		for(int i=0;i<n;i++) locs[i]=t.insert(rand.nextInt(n/1000), ""+i);
		for(int i=0;i<n;i++) {
			keys[i]=rand.nextInt(n/1000);
			t.insert(keys[i], ""+i);
		}
//		for(int i=0;i<n/2;i++) t.remove(locs[i]);
		for(int i=0;i<n/2;i++){
			t.remove(t.find(keys[i]));
		}
		long t2 = System.nanoTime();
		System.out.println("time: "+1e-9*(t2-t1)+" sec for "+n+" insertions and removes");
		System.out.println("height:"+t.root.height); 
//		System.out.println(t);
		t.test();
		Locator[] found =  t.findAll(6);
		for (int i = 0;i<found.length;i++) System.out.println(found[i].key()+", "+found[i].element());
//		Locator loc = t.min();
//		while (loc!=null) {
//			System.out.println(loc.key()+", "+loc.element());
//			loc = t.next(loc);
//		}
	}	

}
