/*
 * Project GrahTool
 * Copyright (c) 2015 by Tina Gerber, Daria Schumacher
 * Berner Fachhochschule, Switzerland
 */
package graphTool;

import java.awt.Color;
import java.awt.Point;

import graphLib.Decorable;
import graphLib.Edge;
import graphLib.Vertex;

/**
 * This class controls the events of the GraphView if the selectstate is chosen
 * @param <V> Vertex
 * @param <E> Edge
 */
public class SelectState<V,E> extends EditorState {

	private GraphTool<V, E> graphTool;
	private Decorable selected;
	private Point selectedPoint;
	private Color oldColor;

	/**
	 * constructor
	 * @param g GraphTool
	 */
	public SelectState(GraphTool<V,E> g){
		this.graphTool=g;
	}
	
	/** if a decorable is clicked, it will change the color
	 * @param d a vertex or an edge
	 * @param p point where the mouse was clicked
	 */
	@Override
	public void mouseDown(Decorable d, Point p) {

		if(selected!=null){
			graphTool.setColor(selected, oldColor);
			selected=null;
		}

		if(d!=null){
			selected=d;
			oldColor=(Color) d.get(Attribute.color);
			graphTool.setColor(d, GraphTool.SELECTED);
			selectedPoint=p;
		}

	}


	/**
	 * moves the selected Vertex to his new position
	 * @param d vertex to move
	 * @param p new position
	 */
	@Override
	public void mouseDrag(Decorable d, Point p) {

		moveVertex(p);
	}

	private boolean moveVertex(Point p){
		if(selected instanceof Vertex){

			if(!(Math.abs(p.getX()-selectedPoint.getX())<=0.5&&Math.abs(p.getY()-selectedPoint.getY())<=0.5)){
				graphTool.moveVertex((Vertex<V>)selected, p);
				return true;
			}
		}
		return false;
	}
	
	/**
	 * moves the selected Vertex to his new position and serializes the graph
	 * @param d vertex to move
	 * @param p point where the mouse was clicked
	 */
	@Override
	public void mouseUp(Decorable d, Point p) {

		if(moveVertex(p))
			graphTool.serializeEditorGraph();
	}

	/**
	 * deletes an edge or a vertex
	 */
	@Override
	public void deleteDecorable(){
		if(selected!=null){
			if(selected instanceof Vertex){
				graphTool.deleteVertex((Vertex<V>)selected);
			}
			else{
				graphTool.deleteEdge((Edge<E>)selected);
			}
			selected=null;
		}
	}

	/**
	 * changes the name of a vertex or the weight of an edge
	 * @param <V1>
	 * @param text the nex text
	 */
	@Override
	public <V1> void changeAttribut(V1 text){
		if(selected instanceof Vertex){
			graphTool.changeAttribut(selected,Attribute.name, text);
		}
		if(selected instanceof Edge){
			graphTool.changeAttribut(selected,Attribute.weight, text);
		}
	}

	/**
	 * returns the selected decorable
	 * @return selected decorable
	 */
	public Decorable getSelected() {

		return selected;
	}
}
