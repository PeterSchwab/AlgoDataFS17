/*
 * Project GrahTool
 * Copyright (c) 2015 by Tina Gerber, Daria Schumacher
 * Berner Fachhochschule, Switzerland
 */
package graphTool;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import graphLib.Decorable;
import graphLib.Edge;
import graphLib.Graph;
import graphLib.Vertex;

/**
 * the class GraphComponent represents rhe graphical view of the graph
 * @param <V> Vertex
 * @param <E> Edge
 */
public class GraphComponent<V,E> extends JComponent{

	private Graph<V,E> graph;
	public static double width=20.0;
	private HashMap<Vertex<V>, Ellipse2D.Double> vertices = new HashMap<>();
	private HashMap<Edge<E>, Line2D.Double> edges=new HashMap<>();
	private GraphView<V,E> graphView;
	private Line2D.Double unfinishedLine=null;
	private boolean nameFlag=false, weightFlag=false, stringFlag=false;
	private JCheckBoxMenuItem name, weight, string;
	private double zoomSize=2.5;
	private JMenuItem rename;
	private ItemListener nameListener, weightListener, stringListener;
	Dimension myDimension=new Dimension(800, 800);

	/**
	 * constructor
	 * @param gv GraphView
	 * @param renameListener ActionListener to rename a vertex or to change the weight of an edge
	 */
	public GraphComponent(GraphView<V,E> gv, ActionListener renameListener){

		this.graphView=gv;
		createListeners(renameListener);
	}

	/**
	 * creates foreach Vertex an Ellipse and foreach Edge a Line
	 * considers the zoomsize by calculating the right size
	 * @param g Graph
	 */
	public void setGraph(Graph<V,E> g){
		graph=g;
		vertices.clear();
		edges.clear();
		double x,y, maxx=0,maxy=0;
		Iterator<Vertex<V>> itv=graph.vertices();
		Iterator<Edge<E>> ite=graph.edges();
		Vertex<V> v;
		Edge<E> e;
		Vertex<V>[] ver;
		while(itv.hasNext()){
			v=(Vertex<V>) itv.next();

			x=(double)v.get(Attribute.pos_x)*zoomSize;
			y=(double)v.get(Attribute.pos_y)*zoomSize;
			vertices.put(v, new Ellipse2D.Double(x,y,width*zoomSize,width*zoomSize));

			if(x>maxx)
				maxx=x;
			if(y>maxy)
				maxy=y;
		}
		while(ite.hasNext()){
			e=(Edge<E>)ite.next();
			ver=g.endVertices(e);
			this.setLine(ver[0], ver[1], e);
		}

		myDimension=new Dimension((int)(maxx+width*zoomSize)+10, (int)(maxy+width*zoomSize)+10);
		this.setSize(myDimension);
		repaint();

	}

