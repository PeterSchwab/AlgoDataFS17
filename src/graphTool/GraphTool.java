/*
 * Project GrahTool
 * Copyright (c) 2015 by Tina Gerber, Daria Schumacher
 * Berner Fachhochschule, Switzerland
 */
package graphTool;

import java.awt.Point;
import java.util.Iterator;
import java.util.Vector;
import java.awt.Color;
import java.io.IOException;
import java.lang.reflect.Method;

import graphLib.Decorable;
import graphLib.Edge;
import graphLib.Graph;
import graphLib.IncidenceListGraph;
import graphLib.Vertex;

import examples.GraphExamples;

/**
 * This class manages the graph
 * @param <V> Vertex
 * @param <E> Edge
 */
public class GraphTool<V,E> {

	private int nameIndex=1;
	private Graph<V, E> currentGraph;
	public static Color STANDARD = Color.BLACK;
	public static Color SELECTED = Color.BLUE;
	private GraphSerializer<V, E> graphSerializer;
	private AnnotationParser<V,E> parser;
	private ViewHandler<V,E> viewHandler;
	private int stepDelay = 1000;

	/**
	 * @return the stepDelay
	 */
	public int getStepDelay() {
		return stepDelay;
	}

	/**
	 * @param stepDelay the stepDelay to set
	 */
	public void setStepDelay(int stepDelay) {
		this.stepDelay = stepDelay;
	}

	/**
	 * constructor without graph
	 * @param ge GraphExamples
	 */
	public GraphTool(GraphExamples<V,E> ge){
		this(new IncidenceListGraph<V,E>(), ge);
		if(!viewHandler.chooseGraphOption())
			this.createGraph(false);
		else
			this.createGraph(true);
	}

	/**
	 * constructor with graph
	 * @param g Graph to draw
	 * @param ge GraphExamples
	 */
	public GraphTool(Graph<V,E> g, GraphExamples<V,E> ge){

		currentGraph=g;
		this.calculatePositions(currentGraph);
		parser = new AnnotationParser<V,E>(ge, this);
		graphSerializer = new GraphSerializer<V, E>();
		viewHandler=new ViewHandler<V,E>(this);
		viewHandler.setGraph(currentGraph);
		serializeEditorGraph();
	}

	/**
	 * returns currentGraph
	 * @return current Graph
	 */
	public Graph<V,E> getCurrentGraph() {
		return currentGraph;
	}

	//------------------------------------------------------------------------------------//
	// Methods for drawing a graph
	//------------------------------------------------------------------------------------//

	/**
	 * creates a new Graph
	 * if the param directed is true, the graph is directed
	 * @param directed to create a directed graph, it has to be true
	 */
	public void createGraph(boolean directed){
		currentGraph=new IncidenceListGraph<V, E>(directed);
		graphSerializer.clearEditorGraphs();
		nameIndex=1;
		viewHandler.setGraph(currentGraph);
		serializeEditorGraph();
	}

	/**
	 * calculate the position of for vertices so that the vertices are arranged in a circle
	 * @param g current graph
	 */
	private void calculatePositions(Graph<V, E> g) {

		double number=g.numberOfVertices();
		double radius=number*10;
		Iterator<Vertex<V>> it =g.vertices();
		int i=0;
		Vertex<V> v;

		while(it.hasNext()){
			v=it.next();
			double x=radius*Math.cos(i/number*2.0*Math.PI)+radius;
			double y=radius*Math.sin(i/number*2.0*Math.PI)+radius;
			v.set(Attribute.pos_x, x);
			v.set(Attribute.pos_y, y);
			v.set(Attribute.color, STANDARD);
			i++;
		}
		Iterator<Edge<E>> itE=g.edges();
		Edge<E> e;
		while(itE.hasNext()){
			e=itE.next();
			e.set(Attribute.color, STANDARD);
			if(!e.has(Attribute.weight))
				e.set(Attribute.weight, 1.0);
		}
	}

	/**
	 * inserts a new vertex into the graph
	 * @param p position for the new vertex
	 * @return a new vertex
	 */
	public Vertex<V> insertVertex(Point p){

		Vertex<V> v=currentGraph.insertVertex((V) "");
		double radius = GraphComponent.width/2.0;
		v.set(Attribute.pos_x, p.getX()-radius);
		v.set(Attribute.pos_y, p.getY()-radius);
		v.set(Attribute.color, STANDARD);
		v.set(Attribute.name, Integer.toString(nameIndex));
		nameIndex++;
		// graph speichern
		viewHandler.setGraph(currentGraph);
		serializeEditorGraph();
		return v;
	}

