package main;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.BorderLayout;
import javax.swing.SwingConstants;

import org.jfugue.Player;

import java.awt.Font;
import javax.swing.JSplitPane;
import javax.swing.JPanel;
import javax.swing.BoxLayout;
import java.awt.GridLayout;
import javax.swing.JSeparator;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import javax.swing.JComboBox;
import javax.swing.JToolBar;
import java.awt.Component;
import javax.swing.Box;
import javax.swing.JTextPane;
import javax.swing.JPopupMenu;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Dimension;
import javax.swing.JMenuItem;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.JLayeredPane;
import javax.swing.JScrollBar;
import javax.swing.JList;
import javax.swing.AbstractListModel;
import javax.swing.ListSelectionModel;
import javax.swing.JFormattedTextField;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.beans.PropertyChangeEvent;
import javax.swing.JCheckBox;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import javax.swing.JFileChooser;
import java.awt.Color;

public class ApplicationWindow {

	String key;
	String instrument;
	int finalInstrument;
	int tempo;
	Song song = new Song();
	String[] chordProgression;
	ArrayList<Integer> finalChordProgression;
	private JFrame frame;
	ArrayList<ArrayList<String>> keySignatures;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ApplicationWindow window = new ApplicationWindow();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ApplicationWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		//JFUGUE INTIALIZATION
		Player player = new Player();
		chordProgression = new String[32];
		finalInstrument = 1;
		finalChordProgression = new ArrayList<Integer>(32);
		for(int i = 0; i < 32; i++)
		{
			chordProgression[i] = "I";
		}
		for(int i = 0; i < 32; i++)
		{
			finalChordProgression.add(-1); //should be overwritten
		}
		keySignatures = new ArrayList<ArrayList<String>>();
		//
		frame = new JFrame();
		//frame.setBounds(, 0, 2200, 1500);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new GridLayout(0, 1, 0, 0));
		JPanel panel = new JPanel();
		frame.getContentPane().add(panel);
		panel.setLayout(null);

		
		JLabel Title = new JLabel("Welcome to the BachBot");
		Title.setBounds(1119, 205, 591, 64);
		panel.add(Title);
		Title.setVerticalAlignment(SwingConstants.TOP);
		Title.setFont(new Font("SansSerif", Font.BOLD, 50));
		Title.setHorizontalAlignment(SwingConstants.CENTER);
		
