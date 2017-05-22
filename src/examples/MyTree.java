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
		TNode p = checkAndCast(parent);
		return new Iterator<Position<E>>(){
			Position<TNode> current = p.children.first();
			@Override
			public boolean hasNext() {
				return current!=null;
			}

			@Override
			public TNode next() {
				Position<TNode> ret = current;
				current = p.children.next(ret);
				return ret.element();
			}};
	}

	@Override
	public Iterator<E> childrenElements(Position<E> parent) {
		TNode p = checkAndCast(parent);
		return new Iterator<E>(){
			Position<TNode> current = p.children.first();
			@Override
			public boolean hasNext() {
				return current!=null;
			}
			@Override
			public E next() {
				Position<TNode> ret = current;
				current = p.children.next(ret);
				return ret.element().elem;
			}};
	}

	@Override
	public int numberOfChildren(Position<E> parent) {
		return checkAndCast(parent).children.size();
	}

	@Override
	public Position<E> insertParent(Position<E> p, E o) {
		TNode n = checkAndCast(p);
		TNode newP = new TNode();
		newP.elem=o;
		if (n == root){
			root = newP;
		}
		else {
			// newP takes the former role of n:
			newP.parent = n.parent;
			newP.myChildrenPos = n.myChildrenPos; // we take the position of p
			newP.parent.children.replaceElement(newP.myChildrenPos,newP);		
		}
		//make 'p' the child of the new position
		n.parent = newP;
		n.myChildrenPos = newP.children.insertFirst(n);
		size++;
		return newP;			}

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
		TNode p = checkAndCast(parent);
		if (pos > p.children.size()|| pos < 0 ) throw new RuntimeException("invalid rank");
		TNode n = new TNode();
		n.elem = o;
		n.parent = p;
		Position<TNode> linkedListPosition = null;
		if (pos == 0) linkedListPosition = p.children.insertFirst(n); 
		else if (pos == p.children.size()) linkedListPosition = p.children.insertLast(n); 
		else {
			Iterator<Position<TNode>> it = p.children.positions();
			// skip pos-2 nodes
			for (int i=0;i<pos-1;i++){
				it.next();
			}
			Position<TNode> lPos = (it.next()); // lPos is the LinkedList-position before the insertion point
			linkedListPosition = p.children.insertAfter(lPos, n);
		}
		n.myChildrenPos = linkedListPosition;
		size++;
		return n;
	}

	@Override
	public Position<E> addSiblingAfter(Position<E> sibling, E o) {
		TNode ns = checkAndCast(sibling);
		TNode n = new TNode();
		n.elem = o;
		n.myChildrenPos = ns.parent.children.insertAfter(ns.myChildrenPos, n);
		n.parent = ns.parent;
		size++;
		return n;
	}

	@Override
	public Position<E> addSiblingBefore(Position<E> sibling, E o) {
		TNode ns = checkAndCast(sibling);
		TNode n = new TNode();
		n.elem = o;
		n.myChildrenPos = ns.parent.children.insertBefore(ns.myChildrenPos, n);
		n.parent = ns.parent;
		size++;
		return n;
	}

	@Override
	public void remove(Position<E> p) {
		TNode n = checkAndCast(p);
		if (n.children.size()!=0) throw new RuntimeException("Can't remove node with children!");
		if (n==root) root = null;
		else n.parent.children.remove(n.myChildrenPos);
		size--;
		n.creator = null;
	}

	@Override
	public boolean isExternal(Position<E> p) {
		return checkAndCast(p).children.size()==0;
	}

	@Override
	public boolean isInternal(Position<E> p) {
		return checkAndCast(p).children.size()!=0;
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public E replaceElement(Position<E> p, E o) {
		TNode np = checkAndCast(p);
		E ret = np.elem;
		np.elem = o;
		return ret;
	}
	
	public int height(){
		if (root==null) return -1;
		return height(root);
	}
	
	private int height(TNode n) {
		List<TNode> cList = n.children; 
		if (cList.size()==0) return 1;
		int max = 0;
		Iterator<TNode> it = cList.elements();
		while (it.hasNext()){
			int h = height(it.next());
			if (h>max) max = h;
		}
		return max+1;
	}

	private TNode deepest;
	private int maxDepth;
	public Position<E> deepestNode(){
		if (root==null) return null;
		deepest=null;
		maxDepth=-1;
		findDeepest(root,0);
		return deepest;
	}

	private void findDeepest(TNode p, int depth) {
		if (depth>maxDepth) {
			maxDepth=depth;
			deepest = p;
		}
		List<TNode> cList = p.children; 
		Iterator<TNode> it = cList.elements();
		while (it.hasNext()) findDeepest(it.next(), depth+1);
	}
	public void print(){
		if (size != 0) print(root,"");
	}


	private void print(TNode r, String ind) {
		System.out.println(ind+r.elem);
		Iterator<TNode> it = r.children.elements();
		while (it.hasNext()) print(it.next(),ind+"-");
	}

	public static void main(String[] args) {
		MyTree<String> t = new MyTree<>();
		Position<String> p = t.createRoot("Buch");
		t.addChild(p,"Kapitel 1");
		Position p2 = t.addChild(p,"Kapitel 2");
		t.addChild(p2,"Kapitel 2.1");
		Position<String> p3 = t.addChild(p2,"Kapitel 2.2");
		t.replaceElement(p3,"Kapitel 2.3");
		t.addChildAt(1,p2,"Kapitel 2.2");
		t.addChild(p3,"Kapitel 2.3.1");
		System.out.println(t.root.element());
		Iterator<String> it = t.childrenElements(p2);
		while (it.hasNext()) System.out.println(it.next());
		System.out.println(t.height());
		System.out.println(t.deepestNode().element());
		t.print();
	}

}
