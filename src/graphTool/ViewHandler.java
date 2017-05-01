/*
 * Project GrahTool
 * Copyright (c) 2015 by Tina Gerber, Daria Schumacher
 * Berner Fachhochschule, Switzerland
 */
package graphTool;

import java.awt.Point;

import graphLib.Graph;

/**
 * The ViewHandler updates the view
 * @param <V> Vertex
 * @param <E> Edge
 */
public class ViewHandler<V,E> {

	private GraphTool<V,E> graphTool;
	private GraphFrame<V,E> frame;
	private GraphView<V,E> graphView;
	
	/**
	 * constructor
	 * @param gt GraphTool
	 */
	public ViewHandler(GraphTool<V,E> gt){
		this.graphTool=gt;
		frame=new GraphFrame<V,E>(this.graphTool);
		this.graphView=frame.getGraphView();
	}
	
	/**
	 * paints the graph
	 * @param g graph to paint
	 */
	public void setGraph(Graph<V,E> g){
		graphView.paintGraph(g);
	}

	/**
	 * deletes an unfinished line
	 */
	public void deleteEdge() {
		graphView.deleteEdge();
		
	}

	/**
	 * draws an unfinished line
	 * @param p1 start point of the line
	 * @param p2 end point of the line
	 */
	public void insertEdge(Point p1, Point p2) {
		graphView.insertEdge(p1, p2);
		
	}

	/**
	 * choose if a vertex is directed or not
	 * @return true if the graph is directed
	 */
	public boolean chooseGraphOption() {
		return frame.chooseGraphOption();
	}

	/**
	 * Resets the color of the startbutton
	 * after the animations of algorithms has finished
	 */
	public void resetStartButton() {
		frame.resetStartButton();
	}

	/**
	 * sets the redo state enabled or disabled
	 * @param enabled to enable the redostate it has to be true
	 */
	public void setRedoState(boolean enabled) {
		frame.setRedoState(enabled);
	}
	
	/**
	 * sets the undo state enabled or disabled
	 * @param enabled to enable the undostate it has to be true
	 */
	public void setUndoState(boolean enabled) {
		frame.setUndoState(enabled);
	}

	/**
	 * this metjod is called if a new graph opened
	 */
	public void newGraphOpened() {
		frame.newGraphOpened();
	}
}
