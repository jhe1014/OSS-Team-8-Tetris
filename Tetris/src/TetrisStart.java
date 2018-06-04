import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;


import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import java.awt.EventQueue;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class TetrisStart extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TetrisStart frame = new TetrisStart();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public TetrisStart() {
		
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 400, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblTitle = new JLabel("Tetris Game");
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		
		lblTitle.setFont(new Font("±¼¸²", Font.BOLD, 20));
		lblTitle.setBounds(125, 100, 130, 24);
		contentPane.add(lblTitle);
		
		JButton btn1P = new JButton("È¥ÀÚÇÏ±â");
		btn1P.setBounds(140, 135, 100, 30);
		btn1P.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				TetrisOnePlayer a = new TetrisOnePlayer();
				a.setVisible(true);
			}
		});
		contentPane.add(btn1P);
		
		JButton btn2P = new JButton("µÑÀÌÇÏ±â");
		btn2P.setBounds(140, 165, 100, 30);
		btn2P.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				TetrisBoard a = new TetrisBoard();
				a.setVisible(true);
			}
		});
		contentPane.add(btn2P);
	}
}
