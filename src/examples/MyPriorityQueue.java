package examples;

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

	private PQLoc<K,E> [] heap = new PQLoc[100];
	private int size;
	
	@Override
	public Locator<K, E> showMin() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Locator<K, E> removeMin() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Locator<K, E> insert(K key, E element) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void remove(Locator<K, E> loc) {
		// TODO Auto-generated method stub

	}

	@Override
	public void replaceKey(Locator<K, E> loc, K newKey) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return 0;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
