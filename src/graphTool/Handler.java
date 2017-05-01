/*
 * Project GrahTool
 * Copyright (c) 2015 by Tina Gerber, Daria Schumacher
 * Berner Fachhochschule, Switzerland
 */
package graphTool;

import java.awt.Point;

import graphLib.Decorable;

public interface Handler<V,E> {
 
	public void mouseDown(Decorable d, Point p);
	public  void mouseDrag(Decorable d, Point p);
	public  void mouseUp(Decorable d, Point p);
	public void clearSelected();
}
