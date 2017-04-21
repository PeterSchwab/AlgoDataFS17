package examples;


import java.util.*;

public class IntRadixSorter {
	// bucket array
	
	// Lenghth of one portion in bits:
	int len = 11;
	// number of portions:
	int anz = 32 % len == 0 ? 32/len : (32/len)+1;

	// number of buckets:
	int s = 1 << len;// (int) Math.pow(2,len);
	// s-1 has the binary representation 0..0011..11 (last 'len' bits turned on) 
	
	
	int [][] buckets = new int[s][];
	int [] cnts = new int[s];
	
	

	public void bsort(int [] a){
		System.out.println("sweeps:"+anz+", buckets: "+s+", KeyLength: "+len);
		// initialize the buckets
		for (int i=0; i<s;i++) {
			buckets[i] = new int[a.length/s];
			cnts[i] = 0; 
		}
		sort(a);
	}

	private void expand(int i){
		buckets[i] = Arrays.copyOf(buckets[i],buckets[i].length*2);
	}

	private void sort(int [] a){
		for (int d=0;d<anz;d++){ // loop over all portions of the int-number
			// fill the buckets
			for (int i=0;i<a.length;i++){
				int k=a[i];
				k = (k >>>len*d) & (s-1); // last 'len' bits 
				//k = (k >>>len*d) % s; // k is the portion nr. d of a[i]
				// put a[i] in the corresponding buckets[k]
				if (cnts[k]>=buckets[k].length) expand(k);
				buckets[k][cnts[k]++]=a[i];
			}
			int k=0;  

			// now empty all buckets (in the right order!)
			for (int i=0;i<s;i++){
				for (int j=0;j<cnts[i];j++){
					a[k++] = buckets[i][j];
				}
				cnts[i]=0;
			}
		}
	}

	public boolean check(int [] a){
		// returns true if a is sorted 
		for (int i=0;i<a.length-1;i++){
			if (a[i]>a[i+1]) return false; 
		}
		return true;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//		System.out.println(257 & 255);	
		Random rand = new Random();
		rand.setSeed(8237493);
		int n = 100000000;
		int [] a = new int[n];
		for (int i=0;i<n;i++) a[i]=rand.nextInt(n);
		IntRadixSorter ibs = new IntRadixSorter();
		System.out.println(n+" elements");
		long before = System.nanoTime();
		ibs.bsort(a);
		long after = System.nanoTime();
		System.out.println("sorted? "+ibs.check(a)+", time: "+(after-before)/1e6+" msec");

	}
}
