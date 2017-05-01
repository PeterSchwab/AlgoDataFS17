/*
 * Project GrahTool
 * Copyright (c) 2015 by Tina Gerber, Daria Schumacher
 * Berner Fachhochschule, Switzerland
 */
package graphTool;

import graphTool.EditorHandler.State;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * The EditorView shows the controls to edit a graph
 * @param <V> Vertex
 * @param <E> Edge
 */
public class EditorView<V, E> extends JPanel {
	
	private EditorHandler<V,E> editorHandler;
	private ImageIcon edgeIcon;
	private ImageIcon dedgeIcon;
	JButton edgeButton;
	
	/**
	 * constructor
	 * @param handler EditorHandler
	 */
	public EditorView(EditorHandler<V,E> handler) {
		
		this.editorHandler=handler;
		setLayout(new BorderLayout());
		constructPanelComponents();
	}
	
	/**
	 * Constructs a panel with buttons for selecting, deleting
	 * or creating vertices or edges
	 */
	private void constructPanelComponents() {
		
		ImageIcon selectIcon = new ImageIcon("Images/selection4.png");
		ImageIcon deleteIcon = new ImageIcon("Images/delete1.png");
		ImageIcon vertexIcon = new ImageIcon("Images/vertex.png");
		edgeIcon = new ImageIcon("Images/edge.png");
		dedgeIcon = new ImageIcon("Images/dedge.png");
		
		JButton selectionButton = new JButton(selectIcon);
		Color bg=selectionButton.getBackground();
		JButton deleteButton = new JButton(deleteIcon);
		JButton vertexButton = new JButton(vertexIcon);
		edgeButton = new JButton(edgeIcon);
		Color color = new Color(184,207,229);//Color.cyan;//new Color(0,0,255, 255*1/4);
		selectionButton.setBackground(color);
		//Selects different vertices and edges individually
		selectionButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
               editorHandler.setState(State.SELECT);
               edgeButton.setBackground(bg);
               vertexButton.setBackground(bg);
               selectionButton.setBackground(color);
               deleteButton.setEnabled(true);
            }
         });
		
		//Deletes a selected vertex or edge
		deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
               editorHandler.deleteDecorable();
            }
         });
		
		//Creates a new vertex
		vertexButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
               editorHandler.setState(State.VERTEX);
               edgeButton.setBackground(bg);
               vertexButton.setBackground(color);
               selectionButton.setBackground(bg);
               deleteButton.setEnabled(false);
            }
         });
		
		//Creates a new edge
		edgeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
               editorHandler.setState(State.EDGE);
               edgeButton.setBackground(color);
               vertexButton.setBackground(bg);
               selectionButton.setBackground(bg);
               deleteButton.setEnabled(false);
            }
         });
		
		JPanel toolPanel = new JPanel(new FlowLayout());

		toolPanel.add(selectionButton);
		toolPanel.add(deleteButton);
		toolPanel.add(vertexButton);
		toolPanel.add(edgeButton);
		
		add(toolPanel, BorderLayout.NORTH);
	}
	
	/**
	 * Changes the edge button whether
	 * the graph is directed or not
	 * @param directed true if the graph is directed
	 */
	public void changeDirectionEdgeButton(boolean directed) {
		if (directed) edgeButton.setIcon(dedgeIcon);
		else edgeButton.setIcon(edgeIcon);
	}
}