		String[] keys = new String[] {"C", "C#", "D", "E", "F", "F#", "G", "Gb", "Ab", "A", "Bb", "B", "Cb"};
		JComboBox KeySignatureDropDown = new JComboBox(keys);
		key = "C";
		KeySignatureDropDown.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED){
					key = (String) e.getItem();
				}
			}
		});
		KeySignatureDropDown.setFont(new Font("SansSerif", Font.PLAIN, 25));
		KeySignatureDropDown.setBounds(2030, 497, 72, 53);
		panel.add(KeySignatureDropDown);
		
		JLabel keySignatureLabel = new JLabel("Key Signature");
		keySignatureLabel.setFont(new Font("SansSerif", Font.PLAIN, 25));
		keySignatureLabel.setBounds(1697, 497, 177, 53);
		panel.add(keySignatureLabel);
		
		JFormattedTextField tempoField = new JFormattedTextField();
		tempoField.setHorizontalAlignment(SwingConstants.CENTER);
		tempoField.setText("70");
		tempo =70;
		tempoField.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				try{
					tempo = Integer.parseInt(tempoField.getText());
				}
				catch(NumberFormatException e)
				{
					tempoField.setText("80");
				}
			}
		});
		tempoField.setFont(new Font("SansSerif", Font.PLAIN, 25));
		tempoField.setBounds(2030, 585, 68, 39);
		panel.add(tempoField);
		
		JLabel tempoLabel = new JLabel("Tempo (BPM)");
		tempoLabel.setFont(new Font("SansSerif", Font.PLAIN, 25));
		tempoLabel.setBounds(1698, 578, 177, 53);
		panel.add(tempoLabel);
		
		JLabel instrumentLabel = new JLabel("Instrument");
		instrumentLabel.setFont(new Font("SansSerif", Font.PLAIN, 25));
		instrumentLabel.setBounds(1698, 665, 177, 53);
		panel.add(instrumentLabel);
		
		String[] instruments = new String[] {"Piano", "Church Organ", "Strings", "Marimba"};
		JComboBox instrumentField = new JComboBox(instruments);
		instrument = "Piano";
		instrumentField.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED){
					instrument = (String) e.getItem();
					finalInstrument = instrument(instrument);
				}
			}
		});
		instrumentField.setFont(new Font("SansSerif", Font.PLAIN, 25));
		instrumentField.setBounds(1932, 665, 166, 53);
		panel.add(instrumentField);
		
		JLabel chordProgressionLabel = new JLabel("Chord Progression");
		chordProgressionLabel.setFont(new Font("SansSerif", Font.PLAIN, 25));
		chordProgressionLabel.setBounds(1796, 750, 222, 53);
		panel.add(chordProgressionLabel);
		
		JLabel label = new JLabel("|");
		label.setFont(new Font("SansSerif", Font.PLAIN, 50));
		label.setBounds(1647, 841, 12, 39);
		panel.add(label);
		
		String[] chords = new String[] {"I","ii","iii","IV","V","vi","vii"};
		JComboBox c1 = new JComboBox(chords);
		c1.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED){
					chordProgression[0] = ((String) e.getItem());
				}
			}
		});
		c1.setFont(new Font("SansSerif", Font.PLAIN, 25));
		c1.setBounds(1395, 831, 56, 53);
		panel.add(c1);
		
		JComboBox comboBox = new JComboBox(chords);
		comboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED){
					chordProgression[1] = ((String) e.getItem());
				}
			}
		});
		comboBox.setFont(new Font("SansSerif", Font.PLAIN, 25));
		comboBox.setBounds(1458, 831, 56, 53);
		panel.add(comboBox);
		
		JComboBox comboBox_1 = new JComboBox(chords);
		comboBox_1.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED){
					chordProgression[2] = ((String) e.getItem());
				}
			}
		});
		comboBox_1.setFont(new Font("SansSerif", Font.PLAIN, 25));
		comboBox_1.setBounds(1523, 831, 56, 53);
		panel.add(comboBox_1);
		
		JComboBox comboBox_2 = new JComboBox(chords);
		comboBox_2.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED){
					chordProgression[3] = ((String) e.getItem());
				}
			}
		});
		comboBox_2.setFont(new Font("SansSerif", Font.PLAIN, 25));
		comboBox_2.setBounds(1588, 831, 56, 53);
		panel.add(comboBox_2);
		
		JComboBox comboBox_3 = new JComboBox(chords);
		comboBox_3.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED){
					chordProgression[4] = ((String) e.getItem());
				}
			}
		});
		comboBox_3.setFont(new Font("SansSerif", Font.PLAIN, 25));
		comboBox_3.setBounds(1666, 831, 56, 53);
		panel.add(comboBox_3);
		
		JComboBox comboBox_4 = new JComboBox(chords);
		comboBox_4.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED){
					chordProgression[5] = ((String) e.getItem());
				}
			}
		});
		comboBox_4.setFont(new Font("SansSerif", Font.PLAIN, 25));
		comboBox_4.setBounds(1729, 831, 56, 53);
		panel.add(comboBox_4);
		
		JComboBox comboBox_5 = new JComboBox(chords);
		comboBox_5.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED){
					chordProgression[6] = ((String) e.getItem());
				}
			}
		});
		comboBox_5.setFont(new Font("SansSerif", Font.PLAIN, 25));
		comboBox_5.setBounds(1794, 831, 56, 53);
		panel.add(comboBox_5);
		
		JComboBox comboBox_6 = new JComboBox(chords);
		comboBox_6.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED){
					chordProgression[7] = ((String) e.getItem());
				}
			}
		});
		comboBox_6.setFont(new Font("SansSerif", Font.PLAIN, 25));
		comboBox_6.setBounds(1859, 831, 56, 53);
		panel.add(comboBox_6);
		
		JLabel label_1 = new JLabel("|");
		label_1.setFont(new Font("SansSerif", Font.PLAIN, 50));
		label_1.setBounds(1918, 841, 12, 39);
		panel.add(label_1);
		
		JComboBox comboBox_7 = new JComboBox(chords);
		comboBox_7.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED){
					chordProgression[8] = ((String) e.getItem());
				}
			}
		});
		comboBox_7.setFont(new Font("SansSerif", Font.PLAIN, 25));
		comboBox_7.setBounds(1930, 831, 56, 53);
		panel.add(comboBox_7);
		
		JComboBox comboBox_8 = new JComboBox(chords);
		comboBox_8.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED){
					chordProgression[9] = ((String) e.getItem());
				}
			}
		});
		comboBox_8.setFont(new Font("SansSerif", Font.PLAIN, 25));
		comboBox_8.setBounds(1993, 831, 56, 53);
		panel.add(comboBox_8);
		
		JComboBox comboBox_9 = new JComboBox(chords);
		comboBox_9.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED){
					chordProgression[10] = ((String) e.getItem());
				}
			}
		});
		comboBox_9.setFont(new Font("SansSerif", Font.PLAIN, 25));
		comboBox_9.setBounds(2058, 831, 56, 53);
		panel.add(comboBox_9);
		
		JComboBox comboBox_10 = new JComboBox(chords);
		comboBox_10.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED){
					chordProgression[11] = ((String) e.getItem());
				}
			}
		});
		comboBox_10.setFont(new Font("SansSerif", Font.PLAIN, 25));
		comboBox_10.setBounds(2123, 831, 56, 53);
		panel.add(comboBox_10);
		
		JLabel label_2 = new JLabel("|");
		label_2.setFont(new Font("SansSerif", Font.PLAIN, 50));
		label_2.setBounds(2182, 841, 12, 39);
		panel.add(label_2);
		
		JComboBox comboBox_11 = new JComboBox(chords);
		comboBox_11.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED){
					chordProgression[12] = ((String) e.getItem());
				}
			}
		});
		comboBox_11.setFont(new Font("SansSerif", Font.PLAIN, 25));
		comboBox_11.setBounds(2194, 831, 56, 53);
		panel.add(comboBox_11);
		
		JComboBox comboBox_12 = new JComboBox(chords);
		comboBox_12.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED){
					chordProgression[13] = ((String) e.getItem());
				}
			}
		});
		comboBox_12.setFont(new Font("SansSerif", Font.PLAIN, 25));
		comboBox_12.setBounds(2257, 831, 56, 53);
		panel.add(comboBox_12);
		
		JComboBox comboBox_13 = new JComboBox(chords);
		comboBox_13.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED){
					chordProgression[14] = ((String) e.getItem());
					chordProgression[15] = ((String) e.getItem());
				}
			}
		});
		comboBox_13.setFont(new Font("SansSerif", Font.PLAIN, 25));
		comboBox_13.setBounds(2322, 831, 108, 53);
		panel.add(comboBox_13);
		
		JLabel label_3 = new JLabel("|");
		label_3.setFont(new Font("SansSerif", Font.PLAIN, 50));
		label_3.setBounds(2446, 841, 12, 39);
		panel.add(label_3);
		
		JLabel label_4 = new JLabel("|");
		label_4.setFont(new Font("SansSerif", Font.PLAIN, 50));
		label_4.setBounds(1647, 1011, 12, 39);
		panel.add(label_4);
		
		JComboBox comboBox_15 = new JComboBox(chords);
		comboBox_15.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED){
					chordProgression[16] = ((String) e.getItem());
				}
			}
		});
		comboBox_15.setFont(new Font("SansSerif", Font.PLAIN, 25));
		comboBox_15.setBounds(1395, 1001, 56, 53);
		panel.add(comboBox_15);
		
		JComboBox comboBox_16 = new JComboBox(chords);
		comboBox_16.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED){
					chordProgression[17] = ((String) e.getItem());
				}
			}
		});
		comboBox_16.setFont(new Font("SansSerif", Font.PLAIN, 25));
		comboBox_16.setBounds(1458, 1001, 56, 53);
		panel.add(comboBox_16);
		
		JComboBox comboBox_17 = new JComboBox(chords);
		comboBox_17.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED){
					chordProgression[18] = ((String) e.getItem());
				}
			}
		});
		comboBox_17.setFont(new Font("SansSerif", Font.PLAIN, 25));
		comboBox_17.setBounds(1523, 1001, 56, 53);
		panel.add(comboBox_17);
		
		JComboBox comboBox_18 = new JComboBox(chords);
		comboBox_18.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED){
					chordProgression[19] = ((String) e.getItem());
				}
			}
		});
		comboBox_18.setFont(new Font("SansSerif", Font.PLAIN, 25));
		comboBox_18.setBounds(1588, 1001, 56, 53);
		panel.add(comboBox_18);
		
		JComboBox comboBox_19 = new JComboBox(chords);
		comboBox_19.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED){
					chordProgression[20] = ((String) e.getItem());
				}
			}
		});
		comboBox_19.setFont(new Font("SansSerif", Font.PLAIN, 25));
		comboBox_19.setBounds(1666, 1001, 56, 53);
		panel.add(comboBox_19);
		
		JComboBox comboBox_20 = new JComboBox(chords);
		comboBox_20.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED){
					chordProgression[21] = ((String) e.getItem());
				}
			}
		});
		comboBox_20.setFont(new Font("SansSerif", Font.PLAIN, 25));
		comboBox_20.setBounds(1729, 1001, 56, 53);
		panel.add(comboBox_20);
		
		JComboBox comboBox_21 = new JComboBox(chords);
		comboBox_21.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED){
					chordProgression[22] = ((String) e.getItem());
				}
			}
		});
		comboBox_21.setFont(new Font("SansSerif", Font.PLAIN, 25));
		comboBox_21.setBounds(1794, 1001, 56, 53);
		panel.add(comboBox_21);
		
		JComboBox comboBox_22 = new JComboBox(chords);
		comboBox_22.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED){
					chordProgression[23] = ((String) e.getItem());
				}
			}
		});
		comboBox_22.setFont(new Font("SansSerif", Font.PLAIN, 25));
		comboBox_22.setBounds(1859, 1001, 56, 53);
		panel.add(comboBox_22);
		
		JLabel label_5 = new JLabel("|");
		label_5.setFont(new Font("SansSerif", Font.PLAIN, 50));
		label_5.setBounds(1918, 1011, 12, 39);
		panel.add(label_5);
		
		JComboBox comboBox_23 = new JComboBox(chords);
		comboBox_23.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED){
					chordProgression[24] = ((String) e.getItem());
				}
			}
		});
		comboBox_23.setFont(new Font("SansSerif", Font.PLAIN, 25));
		comboBox_23.setBounds(1930, 1001, 56, 53);
		panel.add(comboBox_23);
		
		JComboBox comboBox_24 = new JComboBox(chords);
		comboBox_24.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED){
					chordProgression[25] = ((String) e.getItem());
				}
			}
		});
		comboBox_24.setFont(new Font("SansSerif", Font.PLAIN, 25));
		comboBox_24.setBounds(1993, 1001, 56, 53);
		panel.add(comboBox_24);
		
		JComboBox comboBox_25 = new JComboBox(chords);
		comboBox_25.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED){
					chordProgression[26] = ((String) e.getItem());
				}
			}
		});
		comboBox_25.setFont(new Font("SansSerif", Font.PLAIN, 25));
		comboBox_25.setBounds(2058, 1001, 56, 53);
		panel.add(comboBox_25);
		
		JComboBox comboBox_26 = new JComboBox(chords);
		comboBox_26.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED){
					chordProgression[27] = ((String) e.getItem());
				}
			}
		});
		comboBox_26.setFont(new Font("SansSerif", Font.PLAIN, 25));
		comboBox_26.setBounds(2123, 1001, 56, 53);
		panel.add(comboBox_26);
		
		JLabel label_6 = new JLabel("|");
		label_6.setFont(new Font("SansSerif", Font.PLAIN, 50));
		label_6.setBounds(2182, 1011, 12, 39);
		panel.add(label_6);
		
		JComboBox comboBox_27 = new JComboBox(chords);
		comboBox_27.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED){
					chordProgression[28] = ((String) e.getItem());
				}
			}
		});
		comboBox_27.setFont(new Font("SansSerif", Font.PLAIN, 25));
		comboBox_27.setBounds(2194, 1001, 56, 53);
		panel.add(comboBox_27);
		
		JComboBox comboBox_29 = new JComboBox(chords);
		comboBox_29.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED){
					chordProgression[30] = ((String) e.getItem());
					chordProgression[31] = ((String) e.getItem());
				}
			}
		});
		
		JComboBox comboBox_28 = new JComboBox(chords);
		comboBox_28.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED){
					chordProgression[29] = ((String) e.getItem());
				}
			}
		});
		comboBox_28.setFont(new Font("SansSerif", Font.PLAIN, 25));
		comboBox_28.setBounds(2257, 1001, 56, 53);
		panel.add(comboBox_28);
		
		JLabel label_7 = new JLabel("|");
		label_7.setFont(new Font("SansSerif", Font.PLAIN, 50));
		label_7.setBounds(2446, 1011, 12, 39);
		panel.add(label_7);
		
		JLabel label_8 = new JLabel("__________________________________________________________________________________________________________________\r\n");
		label_8.setBounds(1394, 927, 1049, 20);
		panel.add(label_8);
		
		
		
		
		JLabel lblAutowriter = new JLabel("Auto Writer");
		lblAutowriter.setVerticalAlignment(SwingConstants.TOP);
		lblAutowriter.setHorizontalAlignment(SwingConstants.CENTER);
		lblAutowriter.setFont(new Font("SansSerif", Font.BOLD, 50));
		lblAutowriter.setBounds(377, 363, 854, 64);
		panel.add(lblAutowriter);
		
		JLabel lblCustomWriter = new JLabel("Custom Writer");
		lblCustomWriter.setVerticalAlignment(SwingConstants.TOP);
		lblCustomWriter.setHorizontalAlignment(SwingConstants.CENTER);
		lblCustomWriter.setFont(new Font("SansSerif", Font.BOLD, 50));
		lblCustomWriter.setBounds(1459, 363, 854, 64);
		panel.add(lblCustomWriter);
		
		JCheckBox neighboring_tones = new JCheckBox("Neighboring Tones\r\n");
		neighboring_tones.setFont(new Font("SansSerif", Font.PLAIN, 25));
		neighboring_tones.setSelected(true);
		neighboring_tones.setBounds(1818, 1129, 280, 29);
		panel.add(neighboring_tones);
		
		JCheckBox chckbxPassingTones = new JCheckBox("Passing Tones\r\n");
		chckbxPassingTones.setSelected(true);
		chckbxPassingTones.setFont(new Font("SansSerif", Font.PLAIN, 25));
		chckbxPassingTones.setBounds(1818, 1092, 231, 29);
		panel.add(chckbxPassingTones);
		
		JCheckBox chckbxSuspension = new JCheckBox("Suspensions");
		chckbxSuspension.setSelected(true);
		chckbxSuspension.setFont(new Font("SansSerif", Font.PLAIN, 25));
		chckbxSuspension.setBounds(1818, 1166, 231, 29);
		panel.add(chckbxSuspension);
		
		JCheckBox escape_tones = new JCheckBox("Escape Tones");
		escape_tones.setSelected(true);
		escape_tones.setFont(new Font("SansSerif", Font.PLAIN, 25));
		escape_tones.setBounds(1818, 1203, 231, 29);
		panel.add(escape_tones);
		
		JCheckBox appoggiaturas = new JCheckBox("Appoggiaturas");
		appoggiaturas.setSelected(true);
		appoggiaturas.setFont(new Font("SansSerif", Font.PLAIN, 25));
		appoggiaturas.setBounds(1818, 1240, 231, 29);
		panel.add(appoggiaturas);
		
		JLabel lblCertainNonharmonicsAre = new JLabel("<html>*Certain nonharmonics are only added if they do not<br> compromise the quality of the piece. Anticipations <br>are added at each cadence at the <br>BachBot's discretion</html>");
		lblCertainNonharmonicsAre.setFont(new Font("SansSerif", Font.PLAIN, 16));
		lblCertainNonharmonicsAre.setBounds(2256, 1502, 384, 100);
		panel.add(lblCertainNonharmonicsAre);
		
		JButton btnWriteSongAutomatically = new JButton("Write Song Automatically");
		btnWriteSongAutomatically.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				song = new Song();
				PlaySong.playSong(song, player);
			}
		});
		btnWriteSongAutomatically.setFont(new Font("SansSerif", Font.BOLD, 40));
		btnWriteSongAutomatically.setBounds(551, 1335, 575, 100);
		panel.add(btnWriteSongAutomatically);
		
		JButton btnWriteCustomSong = new JButton("Write Custom Song");
		btnWriteCustomSong.setFont(new Font("SansSerif", Font.BOLD, 40));
		btnWriteCustomSong.setBounds(1725, 1335, 437, 100);
		panel.add(btnWriteCustomSong);
		btnWriteCustomSong.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				int keySig = -1;
				for(int i =0; i<chordProgression.length; i++)
				{
					finalChordProgression.set(i,Song.numeralToInteger(chordProgression[i]));
				}
				finalChordProgression.add(0,0);
				for(int i =0; i<keySignatures.size();i++)
				{
					if(key.equals(keySignatures.get(i).get(1)))
					{
						keySig = i;
					}
				}
				boolean passing,neighboring,suspension,escape,appoggiatura;
				if(neighboring_tones.isSelected()){
					neighboring = true;
				}
				else{
					neighboring = false;
				}
				if(chckbxPassingTones.isSelected()){
					passing = true;
				}
				else{
					passing = false;
				}
				if(chckbxSuspension.isSelected()){
					suspension = true;
				}
				else{
					suspension = false;
				}
				if(escape_tones.isSelected()){
					escape = true;
				}
				else{
					escape = false;
				}
				if(appoggiaturas.isSelected())
				{
					appoggiatura = true;
				}
				else{
					appoggiatura = false;
				}
				song = new Song(keySig,tempo,finalInstrument,finalChordProgression,passing,neighboring,suspension,escape,appoggiatura);
				PlaySong.playSong(song,player);
			}
		});
		
		JLabel authors = new JLabel("<html>Created by Joe McAllister and Benjamin Hahn<br>\r\nInquiries/Contact: joseph.l.mcallister@gmail.com</html>");
		authors.setVerticalAlignment(SwingConstants.TOP);
		authors.setHorizontalAlignment(SwingConstants.CENTER);
		authors.setFont(new Font("SansSerif", Font.PLAIN, 24));
		authors.setBounds(1803, 205, 1079, 90);
		panel.add(authors);
		comboBox_29.setFont(new Font("SansSerif", Font.PLAIN, 25));
		comboBox_29.setBounds(2322, 1001, 108, 53);
		panel.add(comboBox_29);
		
		JButton btnSaveSong = new JButton("Save Song");
		btnSaveSong.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				
				JFileChooser chooser =new JFileChooser();
				//chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				chooser.showSaveDialog(null);
				String path=chooser.getSelectedFile().getAbsolutePath();
				String filename=chooser.getSelectedFile().getName();
				File file = new File(path + ".midi");
				try {
					player.saveMidi("T[" + song.tempo +"]" +  " V0 " + "I" + song.instrument + " " +song.bassLine + " "  +" V1 I" + song.instrument +" " + song.tenorLine+ " V2 I" +  song.instrument + " " +  song.altoLine +  " V3 " + " I" +song.instrument + " " + song.sopranoLine, file);
				} catch (IOException e1) {
					System.out.println("Error saving file");
				}
				
			}
		});
		btnSaveSong.setForeground(Color.BLACK);
		btnSaveSong.setFont(new Font("SansSerif", Font.BOLD | Font.ITALIC, 40));
		btnSaveSong.setBounds(1258, 1525, 321, 100);
		panel.add(btnSaveSong);

		
		fillKeysArray();

		
	}
	public int instrument(String instrument)
	{
		if(instrument.equals("Piano"))
		{
			return 0;
		}
		else if(instrument.equals("Church Organ"))
		{
			return 19;
		}
		else if(instrument.equals("Marimba"))
		{
			return 12;
		}
		else if(instrument.equals("Strings"))
		{
			return 48;
		}
		return 1;
	}
	public void fillKeysArray() //made void from ArrayList<String>
	{
		ArrayList<String> cMajor = new ArrayList<String>(); 
		cMajor.add("cMajor"); cMajor.add("C"); cMajor.add("D"); cMajor.add("E"); cMajor.add("F"); cMajor.add("G"); cMajor.add("A"); cMajor.add("B"); 
		ArrayList<String> gMajor = new ArrayList<String>();
		gMajor.add("gMajor"); gMajor.add("G"); gMajor.add("A"); gMajor.add("B"); gMajor.add("C"); gMajor.add("D"); gMajor.add("E"); gMajor.add("F#"); 
		ArrayList<String> dMajor = new ArrayList<String>();
		dMajor.add("dMajor");dMajor.add("D");dMajor.add("E");dMajor.add("F#");dMajor.add("G");dMajor.add("A");dMajor.add("B");dMajor.add("C#");
		ArrayList<String> aMajor = new ArrayList<String>();
		aMajor.add("aMajor");aMajor.add("A");aMajor.add("B");aMajor.add("C#");aMajor.add("D");aMajor.add("E");aMajor.add("F#");aMajor.add("G#");
		ArrayList<String> eMajor = new ArrayList<String>();
		eMajor.add("eMajor");eMajor.add("E");eMajor.add("F#");eMajor.add("G#");eMajor.add("A");eMajor.add("B");eMajor.add("C#");eMajor.add("D#");
		ArrayList<String> bMajor = new ArrayList<String>();
		bMajor.add("bMajor");bMajor.add("B");bMajor.add("C#");bMajor.add("D#");bMajor.add("E");bMajor.add("F#");bMajor.add("G#");bMajor.add("A#");
		ArrayList<String> fSharpMajor = new ArrayList<String>();
		fSharpMajor.add("fSharpMajor");fSharpMajor.add("F#");fSharpMajor.add("G#");fSharpMajor.add("A#");fSharpMajor.add("B");fSharpMajor.add("C#");fSharpMajor.add("D#");fSharpMajor.add("E#");
		ArrayList<String> cSharpMajor = new ArrayList<String>();
		cSharpMajor.add("cSharpMajor");cSharpMajor.add("C#");cSharpMajor.add("D#");cSharpMajor.add("E#");cSharpMajor.add("F#");cSharpMajor.add("G#");cSharpMajor.add("A#");cSharpMajor.add("B#");
		ArrayList<String> fMajor = new ArrayList<String>();
		fMajor.add("fMajor");fMajor.add("F");fMajor.add("G");fMajor.add("A");fMajor.add("Bb");fMajor.add("C");fMajor.add("D");fMajor.add("E");
		ArrayList<String> bFlatMajor = new ArrayList<String>();
		bFlatMajor.add("bFlatMajor");bFlatMajor.add("Bb");bFlatMajor.add("C");bFlatMajor.add("D");bFlatMajor.add("Eb");bFlatMajor.add("F");bFlatMajor.add("G");bFlatMajor.add("A");
		ArrayList<String> eFlatMajor = new ArrayList<String>();
		eFlatMajor.add("eFlatMajor");eFlatMajor.add("Eb");eFlatMajor.add("F");eFlatMajor.add("G");eFlatMajor.add("Ab");eFlatMajor.add("Bb");eFlatMajor.add("C");eFlatMajor.add("D");
		ArrayList<String> aFlatMajor = new ArrayList<String>();
		aFlatMajor.add("aFlatMajor");aFlatMajor.add("Ab");aFlatMajor.add("Bb");aFlatMajor.add("C");aFlatMajor.add("Db");aFlatMajor.add("Eb");aFlatMajor.add("F");aFlatMajor.add("G");
		ArrayList<String> dFlatMajor = new ArrayList<String>();
		dFlatMajor.add("dFlatMajor");dFlatMajor.add("Db");dFlatMajor.add("Eb");dFlatMajor.add("F");dFlatMajor.add("Gb");dFlatMajor.add("Ab");dFlatMajor.add("Bb");dFlatMajor.add("C");
		ArrayList<String> gFlatMajor = new ArrayList<String>();
		gFlatMajor.add("gFlatMajor");gFlatMajor.add("Gb");gFlatMajor.add("Ab");gFlatMajor.add("Bb");gFlatMajor.add("Cb");gFlatMajor.add("Db");gFlatMajor.add("Eb");gFlatMajor.add("F");
		ArrayList<String> cFlatMajor = new ArrayList<String>();
		cFlatMajor.add("cFlatMajor");cFlatMajor.add("Cb");cFlatMajor.add("Db");cFlatMajor.add("Eb");cFlatMajor.add("Fb");cFlatMajor.add("Gb");cFlatMajor.add("Ab");cFlatMajor.add("Bb");
		
		keySignatures.add(cMajor); keySignatures.add(gMajor); keySignatures.add(dMajor);keySignatures.add(aMajor);keySignatures.add(eMajor);keySignatures.add(bMajor);keySignatures.add(fSharpMajor);keySignatures.add(cSharpMajor);
		keySignatures.add(fMajor);keySignatures.add(bFlatMajor);keySignatures.add(eFlatMajor);keySignatures.add(aFlatMajor);keySignatures.add(dFlatMajor);keySignatures.add(gFlatMajor); keySignatures.add(cFlatMajor);
        
	}
	private static void addPopup(Component component, final JPopupMenu popup) {
		component.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			private void showMenu(MouseEvent e) {
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		});
	}
}