	/**
	 * paints all vertices and edges
	 * and arrows and attributes if needed
	 */
	public void paintComponent(Graphics g){
		Graphics2D g2=(Graphics2D)g;
		AffineTransform saved = g2.getTransform();
		Vertex<V> v;
		Edge<E> e;
		Ellipse2D.Double ellipse;
		Line2D.Double line;
		for( Iterator<Ellipse2D.Double> itv = vertices.values().iterator(); itv.hasNext(); ){
			ellipse=itv.next();
			v=this.findKey(vertices, ellipse);
			//-----------------------------------------------
			//Fills the ellipse and set the line thickness
			int red = ((Color)v.get(Attribute.color)).getRed();
			int green = ((Color)v.get(Attribute.color)).getGreen();
			int blue = ((Color)v.get(Attribute.color)).getBlue();
			Color fillColor = new Color(red, green, blue, 255/4);
			g2.setColor(fillColor);
			g2.fill(ellipse);
			g2.setStroke(new BasicStroke(2));
			//-----------------------------------------------
			g2.setColor((Color)v.get(Attribute.color));
			g2.draw(ellipse);
			if(v.has(Attribute.name)&&nameFlag){
				g2.drawString((String)v.get(Attribute.name), (int)((double)v.get(Attribute.pos_x)*zoomSize)+3, (int)(width*zoomSize/2.0+(double)v.get(Attribute.pos_y)*zoomSize));
			}
			if(v.has(Attribute.string)&&stringFlag){
				g2.drawString((String)v.get(Attribute.string), (int) ((int)((double)v.get(Attribute.pos_x)*zoomSize)+width*zoomSize)+3, (int)(width*zoomSize/2.0+(double)v.get(Attribute.pos_y)*zoomSize));
			}

		}
		for( Iterator<Line2D.Double> ite = edges.values().iterator(); ite.hasNext(); ){
			line=ite.next();
			e=this.findKey(edges, line);
			g2.setColor((Color)e.get(Attribute.color));
			g2.draw(line);
			double length = Math.sqrt(Math.pow(line.getX2()-line.getX1(), 2) + Math.pow(line.getY2()-line.getY1(), 2) );
			double angle = Math.acos((line.getX2() - line.getX1()) / length);
			if(line.getX1() > line.getX2()){
				angle = Math.toRadians(180) - angle;
				angle = line.getY1() > line.getY2() ? angle : -angle;
			}else{
				angle = line.getY1() > line.getY2() ? -angle : angle;
			}
			AffineTransform rotate=AffineTransform.getRotateInstance(angle,(int) (line.getX1()+line.getX2())/2,(int) ((line.getY1()+line.getY2())/2));
			g2.transform(rotate);

			if(e.has(Attribute.weight)&&weightFlag){
				//--------------------------------------------
				//Whether the graph comes from graphexamples or not 
				//it has to be casted differently
				if (e.get(Attribute.weight) instanceof String) {
					g2.drawString((String) e.get(Attribute.weight), 
							(int) (line.getX1()+line.getX2())/2,(int) ((line.getY1()+line.getY2())/2)-4);
				} else {
					//For graphs from graphexamples
					g2.drawString(Double.toString((Double) e.get(Attribute.weight)), 
							(int) (line.getX1()+line.getX2())/2,(int) ((line.getY1()+line.getY2())/2)-4);
				}
				//--------------------------------------------

			}
			if(e.has(Attribute.string)&&stringFlag){
				g2.drawString((String)e.get(Attribute.string),(int) (line.getX1()+line.getX2())/2,(int) ((line.getY1()+line.getY2())/2)+15);
			}
			g2.setTransform(saved);
			if(graph.isDirected())
				drawArrowHead(g2, new Point((int) line.x2, (int) line.y2),new Point((int) line.x1, (int) line.y1));

		}

		if(unfinishedLine!=null){
			g2.draw(unfinishedLine);
			if(graph.isDirected())
				drawArrowHead(g2, new Point((int) unfinishedLine.x2, (int) unfinishedLine.y2),new Point((int) unfinishedLine.x1, (int) unfinishedLine.y1));
		}



	}

	//From http://www.coderanch.com/t/340443/GUI/java/Draw-arrow-head-line
	/**
	 * draws an arrow head on the tip point
	 * @param g2 Graphics2D
	 * @param tip startpoint
	 * @param tail endpoint
	 */
	private void drawArrowHead(Graphics2D g2, Point tip, Point tail) {
		double phi = Math.toRadians(22);
		int barb = 12;
		double dy = tip.y - tail.y;
		double dx = tip.x - tail.x;
		double theta = Math.atan2(dy, dx);

		double x, y, rho = theta + phi;
		for(int j = 0; j < 2; j++) {
			x = tip.x - barb * Math.cos(rho);
			y = tip.y - barb * Math.sin(rho);
			g2.draw(new Line2D.Double(tip.x, tip.y, x, y));
			rho = theta - phi;
		}
	}

