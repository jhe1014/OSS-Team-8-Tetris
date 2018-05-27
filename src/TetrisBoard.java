import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JLabel;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.SwingConstants;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.UIManager;

public class TetrisBoard extends JFrame {

	private JPanel contentPane;

	private int port;
	public String ip = "null", nickname = "null";
	
	private boolean svst = false; // 서버로 접속했는지 확인하기 위해 만든 변수
	private boolean ctst = false; // 클라이언트로 접속했는지 확인하기 위해 만든 변수
	
	JLabel lblIpValue = new JLabel();
	JLabel lblPortValue = new JLabel();
	JLabel lblNickNameValue = new JLabel();
	
	JTextField txsip = new JTextField("127.0.0.1"); // 서버창에서 ip 입력하는 칸
	JTextField txspt = new JTextField(""); // 서버창에서 포트번호 입력하는 칸
	JTextField txsnick = new JTextField(""); // 서버창에서 닉네임 입력하는 칸
	
	JTextField txcip = new JTextField("127.0.0.1"); // 클라이언트창에서 ip 입력하는 칸
	JTextField txcpt = new JTextField(""); // 클라이언트창에서 포트번호 입력하는 칸
	JTextField txcnick = new JTextField(""); // 클라이언트창에서 닉네임 입력하는 칸

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
		mntmNewMenuItem_Server.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (svst == false && ctst == false) {
					Serverframe svf = new Serverframe();
					svf.setVisible(true);
				}
				
				else { // 어느 한 쪽 모드로라도 접속 중인 경우 오류 메시지 띄움
					JOptionPane.showMessageDialog(null, "이미 접속 중입니다!", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		mnNewMenu.add(mntmNewMenuItem_Server);
		
		JMenuItem mntmNewMenuItem_Client = new JMenuItem("클라이언트로 게임하기");
		mntmNewMenuItem_Client.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (ctst == false && ctst == false) {
					Clientframe ctf = new Clientframe();
					ctf.setVisible(true);
				}
				
				else { // 어느 한 쪽 모드로라도 접속 중인 경우 오류 메시지 띄움
					JOptionPane.showMessageDialog(null, "이미 접속 중입니다!", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		mnNewMenu.add(mntmNewMenuItem_Client);
		
		JPanel panel = new JPanel();
		panel.setBackground(new Color(0, 128, 128));
		panel.setBounds(0, 26, 784, 40);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JLabel lblIp = new JLabel("ip : ");
		lblIp.setBounds(5, 0, 25, 15);
		panel.add(lblIp);
		
		
		lblIpValue.setBounds(35, 0, 108, 15);
		lblIpValue.setText(ip);
		panel.add(lblIpValue);
		
		JLabel lblPort = new JLabel("Port : ");
		lblPort.setBounds(160, 0, 57, 15);
		panel.add(lblPort);
		
		lblPortValue.setBounds(195,0, 108, 15);
		panel.add(lblPortValue);
		
		JLabel lblNickname = new JLabel("닉네임 : ");
		lblNickname.setBounds(5, 20, 50, 15);
		panel.add(lblNickname);
		
		
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

	
	class Serverframe extends JFrame {
		public Serverframe() {
			super("Serverframe");
			setResizable(false);
			setSize(400, 250);
			getContentPane().setLayout(null);
			
			JLabel svip = new JLabel("IP주소");
			svip.setFont(new Font("굴림", Font.BOLD, 15));
			svip.setBounds(31, 27, 78, 21);
			getContentPane().add(svip);
			
			JLabel svpt = new JLabel("포트번호");
			svpt.setFont(new Font("굴림", Font.BOLD, 15));
			svpt.setBounds(31, 63, 105, 21);
			getContentPane().add(svpt);
			
			JLabel svnick = new JLabel("닉네임");
			svnick.setFont(new Font("굴림", Font.BOLD, 15));
			svnick.setBounds(31, 99, 110, 21);
			getContentPane().add(svnick);
			
			txsip.setBounds(164, 24, 200, 27);
			getContentPane().add(txsip);
			txsip.setColumns(10);
			
			txspt.setBounds(164, 60, 200, 27);
			getContentPane().add(txspt);
			
			txsnick.setBounds(164, 96, 200, 27);
			getContentPane().add(txsnick);
			
			JButton btnOKButton = new JButton("OK");
			btnOKButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String tfsip = "";
					String tfspt = "";
					String tfsnick = "";
					
					tfsip = txsip.getText();
					tfspt = txspt.getText();
					tfsnick = txsnick.getText();
					
					svst = true;
					
					lblIpValue.setText(tfsip);
					lblPortValue.setText(tfspt);
					lblNickNameValue.setText(tfsnick);
					
					setVisible(false);
				}
			});
			
			btnOKButton.setFont(new Font("굴림", Font.BOLD, 18));
			btnOKButton.setBounds(55, 150, 120, 35);
			getContentPane().add(btnOKButton);
			
			JButton btnCancelButton = new JButton("Cancle");
			btnCancelButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
						txsip.setText("127.0.0.1");
						txspt.setText(""); 
						setVisible(false);
				}
			});
			btnCancelButton.setFont(new Font("굴림", Font.BOLD, 18));
			btnCancelButton.setBounds(217, 150, 120, 35);
			getContentPane().add(btnCancelButton);
		}
	}
		
		class Clientframe extends JFrame {
			public Clientframe() {
				super("Clientframe");
				setResizable(false);
				setSize(400, 250);
				getContentPane().setLayout(null);
				
				JLabel ctip = new JLabel("IP주소");
				ctip.setFont(new Font("굴림", Font.BOLD, 15));
				ctip.setBounds(31, 27, 78, 21);
				getContentPane().add(ctip);
				
				JLabel ctpt = new JLabel("포트번호");
				ctpt.setFont(new Font("굴림", Font.BOLD, 15));
				ctpt.setBounds(31, 63, 105, 21);
				getContentPane().add(ctpt);
				
				JLabel ctnick = new JLabel("닉네임");
				ctnick.setFont(new Font("굴림", Font.BOLD, 15));
				ctnick.setBounds(31, 99, 110, 21);
				getContentPane().add(ctnick);
				
				txcip.setBounds(164, 24, 200, 27);
				getContentPane().add(txcip);
				txcip.setColumns(10);
				
				txcpt.setBounds(164, 60, 200, 27);
				getContentPane().add(txcpt);
				
				txcnick.setBounds(164, 96, 200, 27);
				getContentPane().add(txcnick);
				
				JButton btnOKButton = new JButton("OK");
				btnOKButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						String tfcip = "";
						String tfcpt = "";
						String tfcnick = "";
						
						tfcip = txcip.getText();
						tfcpt = txcpt.getText();
						tfcnick = txcnick.getText();
						
						ctst = true;
						
						lblIpValue.setText(tfcip);
					    lblPortValue.setText(tfcpt);
					    lblNickNameValue.setText(tfcnick);
						
						setVisible(false);
					}
				});
				
				btnOKButton.setFont(new Font("굴림", Font.BOLD, 18));
				btnOKButton.setBounds(55, 150, 120, 35);
				getContentPane().add(btnOKButton);
				
				JButton btnCancelButton = new JButton("Cancle");
				btnCancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
							txcip.setText("127.0.0.1");
							txcpt.setText(""); 
							setVisible(false);
					}
				});
				btnCancelButton.setFont(new Font("굴림", Font.BOLD, 18));
				btnCancelButton.setBounds(217, 150, 120, 35);
				getContentPane().add(btnCancelButton);
			}
		}
}
