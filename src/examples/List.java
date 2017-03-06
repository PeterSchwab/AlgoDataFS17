package examples;

import java.util.Iterator;


/**
 * @author ps
 *
 * @param <E> the type of the elements of this list
 */
public interface List<E>{
	/**
	 * @return the first position of this list or 'null'
	 */
	public Position<E> first();
	
	/**
	 * @return the last position of this list or 'null'
	 */
	public Position<E> last();
	
	/**
	 * @param p a position of this list
	 * @return true if 'p' is the first position of this list
	 */
	public boolean isFirst(Position<E> p);
	
	/**
	 * @param p a position of this list
	 * @return true if 'p' is the last position of this list
	 */
	public boolean isLast(Position<E> p);
	
	/**
	 * @param p a position of this list
	 * @return the positionj after 'p' or 'null'
	 */
	public Position<E> next(Position<E> p);
	
	/**
	 * @param p a position of this list
	 * @return the position before 'p' or 'null
	 */
	public Position<E> previous(Position<E> p);
	
	/**
	 * @param p the poition where the element will be replaced 
	 * @param o the new object to replace the former object at 'p'
	 * @return the former object stored at 'p'
	 */
	public E replaceElement(Position<E> p, E o);
	
	/**
	 * @param o the object which will be inserted before position 'p'
	 * @return the new Position object where 'o' is stored 
	 */
	public Position<E> insertFirst(E o);
	
	/**
	 * @param o the object which will be inserted after the last position
	 * @return the new Position object where 'o' is stored
	 */
	public Position<E> insertLast(E o);
	
	/**
	 * @param p the position which will be after the element to be stored
	 * @param o the object to be stored before the position 'p'
	 * @return the new position containing the object 'o'
	 */
	public Position<E> insertBefore(Position<E> p, E o);
	
	/**
	 * @param p the position which will be before the element to be stored
	 * @param o the object to be stored after the position 'p'
	 * @return the new position containing the object 'o'
	 */
	public Position<E> insertAfter(Position<E> p, E o);
	
	/**
	 * @param p the position to be removed from this list
	 */
	public void remove(Position<E> p);

	/**
	 * @return an iterator over all positions of this list in proper order
	 */
	public Iterator<Position<E>> positions();
	
	/**
	 * @return an iterator over all objects stored in this list in proper order
	 */
	public Iterator<E> elements();
	
	/**
	 * @return the number of elements currently in this list
	 */
	public int size();
	
	/**
	 * @return true if there is no element in this list
	 */
	public boolean isEmpty();

	
}
