import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JLabel;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.SwingConstants;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.UIManager;

public class TetrisBoard extends JFrame {
	
	private static final long serialVersionUID = 1L;

	private JPanel contentPane;

	private int port;
	public String ip = "null", nickname = "null";

	// 서버 입력텍스트 내용 저장할 변수
	private String tfsip = "";
	private String tfspt = "";
	private String tfsnick = "";
	
	// 클라이언트 입력텍스트 내용 저장할 변수
	private String tfcip = ""; 
	private String tfcpt = ""; 
	private String tfcnick = "";
	
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
	
	private JTextField CTF; // 채팅메시지 입력칸
	JTextArea ChatArea; // 채팅메시지 띄우는 칸
	
	ServerSocket ssocket; // 서버 소켓
	Socket socket; // 클라이언트 소켓
	
	public TetrisBoard() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 700);
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
				if (svst == false && ctst == false) {
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
		panel_Chat.setBounds(10, 500, 760, 127);
		contentPane.add(panel_Chat);
		panel_Chat.setLayout(null);
		
		CTF = new JTextField();
		CTF.setBounds(0, 100, 630, 27);
		CTF.setBackground(new Color(240, 255, 240));
		panel_Chat.add(CTF);
		CTF.setColumns(10);
		
		JScrollPane Chatrange = new JScrollPane();
		Chatrange.setBounds(0, 0, 630, 100);
		panel_Chat.add(Chatrange);
		
		ChatArea = new JTextArea();
		Chatrange.setViewportView(ChatArea);
		ChatArea.setEditable(false);
		ChatArea.setLineWrap(true);

		JButton btnSend = new JButton("Send"); // 채팅 메시지 전송 버튼
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(ctst == true && svst == false) { // 클라이언트로 접속해 있는 경우 
					String s = CTF.getText().trim();
					
					if(s.length()==0) return;
					
					try {
						if(socket==null) return;
						
						PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);
						pw.println(tfcnick + "] " + s);
						
						ChatArea.append(tfcnick + "]" + s + "\n");
						
						CTF.setText("");
						CTF.requestFocus();
					} catch(Exception e2) {
						ChatArea.append("클라이언트가 접속을 해제함\n");
					}
					//C_send(text);
				}
				
				else if (svst == true && ctst == false) { // 서버로 접속해 있는 경우
						String s = CTF.getText().trim();
						
						if(s.length()==0) return;
						
						try {
							if(socket==null) return;
							
							PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);
							pw.println(tfsnick + "] " + s);
							
							ChatArea.append(tfsnick + "]" + s + "\n");
							
							CTF.setText("");
							CTF.requestFocus();
						} catch(Exception e2) {
							ChatArea.append("클라이언트가 접속을 해제함\n");
						}
					//S_send(text);
					}
				
				else {
					JOptionPane.showMessageDialog(null, "서버나 클라이언트로 접속되어있지않습니다!", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		btnSend.setBounds(630, 100, 130, 27);
		panel_Chat.add(btnSend);
		
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
	
	void startServer() {
		Thread thread = new Thread() {
			BufferedReader br; 
			
			public void run() {
				String cip = null;
				
				try {
					ssocket = new ServerSocket(Integer.parseInt(tfspt));
					ChatArea.append("서버 대기중\n");
					
					while (true) {
						socket = ssocket.accept();
						cip = socket.getInetAddress().getHostAddress();
						ChatArea.append("["+ cip +" 에서 서버에 접속함...]\n");
						br = new BufferedReader(
								new InputStreamReader (socket.getInputStream()));
						String msg = br.readLine();
						ChatArea.append(msg + "\n");
					}
				} catch (Exception e) {
					ChatArea.append("[" + cip + "가 접속을 해제하였습니다.");
				
					//if (!ssocket.isClosed()) {stopServer();};
					//closeAll();
					
					//System.out.println("접속 해제\n");
				}
			}
			
			private void closeAll() {
				try {
					if(br!=null) br.close();
					if(socket!=null) socket.close();
					if(ssocket!=null) ssocket.close();
				} catch(Exception e) {
					System.out.println("closeAll()에서 예외 : " + e);
				}
			}
		};
		thread.start();
	}
	
	/*void S_send(String data) {
		 Thread thread = new Thread() { //데이터를 서버로 보내는 작업 스레드
	         @Override
	         public void run() {
	            try {
	               byte[] byteArr = data.getBytes("UTF-8");

	               OutputStream outputStream = socket.getOutputStream();
	               outputStream.write(byteArr);
	               outputStream.flush();
	            } catch(Exception e) {
	               stopClient();  //클라이언트 멈춤
	            }            
	         }
	      };
	      thread.start(); //스레드 시작
	}*/
	
	void stopServer() {
		 try {
	    	  ChatArea.append("클라이언트와 연결 끊음.\n");
	    	  ChatArea.setText("");
	    
	         if(ssocket!=null && !ssocket.isClosed()) {
	            ssocket.close();
	         }
	      } catch (IOException e) {}
	}
	
	void startClient() { // 클라이언스타트 스레드의 메소드
	      Thread thread = new Thread() { //스레드 생성
	         @Override
	         public void run() { // run메소드 재정의
	        	 try {
	               socket = new Socket(); 
	               socket.connect(new InetSocketAddress(tfcip, Integer.parseInt(tfcpt)));  //지금 사용중인 IP ,포트번호 바인딩
	               ChatArea.append("[연결 완료: " + socket.getRemoteSocketAddress() + "]\n");
	            } catch(Exception e) {
	            	ChatArea.append("[서버 통신 안됨]\n");
	            	
	               if(!socket.isClosed()) { stopClient(); }

	               return;
	            }	    
	            C_receive(); //서버에서 보낸 데이터 받기	            
	         }
	      };   	      
	      thread.start();
	   }
	
	   void stopClient() { //클라이언트 정지 메소드
	      try {
	    	  ChatArea.append("서버와 연결 끊음.\n");
	    	  ChatArea.setText("");
	    
	         if(socket!=null && !socket.isClosed()) {
	            socket.close();
	         }
	      } catch (IOException e) {}
	   }   

	   void C_receive() { // 서버에서 보낸 데이터 받는 메소드
	      while(true) {
	         try {
	            /*byte[] byteArr = new byte[100];
	            InputStream inputStream = socket.getInputStream(); 
	            
	            int readByteCount = inputStream.read(byteArr);    //데이터받기

	            if(readByteCount == -1){
	               throw new IOException(); 
	            }

	            String data = new String(byteArr, 0, readByteCount, "UTF-8"); //문자열로 변환
	            ChatArea.append(data + "\n"); */
	        	 
	        	BufferedReader br = new BufferedReader(
							new InputStreamReader (socket.getInputStream()));
					while (true) {
						String msg = br.readLine();
						ChatArea.append(msg + "\n");
					}
	         } catch (Exception e) {
	            stopClient(); //클라이언트 멈춤
	            break;
	         }
	      }
	   }		   
	   
	   /*void C_send(String data) { 
		      Thread thread = new Thread() { //데이터를 서버로 보내는 작업 스레드
		         @Override
		         public void run() {
		            try {
		               byte[] byteArr = data.getBytes("UTF-8");

		               OutputStream outputStream = socket.getOutputStream();
		               outputStream.write(byteArr);
		               outputStream.flush();
		               
		            } catch(Exception e) {
		               stopClient();  //클라이언트 멈춤
		            }            
		         }
		      };
		      thread.start(); //스레드 시작	   
}*/

	class Serverframe extends JFrame{ // 서버로 접속하기 눌렀을 때 입력창
		
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
					tfsip = txsip.getText();
					tfspt = txspt.getText();
					tfsnick = txsnick.getText();
					
					svst = true;
					
					lblIpValue.setText(tfsip);
					lblPortValue.setText(tfspt);
					lblNickNameValue.setText(tfsnick);
					
					startServer();
					
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
		
		class Clientframe extends JFrame { // 클라이언트로 접속하기 눌렀을 때 입력창
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
						tfcip = txcip.getText();
						tfcpt = txcpt.getText();
						tfcnick = txcnick.getText();
						
						ctst = true;
						
						lblIpValue.setText(tfcip);
					    lblPortValue.setText(tfcpt);
					    lblNickNameValue.setText(tfcnick);
					    
					    startClient();
						
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
