package examples;

import java.util.Iterator;
import java.util.LinkedList;

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
		return checkAndCast(p).prev;
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
		LNode n =new LNode();
		n.elem = o;
		if (size==0){
			last=first=n;
		}
		else {
		 last.next=n;
		 n.prev=last;
		 last=n;
		}
		size++;
		return n;
	}

	@Override
	public Position<E> insertBefore(Position<E> p, E o) {
		LNode np = checkAndCast(p);
		LNode n = new LNode();
		n.elem = o;
		
		// chain: (n before np)
		n.next = np;
		n.prev = np.prev;
		np.prev = n;
		if (np==first) first=n;
		else n.prev.next=n;
		
		size++;
		return n;
	}

	@Override
	public Position<E> insertAfter(Position<E> p, E o) {
		LNode np = checkAndCast(p);
		LNode n = new LNode();
		n.elem = o;
		
		// chain: (n after np)
		n.prev = np;
		n.next = np.next;
		np.next = n;
		if (np==last) last=n;
		else n.next.prev=n;
		
		size++;
		return n;
	}

	@Override
	public void remove(Position<E> p) {
		LNode n = checkAndCast(p);
		if (n==first) first = n.next;
		else n.prev.next=n.next;
		if (n==last) last = n.prev;
		else n.next.prev = n.prev;
		n.creator = null; // invalidate n 
		size--;
	}

	@Override
	public Iterator<Position<E>> positions() {
		return new Iterator<Position<E>>(){
			LNode current = first;
			@Override
			public boolean hasNext() {
				return current!=null;
			}

			@Override
			public Position<E> next() {
				LNode n = current;
				current = current.next;
				return n;
			}};
	}

	@Override
	public Iterator<E> elements() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

	public static void main(String[] args) {
		List<String> list1 = new MyLinkedList<>(); 
		List<String> list2 = new MyLinkedList<>();
		Position<String> p1 = list1.insertFirst("hans");
		Position<String> p2 = list1.insertFirst("beat");
		Position<String> p3 = list1.insertFirst("susi");
		Position<String> pi = list1.first();
		list1.insertAfter(p2,"beat2");
		list1.insertAfter(p1,"hans2");
		while (pi!=null){
			System.out.println(pi.element());
			pi=list1.next(pi);
		}
		System.out.println("last: "+list1.last().element());
	
	}
	
}
