/*
 * Project GrahTool
 * Copyright (c) 2015 by Tina Gerber, Daria Schumacher
 * Berner Fachhochschule, Switzerland
 */
package graphTool;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import graphLib.Decorable;
import graphLib.Graph;

/**
 * this class shows the graphical representation of the graph
 * @param <V> Vertex
 * @param <E> Edge
 */
public class GraphView<V,E> extends JPanel {

	private GraphComponent<V,E> comp;
	private Handler<V,E> handler;
	private JCheckBoxMenuItem name, string, weight;

	public GraphView(ActionListener renameListener){

		comp = new GraphComponent<V, E>(this, renameListener);
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setViewportView(comp);

		//Changes the background of main area
		scrollPane.getViewport().setBackground(Color.white);

		this.setLayout(new BorderLayout());
		this.constructComponents();
		this.add(scrollPane, BorderLayout.CENTER);
	}

	/**
	 * sets an AlgoHandler or an EditorHandler
	 * @param handler the handler to set
	 */
	public void setHandler(Handler<V,E> handler) {
		this.handler = handler;
	}

	/**
	 * paints the graph
	 * @param currentGraph the graph to paint
	 */
	public void paintGraph(Graph<V,E> currentGraph) {

		comp.setGraph(currentGraph);
	}

	/**
	 * inserts an unfinished line
	 * @param p1 startpoint of the line
	 * @param p2 endpoint of the line
	 */
	public void insertEdge(Point p1, Point p2) {

		comp.insertEdge(p1, p2);
	}
	
	/**
	 * calls the handler's function mouseDown
	 * @param d Decorable
	 * @param p position
	 */
	public void mouseDown(Decorable d, Point p){
		handler.mouseDown(d, p);
	}
	/**
	 * calls the handler's function mouseUp
	 * @param d Decorable
	 * @param p position
	 */
	public void mouseUp(Decorable d, Point p){
		handler.mouseUp(d, p);
	}
	/**
	 * calls the handler's function mouseDrag
	 * @param d Decorable
	 * @param p position
	 */
	public void mouseDrag(Decorable d, Point p){
		handler.mouseDrag(d, p);
	}

	/**
	 * deletes the unfinished line
	 */
	public void deleteEdge() {

		comp.deleteEdge();
	}

	/**
	 * sets a flag of an attribute which decides if the attribut is visible or not
	 * @param attr the attribut
	 * @param selected the flag
	 */
	public void setFlag(Attribute attr, boolean selected) {
		comp.setFlag(attr, selected);

	}

	/**
	 * Constructs the menu for handling the attributes of vertices and edges
	 */
	private void constructComponents() {

		JSlider slider = new JSlider(1,20);
		slider.addChangeListener(new ChangeListener(){
			@Override
			public void stateChanged(ChangeEvent e) {
				comp.setZoomSize(slider.getValue());
			}
		});
		
		FlowLayout fl = new FlowLayout();
		JPanel p = new JPanel(fl);
		
		ImageIcon pzoomIcon = new ImageIcon("Images/pzoom.png");
		ImageIcon mzoomIcon = new ImageIcon("Images/mzoom.png");
		
		p.add(new JLabel(mzoomIcon));
		p.add(slider);
		p.add(new JLabel(pzoomIcon));
		
		add(p, BorderLayout.SOUTH);
	}

	/**
	 * create the name checkboxItem for the menu and returns it
	 * @return the name checkboxItem
	 */
	public JCheckBoxMenuItem getNameItem(){
		name = new JCheckBoxMenuItem("name");
		name.addItemListener(new ItemListener(){

			@Override
			public void itemStateChanged(ItemEvent e) {
				comp.setFlag(Attribute.name, name.isSelected());
				comp.setPopupCheckBox(Attribute.name, name.isSelected());
			}
		});
		return name;
	}
	
	/**
	 * create the weight checkboxItem for the menu and returns it
	 * @return the weight checkboxItem
	 */
	public JCheckBoxMenuItem getWeightItem(){
		weight = new JCheckBoxMenuItem("weight");
		weight.addItemListener(new ItemListener(){

			@Override
			public void itemStateChanged(ItemEvent e) {
				comp.setFlag(Attribute.weight, weight.isSelected());
				comp.setPopupCheckBox(Attribute.weight, weight.isSelected());
			}
		});
		return weight;
	}
	
	/**
	 * create the string checkboxItem for the menu and returns it
	 * @return the string checkboxItem
	 */
	public JCheckBoxMenuItem getStringItem(){
		string = new JCheckBoxMenuItem("string");
		string.addItemListener(new ItemListener(){

			@Override
			public void itemStateChanged(ItemEvent e) {
				comp.setFlag(Attribute.string, string.isSelected());
				comp.setPopupCheckBox(Attribute.string, string.isSelected());
			}
		});
		return string;
	}

	/**
	 * sets the state of the checkbox which allows the visibility of the attributs
	 * @param attr the attribut
	 * @param selected the visibility flag
	 */
	public void setMenuCheckBox(Attribute attr, boolean selected) {
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
	 * the popup widow is only visible in the editor mode. With this
	 * method you can disable the rename popup window
	 * @param b to enable the rename visibility it has to be true
	 */
	public void setRenameVisibility(boolean b) {
		comp.setRenameVisibility(b);
		
	}


}
