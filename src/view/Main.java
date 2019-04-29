package view;

import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import controller.Controller;
import controller.FixedController;

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
		frame.setBounds(100, 100, 341, 248);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JPanel panel = new JPanel();
		panel.setBounds(0, 0, 325, 209);
		frame.getContentPane().add(panel);
		panel.setLayout(null);

		JLabel fixedlabel = new JLabel("");
		fixedlabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		fixedlabel.setHorizontalAlignment(SwingConstants.CENTER);
		fixedlabel.setBounds(64, 45, 195, 46);
		panel.add(fixedlabel);

		JLabel lblFixedForm = new JLabel("Fixed Form :");
		lblFixedForm.setBounds(10, 11, 112, 29);
		panel.add(lblFixedForm);

		JLabel lblFreeForm = new JLabel("Free Form :");
		lblFreeForm.setBounds(10, 102, 112, 29);
		panel.add(lblFreeForm);

		JLabel freelabel = new JLabel("");
		freelabel.setHorizontalAlignment(SwingConstants.CENTER);
		freelabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		freelabel.setBounds(64, 139, 195, 46);
		panel.add(freelabel);

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
