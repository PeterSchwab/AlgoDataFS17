package examples;

import java.io.Serializable;

/**
 * @author ps
 *
 */
/**
 * A Locator based priority queue which alows to store key - element 
 * pairs and allows for O(log n) removal and for O(log n) key replacement
 * and insertion
 * @author ps
 *
 * @param <K> The type of the key (has to extend a comparable class) 
 * @param <E> The type of the elements stored
 */
public interface PriorityQueue<K extends Comparable<? super K>,E> extends Serializable{


		/**
		 * @return the Locator with a minimal key of this 
		 * PriorityQueue (does not remove this Locator from the PriorityQueue)
		 */
		public Locator<K,E> showMin();

		/**
		 * @return the Locator with a minimal key of this 
		 * PriorityQueue (the Locator is removed from the PriorityQueue)
		 */
		public Locator<K,E> removeMin();			

		/**
		 * @param key 
		 * @param element
		 * @return the Locator where  the ('key', 'element') pair is stored
		 */
		public Locator<K,E> insert(K key, E element);

		/**
		 * @param loc a valid locator belonging to this PriorityQueue
		 */
		public void remove(Locator<K,E> loc);

		/**
		 * @param loc 
		 * @param newKey the new key to be stored at 'loc' (the element does not change) 
		 */
		public void replaceKey(Locator<K,E> loc, K newKey );

		/**
		 * @return true if there is no (valid) locator in this PriorityQueue
		 */
		public boolean isEmpty();

		/**
		 * @return the number of locators to be stored at this PriorityQueue
		 */
		public int size();
}
