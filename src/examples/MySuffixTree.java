/**
 * 
 */
package examples;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;

/**
 * @author ps
 *
 */
public class MySuffixTree {

	
	class Node { 
		int start;
		int end;
		
		Node(int s, int e){
			start=s;
			end=e;
		}
		
		public String toString(){
			return start+":"+end;
		}
	}
	
	private MyTree<Node> tree = new MyTree<>(); 	
	private char [] t; // text of this suffix tree
	private int n; // last pos in text
//	private char [] p; // pattern
	
	public MySuffixTree(String txt){

		t = new char[txt.length()+1];
		
		txt.getChars(0,txt.length(),t,0);
		n=t.length-1;
		t[n]=0; // 		
		tree.createRoot(new Node(-1,-1)); // dummy node
		for (int i=0;i<n;i++){
			this.insertSuffix(i);
		}
	}

	public void setText(File file) throws IOException{
		FileInputStream in = null;
		int c =-1;

        try {
            in = new FileInputStream(file);
            int len = in.available();
            t = new char[len+1];
            int i=0;
            while ((c = in.read()) != -1 && i<len) {
            	char cb = (char) c;
            	// if (cb<=0 || cb>255) System.out.println("i: "+i+", cb: "+cb);
            	t[i++] = cb;          	
            }
            t[i++]=0;// stopchar
            n = t.length-1;
        } finally {
            if (in != null) {
                in.close();
            }
        }
    }
	

	public MySuffixTree(File file){
		try {
			setText(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		n = t.length-1;
		tree.createRoot(new Node(-1,-1)); // dummy node 
		for (int i=0;i<=n;i++){
			this.insertSuffix(i);
		}
	}
	
	
	private void insertSuffix(int pos){
		Position<Node> po = tree.root();
		boolean f = true; 
		while (f){
			f=false;
			Iterator<Position<Node>> it = tree.childrenPositions(po);
			while (it.hasNext()){
				Position<Node> v = it.next();
				Node n = v.element(); // child
				int i = n.start;
				if (t[i]==t[pos]){
					// we got the right node
					pos++;
					i++;
					// try to match as many chars as possible
					while (i<=n.end){
						if (t[i] != t[pos]) {
							break;
						}
						i++;
						pos++;
					}
					// did we match all ?
					if (i>n.end){
						// yes, we matched all of this node
						// so we break the while hasNext() loop and continue one level deeper
						po=v;
						f = true;
						break; // while hasNext()
					}
					else {
						// we split here
						// i points to the first pos which does not match
						Node newN = new Node(n.start,i-1);
						Position<Node> newP = tree.insertParent(v,newN);
						n.start=i;
						tree.addChild(newP, new Node(pos,this.n));// add the new branch
						return;
					}
				}
			}
		}
		// we add a new child with the rest of the suffix
		tree.addChild(po, new Node(pos,n));
	}
	
	public ArrayList<Integer> search(char[] p){
		ArrayList<Integer> al = new ArrayList<Integer>();
		Position<Node> po = tree.root();
		boolean f = true;
		int j = 0; 	// we start at this position of the pattern
		int len = p.length; // we have to match this many chars
		while (f){ // loop over all levels
			f=false;
			Iterator<Position<Node>> it = tree.childrenPositions(po);
			while (it.hasNext()){
				Position<Node> v = it.next();
				Node nd = (Node)v.element();
				int i = nd.start;
				if (t[i]==p[j]){
					// we got the right node
					int x = nd.end-i+1; // #of chars stored at this node
					// is this node shorter than the rest of the patern?
					if (len <= x){
						// yes. Either we match all chars of p or there is no match at all
						while(j<p.length-1){
							if (p[++j] != t[++i]) return al;
						}
						// we found a pattern that ends at t[i]
						//  System.out.println();
						int offset = nd.end-i+p.length-1;
						findBranchLengths(al,v,offset);
						return al;
					}
					else {
						//No. Therefore all of the chars stored
						// at this node should match and we continue one level deeper
						while(i<nd.end){
							if (p[++j] != t[++i]) return al;
						}					
						// everything of this node matched
						len = len - x;
						j++; // next j
						po=v;
						f=true;
						break; // break while hasNext() --> so we go one level deeper
					}
				}
			}
		}
		return al;
	}

	
	/**
	 * @param al
	 * @param p
	 * @param offset
	 */
	private void findBranchLengths(ArrayList<Integer> al, Position<Node> p, int offset) {
		if (tree.numberOfChildren(p)==0) al.add(this.n-offset);
		else {
			Iterator<Position<Node>> it = tree.childrenPositions(p);
			while (it.hasNext()){
				Position<Node> pc = it.next();
				Node nd = (Node)pc.element();
				int len = nd.end-nd.start+1;
				findBranchLengths(al, pc, offset+len);
			}
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		MySuffixTree st = new MySuffixTree(new File("resources/Goethe.txt"));
		
		//MySuffixTree st = new MySuffixTree("00101110011");
		                                  //0123456789012345678901
		long ts = System.currentTimeMillis();
		// st.tree.print();
		// repeat the search 1'000 times
		ArrayList<Integer> al = null;
		for (int i=0;i<1000;i++){
			al = (st.search("Würzburg".toCharArray()));
		}
		long te= System.currentTimeMillis();
		TreeSet<Integer> s = new TreeSet<>();
		s.addAll(al);
		// st.tree.print();
		System.out.println("pos: "+s+" time: "+(te-ts)+" micro s");
	}

}