	/**
	 * moves a vertex to another position
	 * @param v vertex to move
	 * @param p vertex has to move to this point
	 */
	public void moveVertex(Vertex<V> v, Point p){
		double radius = GraphComponent.width/2.0;
		double x=p.getX();
		double y=p.getY();
		v.set(Attribute.pos_x, x-radius);
		v.set(Attribute.pos_y, y-radius);

		// Graph speichern
		viewHandler.setGraph(currentGraph);
	}

	/**
	 * inserts an unfinished line
	 * @param startVertex start point of the line
	 * @param p2 end point of the line
	 */
	public void insertEdge(Vertex<V> startVertex, Point p2) {

		Point p1=new Point();
		p1.setLocation((double)startVertex.get(Attribute.pos_x),(double)startVertex.get(Attribute.pos_y));
		viewHandler.insertEdge(p1, p2);
	}

	/**
	 * inserts a new edge into the graph
	 * @param from start vertex
	 * @param to end vertex
	 */
	public void insertEdge(Vertex<V> from, Vertex<V> to) {

		Edge<E> e_from;
		if(currentGraph.isDirected()){
			for(Iterator<Edge<E>>it1=currentGraph.incidentInEdges(to);it1.hasNext();){
				e_from=it1.next();
				for(Iterator<Edge<E>>it2=currentGraph.incidentOutEdges(from);it2.hasNext();){
					if(e_from.equals(it2.next())){
						viewHandler.deleteEdge();
						viewHandler.setGraph(currentGraph);
						return;
					}
				}
			}
		}
		else{
			for(Iterator<Edge<E>>it1=currentGraph.incidentEdges(from);it1.hasNext();){
				e_from=it1.next();
				for(Iterator<Edge<E>>it2=currentGraph.incidentEdges(to);it2.hasNext();){
					if(e_from.equals(it2.next())){
						viewHandler.deleteEdge();
						viewHandler.setGraph(currentGraph);
						return;
					}
				}
			}
		}
		Edge<E> e=currentGraph.insertEdge(from, to, (E) "");
		e.set(Attribute.color, STANDARD);
		e.set(Attribute.weight, 1.0);
		viewHandler.deleteEdge();
		viewHandler.setGraph(currentGraph);
		serializeEditorGraph();
	}

	/**
	 * deletes the unfinished line
	 */
	public void deleteEdge(){
		viewHandler.deleteEdge();
	}

	/**
	 * if a vertex is selected, the color changes
	 * with this method a decorable change his color
	 * @param d decorable to change the color
	 * @param c new color
	 */
	public void setColor(Decorable d, Color c){
		d.set(Attribute.color, c);
		viewHandler.setGraph(currentGraph);
	}

	/**
	 * removes a Vertex from the graph
	 * @param the vertex to remove
	 */
	public void deleteVertex(Vertex<V> selected) {	
		currentGraph.removeVertex(selected);
		viewHandler.setGraph(currentGraph);
		serializeEditorGraph();
	}

	/**
	 * removes an Edge from the graph
	 * @param the edge to remove
	 */
	public void deleteEdge(Edge<E> selected) {
		currentGraph.removeEdge(selected);
		viewHandler.setGraph(currentGraph);
		serializeEditorGraph();
	}

	/**
	 * changes an attribut of a decorable
	 * @param <V1>
	 * @param d the decorable to change an attribut
	 * @param attr attribut to change
	 * @param text new content of the attribut
	 */
	public <V1> void changeAttribut(Decorable d, Attribute attr, V1 text){	
		d.set(attr, text);
		viewHandler.setGraph(currentGraph);
		serializeEditorGraph();
	}

	//------------------------------------------------------------------------------------//
	// Methods for creating a new graph, saving and loading a graph
	//------------------------------------------------------------------------------------//

	/**
	 * saves the graph with the given name
	 * @param name given name
	 * @throws IOException
	 */
	public void saveGraph(String name) throws IOException {
		graphSerializer.saveGraph(name, currentGraph);
		viewHandler.setGraph(currentGraph);
	}

	/**
	 * opens a graph with the given name
	 * @param name filename
	 * @return the opened graph
	 * @throws IOException
	 */
	public Graph<V,E> openGraph(String name) throws IOException {
		currentGraph = graphSerializer.openGraph(name);
		viewHandler.setGraph(currentGraph);
		graphSerializer.clearEditorGraphs();
		graphSerializer.serializeEditorGraph(currentGraph);
		viewHandler.newGraphOpened();
		return currentGraph;
	}

