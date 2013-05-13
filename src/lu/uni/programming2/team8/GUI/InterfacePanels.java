package lu.uni.programming2.team8.GUI;

import java.awt.Color;

import javax.swing.JPanel;

import lu.uni.programming2.team8.entity.Player;

public class InterfacePanels extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1417035271286680231L;

	
	// CONSTRUCTOR
	public InterfacePanels(int x, int y, int width, int height) {
		setLayout(null);
		setBackground(new Color(224, 224, 224));
		setForeground(Color.WHITE);
		setBounds(x, y, width, height);
	}
	
	
	
	// Inner classes for all the different panels
	
	public class PlayerPanels extends InterfacePanels {

		/**
		 * 
		 */
		private static final long serialVersionUID = 906898426078895880L;
		
		private Player currentPlayer;
		


		public PlayerPanels(int x, int y, int width, int height) {
			super(x, y, width, height);
		}
		
	}
	
}