	/**
	 * calculates where the edge intersects the vertex, and set this points
	 * to creates a Line with the calculated points as start- and endpoint
	 * @param from startvertex
	 * @param to endvertex
	 * @param e Edge
	 */
	private void setLine(Vertex<V> from, Vertex<V> to, Edge<E> e){

		double radius=GraphComponent.width*zoomSize/2.0;
		double x1=(double)from.get(Attribute.pos_x)*zoomSize+radius;
		double x2=(double)to.get(Attribute.pos_x)*zoomSize+radius;
		double y1=(double)from.get(Attribute.pos_y)*zoomSize+radius;
		double y2=(double)to.get(Attribute.pos_y)*zoomSize+radius;
		double ax=1,ay=1,bx=1,by=1;
		if(x1<x2){
			bx=-1;
		}
		else{
			ax=-1;
		}

		if(y1<y2){
			by=-1;

		}
		else{
			ay=-1;
		}
		double alpha=Math.atan(Math.abs((y2-y1)/(x2-x1)));
		edges.put(e, new Line2D.Double(x1+(Math.cos(alpha)*radius)*ax,y1+(Math.sin(alpha)*radius)*ay,x2+(Math.cos(alpha)*radius)*bx,y2+(Math.sin(alpha)*radius)*by));
	}


	/*
	 * method to set the ScrollPane
	 */
	@Override 
	public Dimension getPreferredSize(){
		return myDimension;

	}

	/**
	 * search if the mouse clicked on a Decorable
	 * @param e MouseEvent
	 */
	private Decorable findDecorable(MouseEvent e){
		double distance=5.0;
		Point p=e.getPoint();
		Decorable d=null;
		Ellipse2D.Double ellipse;
		Line2D.Double line;
		for( Iterator<Ellipse2D.Double> itv = vertices.values().iterator(); itv.hasNext(); ){
			ellipse=itv.next();
			if(ellipse.contains(p))
			{
				d=this.findKey(vertices, ellipse);
				break;
			}
		}
		if(d==null){
			for( Iterator<Line2D.Double> ite = edges.values().iterator(); ite.hasNext(); ){
				line=ite.next();
				if(line.ptSegDist(p)<distance){
					d=this.findKey(edges, line);
					break;
				}
			}
		}
		return d;

	}

	/**
	 * calculates the right position, where the mouse is
	 * @param e MouseEvent
	 */
	private Point findPoint(MouseEvent e) {
		Point p=e.getPoint();
		p.setLocation(p.getX()/zoomSize,p.getY()/zoomSize);
		return p;
	}

	/**
	 * returns the decorable by handover a Line or a Ellipse
	 * @param map hashmap with a Decorable as Key and a graphical representation as value
	 * @param value graphical representation
	 * @return the Decorable which is representated by the value
	 */
	private <V1, K> K findKey(HashMap<K,V1> map, V1 value ){
		for (Entry<K, V1> entry : map.entrySet()) {
			if (entry.getValue().equals(value)) {
				return entry.getKey();
			}
		}
		return null;
	}

	/**
	 * sets a line when a Edge s not yet in the graph,
	 * but the mouse is dragging a Line from a Vertex to a Point
	 * @param p1 start point
	 * @param p2 end point
	 */
	public void insertEdge(Point p1, Point p2) {
		p1.setLocation(p1.getX()*zoomSize+width*zoomSize/2.0,p1.getY()*zoomSize+width*zoomSize/2.0);
		p2.setLocation(p2.getX()*zoomSize,p2.getY()*zoomSize);
		unfinishedLine=new Line2D.Double(p1,p2);
		repaint();
	}

	/**
	 * if an edge is inserted in the graph the Line will be cleared
	 */
	public void deleteEdge(){
		unfinishedLine=null;
		repaint();
	}

