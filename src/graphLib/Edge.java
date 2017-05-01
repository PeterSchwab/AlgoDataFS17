package graphLib;

import java.io.Serializable;
import examples.Position;

/**
 * Edges of a graph. The edge object can be retrieved with 
 * the element() method. 
 * Edges implement the Decorable interface thus allowing to store dynamically 
 * attribute - value pairs at the edges.    
 * @author ps
 * @param <E> the type of the edge elements.
 */
public interface Edge<E> extends Position<E>, Decorable,Serializable {}