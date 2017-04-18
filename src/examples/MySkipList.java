/**
 * 
 */
package examples;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;



/**
 * @author ps
 *
 */
public class MySkipList<K extends Comparable<? super K>, E> implements
		OrderedDictionary<K, E> {
	
	// the class for the nodes:
	class SLNode implements Locator<K,E> {
		SLNode prev,next,above,below; // neighbours
		Object owner = MySkipList.this;
		K key;
		E elem;

		/* (non-Javadoc)
		 * @see examples.Position#element()
		 */
		@Override
		public E element() {
			return elem;
		}

		/* (non-Javadoc)
		 * @see examples.Locator#key()
		 */
		@Override
		public K key() {
			return key;
		}
	}

	
	// instance variables
	// 4 inital nodes which remain always the same
	private SLNode topLeft,bottomLeft,topRight,bottomRight;
	// min, max
	private K minKey,maxKey;
	// 
	private int size;
	private Random rand = new Random();
	private double p = 0.5; // index probability
	private int height = 2;
	
	public MySkipList(K min, K max){
		topLeft = new SLNode();
		topLeft.key = min;
		topRight = new SLNode();
		topRight.key = max; 
		bottomLeft = new SLNode();
		bottomLeft.key = min;
		bottomRight = new SLNode();
		bottomRight.key = max; 
		// connect them
		topLeft.next = topRight;
		topRight.prev = topLeft;
		bottomLeft.next = bottomRight;
		bottomRight.prev = bottomLeft;

		topLeft.below = bottomLeft;
		topRight.below = bottomRight;
		bottomLeft.above = topLeft;
		bottomRight.above = topRight;
		
		minKey = min;
		maxKey = max;
	}
	
	
	

	/* (non-Javadoc)
	 * @see examples.OrderedDictionary#size()
	 */
	@Override
	public int size() {
		return size;
	}


	/* (non-Javadoc)
	 * @see examples.OrderedDictionary#find(java.lang.Comparable)
	 */
	@Override
	public Locator<K, E> find(K key) {
		if (key.compareTo(minKey)<=0) throw new RuntimeException("key not bigger than minKey!");
		if (key.compareTo(maxKey)>=0) throw new RuntimeException("key not smaller than maxKey!");
		SLNode pos = search(key);
		if (pos.key.compareTo(key)!=0) return null; // we found nothing
		else {
			// we take the leftmost Locator with valid key
			while (pos.prev.key.compareTo(key)== 0) pos=pos.prev;
			return pos;
		}
	}

	private SLNode search(K key){
		SLNode n = topLeft;
		while (n.below!= null){
			n=n.below;
			while (key.compareTo(n.next.key)>=0) n=n.next;		
		}
		return n;
	}
	
	
	/* (non-Javadoc)
	 * @see examples.OrderedDictionary#findAll(java.lang.Comparable)
	 */
	@Override
	public Locator<K, E>[] findAll(K key) {
		SLNode n = (SLNode) find(key); // returns the leftmost occurence
		if (n==null) return new Locator[0];
		ArrayList<Locator<K,E>> al = new ArrayList<>();
		while(n.key.compareTo(key)==0){
			al.add(n);
			n=n.next;
		}
		return al.toArray(new Locator[0]);
	}


	/* (non-Javadoc)
	 * @see examples.OrderedDictionary#insert(java.lang.Comparable, java.lang.Object)
	 */
	@Override
	public Locator<K, E> insert(K key, E o) {
		if (key.compareTo(minKey)<=0) throw new RuntimeException("key not bigger than minKey!");
		if (key.compareTo(maxKey)>=0) throw new RuntimeException("key not smaller than maxKey!");
		SLNode pos = search(key);
		// we take the rightmost Locator with valid key
		while (pos.next.key.compareTo(key)== 0) pos=pos.next;		
		// now we want to insert a node at the position pos.next:		
		SLNode nNew = new SLNode();
		nNew.key=key;
		nNew.elem=o;
		nNew.next=pos.next;
		nNew.prev=pos;
		nNew.prev.next=nNew;
		nNew.next.prev=nNew;
		SLNode pn=nNew;
		while (rand.nextFloat()<p){
			SLNode index = new SLNode();
			index.key = key;
			while (pos.above==null) pos=pos.prev;
			pos=pos.above;
			// einhängen
			index.below = pn;
			pn.above = index;
			index.next=pos.next;
			index.prev=pos;
			index.prev.next=index;
			index.next.prev=index;
			if (pos ==topLeft) expand();
			pn=pn.above;
		}
		return nNew;
	}

	private void expand(){
		// adds one index level
		// System.out.println("expanding..");
		SLNode n1 = new SLNode();
		n1.key = minKey;
		SLNode n2 = new SLNode();
		n2.key = maxKey;
		n1.next = n2;
		n2.prev = n1;
		
		n1.below = topLeft;
		n2.below = topRight;
		
		topLeft.above = n1;
		topRight.above = n2;

		topLeft = n1;
		topRight = n2;
		height++;
	}

	/* (non-Javadoc)
	 * @see examples.OrderedDictionary#remove(examples.Locator)
	 */
	@Override
	public void remove(Locator<K, E> loc) {
		SLNode n = (SLNode) loc;
		if (n.owner != this) throw new RuntimeException("invalid locator "+loc.key());
		n.owner=null;
		int lev=0;
		while (n!=null){
			n.prev.next=n.next;
			n.next.prev=n.prev;
			n=n.above;
			lev++;
		}
		if (lev==height-1) shrink();
		size--;
	}


	/**
	 * 
	 */
	private void shrink() {
		// System.out.println("shrink called");
		while (height>2 && topLeft.below.next==topRight.below){
			// System.out.println("shrinking...");
			topLeft = topLeft.below;
			topRight = topRight.below;
			topLeft.above = null;
			topRight.above = null;
			height--;
		}
	}


	/* (non-Javadoc)
	 * @see examples.OrderedDictionary#closestBefore(java.lang.Comparable)
	 */
	@Override
	public Locator<K, E> closestBefore(K key) {
		if (key.compareTo(minKey)<=0) throw new RuntimeException("key not bigger than minKey!");
		if (key.compareTo(maxKey)>=0) throw new RuntimeException("key not smaller than maxKey!");
		SLNode pos = search(key);
		int comp = key.compareTo(pos.key);
		if (comp==0){
			pos = pos.prev;
			// still equal?
			if (pos == bottomLeft) return null;
			while (key.compareTo(pos.key)==0) pos=pos.prev;
		}
		else if (comp>0){
			// in case we have sevearal equal keys take the rightmost locator
			while (pos.key.compareTo(pos.next.key)==0) pos=pos.next;
			if (pos == bottomLeft) pos = null;
			
		}
		else 
			throw new RuntimeException("should never happen!");
		return pos;

	}

	/* (non-Javadoc)
	 * @see examples.OrderedDictionary#closestAfter(java.lang.Comparable)
	 */
	@Override
	public Locator<K, E> closestAfter(K key) {
		if (key.compareTo(minKey)<=0) throw new RuntimeException("key not bigger than minKey!");
		if (key.compareTo(maxKey)>=0) throw new RuntimeException("key not smaller than maxKey!");
		SLNode pos = search(key);
		int comp = key.compareTo(pos.key);
		if (comp==0){
			pos = pos.next;
			// still equal?
			while (key.compareTo(pos.key)==0) pos=pos.next;
			if (pos == bottomRight) pos = null;
		}
		else if (comp>0){
			// in case we have several equal keys take the rightmost locator
			while (pos.key.compareTo(pos.next.key)==0) pos=pos.next;
			// the next key is bigger than 'key'
			pos = pos.next;
			if (pos == bottomRight) pos = null;
			
		}
		else throw new RuntimeException("should never happen!");
		return pos;
	}


	/* (non-Javadoc)
	 * @see examples.OrderedDictionary#next(examples.Locator)
	 */
	@Override
	public Locator<K, E> next(Locator<K, E> loc) {
		SLNode n = (SLNode) loc;
		if (n.owner != this) throw new RuntimeException("invalid locator "+loc.key());
		n = n.next;
		if (n==bottomRight) n=null;
		return n;
	}


	/* (non-Javadoc)
	 * @see examples.OrderedDictionary#previous(examples.Locator)
	 */
	@Override
	public Locator<K, E> previous(Locator<K, E> loc) {
		SLNode n = (SLNode) loc;
		if (n.owner != this) throw new RuntimeException("invalid locator "+loc.key());
		n = n.prev;
		if (n==bottomLeft) n=null;
		return n;
	}


	/* (non-Javadoc)
	 * @see examples.OrderedDictionary#min()
	 */
	@Override
	public Locator<K, E> min() {
		if (size>0) return bottomLeft.next; 
		else return null;
	}


	/* (non-Javadoc)
	 * @see examples.OrderedDictionary#max()
	 */
	@Override
	public Locator<K, E> max() {
		if (size>0) return bottomRight.prev; 
		else return null;
	}


	/* (non-Javadoc)
	 * @see examples.OrderedDictionary#sortedLocators()
	 */
	@Override
	public Iterator<Locator<K, E>> sortedLocators() {
		return new Iterator<Locator<K, E>>(){
			SLNode pos = bottomLeft.next;
			@Override
			public boolean hasNext() {
				return pos != bottomRight;
			}

			@Override
				public Locator<K, E> next() {
				SLNode ret = pos;
				pos = pos.next;
				return ret;
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException(
						"use remove method of MySkipList!");
			}
			
		};
	}

	public void print(){
		System.out.println("-------start------");
		SLNode n = bottomLeft;
		n=n.next;
		StringBuffer lev = new StringBuffer();
		while (n!=bottomRight){
			lev.delete(0,lev.length());
			SLNode m = n;
			int index = 0;
			while (m.above != null) {
				index++;
				m=m.above;
				lev.append("+");
			}
			while(index<height-2){
				index++;
				lev.append("|");
			}
			System.out.println(String.format("%11d", n.key())+lev.toString()+"    elem: "+n.elem);
			n=n.next;
		}
		System.out.println("--------end-------");
		
	}


	/**
	 * @param args
	 */
	public static void main(String[] args) {
		MySkipList<Integer, String> sl = new MySkipList<>(Integer.MIN_VALUE,Integer.MAX_VALUE);
		Random rand = new Random();
		int n  = 100;
//		Locator<Integer,String>[] locs = new Locator[n];
		int [] keys = new int[n];
		long t1 = System.nanoTime();
//		for(int i=0;i<n;i++) locs[i]=t.insert(rand.nextInt(n/1000), ""+i);
		for(int i=0;i<n;i++) {
			keys[i]=rand.nextInt(n);
			sl.insert(keys[i], ""+i);
		}
//		for(int i=0;i<n/2;i++) t.remove(locs[i]);
//		for(int i=0;i<n/2;i++){
//			sl.remove(sl.find(keys[i]));
//		}
		long t2 = System.nanoTime();
		System.out.println("time: "+1e-9*(t2-t1)+" sec for "+n+" insertions and removes");//		Locator<Integer,String>[] locs = new Locator[n];
		System.out.println(sl.height);
		sl.print();
		//		long time1 = System.nanoTime();
//		for (int i=0;i<n;i++) {
//			locs[i]=sl.insert(rand.nextInt(n),""+i); 
//		}
//		long time2 = System.nanoTime();
//		System.out.println("time: "+(time2-time1)*1e-9);
//		System.out.println(sl.height);
//		for (int i=0;i<n-100;i++) {
//			sl.remove(locs[i]); 
//		}
//		Locator<Integer,String>[] ll = sl.findAll(33);
//		for (int i=0;i<ll.length;i++)System.out.println(ll[i].key());
//		System.out.println("elapsed time: "+(time2-time1)*1e-9+" s");
//		System.out.println("height of index: "+sl.height);
//		Iterator<Locator<Integer,String>> it = sl.sortedLocators();
//		while (it.hasNext()){
//			Locator<Integer, String> loc = it.next();
//			System.out.println(loc.key()+" element: "+loc.element());
//		}
//		sl.print();
//		sl.remove(locs[15]);
//		sl.print();
//		Locator<Integer,String> loc = sl.closestBefore(83);
//		if (loc!= null)System.out.println(loc.key()+":"+loc.element());
	}

}
