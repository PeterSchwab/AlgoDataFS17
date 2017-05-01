/**
 * 
 */
package examples;

import java.util.List;
import java.util.ArrayList;

/**
 * @author ps
 *
 */
public class RuckSack {
	
	int n;
	double [] bi; 
	int    [] wi;
	int    wLimit;
	double [][] b;
 	
	RuckSack(double [] b, int [] w, int wLimit){
		if (w.length != b.length) throw new RuntimeException("w and b must have equal length");
		if (wLimit < 0) throw new RuntimeException("Limit must be positive");
		n = w.length;
		this.wLimit = wLimit;
		bi = new double[n];
		wi = new int[n];
		for (int i=0;i<n;i++){
			if (b[i] > 0) bi[i]=b[i];
			else throw new RuntimeException(" benefit must be positive");
			if (w[i] > 0) wi[i]=w[i];
			else throw new RuntimeException(" weights must be positive");
		}
		this.b = new double[n][wLimit+1];
	}

	void  printTab(int len){
		String format = "%0"+len+"d.%0"+1+"d| ";
		for (int i=0;i<n;i++){
			for (int w=0;w<=wLimit;w++){
				System.out.print(String.format(format,(int)b[i][w],
						(int) ( 10* (b[i][w] - (int) b[i][w]))));
			}
			System.out.println();
		}
		
	}

	double findOpt(){
		// first line
		for (int w=1;w<=wLimit;w++){
			// hat object[0] platz?
			if (wi[0]<=w) b[0][w]=bi[0];
		}
		// b[0][0] remains 0!
		for (int i=1;i<n;i++){
			for (int w=1;w<=wLimit;w++){
				if (wi[i] > w) b[i][w]=b[i-1][w];
				else b[i][w] = Math.max(b[i-1][w], b[i-1][w-wi[i]]+bi[i]);
			}
		}
		return b[n-1][wLimit];
	}

	

	List<Integer> findConf(){
		ArrayList<Integer> al = new ArrayList<>();
		int zeil=n-1;
		int kol=wLimit;
		while(zeil>0){
			if ( b[zeil][kol] != b[zeil-1][kol]) {
				al.add(zeil);
				kol = kol-wi[zeil];
			}
			zeil = zeil-1;
		}
		if (b[0][kol]>0) al.add(0);
		return al;
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		double [] b =   {6,   5,  1,  2, 4 }; 
		int    [] w =   {5,   4,  1,  2, 3};
		//               0    1   2   3  0    
		RuckSack rs = new RuckSack(b,w,11);
		System.out.println("dynamic programming");		
		System.out.println(rs.findOpt());
		System.out.println(rs.findConf());
		rs.printTab(2);
	}

}
