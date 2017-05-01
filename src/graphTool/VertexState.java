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
 * This class controls the events of the GraphView if the vertexstate is chosen
 * @param <V> Vertex
 * @param <E> Edge
 */
public class VertexState<V,E> extends EditorState{

	private GraphTool<V,E> graphTool;
	private Vertex<V> insertedVertex;
	
	/**
	 * constructor
	 * @param g GraphTool
	 */
	public VertexState(GraphTool<V,E> gt){
		this.graphTool=gt;
	}
	
	/** inserts a new vertex
	 * @param d not used
	 * @param p point where the mouse was clicked
	 */
	@Override
	public void mouseDown(Decorable d, Point p) {
		insertedVertex=graphTool.insertVertex(p);
	}

	/**
	 * moves the inserted vertex to a new position
	 * @param d vertex to move
	 * @param p point where the mouse was clicked
	 */
	@Override
	public void mouseDrag(Decorable d, Point p) {
		graphTool.moveVertex(insertedVertex, p);
	}
	
	/**
	 * moves the inserted vertex to a new position
	 * @param d vertex to move
	 * @param p point where the mouse was clicked
	 */
	@Override
	public void mouseUp(Decorable d, Point p) {
		graphTool.moveVertex(insertedVertex, p);
	}

}
