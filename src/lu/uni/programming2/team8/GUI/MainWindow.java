package lu.uni.programming2.team8.GUI;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Window.Type;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import java.awt.FlowLayout;
import java.awt.ScrollPane;
import javax.swing.JDesktopPane;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.ScrollPaneConstants;
import javax.swing.JScrollBar;
import javax.swing.JTextPane;
import javax.swing.JTextField;

public class MainWindow {

	private JFrame frmCubeescape;
	private JTextField textField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow window = new MainWindow();
					window.frmCubeescape.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmCubeescape = new JFrame();
		frmCubeescape.setTitle("CubeEscape");
		frmCubeescape.setResizable(false);
		frmCubeescape.setBounds(100, 100, 800, 600);
		frmCubeescape.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmCubeescape.getContentPane().setLayout(null);
		
		JPanel TimerPanel = new JPanel();
		TimerPanel.setBounds(642, 6, 152, 90);
		frmCubeescape.getContentPane().add(TimerPanel);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBounds(642, 108, 152, 464);
		frmCubeescape.getContentPane().add(panel_2);
		
		JPanel panel = new JPanel();
		panel.setBounds(6, 128, 183, 444);
		frmCubeescape.getContentPane().add(panel);
		panel.setLayout(null);
		
		JScrollBar scrollBar = new JScrollBar();
		scrollBar.setBounds(162, 12, 15, 432);
		panel.add(scrollBar);
		
		JScrollBar scrollBar_1 = new JScrollBar();
		scrollBar_1.setOrientation(JScrollBar.HORIZONTAL);
		scrollBar_1.setBounds(6, 423, 171, 15);
		panel.add(scrollBar_1);
		
		JTextPane textPane = new JTextPane();
		textPane.setBounds(201, 527, 429, 45);
		frmCubeescape.getContentPane().add(textPane);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(201, 6, 429, 503);
		frmCubeescape.getContentPane().add(scrollPane);
		
		JTextPane textPane_1 = new JTextPane();
		scrollPane.setViewportView(textPane_1);
		
		textField = new JTextField();
		textField.setBounds(201, 527, 429, 45);
		frmCubeescape.getContentPane().add(textField);
		textField.setColumns(10);
	}

	public ScrollPane getScrollPane() {
		return scrollPane;
	}
}
