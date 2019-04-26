package com.balancedbytes.tool.kodi;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker.StateValue;
import javax.swing.UIManager;
import javax.swing.text.BadLocationException;

@SuppressWarnings("serial")
public class RenameKodiEpisodes extends JFrame {

	private JTextField fDirTextField;
	private JButton fDirButton;
	private JTextField fSeriesTextField;
	private JTextPane fOutputPane;
	private JScrollPane fScrollPane;
	private JProgressBar fProgressBar;
	private JButton fUpdateButton;
	
	private Map<String, String> fEpisodesMap;

	public RenameKodiEpisodes() {

		super("Rename Kodi Episodes");

		setDefaultCloseOperation(EXIT_ON_CLOSE);

		Box centerBox = Box.createVerticalBox();
		centerBox.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		// Series directory

		Box dirBox1 = Box.createHorizontalBox();
		JLabel dirLabel = new JLabel("Series directory:");
		// dirLabel.setFont(dirLabel.getFont().deriveFont(Font.BOLD));
		dirBox1.add(dirLabel);
		dirBox1.add(Box.createHorizontalGlue());

		centerBox.add(dirBox1);

		fDirTextField = new JTextField("");
		fDirTextField.setMaximumSize(new Dimension(Integer.MAX_VALUE, fDirTextField.getPreferredSize().height));
		// Listen for changes in the text
		/*
		 * fDirTextField.getDocument().addDocumentListener(new DocumentListener() {
		 * public void changedUpdate(DocumentEvent e) { updateUrl(); } public void
		 * removeUpdate(DocumentEvent e) { updateUrl(); } public void
		 * insertUpdate(DocumentEvent e) { updateUrl(); } private void updateUrl() { if
		 * ((fDirTextField.getText().length() > 0) &&
		 * (fSeriesTextField.getText().length() == 0)) { String url =
		 * findUrlShortcut(new File(fDirTextField.getText())); if (url != null) {
		 * fSeriesTextField.setText(url); } } } });
		 */

		fDirButton = new JButton();
		try (InputStream in = this.getClass().getResourceAsStream("/TreeOpen.gif")) {
			fDirButton.setIcon(new ImageIcon(ImageIO.read(in)));
		} catch (IOException ex) {
		}

		// create a file chooser that allows you to pick a directory
		fDirButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				fUpdateButton.setEnabled(false);
				JFileChooser chooser = new JFileChooser();
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int option = chooser.showOpenDialog(RenameKodiEpisodes.this);
				File selectedFile = chooser.getSelectedFile();
				if ((option == JFileChooser.APPROVE_OPTION) && (selectedFile != null)) {
					fDirTextField.setText(selectedFile.getAbsolutePath());
					findSeriesAndEpisode(selectedFile);
				} else {
					fDirTextField.setText("");
				}
			}
		});

		Box dirBox2 = Box.createHorizontalBox();
		dirBox2.add(fDirTextField);
		dirBox2.add(Box.createHorizontalStrut(5));
		dirBox2.add(fDirButton);

		centerBox.add(dirBox2);

		// Series Title and Season

		Box seriesBox1 = Box.createHorizontalBox();
		JLabel urlLabel = new JLabel("Series on thetvdb.com:");
		// urlLabel.setFont(urlLabel.getFont().deriveFont(Font.BOLD));
		seriesBox1.add(urlLabel);
		seriesBox1.add(Box.createHorizontalGlue());

		centerBox.add(Box.createVerticalStrut(5));
		centerBox.add(seriesBox1);

		fSeriesTextField = new JTextField("");
		fSeriesTextField.setMaximumSize(new Dimension(Integer.MAX_VALUE, fSeriesTextField.getPreferredSize().height));

		Box urlBox2 = Box.createHorizontalBox();
		urlBox2.add(fSeriesTextField);

		centerBox.add(urlBox2);

		// Output

		Box outputBox1 = Box.createHorizontalBox();
		JLabel outputLabel = new JLabel("Renamed Files:");
		// urlLabel.setFont(urlLabel.getFont().deriveFont(Font.BOLD));
		outputBox1.add(outputLabel);
		outputBox1.add(Box.createHorizontalGlue());

		centerBox.add(Box.createVerticalStrut(5));
		centerBox.add(outputBox1);

		fOutputPane = new JTextPane();
		fScrollPane = new JScrollPane(fOutputPane);
		fScrollPane.setPreferredSize(new Dimension(fScrollPane.getPreferredSize().width, 100));
		fScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		Box outputBox2 = Box.createHorizontalBox();
		outputBox2.add(fScrollPane);

		centerBox.add(outputBox2);

		fProgressBar = new JProgressBar(0, 100);
		fProgressBar.setStringPainted(true);
		fProgressBar.setString("");

		Box progressBox = Box.createHorizontalBox();
		progressBox.add(fProgressBar);

		centerBox.add(Box.createVerticalStrut(5));
		centerBox.add(progressBox);

		centerBox.add(new Box.Filler(new Dimension(0, 5), new Dimension(0, 5), new Dimension(0, Integer.MAX_VALUE)));

		fUpdateButton = new JButton("Update Filenames");
		fUpdateButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				File directory = new File(fDirTextField.getText());
				RenameKodiEpisodesTask renameTask = new RenameKodiEpisodesTask(directory, fEpisodesMap, fOutputPane.getDocument());
				renameTask.addPropertyChangeListener(new PropertyChangeListener() {
					@Override
					public void propertyChange(PropertyChangeEvent evt) {
						if ("progress".equals(evt.getPropertyName())) {
							int progress = (Integer) evt.getNewValue();
							fProgressBar.setIndeterminate(false);
							fProgressBar.setValue(progress);
							fProgressBar.setString(progress + "%");
						}
						if ("state".equals(evt.getPropertyName())) {
							StateValue state = (StateValue) evt.getNewValue();
							if (StateValue.STARTED == state) {
								clearOutputPane();
								fProgressBar.setValue(0);
								fUpdateButton.setEnabled(false);
							}
							if (StateValue.DONE == state) {
								fUpdateButton.setEnabled(true);
							}
						}
					}
				});
				renameTask.execute();
			}
		});

		JPanel updatePanel = new JPanel();
		updatePanel.setLayout(new BoxLayout(updatePanel, BoxLayout.X_AXIS));
		updatePanel.add(Box.createHorizontalGlue());
		updatePanel.add(fUpdateButton);
		updatePanel.add(Box.createHorizontalGlue());

		centerBox.add(updatePanel);

		centerBox.add(Box.createHorizontalGlue());

		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(centerBox, BorderLayout.CENTER);
		pack();

		setSize(450, (int) getSize().getHeight());

	}
	
	private void clearOutputPane() {
		try {
			fOutputPane.getDocument().remove(0, fOutputPane.getDocument().getLength());
		} catch (BadLocationException badLocationException) {
			// do nothing
		}
	}

	/**
	 * Create the GUI and show it. For thread safety, this method should be invoked
	 * from the event-dispatching thread.
	 */
	private static void createAndShowUi() {

		try {
			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (Exception e) {
			System.err.println("Can't change Look&Feel: " + e);
		}

		// make sure we have nice window decorations.
		JFrame.setDefaultLookAndFeelDecorated(true);

		RenameKodiEpisodes ui = new RenameKodiEpisodes();
		ui.setVisible(true);

	}

	private void findSeriesAndEpisode(File directory) {
		SearchTheTvDbEpisodesTask searchTask = new SearchTheTvDbEpisodesTask(directory);
		searchTask.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if ("state".equals(evt.getPropertyName())) {
					StateValue state = (StateValue) evt.getNewValue();
					if (StateValue.STARTED == state) {
						fSeriesTextField.setText("");
						fProgressBar.setValue(0);
						fProgressBar.setIndeterminate(true);
						fProgressBar.setString("Search Series");
					}
					if (StateValue.DONE == state) {
						if (KodiTools.isProvided(searchTask.getSeriesName())) {
							fUpdateButton.setEnabled(true);
							fEpisodesMap = searchTask.getEpisodeMap();
							StringBuilder seriesText = new StringBuilder();
							seriesText.append(searchTask.getSeriesName());
							seriesText.append(" (Season ").append(searchTask.getSeasonNr());
							seriesText.append(" with ").append(fEpisodesMap.size()).append(" Episodes").append(")");
							fSeriesTextField.setText(seriesText.toString());
							clearOutputPane();
						}
						fProgressBar.setIndeterminate(false);
						fProgressBar.setString("");
					}
				}
			}
		});
		searchTask.execute();
	}

	public static void main(String[] args) {
		// schedule a job for the event-dispatching thread:
		// creating and showing this application's GUI.
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowUi();
			}
		});
	}

}
