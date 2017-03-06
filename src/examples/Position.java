/**
 * 
 */
package examples;

import java.io.Serializable;

/**
 * @author ps
 * Position ojects give the user immediate access to an object stored in a
 * position based data collection. Classes that implement this interface are usually 
 * auxillary classes of the concrete data collection class. 
 *
 * @param <E> the type of the objects returned by a position 
 */
public interface Position<E> extends Serializable{
	/**
	 * @return the object (of type E) stored at this position;
	 */
	public E element();
}
