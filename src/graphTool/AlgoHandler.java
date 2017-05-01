/*
 * Project GrahTool
 * Copyright (c) 2015 by Tina Gerber, Daria Schumacher
 * Berner Fachhochschule, Switzerland
 */
package graphTool;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Method;
import java.util.Vector;

import javax.swing.Timer;




import graphLib.Decorable;
import graphLib.Vertex;

/**
 * This class handles the algorithm animation
 * @param <V> Vertex
 * @param <E> Edge
 */
public class AlgoHandler<V,E> implements Handler<V,E> {

	private GraphTool<V,E> graphTool;
	private SelectState<V,E> selectState;
	private Vertex<V> startVertex;
	private Vertex<V> endVertex;
	private boolean algoExecuted=false;
	private Timer t;
	private Method currentAlgoMethod;
	private boolean startVertexSelected=false, endVertexSelected=false;

	/**
	 * constructor
	 * @param gt Graphtool
	 */
	public AlgoHandler(GraphTool<V, E> gt) {
		selectState = new SelectState<V,E>(gt);
		this.graphTool=gt;
		//Timer for animating an algorithm
		t = new Timer(gt.getStepDelay(), new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				if (graphTool.hasNextGraph()) graphTool.nextGraph();
				else stopAfterAlgoAnimation();
			}
		});
	}


	/**
	 * selects a vertex
	 * @param d vertex
	 * @param p point where the mouse was clicked
	 */
	@Override
	public void mouseDown(Decorable d, Point p) {
		//Only vertices can be selected
		if (d instanceof Vertex && !t.isRunning()) selectState.mouseDown(d, p);
		else selectState.mouseDown(null, null);
	}

	/**
	 * not used
	 */
	@Override
	public void mouseDrag(Decorable d, Point p) {
		// TODO Auto-generated method stub

	}

	/**
	 * not used
	 */
	@Override
	public void mouseUp(Decorable d, Point p) {
		// TODO Auto-generated method stub

	}


	/**
	 * Clears the current selection of a vertex or edge,
	 * if user changes tab
	 */
	public void clearSelected() {
		selectState.mouseDown(null, null);
	}

	//------------------------------------------------------------------------------------//
	// Methods for controlling the animating of an algorithm
	//------------------------------------------------------------------------------------//


	/**
	 * Gets all annotated methods from the AnnotationParser
	 * @return annotateted methods
	 */
	public Vector<Method> getAnnotatedMethods() {
		return graphTool.getAnnotatedMethods();
	}

	/**
	 * Executes an algorithm method
	 * and start the timer for animating it
	 * @param currentAlgoMethod the chosen algorithm
	 */
	public void executeMethod(Method currentAlgoMethod){
		this.currentAlgoMethod=currentAlgoMethod;
		graphTool.executeMethod(currentAlgoMethod, startVertex, endVertex);
		algoExecuted=true;
	}

	/**
	 * starts the timer for animating an algorithm
	 */
	public void startAlgo() {
		t.setDelay(this.graphTool.getStepDelay());
		t.start();
	}

	/**
	 * Pauses the timer for animating an algorithm
	 */
	public void pauseAlgo() {
		t.stop();
		graphTool.resetStartButton();
	}


	/**
	 * Gets the previous step in the animation of an algorithm
	 */
	public void previousAlgo() {
		graphTool.previousGraph();
	}

	/**
	 * Gets the next step in the animation of an algorithm
	 */
	public void nextAlgo() {
		graphTool.nextGraph();
	}

	/**
	 * Stops the animation of an algorithm
	 * and stops the timer for animating an algorithm
	 * after the animation gets stopped manually
	 */
	public void stopAlgo() {
		if (t.isRunning()) {
			t.stop();
			graphTool.resetStartButton();
		}
		graphTool.stop(true);
	}

	/**
	 * Stops the animation of an algorithm
	 * and stops the timer for animating an algorithm
	 * after the automatic animation is run through
	 */
	public void stopAfterAlgoAnimation() {
		if (t.isRunning()) {
			t.stop();
			graphTool.stop(false);
			graphTool.resetStartButton();
		}
	}

	/**
	 * Changes the delay of the timer
	 * for animating an algorithm to a given time
	 */
	public void setTimerTime(int time) {
		t.setDelay(time);
	}

	//------------------------------------------------------------------------------------//
	// Methods for controlling the startvertex and endvertex of an algorithm
	//------------------------------------------------------------------------------------//

	/**
	 * Gets the current selection from the AnnotationParser
	 * and saves it as the startVertex
	 */
	public void setStartVertex() {
		Decorable d = selectState.getSelected();
		if (d instanceof Vertex){ 
			startVertex = (Vertex<V>) d;
			startVertexSelected=true;
			if(algoExecuted){
				graphTool.executeMethod(currentAlgoMethod, startVertex, endVertex);
			}


		}
		clearSelected();
	}

	/**
	 * Gets the current selection from the AnnotationParser
	 * and saves it as the endVertex
	 */
	public void setEndVertex() {
		Decorable d = selectState.getSelected();
		if (d instanceof Vertex){
			endVertex = (Vertex<V>) d;
			endVertexSelected=true;
			if(algoExecuted){
				graphTool.executeMethod(currentAlgoMethod, startVertex, endVertex);
			}
		}
		clearSelected();
	}

	/**
	 * Deletes the saved start and end vertex
	 */
	public void clearStartEndVertex() {
		startVertex = null;
		startVertexSelected=false;
		endVertex = null;
		endVertexSelected=false;
		algoExecuted=false;
	}
	/**
	 * returns if a startvertex is selected
	 * @return true if a startvertex is selected
	 */
	public boolean isStartVertexSelected(){
		return startVertexSelected;
	}

	/**
	 * returns whether an endvertex is selected
	 * @return true if an endvertex is selected
	 */
	public boolean isEndVertexSelected(){
		return endVertexSelected;
	}

	/**
	 * returns whether the algorithm is executed
	 * @return true if the algorithm is executed
	 */
	public boolean isMethodExecuted(){
		return algoExecuted;
	}
}
