import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.JLabel;

public class AutoanalyserWindow {

	private JFrame frame;
	private JTextField txtressamplecode;
	private JTextField txtresoutputsamplecodejava;
	private JButton button;
	private JButton button_1;
	private JButton btnGenerateJava;
	private JButton btnCompile;
	private JButton btnRun;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AutoanalyserWindow window = new AutoanalyserWindow();
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
	public AutoanalyserWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 981, 518);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		txtressamplecode = new JTextField();
		txtressamplecode.setText("/res/sample_code");
		txtressamplecode.setBounds(39, 69, 116, 22);
		frame.getContentPane().add(txtressamplecode);
		txtressamplecode.setColumns(10);
		
		txtresoutputsamplecodejava = new JTextField();
		txtresoutputsamplecodejava.setText("/res/output/sample_code.java");
		txtresoutputsamplecodejava.setBounds(41, 148, 195, 22);
		frame.getContentPane().add(txtresoutputsamplecodejava);
		txtresoutputsamplecodejava.setColumns(10);
		
		JLabel lblInput = new JLabel("Input");
		lblInput.setBounds(62, 34, 78, 31);
		frame.getContentPane().add(lblInput);
		
		JLabel lbOutput = new JLabel("Output");
		lbOutput.setBounds(62, 125, 56, 16);
		frame.getContentPane().add(lbOutput);
		
		button = new JButton("...");
		button.setBounds(167, 68, 33, 22);
		frame.getContentPane().add(button);
		
		button_1 = new JButton("...");
		button_1.setBounds(248, 147, 33, 22);
		frame.getContentPane().add(button_1);
		
		btnGenerateJava = new JButton("Generate JAVA");
		btnGenerateJava.setBounds(121, 214, 195, 25);
		frame.getContentPane().add(btnGenerateJava);
		
		btnCompile = new JButton("Compile");
		btnCompile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnCompile.setBounds(121, 282, 195, 25);
		frame.getContentPane().add(btnCompile);
		
		btnRun = new JButton("Run");
		btnRun.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				/*Class2 nw = new Class2();
				nw.NewS*/
			}
		});
		btnRun.setBounds(121, 352, 195, 25);
		frame.getContentPane().add(btnRun);
	}
}
