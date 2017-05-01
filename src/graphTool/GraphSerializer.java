/*
 * Project GraphTool
 * Copyright (c) 2015 by Tina Gerber, Daria Schumacher
 * Berner Fachhochschule, Switzerland
 */
package graphTool;

import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Iterator;

import graphLib.Edge;
import graphLib.Graph;
import graphLib.Vertex;

/**
 * Serializes and deserializes graphs permanently and temporally
 * for saving graphs and animating algorithms
 */
public class GraphSerializer<V,E> {

	private ArrayList<byte[]> byteEditorGraphs;
	private int editorIndex = -1;
	private ArrayList<byte[]> byteAlgoGraphs;
	ArrayList<Graph<V,E>> algoGraphs;
	private int algoIndex = -1;
	private int sizeLimit = 10000;

	/**
	 * constructor
	 */
	public GraphSerializer(){
		byteEditorGraphs= new ArrayList<byte[]>();
		byteAlgoGraphs= new ArrayList<byte[]>();
		algoGraphs = new ArrayList<Graph<V,E>>();
	}
	
	//------------------------------------------------------------------------------------//
	// Methods for serialize and deserialize graphs
	//------------------------------------------------------------------------------------//
	
	/**
	 * serialize a graph and returns the serialized graph as a byte array
	 * @param g graph to serialize
	 * @return serialized graph as a byte array
	 */
	private byte[] serializeGraph(Graph<V,E> g){
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream oos = null;
		try {			
			oos = new ObjectOutputStream(bos);
			oos.writeObject(g);			
			return bos.toByteArray();
		} catch (IOException e1) {
			System.out.println("@GraphSerializer: GraphSerializer failed to serialize a graph");
			e1.printStackTrace();
		} finally {
			try {
				oos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * deserializes a graph as a byte array and returns a graph
	 * @param graphArray the serialized graph
	 * @return the deserialized graph
	 */
	public Graph<V,E> deserializeGraph(byte[] graphArray){
		ObjectInputStream ois;
		try {
			ois = new ObjectInputStream(new ByteArrayInputStream(graphArray));
			try {			
				return (Graph<V,E>) ois.readObject();
			} catch (IOException e1) {
				System.out.println("@GraphSerializer: GraphSerializer failed to deserialize a graph");
				e1.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} finally {
				ois.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;

	}
	
	//------------------------------------------------------------------------------------//
	// Methods for storing graphs permanently
	//------------------------------------------------------------------------------------//

	/**
	 * Saves a graph permanently as a file by serializing
	 * @param name filename
	 * @param graph the graph which will be saved
	 * @throws IOException
	 */
	public void saveGraph(String name, Graph<V,E> graph) throws IOException {

		resetColor(graph);

		String filename = "GraphFiles/" + name + ".ser";
		ObjectOutputStream oos = null;
		try {			
			oos = new ObjectOutputStream(new FileOutputStream(new File(filename)));	
			oos.writeObject(graph); 
		} catch (IOException e1) {
			System.out.println("@GraphSerializer: GraphSerializer failed to serialize a graph");
			e1.printStackTrace();
		} finally {
			oos.close();
		}
	}

	/**
	 * Opens a saved graph from a file by deserializing
	 * @param name filename
	 * @return a graph
	 * @throws IOException
	 */
	public Graph<V,E> openGraph(String name) throws IOException {
		String filename = "GraphFiles/" + name + ".ser";
		Graph<V,E> graph = null;
		ObjectInputStream ois = null;
		try {
			ois = new ObjectInputStream(new FileInputStream(filename));
			graph = (Graph<V,E>) ois.readObject();
		} catch (IOException e) {
			System.out.println("@GraphSerializer: GraphSerializer failed to deserialize a graph");
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			System.out.println("@GraphSerializer: GraphSerializer failed to deserialize a graph");
			e.printStackTrace();
		} finally {
			ois.close();
		}
		return graph;
	}
	
	/**
	 * changes the color of all vertices and edges into the standardcolor
	 * @param graph the graph which should have the standard color
	 */
	public void resetColor(Graph<V,E> graph){
		Iterator<Vertex<V>> vIt = graph.vertices();
		while (vIt.hasNext()) {
			vIt.next().set(Attribute.color, GraphTool.STANDARD);
		}

		Iterator<Edge<E>> eIt = graph.edges();
		while (eIt.hasNext()) {
			eIt.next().set(Attribute.color, GraphTool.STANDARD);
		}
	}
	
	//------------------------------------------------------------------------------------//
	// Methods for coping graph temporarily in the graph editor
	//------------------------------------------------------------------------------------//

	/**
	 * Makes a temporary copy of a graph by serializing
	 * For undoing and redoing actions in the graph editor
	 * @param g the graph to serialize
	 */
	public void serializeEditorGraph(Graph<V, E> g) {

		Graph<V,E> graph=deserializeGraph(serializeGraph(g));
		resetColor(graph);
		if(editorIndex!=byteEditorGraphs.size()){
			for(int i=byteEditorGraphs.size()-1;i>editorIndex;i--){
				byteEditorGraphs.remove(i);
			}
		}
		editorIndex++;
		byteEditorGraphs.add(editorIndex, serializeGraph(graph));
	}

	/**
	 * Deserializes all temporary copies of the graphs
	 * For undoing and redoing actions in the graph editor
	 * @return null, if the graph doesn't exist, otherwise the graph
	 */
	public Graph<V,E> deserializeEditorGraph() {

		return deserializeGraph(byteEditorGraphs.get(editorIndex));
	}

	/**
	 * Checks if the arraylist of graphs has a previous element,
	 * going from the editorIndex
	 * @return true if undo is possible
	 */
	public boolean isUndoPossible() {
		if (editorIndex <= 0) return false;
		else return true;
	}

	/**
	 * Returns the previous element in the arraylist of graphs
	 * If the previous element exists, has to be checked first
	 * Returns null, if the graph doesn't exist
	 * @return the deserialized graph
	 */
	public Graph<V,E> undo() {
		if(isUndoPossible())editorIndex--;
		return deserializeEditorGraph();
	}

	/**
	 * Checks if the arraylist of graphs has a next element,
	 * going from the editorIndex
	 * @return true if redo is possible
	 */
	public boolean isRedoPossible() {
		if (editorIndex >= byteEditorGraphs.size()-1) return false;
		else return true;
	}

	/**
	 * Returns the next element in the arraylist of graphs
	 * If the next element exists, has to be checked first
	 * Returns null, if the graph doesn't exist
	 * @return the deserialized graph
	 */
	public Graph<V,E> redo() {
		if (isRedoPossible()) editorIndex++;
		return deserializeEditorGraph();
	}

	/**
	 * Resets the byte array and the index
	 * for saving graphs for undoing and redoing actions
	 */
	public void clearEditorGraphs() {
		byteEditorGraphs.clear();
		editorIndex = -1;
	}

	//------------------------------------------------------------------------------------//
	// Methods for coping graph temporarily in the algorithm editor
	//------------------------------------------------------------------------------------//

	/**
	 * Makes a temporary copy of a graph by serializing
	 * For animating an algorithm in the algorithm editor
	 * @param g the graph to serialize
	 */
	public void serializeAlgoGraph(Graph<V, E> g) {
		if (byteAlgoGraphs.size()>sizeLimit) throw new RuntimeException("Too many algo steps. Maybe this algo is looping");
		byteAlgoGraphs.add(serializeGraph(g));
	}

	/**
	 * Deserializes all temporary copies of the graphs
	 * For animating an algorithm in the algorithm editor
	 */
	public void deserializeAlgoGraphs() {

			//Clears the list of deserialized graphs
			algoGraphs.clear();
			algoIndex = -1;
			for (int i = 0; i < byteAlgoGraphs.size(); i++) {
				algoGraphs.add(deserializeGraph(byteAlgoGraphs.get(i)));
			}
			//Clears the list of serialized graphs
			byteAlgoGraphs.clear();

	}

	/**
	 * Checks if the arraylist of graphs has a next element,
	 * going from the algoIndex
	 * @return true if there is a next graph
	 */
	public boolean hasNextGraph() {
		if (algoIndex >= algoGraphs.size()-1) return false;
		else return true;
	}

	/**
	 * Returns the next element in the arraylist of graphs
	 * If the next element exists, has to be checked first
	 * @return the next graph
	 */
	public Graph<V,E> getNextGraph() {
		algoIndex++;
		return algoGraphs.get(algoIndex);
	}

	/**
	 * Checks if the arraylist of graphs has a previous element,
	 * going from the algoIndex
	 * @return true if there is a previous graph
	 */
	public boolean hasPreviousGraph() {
		if (algoIndex <= 0) return false;
		else return true;
	}

	/**
	 * Returns the previous element in the arraylist of graphs
	 * If the previous element exists, has to be checked first
	 * @return the previous graph
	 */
	public Graph<V,E> getPreviousGraph() {
		algoIndex--;
		System.out.println("algoIndex"+algoIndex);
		return algoGraphs.get(algoIndex);
	}

	/**
	 * resets the index
	 * of graphs for animating algorithms
	 */
	public void resetAlgoIndex() {
		algoIndex=-1;
	}
}
