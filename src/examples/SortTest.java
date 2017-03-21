package examples;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.ArrayList;
import java.util.Random;


/**
 * @author ps
 * Various sort programs for int arrays (exercise) 
 */
public class SortTest {
	
	public static long cnt,cnt2;
	static int [] b;
	/**
	 * @param a int aray
	 * @return 'true' if 'a' is sorted 
	 */
	public static boolean sortCheck(int[] a) {
		for (int i=0;i<a.length-1;i++){
			if (a[i]>a[i+1]) return false; 
		}
		return true;
	}	

	static public boolean heapCheck(int [] a){
	  for (int i=1;i<a.length;i++) if (a[i]>a[(i-1)/2]) return false;
	  return true;
	}
	
	static public void heapSort(int [] a){
		//for (int i=1;i<a.length;i++) upHeap(a,i);
		for (int i=a.length/2+1;i>=0;i--)downHeap(a,i,a.length);
		System.out.println("heap? "+heapCheck(a));
		// now a[0..a.length-1] is a max-heap
		for (int i=a.length-1;i>0;i--) {
			swap(a,0,i);
			downHeap(a,0,i);
		}
	}


	private static void upHeap(int[] a, int i) {
		// a[i] has to be swapped with its parent 
		// until heap condition ok
		// at the end a[0..i] is again a maxHeap
		int parent = (i-1)/2;
		while (i > 0 && a[i]>a[parent]){			
			swap(a,i,parent);
			i=parent;
			parent = (i-1)/2;
		}
	}

	private static void downHeap(int[] a, int pos, int len) {
		// a[pos] has to be swapped with its bigger child
		// until heap condition ok.
		// at the end a[pos..len-1] is again a max-subheap
		int left = pos*2+1, right = left+1;
		while (left<len){
			int biggerChild = left;
			if (right<len && a[right]>a[left]) biggerChild = right;
			if (a[pos]>=a[biggerChild]) break;
			swap(a,pos,biggerChild);
			pos = biggerChild;
			left = 2*pos+1;
			right = left+1;
		}
	}

	/**
	 * Non optimized bubble sort for an int array 
	 * @param a
	 */
	public static void bubbleSort(int[] a) {
		cnt=0;
		int m = a.length-1;
		for(int i=m; i>0; i--){ 

			for (int k=0; k < i; k++){
				if(a[k]>a[k+1]) swap(a,k,k+1);
			}
			// now a[i] is on its final position!
		}
	}

	public static void mergeSort(int [] a){
		cnt=0;
		b = new int[a.length];
		mSort(a,0,a.length-1);
	}

	private static void mSort(int[] a, int from, int to) {
		if (from==to) return;
		int med = from +(to-from)/4;
		mSort(a,from,med);
		mSort(a,med+1,to);
		merge(a,from,med,to);
	}

	private static void merge(int[] a, int from, int med, int to) {
		// System.out.println("merge: "+from+", "+med+", "+to);
		// precondition:
		// a[from..med] and a[med+1..to] are already sorted (each)
		// postcondition: 
		// a[from..to] is sorted
		int left=from,right=med+1,i=from;
		while (true){
			// finished?
			if (left>med) break;
			if (right>to) {
				// copy reminding elements from left part:
				cnt2 += (med-left);
				while (left<=med){
					b[i++]=a[left++];
				}
				break;		
			}
			// not finished. So copy the smaller of the 
			// two parts to 'b'
			if (a[left]<=a[right]) b[i++] = a[left++];
			else b[i++]=a[right++];
			//cnt2++;
		}
		//copy back from 'b' to 'a'
		cnt+=(i-from);
		while(i>from) a[--i]=b[i];
		
	}

	/**
	 * swap the array elements a[i] and a[k]
	 * @param a int array 
	 * @param i position in the array 'a'
	 * @param k position in the array 'a'
	 */
	static void swap(int [] a, int i, int k){
		int tmp=a[i];
		a[i]=a[k];
		a[k]=tmp;
		cnt++;
	}

	public static void main(String[] args) {
		long t1=0,t2=0,te1=0,te2=0,eTime=0,time=0;
		int n = 10000000;
		// we need a random generator
		Random rand=new Random();
		// rand.setSeed(8237493); // initialize always in the same state
		ThreadMXBean threadBean = ManagementFactory.getThreadMXBean();	
		// new array
		int [] a = new int[n];
		// fill it randomly
		for (int i=0;i<a.length ;i++) {
			a[i]=rand.nextInt(n);
		}

		
		cnt=0;  // for statistcs reasons
		// get Time
		te1=System.nanoTime();
		t1 = threadBean.getCurrentThreadCpuTime();
		heapSort(a);
		te2 = System.nanoTime();
		t2 = threadBean.getCurrentThreadCpuTime();
		time=t2-t1;
		eTime=te2-te1;
		System.out.println("# elements: "+n);
		System.out.println("CPU-Time usage: "+time/1000000.0+" ms");
		System.out.println("elapsed time: "+eTime/1e6+" ms");
		System.out.println("sorted? "+sortCheck(a));
//		System.out.println("swap operations needed: "+cnt);		
		System.out.println("merge compaires needed: "+(cnt-cnt2));		
		System.out.println("merge copy only needed: "+cnt2);		
		// ok
	}


}
