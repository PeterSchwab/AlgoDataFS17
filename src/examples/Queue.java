/**
 * 
 */
package examples;
/**
 * FIFO queue
 * @author ps
 *
 * @param <E> type of the elements stored in the queue
 */
public interface Queue<E>{
	/**
	 * @param o the object to be added to the tail of this queue 
	 */
	public void enqueue(E o);
	
	/**
	 * @return and remove the element at the head of this queue 
	 */
	public E dequeue();
	
	/**
	 * @return but do not remove the element at the head of this queue 
	 */
	public E head();
	
	/**
	 * @return the number of elements currently on this queue
	 */
	public int size();
	
	/**
	 * @return true if there is no element on this queue
	 */
	public boolean isEmpty();


}
