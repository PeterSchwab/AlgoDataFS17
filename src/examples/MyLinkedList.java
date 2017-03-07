package examples;

import java.util.Iterator;

public class MyLinkedList<E> implements List<E> {

	//auxiliary class
	class LNode implements Position<E>{
		E elem;
		LNode prev,next;
		Object creator = MyLinkedList.this;
		@Override
		public E element() {
			return elem;
		}		
	}
	
	// instance variables
	private LNode first,last;
	private int size;
	

	private LNode checkAndCast(Position<E> p) {
		LNode n;
		try {
			n = (LNode) p;
		} catch (ClassCastException e) {
			throw new RuntimeException("This is not a Position belonging to MyLinkedList"); 
		}
		if (n.creator == null) throw new RuntimeException("position was allready deleted!");
		if (n.creator != this) throw new RuntimeException("position belongs to another MyLinkedList instance!");			
		return n;
	}

	
	@Override
	public Position<E> first() {
		return first;
	}

	@Override
	public Position<E> last() {
		return last;
	}

	@Override
	public boolean isFirst(Position<E> p) {
		return first==p;
	}

	@Override
	public boolean isLast(Position<E> p) {
		return last==p;
	}

	@Override
	public Position<E> next(Position<E> p) {
		return checkAndCast(p).next;
	}

	@Override
	public Position<E> previous(Position<E> p) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public E replaceElement(Position<E> p, E o) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Position<E> insertFirst(E o) {
		LNode n =new LNode();
		n.elem = o;
		if (size==0){
			last=first=n;
		}
		else {
		 first.prev=n;
		 n.next=first;
		 first=n;
		}
		size++;
		return n;
	}

	@Override
	public Position<E> insertLast(E o) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Position<E> insertBefore(Position<E> p, E o) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Position<E> insertAfter(Position<E> p, E o) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void remove(Position<E> p) {
		// TODO Auto-generated method stub

	}

	@Override
	public Iterator<Position<E>> positions() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterator<E> elements() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

	public static void main(String[] args) {
		List<String> list1 = new MyLinkedList<>(); 
		List<String> list2 = new MyLinkedList<>();
		Position p1 = list1.insertFirst("hans");
		Position p2 = list1.insertFirst("beat");
		
		list1.insertFirst("susi");
		Position p3 = list1.first();
		while (p3!=null){
			System.out.println(p3.element());
			p3=list1.next(p3);
		}
		
	}

}
