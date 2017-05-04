package examples;
import graphLib.*;
import graphTool.Algorithm;
import graphTool.Attribute;
import graphTool.GraphTool;

import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;
import java.util.Set;


public class GraphExamples<V,E> {	 
	// annotations for Graph tool:
	// (Argumente:  Graph, GraphTool)
	// @Algorithm
	// (Argumente:  Graph, Vertex, GraphTool)
	// @Algorithm(vertex=true) 
	// (Argumente:  Graph, Vertex, Vertex, GraphTool)
	// @Algorithm(vertex=true,vertex2=true) 
	
	@Algorithm
	public boolean isConnected(Graph g, GraphTool gt){
		Iterator<Vertex> it = g.vertices();
		if (it.hasNext()) dfs(g,it.next(),gt,1);
		while(it.hasNext()) if ( ! it.next().has(Attribute.VISITED)) return false;
		return true;
	}
	
	@Algorithm(vertex=true,vertex2=true)
	public java.util.List findPath(Graph g, Vertex from, Vertex to, GraphTool gt){
		LinkedList<Vertex> q = new LinkedList();
		from.set(Attribute.VISITED,null);
		q.addLast(from);
		boolean found=false;
		while ( ! q.isEmpty() && ! found){
			Vertex cur = q.removeFirst();
			Iterator<Edge> eIt = null;
			if (g.isDirected()) eIt = g.incidentOutEdges(cur);
			else eIt = g.incidentEdges(cur);
			while (eIt.hasNext() && ! found){
				Edge e = eIt.next();
				Vertex w = g.opposite(e, cur);
				if ( ! w.has(Attribute.VISITED)){
					e.set(Attribute.color,Color.green);
					w.set(Attribute.color,Color.green);
					w.set(Attribute.VISITED,e);
					q.addLast(w);
					gt.show(g);
					if (w == to) found = true;
				}
			}
		}
		if ( ! found) return null;
		LinkedList li = new LinkedList();
		li.addLast(to);
		while (to!=from){
			Edge e=(Edge)to.get(Attribute.VISITED);
			e.set(Attribute.color,Color.RED);
			to = g.opposite(e,to);
			gt.show(g);
			li.addLast(to);
		}
		return li;
	}
	
	@Algorithm
	public int  connectedComponents(Graph g, GraphTool gt){
		Iterator<Vertex> it = g.vertices();
		int num = 0;
		while(it.hasNext()){
			Vertex v = it.next();
			if (! v.has(Attribute.VISITED)) dfs(g,v,gt,num++);
		}
		return num;
	}

	private void dfs(Graph g, Vertex v, GraphTool gt, int num) {
		// depth first search
		// label the vertex 'v'
		v.set(Attribute.VISITED,null);
		v.set(Attribute.color,Color.GREEN);
		v.set(Attribute.string,""+num);
		gt.show(g);
		Iterator<Edge> eIt = g.incidentEdges(v);
		while (eIt.hasNext()) {
			Edge e = eIt.next();
			Vertex w = g.opposite(e, v);
			if ( ! w.has(Attribute.VISITED)){
				e.set(Attribute.color, Color.GREEN);
				dfs(g,w,gt,num);
			}
		}
	}

	/**
	 * @param args
	 * 
	 */
	public static void main(String[] args) {

		//		 make an undirected graph
		IncidenceListGraph<String,String> g = 
				new IncidenceListGraph<>(false);
		GraphExamples<String,String> ge = new GraphExamples<>();
		Vertex vA = g.insertVertex("A");
		vA.set(Attribute.name,"A");
		Vertex vB = g.insertVertex("B");
		vB.set(Attribute.name,"B");
		Vertex vC = g.insertVertex("C");
		vC.set(Attribute.name,"C");
		Vertex vD = g.insertVertex("D");
		vD.set(Attribute.name,"D");
		Vertex vE = g.insertVertex("E");
		vE.set(Attribute.name,"E");
		Vertex vF = g.insertVertex("F");
		vF.set(Attribute.name,"F");
		Vertex vG = g.insertVertex("G");
		vG.set(Attribute.name,"G");

		Edge e_a = g.insertEdge(vA,vB,"AB");
		Edge e_b = g.insertEdge(vD,vC,"DC");
		Edge e_c = g.insertEdge(vD,vB,"DB");
		Edge e_d = g.insertEdge(vC,vB,"CB");
		Edge e_e = g.insertEdge(vC,vE,"CE");
		e_e.set(Attribute.weight,2.0);
		Edge e_f = g.insertEdge(vB,vE,"BE"); 
		e_f.set(Attribute.weight, 7.0); 
		Edge e_g = g.insertEdge(vD,vE,"DE");
		Edge e_h = g.insertEdge(vE,vG,"EG");
		e_h.set(Attribute.weight,3.0);
		Edge e_i = g.insertEdge(vG,vF,"GF");
		Edge e_j = g.insertEdge(vF,vE,"FE");
		
		// use graph Tool
		
		GraphTool t = new GraphTool(g,ge);

		//    A__B     F
		//      /|\   /|
		//     / | \ / |
		//    C__D__E__G   
		//    \     /
		//     \___/
		//  
		System.out.println(g);
	}
}
