/*
 * Project GrahTool
 * Copyright (c) 2015 by Tina Gerber, Daria Schumacher
 * Berner Fachhochschule, Switzerland
 */
package graphTool;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Vector;

import graphLib.Graph;
import graphLib.Vertex;

// import examples.GraphExamples;

/**
 * This class locares annotated methods in the class GraphExamples
 * @param <V> Vertex
 * @param <E> Edge
 */
public class AnnotationParser<V, E> {

//	private GraphExamples<V,E> graphExamples;
	private Object graphExamples;
	private GraphTool<V,E> graphTool;
	private ArrayList<Method> annotatedMethods;
	
	/**
	 * constructor
	 * @param ge GraphExamples
	 * @param gt Graphtool
	 */
//	public AnnotationParser(GraphExamples<V,E> ge, GraphTool<V,E> gt){
	public AnnotationParser(Object ge, GraphTool<V,E> gt){
		this.graphExamples=ge;
		this.graphTool=gt;
	}
	
	/**
	 * returns annotated methods
	 * @return annotated methods
	 */
	public Vector<Method> getAnnotatedMethods(){
	
		annotatedMethods=new ArrayList<>();
		Method[] methods = graphExamples.getClass().getMethods();
		for(int i=0; i<methods.length; i++){
			if(methods[i].isAnnotationPresent(Algorithm.class)){
				annotatedMethods.add(methods[i]);
			}
		}
		return new Vector<Method>(annotatedMethods);
	}

	 /** returns whether the method needs a startvertex
	 * @param method annotated method
	 * @return true if the method needs a startvertex
	 */
	public boolean isStartvertexNeeded(Method method){
		return method.getAnnotation(Algorithm.class).vertex();
	}
	
	/** returns whether the method needs an endvertex
	 * @param method annotated method
	 * @return true if the method needs an endvertex
	 */
	public boolean isEndvertexNeeded(Method method){
		return method.getAnnotation(Algorithm.class).vertex2();
	}
	
	
	/**
	 * Executes an algorithm
	 * @param method selected method
	 * @param startVertex selected startvertex, null if it isn't needed
	 * @param endVertex selected endvertex, null if it isn't needed
	 */
	public void executeMethod(Method method, Vertex<V> startVertex, Vertex<V> endVertex){
		Graph<V,E> graph = graphTool.getCurrentGraph();
		Vertex<V> v1=startVertex, v2=endVertex;
		if(method!=null){
	
			if(!method.getAnnotation(Algorithm.class).vertex()){
				try {
					method.invoke(graphExamples, graph, graphTool);
				} catch (IllegalAccessException | IllegalArgumentException
						| InvocationTargetException e) {
					e.printStackTrace();
				}
			}
			else {
				if(v1==null) throw new NullPointerException("startvertex");
				if(!method.getAnnotation(Algorithm.class).vertex2()){
					try {
						method.invoke(graphExamples, graph, v1, graphTool);
					} catch (IllegalAccessException | IllegalArgumentException
							| InvocationTargetException e) {
						e.printStackTrace();
					}
				}else{
					if(v2==null)throw new NullPointerException("stopvertex");

					try {
						method.invoke(graphExamples, graph, v1, v2, graphTool);
					} catch (IllegalAccessException | IllegalArgumentException
							| InvocationTargetException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}
