package examples;

import java.util.Iterator;

import com.sun.org.apache.bcel.internal.generic.CASTORE;

import examples.MyLinkedList.LNode;

public class MyTree<E> implements Tree<E> {

	// auxiliary class
	class TNode implements Position<E>{
		E elem;
		TNode parent;
		MyLinkedList<TNode> children = new MyLinkedList<>();
		Position<TNode> myChildrenPos;
		Object creator = MyTree.this;
		
		@Override
		public E element() {
			return elem;
		}
		
	}
	
	// instance variables
	private TNode root;
	private int size;


	private TNode checkAndCast(Position<E> p) {
		TNode n;
		try {
			n = (TNode) p;
		} catch (ClassCastException e) {
			throw new RuntimeException("This is not a Position belonging to MyTree"); 
		}
		if (n.creator == null) throw new RuntimeException("position was allready deleted!");
		if (n.creator != this) throw new RuntimeException("position belongs to another MyTree instance!");			
		return n;
	}

	@Override
	public Position<E> root() {
		return root;
	}

	@Override
	public Position<E> createRoot(E o) {
		if (size!=0) throw new RuntimeException("only valid for empty tree!");
		root = new TNode();
		root.elem = o;
		size++;
		return root;
	}

	@Override
	public Position<E> parent(Position<E> child) {
		return checkAndCast(child).parent;
	}

	@Override
	public Iterator<Position<E>> childrenPositions(Position<E> parent) {
		return new Iterator<Position<E>>(){
			Position<TNode> current = (Position<TNode>)checkAndCast(parent).children.first();
			@Override
			public boolean hasNext() {
				return current!=null;
			}

			@Override
			public TNode next() {
				Position<TNode> ret = current;
				current = ((TNode)parent).children.next(ret);
				return ret.element();
			}};
	}

	@Override
	public Iterator<E> childrenElements(Position<E> parent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int numberOfChildren(Position<E> parent) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Position<E> insertParent(Position<E> p, E o) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Position<E> addChild(Position<E> parent, E o) {
		TNode np = checkAndCast(parent);
		TNode n = new TNode();
		n.elem = o;
		
		n.parent = np;
		size++;
		n.myChildrenPos = np.children.insertLast(n);
		return n;
	}

	@Override
	public Position<E> addChildAt(int pos, Position<E> parent, E o) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Position<E> addSiblingAfter(Position<E> sibling, E o) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Position<E> addSiblingBefore(Position<E> sibling, E o) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void remove(Position<E> p) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isExternal(Position<E> p) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isInternal(Position<E> p) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public E replaceElement(Position<E> p, E o) {
		// TODO Auto-generated method stub
		return null;
	}

	public static void main(String[] args) {
		MyTree<String> t = new MyTree<>();
		Position<String> p = t.createRoot("Buch");
		t.addChild(p,"Kapitel 1");
		Position p2 = t.addChild(p,"Kapitel 2");
		t.addChild(p2,"Kapitel 2.1");
		System.out.println(t.root.element());
		Iterator<Position<String>> it = t.childrenPositions(p2);
		while (it.hasNext()) System.out.println(it.next().element());
		
	}

}
