package graphLib ;

import java.io.Serializable;
import java.util.Iterator;

/**
 * A graph consists of a set of vertices and a set of edges joining a pair of vertices
 * @author ps
 * @param <V> the type of the vertex elements of this graph 
 * @param <E> the type of the edge elements of this graph
 */
public interface Graph <V,E> extends Serializable{
	
	/**
	 * @return an arbitrary vertex of this graph 
	 * (or null if the graph is empty)
	 */
	public Vertex<V> aVertex();
	
	/**
	 * @return the number of vertices of this graph
	 */
	public int numberOfVertices();
	
	/**
	 * @return the number of edges of this graph
	 */
	public int NumberOfEdges();
	
	/**
	 * @return true if this is a directed graph
	 */
	public boolean isDirected();
	
	/**
	 * @return an iterator over all the vertices of this graph
	 */
	public Iterator<Vertex<V>> vertices();	
	
	/**
	 * @return an iterator over all the edgees of this graph
	 */
	public Iterator<Edge<E>> edges();	
	
	/**
	 * @param v a vertex of this graph.
	 * @return an iterator over all incident edges of 'v'
	 */
	public Iterator<Edge<E>> incidentEdges(Vertex<V> v);

	/**
	 * @param v a vertex of this graph.
	 * @return an iterator over all incoming edges of 'v' 
	 * (If the graph is not directed an error is thrown).
	 */
	public Iterator<Edge<E>> incidentInEdges(Vertex<V> v);
	
	/**
	 * @param v a vertex of this graph.
	 * @return an iterator over all outgoing edges of 'v' 
	 * (If the graph is not directed an error is thrown).
	 */
	public Iterator<Edge<E>> incidentOutEdges(Vertex<V> v);
	
	/**
	 * @param v  a vertex of this graph.
	 * @return the number of incident edges of the vertex 'v'
	 */
	public int degree(Vertex<V> v);
	
	/**
	 * @param v  a vertex of this graph.
	 * @return the number of incoming edges of the vertex 'v'
	 * (If the graph is not directed an error is thrown).
	 */
	public int inDegree(Vertex<V> v);

	/**
	 * @param v  a vertex of this graph.
	 * @return the number of outgoing edges of the vertex 'v'
	 * (If the graph is not directed an error is thrown).
	 */
	public int outDegree(Vertex<V> v);

	/**
	 * finds the other endVerdex of a edge if one 
	 * of the endVertices is supplied 
	 * @param e an edge of this graph
	 * @param v an endpoint of 'e'
	 * @return the endpoint of 'e' which is not 'v'
	 * (If 'v' is not an endpoint of 'e' an eror is thrown)
	 */
	public Vertex<V> opposite(Edge<E> e, Vertex<V> v);

	/**
	 * @param e en edge of this graph
	 * @return the origin of 'e'
	 * (If this graph is undirected an error is thrown)
	 */
	public Vertex<V> origin(Edge<E> e);
	/**
	 * @param e en edge of this graph
	 * @return the destination of 'e'
	 * (If this graph is undirected an error is thrown)
	 */
	public Vertex<V> destination(Edge<E> e);

	/**
	 * @param e en edge of this graph
	 * @return an array with the two endpoints of 'e'
	 */
	public Vertex<V> [] endVertices(Edge<E> e);

	/**
	 * @param v1  a vertex of this graph.
	 * @param v2  a vertex of this graph.
	 * @return true if there is an edge connecting 'v1' with 'v2'
	 */
	public boolean areAdjacent(Vertex<V> v1, Vertex<V> v2);

	/**
	 * @param elem the element (of type V) to be stored at the vertex to be created
	 * @return the created vertex object where 'elem' is stored
	 */
	public Vertex<V> insertVertex(V elem);

	/**
	 * inserts an edge to this graph.
	 * If the graph is undirected the names 'origin' and 'destinaion' are meaningless   
	 * @param from the origin vertex
	 * @param to the destination vertex
	 * @param elem the element to be stored at the edge to be created
	 * @return the created edge object where 'elem' is stored
	 */
	public Edge<E> insertEdge(Vertex<V> from, Vertex<V> to, E elem);

	/**
	 * @param e the edge to be removed
	 * @return the element stored at the edge 'e'
	 */
	public E removeEdge(Edge<E> e);
	
	/**
	 * removes a vertex 'v' and all edges incident to 'v'
	 * @param v the vertex to be removed
	 * @return the element stored at 'v'
	 */
	public V removeVertex(Vertex<V> v);
}
