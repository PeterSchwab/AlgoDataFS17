package graphLib;

import java.io.Serializable;

/**
 * Classes implementing the decorable interface allow to attach 
 * attribute - value pairs to its instances.
 * @author ps
 */
public interface Decorable extends Serializable{
	
	/**
	 * retrieves the value of the attribute 'attr' an error is thrown
	 * if no such attribute is found
	 * @param attr
	 * @return the value object belonging to the attribute 'attr'
	 */
	public Object get(Object attr);
	
	/**
	 * @param attr
	 * @return true if this Decorable object has an attribute 'attr' atached
	 */
	public boolean has(Object attr);
	
	/**
	 * attaches the attribute 'attr' and its value 'val' to this Decorable
	 * @param attr
	 * @param value
	 */
	public void set(Object attr, Object val);
	
	/**
	 * removes the attribute 'attr' and its value from the list of attributes
	 * @param attr
	 * @return the value which was attached to 'attr'
	 */
	public Object destroy(Object attr);
	
	/**
	 * destroys all attached attributes
	 */
	public void clearAll();
}
