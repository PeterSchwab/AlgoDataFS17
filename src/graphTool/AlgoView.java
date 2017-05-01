/*
 * Project GrahTool
 * Copyright (c) 2015 by Tina Gerber, Daria Schumacher
 * Berner Fachhochschule, Switzerland
 */
package graphTool;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Method;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * The AlgoView shows the controls to animate an algorithm
 * @param <V> Vertex
 * @param <E> Edge
 */
public class AlgoView<V,E> extends JPanel {

	private static final long serialVersionUID = 1L;
	private AlgoHandler<V,E> algoHandler;
	private Method currentAlgoMethod;
	JButton startButton;
	ImageIcon playIcon;
	private boolean isStartButton = true;

	/**
	 * constructor
	 * @param algoHandler AlgoHandler
	 */
	public AlgoView(AlgoHandler<V,E> algoHandler) {	

		this.setBackground(new Color(100,100,100));

		this.algoHandler= algoHandler;
		setLayout(new BorderLayout());
		constructPanelComponents();
	}

	/**
	 * Constructs a panel with buttons for choosing different algorithm
	 * and animating automatically or manually
	 */
	private void constructPanelComponents() {

		playIcon = new ImageIcon("Images/play.png");
		ImageIcon leftIcon = new ImageIcon("Images/left.png");
		ImageIcon rightIcon = new ImageIcon("Images/right.png");
		ImageIcon pauseIcon = new ImageIcon("Images/pause.png");
		ImageIcon stopIcon = new ImageIcon("Images/stop1.png");
		ImageIcon startvertexIcon = new ImageIcon("Images/startvertex.png");
		ImageIcon endvertexIcon = new ImageIcon("Images/endvertex.png");

		startButton = new JButton(playIcon);
		JButton backButton = new JButton(leftIcon);
		JButton forwardButton = new JButton(rightIcon);
		JButton stopButton = new JButton(stopIcon);
		//If the algorithm needs an start or a end vertex
		JButton startVertexButton = new JButton(startvertexIcon);
		JButton endVertexButton = new JButton(endvertexIcon);

		//Slider for controlling the tempo of the animation of an algorithm
		JSlider slider = new JSlider(100, 10000);
		slider.setValue(1000);
		slider.setInverted(true);
		slider.addChangeListener(new ChangeListener(){
			@Override
			public void stateChanged(ChangeEvent e) {
				algoHandler.setTimerTime(slider.getValue());
			}
		});

		//For the layout for the slider with its icon
		FlowLayout fl = new FlowLayout();
		JPanel p = new JPanel(fl);
		ImageIcon timeIcon = new ImageIcon("Images/time.png");
		p.add(new JLabel(timeIcon));
		p.add(slider);

		add(p, BorderLayout.SOUTH);

		//Makes a list to choose from with all available algorithms
		Vector<Method> algoMethods = algoHandler.getAnnotatedMethods();
		String[] algoMethodNames = new String[algoMethods.size()];
		for (int i = 0; i < algoMethods.size(); i++) {
			algoMethodNames[i] = algoMethods.get(i).getName();
		}
		JComboBox algoList = new JComboBox(algoMethodNames);
		currentAlgoMethod = algoMethods.get(algoList.getSelectedIndex());

		//Starts the automatic animation of an algorithm
		startButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {

				//Executes an algorithm and its animation when it get started
				//Otherwise it only gets paused
				if (isStartButton) {
					isStartButton=false;
					if(algoHandler.isMethodExecuted())
					{
						algoHandler.startAlgo();
						JButton source = (JButton) event.getSource();
						source.setBackground(new Color(184,207,229));
						startButton.setIcon(pauseIcon);
					}	
					else{
						if(requiresVertexSelection())
							isStartButton=true;
						else{
							algoHandler.executeMethod(currentAlgoMethod);
							algoHandler.startAlgo();
							JButton source = (JButton) event.getSource();
							source.setBackground(new Color(184,207,229));
							startButton.setIcon(pauseIcon);
						}
					}

					//If algorithm needs a startVertex or an endVertex and they're not set,
					//then prompt for the vertices to be set


				} else {
					algoHandler.pauseAlgo();
					startButton.setIcon(playIcon);
					isStartButton = true;
				}
			}
		});

		//Goes a step back in the animation of an algorithm
		backButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				algoHandler.previousAlgo();
			}
		});

		//Goes a step forward in the animation of an algorithm
		forwardButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				if(algoHandler.isMethodExecuted())
					algoHandler.nextAlgo();
				else{
					if(!requiresVertexSelection())
						algoHandler.executeMethod(currentAlgoMethod);
					algoHandler.nextAlgo();
				}
			}
		});

		//Stops the animation of an algorithm
		stopButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				algoHandler.stopAlgo();
			}
		});
	
		//A list to choose different algorithms from
		algoList.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {

				String algoName = (String) algoList.getSelectedItem();
				int index = 0;
				for (int i = 0; i < algoMethods.size(); i++) {
					if (algoName.equals(algoMethods.get(i).getName())) index = i;
				}
				currentAlgoMethod = algoMethods.get(index);

				//Reset all selections and stop the timer
				algoHandler.stopAlgo();
				algoHandler.clearStartEndVertex();

				//Set the buttons for startvertex or endvertex visible or invisible
				//whether they are needed for an algorithm or not
				if (currentAlgoMethod.getAnnotation(Algorithm.class).vertex()) 
					startVertexButton.setVisible(true);
				else startVertexButton.setVisible(false);
				if (currentAlgoMethod.getAnnotation(Algorithm.class).vertex2()) 
					endVertexButton.setVisible(true);
				else endVertexButton.setVisible(false);
			}
		});

		//To set a selected vertex as startvertex if needed
		startVertexButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				algoHandler.setStartVertex();
			}
		});

		//To set a selected vertex as endvertex if needed
		endVertexButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				algoHandler.setEndVertex();
			}
		});

		JPanel toolPanel = new JPanel(new FlowLayout());

		toolPanel.add(startButton);
		toolPanel.add(backButton);
		toolPanel.add(forwardButton);
		toolPanel.add(stopButton);
		toolPanel.add(algoList);
		toolPanel.add(startVertexButton);
		startVertexButton.setVisible(false);
		toolPanel.add(endVertexButton);
		endVertexButton.setVisible(false);

		add(toolPanel, BorderLayout.NORTH);
	}

	/**
	 * Resets the color of the startbutton
	 * after the animations of algorithms has finished
	 */
	public void resetStartButton() {
		startButton.setBackground(null);
		startButton.setIcon(playIcon);
		isStartButton = true;
	}

	/**
	 * returns whether the necessary vertices are selected
	 * @return true if a vertex is used but not selected
	 */
	public boolean requiresVertexSelection(){
		if (currentAlgoMethod.getAnnotation(Algorithm.class).vertex() && !algoHandler.isStartVertexSelected()) {
			JOptionPane.showMessageDialog(null, "A start vertex needs to be selected");
			return true;
		} else if (currentAlgoMethod.getAnnotation(Algorithm.class).vertex2() && !algoHandler.isEndVertexSelected()) {
			JOptionPane.showMessageDialog(null, "A end vertex needs to be selected");
			return true;
		} else {
			return false;
		}
	}

}
