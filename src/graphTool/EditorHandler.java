/*
 * Project GrahTool
 * Copyright (c) 2015 by Tina Gerber, Daria Schumacher
 * Berner Fachhochschule, Switzerland
 */
package graphTool;

import java.awt.Point;

import graphLib.Decorable;

/**
 * In the editor mode this class handles the user events
 * @param <V> Vertex
 * @param <E> Edge
 */
public class EditorHandler<V,E> implements Handler<V, E> {

	public enum State{
		VERTEX,
		EDGE,
		SELECT,
		INACTIVE
	}

	private EditorState currentState;
	private VertexState<V,E> vertexState;
	private EdgeState<V,E> edgeState;
	private SelectState<V,E> selectState;
	private GraphTool<V,E> graphTool;
	
	/**
	 * constructor
	 * @param gt GraphTool
	 */
	public EditorHandler(GraphTool<V, E> gt) {
		graphTool = gt;
		this.selectState=new SelectState<V,E>(gt);
		this.vertexState=new VertexState<V,E>(gt);
		this.edgeState=new EdgeState<V,E>(gt);
		this.currentState=this.selectState;
	}


	/**
	 * sets a new state. 
	 * depending on which state is set,
	 * another method is called in the other methods of this class
	 * @param new state
	 */
	public void setState(State state){
	
		switch(state){
		case VERTEX:
			currentState=vertexState;
			clearSelected();
			break;
		case EDGE:
			currentState=edgeState;
			clearSelected();
			break;
		case SELECT:
			currentState=selectState;
			break;
		case INACTIVE:
			currentState=null;
			break;
		}	
	}


	/**
	 * calls the method mouseDown of the currentState
	 * @param d Vertex, Edge or null
	 * @param p point where the mouse was clicked
	 */
	@Override
	public void mouseDown(Decorable d, Point p){
		if(currentState!=null) 
			currentState.mouseDown(d, p);	
	}
	
	/**
	 * calls the method mouseDrag of the currentState
	 * @param d Vertex, Edge or null
	 * @param p point where the mouse was clicked
	 */
	@Override
	public  void mouseDrag(Decorable d, Point p){
		if(currentState!=null)
			currentState.mouseDrag(d, p);
	}
	
	/**
	 * calls the method mouseUp of the currentState
	 * @param d Vertex, Edge or null
	 * @param p point where the mouse was clicked
	 */
	@Override
	public  void mouseUp(Decorable d, Point p){
		if(currentState!=null)
			currentState.mouseUp(d, p);	
	}
	
	/**
	 * deletes the selected Decorable if you are in the mode SelectState
	 */
	public void deleteDecorable(){
		if(currentState!=null) {
			currentState.deleteDecorable();
		}
	}
	
	/**
	 * Undoes the last saved action
	 */
	public void undo(){
		graphTool.undo();
		
	}
	
	/**
	 * Redoes the next saved action
	 */
	public void redo(){
		graphTool.redo();
	}
	
	/**
	 * if the currentState is SelectState,
	 * the attribut of the selected Decorable will be changed
	 * @param <V1>
	 * @param text attribut
	 */
	public <V1> void changeAttribut(V1 text) {
		if(currentState!=null)
			currentState.changeAttribut(text);
	}

	/**
	 *  returns the selected Decorable
	 * @return selected Decorable
	 */
	public Decorable getSelected() {
		
		return selectState.getSelected();
	}
	
	/**
	 * clears the selection
	 */
	@Override
	public void clearSelected(){
		selectState.mouseDown(null, null);
	}
}