	//------------------------------------------------------------------------------------//
	// Helper-methods for undoing and redoing an action in the graph editor
	//------------------------------------------------------------------------------------//

	/**
	 * Serializes a graph after a change was made to it
	 */
	public void serializeEditorGraph() {

		graphSerializer.serializeEditorGraph(currentGraph);
		viewHandler.setRedoState(false);
		if(graphSerializer.isUndoPossible())
			viewHandler.setUndoState(true);
		else
			viewHandler.setUndoState(false);

	}

	/**
	 * Undoes the last saved action in the graph editor
	 * if the action is possible
	 */
	public void undo() {
		if (graphSerializer.isUndoPossible()) {
			Graph<V,E> g = null;
			g = graphSerializer.undo();
			if (g != null) {
				currentGraph = g;
				viewHandler.setGraph(currentGraph);
				viewHandler.setRedoState(true);
				if(!graphSerializer.isUndoPossible())
					viewHandler.setUndoState(false);
			}
		}
	}

	/**
	 * Redoes the next saved action in the graph editor
	 * if the action is possible
	 */
	public void redo() {
		if (graphSerializer.isRedoPossible()) {

			Graph<V,E> g = null;
			g = graphSerializer.redo();

			if (g != null) {
				currentGraph = g;
				viewHandler.setGraph(currentGraph);
				viewHandler.setUndoState(true);
				if(!graphSerializer.isRedoPossible())
					viewHandler.setRedoState(false);
			}
		}	
	}

	//------------------------------------------------------------------------------------//
	// Helper-methods for executing an animating an algorithm in the algorithm editor
	//------------------------------------------------------------------------------------//

	/**
	 * returns annotated methods from GraphExamples
	 * @return annotated methods
	 */
	public Vector<Method> getAnnotatedMethods() {
		return parser.getAnnotatedMethods();
	}

	/**
	 * Executes an algorithm
	 * an returns all by the algorithm generated graphs
	 * Return value will be null, if the deserializing of the graphs fails
	 * and has to be tested for it
	 * @param method method to execute
	 * @param startVertex startvertex if needed, null otherwise
	 * @param endVertex endvertex if needed, null otherwise
	 */
	public void executeMethod(Method method, Vertex<V> startVertex, Vertex<V> endVertex) {
			graphSerializer.resetColor(currentGraph);
			parser.executeMethod(method, startVertex, endVertex);
			graphSerializer.deserializeAlgoGraphs();
			currentGraph=graphSerializer.deserializeEditorGraph();
			viewHandler.setGraph(currentGraph);
	}

	/**
	 * Get the previous graph in the algorithm animation
	 * based on the current position in the arraylist of graphs
	 */
	public void previousGraph() {
		if (graphSerializer.hasPreviousGraph()) {
			currentGraph = graphSerializer.getPreviousGraph();
			viewHandler.setGraph(currentGraph);
		}
	}

	/**
	 * Checks if the arraylist of graphs has a next element
	 * @return true if there is a next graph
	 */
	public boolean hasNextGraph() {
		return graphSerializer.hasNextGraph();
	}

	/**
	 * Get the previous graph in the algorithm animation
	 * based on the current position in the arraylist of graphs
	 */
	public void nextGraph() {
		if (graphSerializer.hasNextGraph()) {
			currentGraph = graphSerializer.getNextGraph();
			viewHandler.setGraph(currentGraph);
		} else {
			//Stops the animation when it gets to end manually
			stop(true);
		}
	}

	/**
	 * Resets the current index in the arraylist of graphs,
	 * when the algorithm animation gets stopped 
	 * @param resetGraph if it is true the first graph appears
	 */
	public void stop(boolean resetGraph){
		graphSerializer.resetAlgoIndex();
		if(resetGraph){
			currentGraph=graphSerializer.deserializeEditorGraph();
			viewHandler.setGraph(currentGraph);
		}
	}

	/**
	 * Serializes the graph after each change 
	 * an algorithm in the GraphExamples class made
	 * @param g graph to serialize
	 */
	public void show(Graph<V,E> g) {

		graphSerializer.serializeAlgoGraph(g);

	}
	
	/**
	 * Serializes the graph after each change 
	 * an algorithm in the GraphExamples class made
	 * @param g graph to serialize
	 */
	public void serializeAlgoGraph(Graph<V,E> g) {

		graphSerializer.serializeAlgoGraph(g);

	}

	/**
	 * Resets the color of the startbutton
	 * after the animations of algorithms has finished
	 */
	public void resetStartButton() {
		viewHandler.resetStartButton();
	}

}