	/**
	 * sets the flag which decide if an attribut is shown or not
	 * @param attr changed attribut visibility
	 * @param selected true if the attribut should be visible
	 */
	public void setFlag(Attribute attr, boolean selected) {
		switch(attr){
		case name:
			this.nameFlag=selected;
			break;
		case weight:
			this.weightFlag=selected;
			break;
		case string: this.stringFlag=selected;
		}

	}
	
	/**
	 * sets the zoomsize of the slider
	 * @param value zoom size
	 */
	public void setZoomSize(int value) {
		this.zoomSize=value/4.0;
		this.setGraph(graph);
	}
	
	/**
	 * sets the state of the checkbox which allows the visibility of the attributs
	  * @param attr checkbox attribut
	 * @param selected true if the checkbox attribut should be visible
	 */
	public void setPopupCheckBox(Attribute attr, boolean selected) {
		switch (attr){
		case name:
			name.setState(selected);
			break;
		case weight:
			weight.setState(selected);
			break;
		case string:
			string.setState(selected);
			break;
		}

	}

	/**
	 * creates mouselistener and popuplistener
	 * @param renameListener listener for popup menuitem
	 */
	private void createListeners(ActionListener renameListener){
		JPopupMenu popupRename = new JPopupMenu();
		rename = new JMenuItem("Rename...");
		popupRename.add(rename);
		rename.addActionListener(renameListener);
		JPopupMenu popupVisible = new JPopupMenu();
		name = new JCheckBoxMenuItem("name");
		weight = new JCheckBoxMenuItem("weight");
		string = new JCheckBoxMenuItem("string");
		popupVisible.add(name);
		popupVisible.add(weight);
		popupVisible.add(string);
		nameListener=new ItemListener(){

			@Override
			public void itemStateChanged(ItemEvent e) {

				nameFlag=name.isSelected();
				graphView.setMenuCheckBox(Attribute.name, nameFlag);
				repaint();
			}

		};
		name.addItemListener(nameListener);
		weightListener= new ItemListener(){

			@Override
			public void itemStateChanged(ItemEvent e) {

				weightFlag=weight.isSelected();
				graphView.setMenuCheckBox(Attribute.weight, weightFlag);
				repaint();
			}

		};
		weight.addItemListener(weightListener);
		stringListener=new ItemListener(){

			@Override
			public void itemStateChanged(ItemEvent e) {

				stringFlag=string.isSelected();
				graphView.setMenuCheckBox(Attribute.string, stringFlag);
				repaint();
			}

		};
		string.addItemListener(stringListener);
		this.addMouseListener(new MouseAdapter(){
			@Override
			public void mousePressed(MouseEvent e){
				if(e.getButton()==MouseEvent.BUTTON1){
					Decorable d=findDecorable(e);
					graphView.mouseDown(d, findPoint(e));
				}
			}

			@Override
			public void mouseReleased(MouseEvent e){

				Decorable d=findDecorable(e);
				if(e.getButton()==MouseEvent.BUTTON1){

					graphView.mouseUp(d, findPoint(e));
				}
				if(e.getButton()==MouseEvent.BUTTON3){
					if(d!=null){
						graphView.mouseDown(d, findPoint(e));
						popupRename.show(graphView, e.getX(), e.getY());

					}
					else{
						popupVisible.show(graphView, e.getX(), e.getY());
						repaint();
					}

				}
			}
		});
		this.addMouseMotionListener(new MouseMotionAdapter(){
			@Override
			public void mouseDragged(MouseEvent e) {

				if ((e.getModifiersEx() & MouseEvent.BUTTON1_DOWN_MASK) != 0) {
					Decorable d=findDecorable(e);
					graphView.mouseDrag(d, findPoint(e));
				}
			}
		});
	}

	/**
	 * the popup widow is only visible in the editor mode. With this
	 * method you can disable the rename popup window
	 * @param b true if the rename popup window should be visible
	 */
	public void setRenameVisibility(boolean b) {
		rename.setVisible(b);
	}

}
