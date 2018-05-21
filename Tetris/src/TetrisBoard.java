
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JLabel;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.SwingConstants;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.UIManager;

public class TetrisBoard extends JFrame {

	private JPanel contentPane;
	
	private int port;
	private String ip = "null", nickname = "null";

	public TetrisBoard() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 650);
		setResizable(false);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(176, 224, 230));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.setBounds(0, 0, 784, 26);
		contentPane.add(menuBar);
		
		JMenu mnNewMenu = new JMenu("게임 하기");
		menuBar.add(mnNewMenu);
		
		JMenuItem mntmNewMenuItem_Server = new JMenuItem("서버로 게임하기");
		mnNewMenu.add(mntmNewMenuItem_Server);
		
		JMenuItem mntmNewMenuItem_Client = new JMenuItem("클라이언트로 게임하기");
		mnNewMenu.add(mntmNewMenuItem_Client);
		
		JPanel panel = new JPanel();
		panel.setBackground(new Color(0, 128, 128));
		panel.setBounds(0, 26, 784, 40);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JLabel lblIp = new JLabel("ip : ");
		lblIp.setBounds(5, 0, 25, 15);
		panel.add(lblIp);
		
		JLabel lblIpValue = new JLabel();
		lblIpValue.setBounds(35, 0, 108, 15);
		lblIpValue.setText(ip);
		panel.add(lblIpValue);
		
		JLabel lblPort = new JLabel("Port : ");
		lblPort.setBounds(160, 0, 57, 15);
		panel.add(lblPort);
		
		JLabel lblNickname = new JLabel("닉네임 : ");
		lblNickname.setBounds(5, 20, 50, 15);
		panel.add(lblNickname);
		
		JLabel lblNickNameValue = new JLabel();
		lblNickNameValue.setBounds(60, 20, 108, 15);
		lblNickNameValue.setText(nickname);
		panel.add(lblNickNameValue);
		
		JPanel panel_My = new JPanel();
		panel_My.setBackground(Color.DARK_GRAY);
		panel_My.setForeground(Color.BLACK);
		panel_My.setBounds(120, 65, 200, 420);
		contentPane.add(panel_My);
		
		JLabel lblHold = new JLabel("HOLD");
		lblHold.setHorizontalAlignment(SwingConstants.CENTER);
		lblHold.setFont(new Font("굴림", Font.BOLD, 18));
		lblHold.setBounds(10, 80, 100, 20);
		contentPane.add(lblHold);
		
		JPanel panel_Hold = new JPanel();
		panel_Hold.setBackground(Color.DARK_GRAY);
		panel_Hold.setBounds(10, 105, 100, 100);
		contentPane.add(panel_Hold);
		
		JLabel lblNewLabel = new JLabel("NEXT");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setFont(new Font("굴림", Font.BOLD, 18));
		lblNewLabel.setBounds(330, 80, 100, 20);
		contentPane.add(lblNewLabel);
		
		JPanel panel_Next = new JPanel();
		panel_Next.setBackground(Color.DARK_GRAY);
		panel_Next.setBounds(330, 105, 100, 100);
		contentPane.add(panel_Next);
		
		JPanel panel_NextList = new JPanel();
		panel_NextList.setBackground(Color.DARK_GRAY);
		panel_NextList.setBounds(330, 225, 100, 240);
		contentPane.add(panel_NextList);
		
		JPanel panel_Your = new JPanel();
		panel_Your.setBackground(Color.DARK_GRAY);
		panel_Your.setBounds(500, 65, 200, 420);
		contentPane.add(panel_Your);
		
		JPanel panel_Chat = new JPanel();
		panel_Chat.setBounds(10, 500, 760, 100);
		contentPane.add(panel_Chat);
		panel_Chat.setLayout(null);
		
		JTextArea textArea = new JTextArea();
		textArea.setBackground(new Color(240, 255, 240));
		textArea.setBounds(0, 0, 630, 100);
		panel_Chat.add(textArea);
		
		JButton btnChat = new JButton("시작하기");
		btnChat.setFont(new Font("굴림", Font.PLAIN, 15));
		btnChat.setBackground(UIManager.getColor("Button.background"));
		btnChat.setBounds(630, 0, 130, 51);
		panel_Chat.add(btnChat);
		
		JButton btnEnd = new JButton("나가기");
		btnEnd.setFont(new Font("굴림", Font.PLAIN, 15));
		btnEnd.setBounds(630, 49, 130, 51);
		btnEnd.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				dispose();
			}
		});
		panel_Chat.add(btnEnd);
	}
}
