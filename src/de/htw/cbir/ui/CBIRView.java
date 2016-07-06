package de.htw.cbir.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.event.ChangeEvent;

import de.htw.cbir.CBIRController;

public class CBIRView  {
	
	// anfägnliche Fenstergröße
	private static final int frameSizeX = 500; 
	private static final int frameSizeY = 500;
	
	// UI Elemente
	private JFrame frame;
	private ImageGrid grid;
	
	// der Constroller für diese View
	private CBIRController controller;
	
	public CBIRView(CBIRController controller) {
		this.controller = controller;
		
		// Hauptfenster
		frame = new JFrame("CBIR Project");
		frame.setJMenuBar(createMenuBar());
		frame.setSize(frameSizeX, frameSizeY);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
		// Komponente die die Bilder darstellt
		grid = new ImageGrid(controller, frameSizeX, frameSizeY);
		frame.add(grid);
		
		// lass alles zeichnen
		repaint();
	}
	
	/**
	 * Alles neuzeichnen
	 */
	public void repaint() {
		grid.doDrawing(); // zeichne die Bilder
	}
	
	private JMenuBar createMenuBar() {
		
		// Menubar 
		JMenuBar menuBar = new JMenuBar();
		
		// Menu "Algorithm"
		JMenu methodMenu = new JMenu("Algorithm");
		ButtonGroup buttonGroup = new ButtonGroup();
		String[] methodNames = controller.getFeatureFactoryNames(); 
		for (String methodName : methodNames) {
			JRadioButtonMenuItem mI_methodName = new JRadioButtonMenuItem(methodName,true);
			mI_methodName.addActionListener((ActionEvent e) -> { 
				controller.changeFeatureFactory(e.getActionCommand()); 
			});
			methodMenu.add(mI_methodName);
			buttonGroup.add(mI_methodName);
		}
		menuBar.add(methodMenu);
		
		// Menu "Testen"
		JMenu testMenu = new JMenu("Test");		
		ActionListener testMenuListener = (ActionEvent e) -> { controller.triggerTests(e.getActionCommand()); };

		int index = 0;
		for (String categoryName : controller.getImageManager().getCategoryNames()) {
			JMenuItem mI_group = new JMenuItem(categoryName);
			mI_group.addActionListener(testMenuListener);
			testMenu.add(mI_group);
			
			// zeige nur die ersten 20 Kategorien
			if(index++ > 20) break;
		}
		menuBar.add(testMenu);
		
		// "Testen" Button
		JButton mI_test = new JButton("Test All");
		mI_test.addActionListener((ActionEvent e) -> { controller.triggerTests("All"); } );
		menuBar.add(mI_test);
		
		// Menu "Einstellungen"
		JMenu settingsMenu = new JMenu("Settings");
		
		// Menupunkt "Sättigung"
		JMenu m_satValue = new JMenu("Saturation");
		final JSliderDecimal satSlider = JSliderDecimal.createDoubleJSlider(0.0, 5.0, 1.0, 1);
		satSlider.setMajorTickSpacing(1.0);
		satSlider.setMinorTickSpacing(.2);
		satSlider.setPaintTicks(true);
		satSlider.setPaintLabels(true);
		satSlider.addChangeListener((ChangeEvent e) -> { 
			if(!satSlider.getValueIsAdjusting()) 
				controller.getSettings().setSaturation(satSlider.getDecimalValue()); 
		});
		m_satValue.add(satSlider);
		settingsMenu.add(m_satValue);

		// Menupunkt "Histogramm Bins"
		JMenu m_binValue = new JMenu("Histogramm Bins");
		final JSliderDecimal binSlider = JSliderDecimal.createDoubleJSlider(1.0, 5.0, 2.0, 0);
		binSlider.setMajorTickSpacing(1.0);
		binSlider.setPaintTicks(true);
		binSlider.setPaintLabels(true);
		binSlider.addChangeListener((ChangeEvent e) -> { 
			if(!binSlider.getValueIsAdjusting()) 
				controller.getSettings().setNumOfHistogramBins((int) binSlider.getDecimalValue()); 
		});
		m_binValue.add(binSlider);
		settingsMenu.add(m_binValue);

		// Menupunkt "ClusterIterations"
		JMenu m_clusterIterationsValue = new JMenu("Cluster Iterations");
		final JSliderDecimal clusterIterationSlider = JSliderDecimal.createDoubleJSlider(1.0, 12.0, 2.0, 0);
		clusterIterationSlider.setMajorTickSpacing(1.0);
		clusterIterationSlider.setPaintTicks(true);
		clusterIterationSlider.setPaintLabels(true);
		clusterIterationSlider.addChangeListener((ChangeEvent e) -> { 
			if(!clusterIterationSlider.getValueIsAdjusting()) 
				controller.getSettings().setNumOfClusterIterations((int) clusterIterationSlider.getDecimalValue()); 
		});
		m_clusterIterationsValue.add(clusterIterationSlider);
		settingsMenu.add(m_clusterIterationsValue);

		// Menupunkt "N² Cluster"
		JMenu m_numClustersValue = new JMenu("N² Clusters");
		final JSliderDecimal numClustersSlider = JSliderDecimal.createDoubleJSlider(1.0, 5.0, 2.0, 0);
		numClustersSlider.setMajorTickSpacing(1.0);
		numClustersSlider.setPaintTicks(true);
		numClustersSlider.setPaintLabels(true);
		numClustersSlider.addChangeListener((ChangeEvent e) -> { 
			if(!numClustersSlider.getValueIsAdjusting()) 
				controller.getSettings().setNumOfNSquareClusters((int) numClustersSlider.getDecimalValue()); 
		});
		m_numClustersValue.add(numClustersSlider);
		settingsMenu.add(m_numClustersValue);
		
		
		
		menuBar.add(settingsMenu);
		
		return menuBar;
	}
}