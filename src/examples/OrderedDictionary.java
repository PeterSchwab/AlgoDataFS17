package examples;

import java.util.Iterator;

/**
 * Orderer Dictionary based on Locators which allows to store key - element pairs
 * The order of keys is stable (equal keys keep their order).
 * @author ps
 *
 * @param <K> The type of the keys which has to extend a comparable type
 * @param <E> The type of the elements stored at this dictionary
 */
public interface OrderedDictionary<K extends Comparable<? super K>,E> {
	
	
	/**
	 * @return the size (number of stored locators) of this dictionary object
	 */
	public int size();

	/**
	 * @param key
	 * @return a Locator object with its key equal to 'key' or 
	 * null if there is no such locator in this dictionary (if there
	 * are several equal keys all others can be retrieved with the 
	 * 'next' method )
	 */
	public Locator<K,E> find(K key); // returns null if key not present

	/**
	 * @param key
	 * @return all Locator objects with its key equal to 'key' or 
	 * an arry of size 0 if there is no such locator in this dictionary 
	 */
	public Locator<K,E>[] findAll(K key); 

	/**
	 * @param key (not necessarily unique!)
	 * @param o the object associated with 'key' 
	 * @return the Locator where the pair ('key','o') is stored 
	 */
	public Locator<K,E> insert(K key, E o); 

	/**
	 * @param loc the (valid) locator to be removed from this dictionary
	 */
	public void remove(Locator<K,E> loc); 
	
	/**
	 * @param key
	 * @return a locator with the largegst key smaller than 'key' is returned
	 * (can be used to iterate over all locators)
	 */
	public Locator<K,E> closestBefore(K key); 
	/**
	 * @param key
	 * @return a locator with the smallest key larger than 'key' is returned
	 * (can be used to iterate over all locators)
	 */
	public Locator<K,E> closestAfter(K key);
	/**
	 * @param loc
	 * @return the next Locator in the order (can be used to iterate over all elements)
	 */
	public Locator<K,E> next(Locator<K,E> loc);
	/**
	 * @param loc
	 * @return the previous Locator in the order (can be used to iterate over all elements)
	 */
	public Locator<K,E> previous(Locator<K,E> loc); 	
	/**
	 * @return the locator with  minimal key (subsequent calls
	 * of 'next' will find all of the locators)	
	 */
	public Locator<K,E> min();
	/**
	 * @return  the Locator with minimal key (subsequent calls
	 * of 'previous' will find all of the locators)
	 */
	public Locator<K,E> max();

	/**
	 * @return an Iterator on all locators of this 
	 * dictionary in sorted order;
	 */
	public Iterator<Locator<K,E>> sortedLocators();	
	
	
}
