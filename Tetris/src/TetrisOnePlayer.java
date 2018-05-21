



import java.awt.EventQueue;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.Label;
import java.awt.Panel;







public class TetrisOnePlayer extends JFrame {

	private JPanel contentPane;

	
	

	/**
	 * Create the frame.
	 */
	public TetrisOnePlayer() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 500, 650);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(176, 224, 230));
		
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel PanelHold = new JPanel();
		PanelHold.setBackground(Color.DARK_GRAY);
		PanelHold.setBounds(10, 105, 100, 100);
		contentPane.add(PanelHold);
		
		JPanel MainPanel = new JPanel();
		MainPanel.setBackground(Color.DARK_GRAY);
		MainPanel.setBounds(120, 65, 200, 420);
		contentPane.add(MainPanel);
		
		JPanel PanelNext = new JPanel();
		PanelNext.setBackground(Color.DARK_GRAY);
		PanelNext.setBounds(330, 105, 100, 100);
		contentPane.add(PanelNext);
		
		JPanel PanelNextBoard = new JPanel();
		PanelNextBoard.setBackground(Color.DARK_GRAY);
		PanelNextBoard.setBounds(330, 225, 100, 240);
		contentPane.add(PanelNextBoard);
		
		JPanel PanelTop = new JPanel();
		PanelTop.setBackground(new Color(0, 128, 128));
		PanelTop.setBounds(0, 0, 478, 65);
		contentPane.add(PanelTop);
		PanelTop.setLayout(null);
		
		JLabel lblTetrisGame = new JLabel("Tetris Game   ");
		lblTetrisGame.setBounds(122, 15, 227, 35);
		PanelTop.add(lblTetrisGame);
		lblTetrisGame.setFont(new Font("굴림", Font.BOLD, 30));
		
		JButton btnStart = new JButton("시작하기");
		btnStart.setBounds(336, 520, 125, 29);
		contentPane.add(btnStart);
		
		JButton btnEnd = new JButton("나가기");
		btnEnd.setBounds(336, 550, 125, 29);
		btnEnd.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				dispose();
			}
		});
		contentPane.add(btnEnd);
		
	
		
		JLabel lblHold = new JLabel("H O L D");
		lblHold.setBounds(25, 80, 78, 21);
		contentPane.add(lblHold);
		
		JLabel lblNext = new JLabel("N E X T");
		lblNext.setBounds(352, 80, 78, 21);
		contentPane.add(lblNext);
		
		
		
	}
}
