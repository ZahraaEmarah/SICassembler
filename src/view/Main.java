package view;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import controller.Controller;
import controller.FixedController;
import memory.AdjacencyMatrix;

public class Main {

	private JFrame frame;
	JRadioButton rdbtnFixedform;
	FixedController fix = new FixedController();
	Controller f = new Controller();
	int state;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main window = new Main();
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
	public Main() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {

		frame = new JFrame();
		frame.setBounds(100, 100, 1299, 731);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JPanel panel = new JPanel();
		panel.setBackground(new Color(0, 128, 128));
		panel.setBounds(10, 0, 1350, 692);
		frame.getContentPane().add(panel);
		panel.setLayout(null);

		AdjacencyMatrix m = new AdjacencyMatrix();

		JButton btnNewButton = new JButton("Dump memory contents");
		btnNewButton.setBackground(new Color(192, 192, 192));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				m.print();
				m.makeLocation(5, 5, 9);
				m.print();
			}
		});
		btnNewButton.setBounds(675, 460, 305, 35);
		btnNewButton.setVisible(false);
		panel.add(btnNewButton);

		JLabel lblSicxeAssembler = new JLabel("SIC/XE Assembler");
		lblSicxeAssembler.setForeground(new Color(255, 255, 224));
		lblSicxeAssembler.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblSicxeAssembler.setHorizontalAlignment(SwingConstants.CENTER);
		lblSicxeAssembler.setBounds(555, 22, 195, 29);
		panel.add(lblSicxeAssembler);

		JRadioButton rdbtnFreeform = new JRadioButton("Free-Form");
		rdbtnFreeform.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				rdbtnFixedform.setSelected(false);
			}
		});
		rdbtnFreeform.setForeground(new Color(255, 255, 224));
		rdbtnFreeform.setBackground(new Color(0, 128, 128));
		rdbtnFreeform.setBounds(565, 58, 96, 23);
		panel.add(rdbtnFreeform);

		rdbtnFixedform = new JRadioButton("Fixed-Form");
		rdbtnFixedform.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				rdbtnFreeform.setSelected(false);
			}
		});
		rdbtnFixedform.setForeground(new Color(255, 255, 224));
		rdbtnFixedform.setBackground(new Color(0, 128, 128));
		rdbtnFixedform.setBounds(663, 58, 109, 23);
		panel.add(rdbtnFixedform);

		JLabel lblSourceFile = new JLabel("Source File:");
		lblSourceFile.setHorizontalAlignment(SwingConstants.CENTER);
		lblSourceFile.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblSourceFile.setForeground(new Color(255, 255, 224));
		lblSourceFile.setBounds(56, 87, 109, 29);
		panel.add(lblSourceFile);

		JTextArea textArea = new JTextArea();
		textArea.setBounds(56, 128, 575, 241);
		panel.add(textArea);

		JLabel lblListFile = new JLabel("List File:");
		lblListFile.setForeground(new Color(255, 255, 224));
		lblListFile.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblListFile.setBounds(673, 87, 109, 29);
		panel.add(lblListFile);

		JTextArea textArea_1 = new JTextArea();
		textArea_1.setEditable(false);
		textArea_1.setBounds(658, 128, 575, 241);
		panel.add(textArea_1);

		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(35, 416, 1214, 2);
		panel.add(separator_1);

		JLabel lblObjectFile = new JLabel("Object File:");
		lblObjectFile.setForeground(new Color(255, 255, 224));
		lblObjectFile.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblObjectFile.setBounds(65, 426, 86, 29);
		panel.add(lblObjectFile);

		JTextArea textArea_2 = new JTextArea();
		textArea_2.setBounds(0, 0, 573, 241);
		panel.add(textArea_2);
		textArea_2.setEditable(false);

		JScrollPane scrollPane = new JScrollPane(textArea_1);
		scrollPane.setBounds(657, 128, 576, 241);
		panel.add(scrollPane);

		JScrollPane scrollPane_1 = new JScrollPane(textArea);
		scrollPane_1.setBounds(56, 128, 575, 241);
		panel.add(scrollPane_1);

		JScrollPane scrollPane_2 = new JScrollPane(textArea_2);
		scrollPane_2.setBounds(56, 454, 575, 200);
		panel.add(scrollPane_2);

		JButton btnAssemble = new JButton("Assemble");
		btnAssemble.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (rdbtnFixedform.isSelected()) {
					fix = new FixedController();
					fix.TakeFromGUI(textArea);
					fix.ReadFixedFile();

					if (fix.state == 0)
						JOptionPane.showMessageDialog(panel, "SUCCESSFUL ASSEMBLY", "SIC/XE Assembler",
								JOptionPane.INFORMATION_MESSAGE);
					else {
						JOptionPane.showMessageDialog(panel, "INCOMPLETE ASSEMBLY", "SIC/XE Assembler",
								JOptionPane.ERROR_MESSAGE);
					}

					fix.DisplayinGUI(textArea_1);
					fix.DisplayOBJinGUI(textArea_2);
				} else if (rdbtnFreeform.isSelected()) {
					f = new Controller();
					f.TakeFromGUI(textArea);
					f.ReadFile();

					if (f.state == 0)
						JOptionPane.showMessageDialog(panel, "SUCCESSFUL ASSEMBLY", "SIC/XE Assembler",
								JOptionPane.INFORMATION_MESSAGE);
					else {
						JOptionPane.showMessageDialog(panel, "INCOMPLETE ASSEMBLY", "SIC/XE Assembler",
								JOptionPane.ERROR_MESSAGE);
					}

					f.DisplayinGUI(textArea_1);
					f.DisplayOBJinGUI(textArea_2);
				} else {
					JOptionPane.showMessageDialog(panel, "You must choose an assembly form", "SIC/XE Assembler",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		btnAssemble.setBackground(new Color(192, 192, 192));
		btnAssemble.setBounds(555, 380, 195, 25);
		panel.add(btnAssemble);

	}
}