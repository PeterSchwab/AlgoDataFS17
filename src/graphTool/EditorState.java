/*
 * Project GrahTool
 * Copyright (c) 2015 by Tina Gerber, Daria Schumacher
 * Berner Fachhochschule, Switzerland
 */
package graphTool;

import java.awt.Point;

import graphLib.Decorable;

/**
 * each state to edit the graph has to extend this class
 */
public abstract class EditorState {

	public abstract void mouseDown(Decorable d, Point p);
	public abstract void mouseDrag(Decorable d, Point p);
	public abstract void mouseUp(Decorable d, Point p);
	
	/**
	 * only SelectState uses this function
	 */
	public void deleteDecorable(){
		// nothing to implement
	}
	
	/**
	 * only SelectState uses this function
	 * @param <V1>
	 */

	public <V1> void changeAttribut(V1 text) {
		// TODO Auto-generated method stub
		
	}
}
