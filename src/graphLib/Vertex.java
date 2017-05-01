package graphLib;

import java.io.Serializable;
import examples.Position;

/**
 * Vertices of a graph. The vertex object can be retrieved with 
 * the element() method. 
 * Vertices implement the Decorable interface thus allowing to store dynamically 
 * attribute - value pairs at the vertices.    
 * @author ps
 *
 * @param <V> the type of the vertex elements.
 */
public interface Vertex<V> extends Position<V>, Decorable, Serializable {}