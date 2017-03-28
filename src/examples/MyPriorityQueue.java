package examples;

import java.util.Arrays;
import java.util.Random;


public class MyPriorityQueue<K extends Comparable<? super K>, E> implements
		PriorityQueue<K, E> {

	class PQLoc<K1 extends Comparable<? super K1>,E1> implements Locator<K1,E1>{
		K1 key;
		E1 elem;
		int pos;  // position in our heap
		Object creator = MyPriorityQueue.this;
		
		@Override
		public E1 element() {
			return elem;
		}

		@Override
		public K1 key() {
			return key;
		}
	}

	private PQLoc<K,E> [] heap = new PQLoc[1000000];
	private int size=0; // we start at position 1 in our heap

	private PQLoc checkAndCast(Locator<K,E> p) {
		PQLoc n;
		try {
			n = (PQLoc) p;
		} catch (ClassCastException e) {
			throw new RuntimeException("This is not a Locator belonging to MyPriorityQueue"); 
		}
		if (n.creator == null) throw new RuntimeException("locator was allready deleted!");
		if (n.creator != this) throw new RuntimeException("locator belongs to another MyPriorityQueue instance!");			
		return n;
	}

	private void swap(int i, int k){
		PQLoc<K,E> tmp = heap[i];
		heap[i]=heap[k];
		heap[i].pos=i;
		heap[k]=tmp;
		heap[k].pos=k;
	}
	
	private boolean check(){
		for (int i=2;i<=size;i++)
			if (heap[i].key.compareTo(heap[i/2].key)<0) return false;
		return true;
	}
	
	private void upHeap(int pos){
		// heap[1..pos-1] is a min-heap
		// heap[pos] is eventually too small
		// at the end heap[1..pos] should be a min-heap
		int parent= pos/2;
		while (pos>1 && heap[parent].key.compareTo(heap[pos].key)>0){
			// heap[pos] is still too small
			swap(pos,parent);
			pos=parent;
			parent=pos/2;
		}
	}

	private void downHeap(int pos){
		int left = pos*2, right = left+1;
		while (left<=size){
			int smallerChild = left;
			if (right<=size && heap[right].key.compareTo(heap[left].key)<0) smallerChild = right;
			if (heap[pos].key.compareTo(heap[smallerChild].key)<=0) break;
			swap(pos,smallerChild);
			pos = smallerChild;
			left = 2*pos;
			right = left+1;
		}
	}
	
	@Override
	public Locator<K, E> showMin() {
		if (size==0) throw new RuntimeException("empty PriorityQueue!");
		return heap[size];
	}

	@Override
	public Locator<K, E> removeMin() {
		if (size==0) throw new RuntimeException("empty priority queue!");
		PQLoc<K,E> ret = heap[1];
		swap(1,size);
		size--;
		downHeap(1);
		ret.creator = null;
		return ret;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + size;
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof MyPriorityQueue)) {
			return false;
		}
		MyPriorityQueue other = (MyPriorityQueue) obj;
		if (size != other.size) {
			return false;
		}
		return true;
	}

	@Override
	public Locator<K, E> insert(K key, E element) {
		if (size==heap.length-1) heap = Arrays.copyOf(heap,heap.length*2);
		size++;
		PQLoc ret = new PQLoc<K,E>();
		ret.pos = size;
		ret.key = key;
		ret.elem = element;
		heap[size]=ret;
		upHeap(size);
		return ret;
	}

	@Override
	public void remove(Locator<K, E> loc) {
		PQLoc<K,E> ploc = checkAndCast(loc);
		int pos = ploc.pos;
		swap(ploc.pos,size);
		size--;
		downHeap(pos);
		upHeap(pos);
		ploc.creator = null;
	}

	@Override
	public void replaceKey(Locator<K, E> loc, K newKey) {
		PQLoc<K,E> ploc = checkAndCast(loc);
		int pos = ploc.pos;
		ploc.key = newKey;
		downHeap(pos);		
		upHeap(pos);
	}

	@Override
	public boolean isEmpty() {
		return size==0;
	}

	@Override
	public int size() {
		return size;
	}

	public static void main(String[] args) {
		MyPriorityQueue<Integer,String> pq = new MyPriorityQueue<>();
		Random rand = new Random();
		int [] keys;
		long time1,time2;
		int n=1000000;
		Locator [] locs = new Locator[n];
		keys = new int[n];
		System.out.println("#: "+n);
		time1=System.nanoTime();
		for (int i=0;i<n;i++) locs[i] = pq.insert(rand.nextInt(n),"");
		time2=System.nanoTime();
		System.out.println("erstellen:[s] "+(time2-time1)*1e-9);
		System.out.println(pq.check());
		// keys verändern:
		for (int i=0;i<n/2;i++) pq.replaceKey(locs[i],rand.nextInt(n));
		time1=System.nanoTime();
		for (int i=0;i<n;i++) keys[i]=pq.removeMin().key();
		time2=System.nanoTime();
		System.out.println("extrahieren:[s] "+(time2-time1)*1e-9);
		for (int i=1;i<n;i++) if (keys[i]<keys[i-1]) System.out.println("error"); 
	}

}
