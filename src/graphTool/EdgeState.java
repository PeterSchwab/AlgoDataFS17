/*
 * Project GrahTool
 * Copyright (c) 2015 by Tina Gerber, Daria Schumacher
 * Berner Fachhochschule, Switzerland
 */
package graphTool;

import java.awt.Point;

import graphLib.Decorable;
import graphLib.Vertex;

/**
 * This class controls the events of the GraphView if the edgestate is chosen
 * @param <V> Vertex
 * @param <E> Edge
 */
public class EdgeState<V, E> extends EditorState {

	private GraphTool<V,E> graphTool;
	private Vertex<V> startVertex;

	/**
	 * constructor
	 * @param g GraphTool
	 */
	public EdgeState(GraphTool<V,E> g){
		this.graphTool=g;
	}
	
	/** if a Vertex is clicked, the vertex is the startvertex of the new edge
	 * @param d hopefully a startvertex
	 * @param p point where the mouse was clicked
	 */
	@Override
	public void mouseDown(Decorable d, Point p) {

		startVertex=null;
		if(d!=null){
			if(d instanceof Vertex){
				startVertex=(Vertex<V>) d;
			}
		}
	}

	/**
	 * this method calls graphtool to draw an unfinished line
	 * @param d vertex
	 * @param p point where the mouse was clicked
	 */
	@Override
	public void mouseDrag(Decorable d, Point p) {
		if(startVertex!=null)
			graphTool.insertEdge(startVertex, p);
	}

	/**
	 * if a startvertex is selected and the mouseUp is over a vertex,
	 * this method calls graphtool to insert a new edge into the graph
	 * @param d vertex
	 * @param p point where the mouse was clicked
	 */
	@Override
	public void mouseUp(Decorable d, Point p) {

		if(d!=null&&d!=startVertex&&startVertex!=null){
			if(d instanceof Vertex){
				if(!d.equals(startVertex)){
					graphTool.insertEdge(startVertex, (Vertex<V>)d);
				}

			}
		}
		else
			graphTool.deleteEdge();
	}
}
