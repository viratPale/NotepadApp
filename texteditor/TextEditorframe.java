package com.texteditor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.MenuBar;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

public class TextEditorframe extends JFrame implements ActionListener {

	JTextArea textArea;
	JScrollPane scrollPane; // Scroll bar for your text
	JSpinner fontSizeSpinner;
	JLabel fontLabel;
	JButton fontColorButton;
	JComboBox fontBox;

	JMenuBar menuBar; // Creates a vertical bar
	JMenu fileMenu; // We will need a menu of file to list it's contents
	JMenuItem openItem; // Need some items to add to the menu
	JMenuItem saveItem;
	JMenuItem exitItem;

	TextEditorframe() {

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("Text Editor");
		this.setSize(600, 600);
		this.setLayout(new FlowLayout());
		this.setLocationRelativeTo(null); // Displaying the text editor in the middle when executed

		textArea = new JTextArea();
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.setFont(new Font("Arial", Font.PLAIN, 20));

		scrollPane = new JScrollPane(textArea);
		scrollPane.setPreferredSize(new Dimension(550, 550));
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED); // When the scroll bar
																									// is needed then
																									// that's when it
																									// will be
																									// displayed.

		fontLabel = new JLabel("Font: ");

		fontSizeSpinner = new JSpinner();
		fontSizeSpinner.setPreferredSize(new Dimension(50, 25));
		fontSizeSpinner.setValue(20);
		fontSizeSpinner.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {

				textArea.setFont(
						new Font(textArea.getFont().getFamily(), Font.PLAIN, (int) fontSizeSpinner.getValue()));

			}
		});

		fontColorButton = new JButton("Color");
		fontColorButton.addActionListener(this);

		String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
		fontBox = new JComboBox(fonts);
		fontBox.addActionListener(this);
		fontBox.setSelectedItem("Arial");

		// ---- Menubar --

		menuBar = new JMenuBar();
		fileMenu = new JMenu("File");
		openItem = new JMenuItem("Open");
		saveItem = new JMenuItem("Save");
		exitItem = new JMenuItem("Exit");

		fileMenu.add(openItem);
		fileMenu.add(saveItem);
		fileMenu.add(exitItem);

		menuBar.add(fileMenu);

		openItem.addActionListener(this);
		saveItem.addActionListener(this);
		exitItem.addActionListener(this);

		// -- Menubar-*--

		this.setJMenuBar(menuBar);
		this.add(fontLabel);
		this.add(fontSizeSpinner);
		this.add(fontColorButton);
		this.add(fontBox);
		this.add(scrollPane);
		this.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == fontColorButton) {

			JColorChooser colorChooser = new JColorChooser();

			Color color = colorChooser.showDialog(null, "Choose a Color", Color.black);

			textArea.setForeground(color);

		}

		if (e.getSource() == fontBox) {

			textArea.setFont(new Font((String) fontBox.getSelectedItem(), Font.PLAIN, textArea.getFont().getSize()));

		}

		if (e.getSource() == openItem) {

			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setCurrentDirectory(new File(".")); // . indicates the current project folder

			FileNameExtensionFilter filter = new FileNameExtensionFilter("Text files", "txt");
			fileChooser.setFileFilter(filter);

			int response = fileChooser.showOpenDialog(null);

			if (response == JFileChooser.APPROVE_OPTION) {
				File file = new File(fileChooser.getSelectedFile().getAbsolutePath());
				Scanner fileIn = null;

				try {
					fileIn = new Scanner(file);
					if (file.isFile()) {
						while (fileIn.hasNextLine()) {
							String line = fileIn.nextLine() + "\n";
							textArea.append(line);
						}
					}

				} catch (Exception e1) {
					e1.printStackTrace();
				} finally {
					fileIn.close();
				}
			}

		}

		if (e.getSource() == saveItem) {

			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setCurrentDirectory(new File(".")); // . indicates the current project folder

			int response = fileChooser.showSaveDialog(null);

			if (response == JFileChooser.APPROVE_OPTION) {
				File file;
				PrintWriter fileOut = null;

				file = new File(fileChooser.getSelectedFile().getAbsolutePath());

				try {
					fileOut = new PrintWriter(file);
					fileOut.println(textArea.getText());
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} finally {
					fileOut.close();
				}
			}
		}

		if (e.getSource() == exitItem) {

			System.exit(0);
		}
	}
}
