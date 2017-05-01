/*
 * Project GrahTool
 * Copyright (c) 2015 by Tina Gerber, Daria Schumacher
 * Berner Fachhochschule, Switzerland
 */
package graphTool;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;

import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import graphLib.Decorable;
import graphLib.Edge;
import graphLib.Graph;
import graphLib.Vertex;

/**	
 * Constructs the frame with the different menus
 * for drawing a graph or animating an algorithm
 */
public class GraphFrame<V, E> extends JFrame {

	public static final int FRAME_WIDTH = 900;
	public static final int FRAME_HEIGHT = 700;
	private GraphView<V,E> graphView;
	private EditorView<V,E> editorPanel;
	private EditorHandler<V,E> editorHandler;
	private AlgoView<V,E> algoPanel;
	private AlgoHandler<V,E> algoHandler;
	private GraphTool<V,E> graphTool;
	private String currentGraphName;
	private ActionListener renameListener;
	private JMenuItem undo;
	private JMenuItem redo;
	private JTabbedPane tabpane;
	JMenu edit;
	JMenuItem newGraph;
	JMenuItem save;
	JMenuItem saveAs;
	JMenuItem delete;

	/**
	 * constructor
	 * @param gt GraphTool
	 */
	public GraphFrame(GraphTool<V,E> gt) {

		this.editorHandler=new EditorHandler<V,E>(gt);
		this.algoHandler=new AlgoHandler<V,E>(gt);
		graphTool = gt;
		createRenameListener();
		this.graphView=new GraphView<V,E>(renameListener);
		graphView.setHandler(editorHandler);
		constructMenuComponents();
		constructTabComponents();
		this.add(this.graphView, BorderLayout.CENTER);
		setSize(FRAME_WIDTH, FRAME_HEIGHT);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("GraphTool");
		setVisible(true);
	}



	//------------------------------------------------------------------------------------//
	// Helper methods for constructing the frame
	//------------------------------------------------------------------------------------//

