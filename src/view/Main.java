package view;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import controller.Controller;
import controller.FixedController;
import memory.AdjacencyMatrix;

public class Main {

	private JFrame frame;
	FixedController fix = new FixedController();
	Controller f = new Controller();

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

		f = new Controller();
		f.ReadFile();

		fix = new FixedController();
		fix.ReadFixedFile();

		frame = new JFrame();
		frame.setBounds(100, 100, 977, 617);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JPanel panel = new JPanel();
		panel.setBackground(new Color(0, 128, 128));
		panel.setBounds(0, 0, 961, 578);
		frame.getContentPane().add(panel);
		panel.setLayout(null);

		JLabel fixedlabel = new JLabel("");
		fixedlabel.setForeground(new Color(255, 255, 224));
		fixedlabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		fixedlabel.setHorizontalAlignment(SwingConstants.CENTER);
		fixedlabel.setBounds(64, 45, 195, 46);
		panel.add(fixedlabel);

		JLabel lblFixedForm = new JLabel("Fixed Form :");
		lblFixedForm.setForeground(new Color(255, 255, 224));
		lblFixedForm.setBounds(10, 11, 112, 29);
		panel.add(lblFixedForm);

		JLabel lblFreeForm = new JLabel("Free Form :");
		lblFreeForm.setForeground(new Color(255, 255, 224));
		lblFreeForm.setBounds(10, 102, 112, 29);
		panel.add(lblFreeForm);

		JLabel freelabel = new JLabel("");
		freelabel.setForeground(new Color(255, 255, 224));
		freelabel.setHorizontalAlignment(SwingConstants.CENTER);
		freelabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		freelabel.setBounds(64, 139, 195, 46);
		panel.add(freelabel);

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
		btnNewButton.setBounds(10, 532, 305, 35);
		panel.add(btnNewButton);

		if (fix.state == 0)
			fixedlabel.setText("SUCCESSFUL ASSEMBLY");
		else {
			fixedlabel.setText("INCOMPLETE ASSEMBLY");
		}

		if (f.state == 0)
			freelabel.setText("SUCCESSFUL ASSEMBLY");
		else {
			freelabel.setText("INCOMPLETE ASSEMBLY");
		}

	}
}