	/**
	 * creates the listener to rename a Vertex or an Edge
	 */
	private void createRenameListener() {
		renameListener=new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				String text;
				Decorable d = editorHandler.getSelected();
				if(d==null){
					JOptionPane.showMessageDialog(graphView, "No shape selected");
				}
				else{
					if(d instanceof Vertex){
						text="Change name";
					}
					else{
						text="Change weight";
					}
					String usertext=JOptionPane.showInputDialog(text);
					if(usertext!=null){
						if(d instanceof Edge){
							Double weight=new Double(0.0);
							try {
								weight = Double.parseDouble(usertext);

							} catch (NumberFormatException ex) {
								//If the string cannot be parsed, set the weight to 0
								System.out.println("@GraphFrame: Failed to parse a string to a double");
								weight=(double) 0.0;
							}
							editorHandler.changeAttribut(weight);
						}
						else
							editorHandler.changeAttribut(usertext);
					}
				}

			}

		};

	}
	/**
	 * Constructs the main menu with options for creating a new graph
	 * or saving and opening graphs
	 */
	private void constructMenuComponents() {

		JMenuBar menubar=new JMenuBar();
		JMenu file = new JMenu("File");
		edit = new JMenu("Edit");
		JMenu view = new JMenu("View");
		newGraph = new JMenuItem("New");
		save = new JMenuItem("Save");
		save.setAccelerator(KeyStroke.getKeyStroke("ctrl S"));
		saveAs = new JMenuItem("Save as");
		JMenuItem open = new JMenuItem("Open");
		delete = new JMenuItem("Delete");
		undo = new JMenuItem("Undo");
		undo.setAccelerator(KeyStroke.getKeyStroke("ctrl Z"));
		redo = new JMenuItem("Redo");
		redo.setAccelerator(KeyStroke.getKeyStroke("ctrl Y"));
		JMenuItem rename = new JMenuItem("Rename...");
		JCheckBoxMenuItem name = graphView.getNameItem();
		name.setAccelerator(KeyStroke.getKeyStroke("ctrl alt N"));
		JCheckBoxMenuItem weight = graphView.getWeightItem();
		weight.setAccelerator(KeyStroke.getKeyStroke("ctrl alt W"));
		JCheckBoxMenuItem string = graphView.getStringItem();
		string.setAccelerator(KeyStroke.getKeyStroke("ctrl alt S"));

		file.add(newGraph);
		file.add(save);
		file.add(saveAs);
		file.add(open);
		file.add(delete);
		edit.add(undo);
		edit.add(redo);
		edit.add(rename);
		view.add(name);
		view.add(weight);
		view.add(string);
		menubar.add(file);
		menubar.add(edit);
		menubar.add(view);
		this.setJMenuBar(menubar);

		//Creates a new undirected or directed graph
		newGraph.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {

				chooseGraphOption();
			}
		});

		/*
		 * Saves the current graph
		 * Asks for a name if the graph doesn't have one
		 */
		save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {

				if (currentGraphName == null) currentGraphName = askForGraphName();
				saveGraph(currentGraphName);
			}
		});

		//Saves the current graph under a new name
		//Warns before overwriting an existing graph
		saveAs.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {

				currentGraphName = askForGraphName();
				saveGraph(currentGraphName);	
			}
		});

		//Opens a saved graph for the directory GraphFiles		
		open.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {

				//Stops running algorithms animations
				//before opening a new graph
				algoHandler.stopAlgo();

				String[] options = getFileNames();

				String name = (String) JOptionPane.showInputDialog(
						null,
						"Choose a graph",
						"Open a graph",
						JOptionPane.PLAIN_MESSAGE,
						null,
						options,
						options[0]);

				if (name != null && !name.isEmpty()) {

					try {
						Graph<V,E> g = graphTool.openGraph(name);
						//Changes the edge button whether the graph is directed or not
						if (g.isDirected()) editorPanel.changeDirectionEdgeButton(true);
						else editorPanel.changeDirectionEdgeButton(false);		
						currentGraphName = name;
					} catch (IOException e) {
						System.out.println("@GraphFrame: Failed to open a graph");
						e.printStackTrace();
					}
				}

			}
		});

		//Deletes a saved graph
		//Warns before deleting a graph
		delete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {

				String[] options = getFileNames();

				String name = (String) JOptionPane.showInputDialog(
						null,
						"Choose a graph",
						"Open a graph",
						JOptionPane.PLAIN_MESSAGE,
						null,
						options,
						options[0]);

				if (name != null && !name.isEmpty()) {

					File file = new File("GraphFiles/" + name + ".ser");
					Path path = file.toPath();

					int deleteOption = 0;
					for (int i = 0; i < options.length; i++) {
						if (options[i].equals(name)) {
							deleteOption = JOptionPane.showConfirmDialog(null,
									"Do you want to delete this graph?", 
									"Delete a graph", JOptionPane.YES_NO_OPTION);
						}
					}

					if (deleteOption == JOptionPane.YES_OPTION) {

						try {
							Files.delete(path);
						} catch (NoSuchFileException x) {
							System.err.format("%s: no such" + " file or directory%n", path);
						} catch (DirectoryNotEmptyException x) {
							System.err.format("%s not empty%n", path);
						} catch (IOException x) {
							// File permission problems are caught here.
							System.err.println(x);
						}
					}

				}
			}
		});

		undo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				graphTool.undo();
			}
		});

		redo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				graphTool.redo();
			}
		});

		rename.addActionListener(renameListener);
	}


	/**
	 * Constructs the tabs for either drawing graphs or animating algorithms
	 */
	private void constructTabComponents() {

		editorPanel = new EditorView<V, E>(editorHandler);

		algoPanel = new AlgoView<V, E>(algoHandler);
		tabpane = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);

		ImageIcon graphIcon = new ImageIcon("Images/draw.png");
		ImageIcon algoIcon = new ImageIcon("Images/anim.png");

		tabpane.addTab("", editorPanel);
		tabpane.addTab("", algoPanel);

		tabpane.setIconAt(0, graphIcon);
		tabpane.setIconAt(1, algoIcon);

		tabpane.addChangeListener(new ChangeListener(){

			@Override
			public void stateChanged(ChangeEvent e) {
				// shows which panel is selected
				int i=tabpane.getSelectedIndex();
				if(i==0){
					algoHandler.clearSelected();
					algoHandler.stopAlgo();
					algoHandler.clearStartEndVertex();
					graphView.setHandler(editorHandler);
					//Reactivates the save and delete option
					graphView.setRenameVisibility(true);
					edit.setEnabled(true);
					newGraph.setEnabled(true);
					save.setEnabled(true);
					saveAs.setEnabled(true);
					delete.setEnabled(true);

				} else {

					graphView.setHandler(algoHandler);
					editorHandler.clearSelected();
					//Deactivates the save and delete option
					graphView.setRenameVisibility(false);
					edit.setEnabled(false);
					newGraph.setEnabled(false);
					save.setEnabled(false);
					saveAs.setEnabled(false);
					delete.setEnabled(false);
				}
			}

		});

		add(tabpane, BorderLayout.SOUTH);
	}

	//------------------------------------------------------------------------------------//
	// Helper method for creating a new graph
	//------------------------------------------------------------------------------------//

	/**
	 * Creates a new graph
	 * Gives the choice to create a directed or an undirected graph
	 * @return true if you choose a directed graph
	 */
	public boolean chooseGraphOption(){
		Object[] options = {"Undirected graph", "Directed graph"};
		int choice = JOptionPane.showOptionDialog(null,
				"Choose a graph option:",
				"Graph options",
				JOptionPane.YES_NO_CANCEL_OPTION,
				JOptionPane.QUESTION_MESSAGE,
				null,
				options,
				options[1]);
		if(choice==JOptionPane.CLOSED_OPTION){
			return false;
		}
		else{
			if (choice == 0) {
				graphTool.createGraph(false);
				editorPanel.changeDirectionEdgeButton(false);
				currentGraphName = null;
				return true;
			} else {
				graphTool.createGraph(true);
				editorPanel.changeDirectionEdgeButton(true);
				currentGraphName = null;
				return true;
			}
		}
	}

	//------------------------------------------------------------------------------------//
	// Helper methods for saving a graph
	//------------------------------------------------------------------------------------//

	/**
	 * Prompts for a name to save the graph under
	 * If the name already exists, prompts for overwriting name or not
	 * @return the name
	 */
	private String askForGraphName() {

		String[] options = getFileNames();

		String name = null;
		boolean goodName = false;
		int overwriteOption = 0;

		do {

			overwriteOption = 0;

			name = (String) JOptionPane.showInputDialog(null, "Enter a file name", 
					"Save a graph", JOptionPane.PLAIN_MESSAGE);

			//Leaves the loop if the saving is canceled or the window is closed
			if (name == null) break;

			//Leaves the loop if the name is not empty or does not already exist
			if (name != null && !name.isEmpty()) {

				//Checks if the name already exists
				for (int i = 0; i < options.length; i++) {
					if (options[i].equals(name)) {
						overwriteOption = JOptionPane.showConfirmDialog(null,
								"The name already exists. Overwrite?", 
								"File already exists", JOptionPane.YES_NO_OPTION);
					}
				}
				goodName = true;
				if (overwriteOption == JOptionPane.NO_OPTION) goodName = false;
			}

		} while (!goodName);

		return name;
	}

	/**
	 * Gets all files from GraphFiles
	 * and puts the filenames without endings in an array
	 * @return all files from GraphFiles
	 */
	private String[] getFileNames() {

		File folder = new File("GraphFiles/");
		File[] listOfFiles = folder.listFiles();

		String[] options = new String[listOfFiles.length];

		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				String filename = listOfFiles[i].getName();
				filename = filename.substring(0, filename.length()-4);
				options[i] = filename;
			}
		}

		return options;
	}

	/**
	 * Saves the graph under the given name
	 * @param name the given name
	 */
	private void saveGraph(String name) {

		if (name != null) {

			try {
				graphTool.saveGraph(name);
				currentGraphName = name;
			} catch (IOException e) {
				System.out.println("@GraphFrame: Failed to save a graph");
				e.printStackTrace();
			}
		}
	}

	/**
	 * returns the graphview
	 * @return GraphView
	 */
	public GraphView<V, E> getGraphView() {
		return graphView;
	}

	/**
	 * Resets the color of the startbutton
	 * after the animations of algorithms has finished
	 */
	public void resetStartButton() {
		algoPanel.resetStartButton();

	}
	
	/**
	 * sets the redo state enabled or disabled
	 * @param enabled to enable the redostate it has to be true
	 */
	public void setRedoState(boolean enabled) {
		redo.setEnabled(enabled);
	}
	
	/**
	 * sets the undo state enabled or disabled
	 * @param enabled to enable the undostate it has to be true
	 */
	public void setUndoState(boolean enabled) {
		undo.setEnabled(enabled);	
	}


	/**
	 * if a new graph opened and the algotab is shown,
	 * old data will be removed
	 */
	public void newGraphOpened() {
		
		if(tabpane.getSelectedIndex()==1){
			algoHandler.stopAlgo();
			algoHandler.clearStartEndVertex();
		}
	}
	
}
