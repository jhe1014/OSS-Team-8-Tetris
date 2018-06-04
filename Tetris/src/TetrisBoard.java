import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import com.tetris.blocks.Block;
import com.tetris.blocks.TetrisBlock;
import com.tetris.controller.TetrisController;
import com.tetris.shape.CenterUp;
import com.tetris.shape.LeftTwoUp;
import com.tetris.shape.LeftUp;
import com.tetris.shape.Line;
import com.tetris.shape.Nemo;
import com.tetris.shape.RightTwoUp;
import com.tetris.shape.RightUp;

import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JLabel;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.SwingConstants;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.UIManager;

public class TetrisBoard extends JFrame implements Runnable{
	
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
	JScrollPane Chatrange;
	
	ServerSocket ssocket; // 서버 소켓
	Socket socket; // 클라이언트 소켓
	
	private Thread th;
	public static final int BLOCK_SIZE = 20;
	public static final int BOARD_X = 120;
	public static final int BOARD_Y = 50;
	private int minX=1, minY=0, maxX=10, maxY=21, down=50, up=0;
	private ArrayList<Block> blockList;
	private ArrayList<TetrisBlock> nextBlocks;
	private Block map[][];
	private TetrisController controller;
	private TetrisBlock shap;
	private TetrisBlock hold;
	private Integer[] lv = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20};
	private JComboBox<Integer> comboSpeed = new JComboBox<Integer>(lv);
	
	private boolean isPlay = false;
	private boolean isHold = false;
	private int removeLineCount = 0;
	private int removeLineCombo = 0;
	
	
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
		
		JPanel panel_My = new JPanel() {
				public void paintComponent(Graphics g) {
					g.clearRect(0, 0, this.getWidth(), this.getHeight()+1);
					g.setColor(Color.BLACK);
					g.fillRect(0, 0, this.getWidth(), this.getHeight()+1);
					
					//그리드 표시
					g.setColor(Color.darkGray);
					for(int i=1;i<maxY;i++) g.drawLine(0 + BLOCK_SIZE*(minX-1), 0+BLOCK_SIZE*(i+minY), 0 + (maxX+minX-1)*BLOCK_SIZE, 0+BLOCK_SIZE*(i+minY));
					for(int i=1;i<maxX;i++) g.drawLine(0 + BLOCK_SIZE*(i+minX-1), 0 + BLOCK_SIZE*minY, 0 + BLOCK_SIZE*(i+minX-1), 0 + BLOCK_SIZE*(minY+maxY));
				
					if(blockList!=null){
						int x=0, y=0;
						for(int i = 0 ; i<blockList.size() ; i++){
							Block block = blockList.get(i);
							x = block.getPosGridX();
							y = block.getPosGridY();
							block.setPosGridX(x+minX);
							block.setPosGridY(y+minY);
							block.drawColorBlock(g);
							block.setPosGridX(x);
							block.setPosGridY(y);
						}
					}
					

					if(shap!=null){
						int x=0, y=0;
						x = shap.getPosX();
						y = shap.getPosY();
						shap.setPosX(x+minX);
						shap.setPosY(y+minY);
						shap.drawBlock(g);
						shap.setPosX(x);
						shap.setPosY(y);
					}
				}
		};
		
		//panel_My.setForeground(Color.BLACK);
		panel_My.setBounds(120, 65, 200, 420);
		contentPane.add(panel_My);
		
		JLabel lblHold = new JLabel("HOLD");
		lblHold.setHorizontalAlignment(SwingConstants.CENTER);
		lblHold.setFont(new Font("굴림", Font.BOLD, 18));
		lblHold.setBounds(10, 80, 100, 20);
		contentPane.add(lblHold);
		
		JPanel panel_Hold = new JPanel() {
			public void paintComponent (Graphics g) {
				g.clearRect(0, 0, this.getWidth(), this.getHeight()+1);
				
				g.setColor(Color.BLACK);
				g.fillRect(0, 0, this.getWidth(), this.getHeight()+1);
				
				// 그리드 표시
				g.setColor(Color.darkGray);
				for(int i=1;i<5;i++) g.drawLine(BLOCK_SIZE*(minX-1) ,0 + BLOCK_SIZE*(i), BLOCK_SIZE*(minX+5),0 + BLOCK_SIZE*(i));
				for(int i=1;i<5;i++) g.drawLine(BLOCK_SIZE*(minY+i) ,0 + BLOCK_SIZE*(i-4), BLOCK_SIZE*(minY+i),0 + BLOCK_SIZE*(minY+5)-1);
			
				if(hold!=null){
					int x=0, y=0, newY=3;
					x = hold.getPosX();
					y = hold.getPosY();
					hold.setPosX(-4+minX);
					hold.setPosY(newY+minY);
					hold.drawBlock(g);
					hold.setPosX(x);
					hold.setPosY(y);
				}
				

				if(shap!=null){
					int x=0, y=0;
					x = shap.getPosX();
					y = shap.getPosY();
					shap.setPosX(x+minX);
					shap.setPosY(y+minY);
					shap.drawBlock(g);
					shap.setPosX(x);
					shap.setPosY(y);
				}
			}
		};
		//panel_Hold.setBackground(Color.DARK_GRAY);
		panel_Hold.setBounds(10, 105, 100, 100);
		contentPane.add(panel_Hold);
		
		JLabel lblNewLabel = new JLabel("NEXT");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setFont(new Font("굴림", Font.BOLD, 18));
		lblNewLabel.setBounds(330, 80, 100, 20);
		contentPane.add(lblNewLabel);
		
		JPanel panel_Next = new JPanel() {
			public void paintComponent(Graphics g) {
				g.clearRect(0, 0, this.getWidth(), this.getHeight()+1);
				
				g.setColor(Color.BLACK);
				g.fillRect(0, 0, this.getWidth(), this.getHeight()+1);
				
				g.setColor(Color.DARK_GRAY);
				for(int i=1;i<5;i++) g.drawLine(BLOCK_SIZE*(minX-1) ,0 + BLOCK_SIZE*(i), BLOCK_SIZE*(minX+5),0 + BLOCK_SIZE*(i));
				for(int i=1;i<5;i++) g.drawLine(BLOCK_SIZE*(minY+i) ,0 + BLOCK_SIZE*(i-4), BLOCK_SIZE*(minY+i),0 + BLOCK_SIZE*(minY+5)-1);
				
				if(shap!=null){
					int x=0, y=0;
					x = shap.getPosX();
					y = shap.getPosY();
					shap.setPosX(x+minX);
					shap.setPosY(y+minY);
					shap.drawBlock(g);
					shap.setPosX(x);
					shap.setPosY(y);
				}
			}
		};
		//panel_Next.setBackground(Color.DARK_GRAY);
		panel_Next.setBounds(330, 105, 100, 100);
		contentPane.add(panel_Next);
		
		JPanel panel_NextList = new JPanel() {
			public void paintComponent(Graphics g) {
				g.clearRect(0, 0, this.getWidth(), this.getHeight()+1);
				
				g.setColor(Color.BLACK);
				g.fillRect(0, 0, this.getWidth(), this.getHeight()+1);
				
				if(nextBlocks!=null){
					int x=0, y=0, newY=3;
					for(int i = 0 ; i<nextBlocks.size() ; i++){
						TetrisBlock block = nextBlocks.get(i);
						x = block.getPosX();
						y = block.getPosY(); 
						block.setPosX(13+minX);
						block.setPosY(newY+minY);
						if(newY==3) newY=6;
						block.drawBlock(g);
						block.setPosX(x);
						block.setPosY(y);
						newY+=3;
					}
				}

				if(shap!=null){
					int x=0, y=0;
					x = shap.getPosX();
					y = shap.getPosY();
					shap.setPosX(x+minX);
					shap.setPosY(y+minY);
					shap.drawBlock(g);
					shap.setPosX(x);
					shap.setPosY(y);
				}
			}
		};
		//panel_NextList.setBackground(Color.DARK_GRAY);
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
		
		Chatrange = new JScrollPane();
		Chatrange.setBounds(0, 0, 630, 100);
		panel_Chat.add(Chatrange);
		
		ChatArea = new JTextArea();
		Chatrange.setViewportView(ChatArea);
		ChatArea.setEditable(false);
		ChatArea.setLineWrap(true);
		
		CTF.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(ctst == true && svst == false) { // 클라이언트로 접속해 있는 경우 
					C_send();
				}
				
				else if (svst == true && ctst == false) { // 서버로 접속해 있는 경우
					S_send();
				}
				
				else {
					JOptionPane.showMessageDialog(null, "서버나 클라이언트로 접속되어있지않습니다!", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		JButton btnSend = new JButton("Send"); // 채팅 메시지 전송 버튼
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(ctst == true && svst == false) { // 클라이언트로 접속해 있는 경우 
					C_send();
				}
				
				else if (svst == true && ctst == false) { // 서버로 접속해 있는 경우
					S_send();
				}
				
				else {
					JOptionPane.showMessageDialog(null, "서버나 클라이언트로 접속되어있지않습니다!", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		btnSend.setBounds(630, 100, 130, 27);
		panel_Chat.add(btnSend);
	
		
		JButton btnStart = new JButton("시작하기");
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				gameStart((int)comboSpeed.getSelectedItem());
			}
		});
		btnStart.setFont(new Font("굴림", Font.PLAIN, 15));
		btnStart.setBackground(UIManager.getColor("Button.background"));
		btnStart.setBounds(630, 0, 130, 51);
		panel_Chat.add(btnStart);
		
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
					
						socket = ssocket.accept();
						cip = socket.getInetAddress().getHostAddress();
						ChatArea.append("["+ cip +" 에서 서버에 접속함...]\n");
						
						while(true) {
							br = new BufferedReader(
									new InputStreamReader (socket.getInputStream()));
							String msg = br.readLine();
							ChatArea.append(msg + "\n");
							Chatrange.getVerticalScrollBar().setValue(Chatrange.getVerticalScrollBar().getMaximum());
						}
						
				} catch (Exception e) {
					ChatArea.append("[" + cip + "가 접속을 해제하였습니다.]");
				
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
	
	void stopServer() {
		 try {
	    	  ChatArea.append("클라이언트와 연결 끊음.\n");
	    	  ChatArea.setText("");
	    
	         if(ssocket!=null && !ssocket.isClosed()) {
	            ssocket.close();
	         }
	      } catch (IOException e) {}
	}
	
	void S_send() { // 서버 메시지 보내기
		String s = CTF.getText().trim();
		
		Thread thread = new Thread() {
			public void run() {
				if(s.length()==0) return;
		
				try {
					if(socket==null) return;
			
					PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);
					pw.println(tfsnick + "] " + s);
			
					ChatArea.append(tfsnick + "]" + s + "\n");
			
					CTF.setText("");
					CTF.requestFocus();
					Chatrange.getVerticalScrollBar().setValue(Chatrange.getVerticalScrollBar().getMaximum());
				} catch(Exception e2) {
					ChatArea.append("클라이언트가 접속을 해제함\n");
				}
			}
				//S_send(text);
		};
		thread.start();
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
	   
	   void C_send() { // 클라이언트 메시지 보내기
		   String s = CTF.getText().trim();
		   
			Thread thread = new Thread() {
				public void run() {
					if(s.length()==0) return;
			
					try {
						if(socket==null) return;
				
						PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);
						pw.println(tfcnick + "] " + s);
				
						ChatArea.append(tfcnick + "]" + s + "\n");
				
						CTF.setText("");
						CTF.requestFocus();
						Chatrange.getVerticalScrollBar().setValue(Chatrange.getVerticalScrollBar().getMaximum());
					} catch(Exception e2) {
						ChatArea.append("클라이언트가 접속을 해제함\n");
					}
						//C_send(text);
				}
			};
			thread.start();
	   }

	   void C_receive() { // 서버에서 보낸 데이터 받는 메소드
	      while(true) {
	         try {
	        	BufferedReader br = new BufferedReader(
							new InputStreamReader (socket.getInputStream()));
					while (true) {
						String msg = br.readLine();
						ChatArea.append(msg + "\n");
						Chatrange.getVerticalScrollBar().setValue(Chatrange.getVerticalScrollBar().getMaximum());
					}
	         } catch (Exception e) {
	            stopClient(); //클라이언트 멈춤
	            break;
	         }
	      }
	   }
	   
	   void gameStart(int speed){
			comboSpeed.setSelectedItem(new Integer(speed));
			//돌고 있을 스레드를 정지시킨다.
			if(th!=null){
				try {isPlay = false;th.join();} 
				catch (InterruptedException e) {e.printStackTrace();}
			}
			
			//맵셋팅
			map = new Block[maxY][maxX];
			blockList = new ArrayList<Block>();
			nextBlocks = new ArrayList<TetrisBlock>();
			
			//도형셋팅
			shap = getRandomTetrisBlock();
			hold = null;
			isHold = false;
			controller = new TetrisController(shap,maxX-1,maxY-1,map);
			for(int i=0 ; i<5 ; i++){
				nextBlocks.add(getRandomTetrisBlock());
			}
			
			//스레드 셋팅
			isPlay = true;
			th = new Thread(this);
			th.start();
		}
		
		@Override
		public void run() {
			int countMove = (21-(int)comboSpeed.getSelectedItem())*5;
			int countDown = 0;
			int countUp = up;
			
			while(isPlay){
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				if(countDown!=0){
					countDown--;
					if(countDown==0){
						
						if(controller!=null && !controller.moveDown()) this.fixingTetrisBlock();
					}
					this.repaint();
					continue;
				}
				
				countMove--;
				if (countMove == 0) {
					countMove = (21-(int)comboSpeed.getSelectedItem())*5;
					if (controller != null && !controller.moveDown()) countDown = down;
				}
				
				if (countUp != 0) {
					countUp--;
					if (countUp == 0) {
						countUp = up;
						addBlockLine(1);
					}
				}
				this.repaint();
			}//while()
		}//run()

		
		/**
		 * 맵(보이기, 논리)을 상하로 이동한다.
		 * @param lineNumber	
		 * @param num -1 or 1
		 */
		public void dropBoard(int lineNumber, int num){
			
			// 맵을 떨어트린다.
			this.dropMap(lineNumber,num);
			
			//좌표바꿔주기(1만큼증가)
			this.changeTetrisBlockLine(lineNumber,num);
			
			//다시 체크하기
			this.checkMap();
		}
		
		
		/**
		 * lineNumber의 위쪽 라인들을 모두 num칸씩 내린다.
		 * @param lineNumber
		 * @param num 칸수 -1,1
		 */
		private void dropMap(int lineNumber, int num) {
			if(num==1){
				//한줄씩 내리기
				for(int i= lineNumber ; i>0 ;i--){
					for(int j=0 ; j<map[i].length ;j++){
						map[i][j] = map[i-1][j];
					}
				}
				
				//맨 윗줄은 null로 만들기
				for(int j=0 ; j<map[0].length ;j++){
					map[0][j] = null;
				}
			}
			else if(num==-1){
				//한줄씩 올리기
				for(int i= 1 ; i<=lineNumber ;i++){
					for(int j=0 ; j<map[i].length ;j++){
						map[i-1][j] = map[i][j];
					}
				}
				
				//removeLine은 null로 만들기
				for(int j=0 ; j<map[0].length ;j++){
					map[lineNumber][j] = null;
				}
			}
		}
		
		
		/**
		 * lineNumber의 위쪽 라인들을 모두 num만큼 이동시킨다.
		 * @param lineNumber 
		 * @param num	이동할 라인
		 */
		private void changeTetrisBlockLine(int lineNumber, int num){
			int y=0, posY=0;
			for(int i=0 ; i<blockList.size() ; i++){
				y = blockList.get(i).getY();
				posY = blockList.get(i).getPosGridY();
				if(y<=lineNumber)blockList.get(i).setPosGridY(posY + num);
			}
		}
		
		/**
		 * 테트리스 블럭을 고정시킨다. 
		 */
		private void fixingTetrisBlock() {
			synchronized (this) {
				if(stop){
					try {
						this.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			
			boolean isCombo = false;
			removeLineCount = 0;
			
			// drawList 추가
			for (Block block : shap.getBlock()) {
				blockList.add(block);
			}
			
			// check
			isCombo = checkMap();

			if(isCombo) removeLineCombo++;
			else removeLineCombo = 0;
			
			//다음 테트리스 블럭을 가져온다.
			this.nextTetrisBlock();
			
			//홀드가능상태로 만들어준다.
			isHold = false;
		}//fixingTetrisBlock()
		
		
		/**
		 * 
		 * @return true-지우기성공, false-지우기실패
		 */
		private boolean checkMap(){
			boolean isCombo = false;
			int count = 0;
			Block mainBlock;
			
			for(int i=0 ; i<blockList.size() ;i++){
				mainBlock = blockList.get(i);
				
				// map에 추가
				if(mainBlock.getY()<0 || mainBlock.getY() >=maxY) continue;
				
				if(mainBlock.getY()<maxY && mainBlock.getX()<maxX) 
					map[mainBlock.getY()][mainBlock.getX()] = mainBlock;

				// 줄이 꽉 찼을 경우. 게임을 종료한다.
				if (mainBlock.getY() == 1 && mainBlock.getX() > 2 && mainBlock.getX() < 7) {
					this.gameEndCallBack();
					break;
				}
				
				//1줄개수 체크
				count = 0;
				for (int j = 0; j < maxX; j++) {
					if(map[mainBlock.getY()][j] != null) count++;
				}
				
				//block의 해당 line을 지운다.
				if (count == maxX) {
					removeLineCount++;
					this.removeBlockLine(mainBlock.getY());
					isCombo = true;
				}
			}
			return isCombo;
		}
		
		/**
		 * 테트리스 블럭 리스트에서 테트리스 블럭을 받아온다.
		 */
		public void nextTetrisBlock(){
			shap = nextBlocks.get(0);
			this.initController();
			nextBlocks.remove(0);
			nextBlocks.add(getRandomTetrisBlock());
		}
		private void initController(){
			controller.setBlock(shap);
		}
		
		
		/**
		 * lineNumber 라인을 삭제하고, drawlist에서 제거하고, map을 아래로 내린다.
		 * @param lineNumber 삭제라인
		 */
		private void removeBlockLine(int lineNumber) {
			// 1줄을 지워줌
			for (int j = 0; j < maxX ; j++) {
				for (int s = 0; s < blockList.size(); s++) {
					Block b = blockList.get(s);
					if (b == map[lineNumber][j])
						blockList.remove(s);
				}
				map[lineNumber][j] = null;
			}// for(j)

			this.dropBoard(lineNumber,1);
		}
		
		
		/**TODO : 게임종료콜벡
		 * 게임이 종료되면 실행되는 메소드
		 */
		public void gameEndCallBack(){
			this.isPlay = false;
		}
		
		
		/**
		 * 랜덤으로 테트리스 블럭을 생성하고 반환한다.
		 * @return 테트리스 블럭
		 */
		public TetrisBlock getRandomTetrisBlock(){
			switch((int)(Math.random()*7)){
			case TetrisBlock.TYPE_CENTERUP : return new CenterUp(4, 1);
			case TetrisBlock.TYPE_LEFTTWOUP : return new LeftTwoUp(4, 1);
			case TetrisBlock.TYPE_LEFTUP : return new LeftUp(4, 1);
			case TetrisBlock.TYPE_RIGHTTWOUP : return new RightTwoUp(4, 1);
			case TetrisBlock.TYPE_RIGHTUP : return new RightUp(4, 1);
			case TetrisBlock.TYPE_LINE : return new Line(4, 1);
			case TetrisBlock.TYPE_NEMO : return new Nemo(4, 1);
			}
			return null;
		}
		
		/**
		 * tetrisBlock과 같은 모양으로 고스트의 블럭모양을 반환한다.
		 * @param tetrisBlock 고스트의 블럭모양을 결정할 블럭
		 * @return 고스트의 블럭모양을 반환
		 */
		public TetrisBlock getBlockClone(TetrisBlock tetrisBlock, boolean isGhost){
			TetrisBlock blocks = null;
			switch(tetrisBlock.getType()){
			case TetrisBlock.TYPE_CENTERUP : blocks =  new CenterUp(4, 1); break;
			case TetrisBlock.TYPE_LEFTTWOUP : blocks =  new LeftTwoUp(4, 1); break;
			case TetrisBlock.TYPE_LEFTUP : blocks =  new LeftUp(4, 1); break;
			case TetrisBlock.TYPE_RIGHTTWOUP : blocks =  new RightTwoUp(4, 1); break;
			case TetrisBlock.TYPE_RIGHTUP : blocks =  new RightUp(4, 1); break;
			case TetrisBlock.TYPE_LINE : blocks =  new Line(4, 1); break;
			case TetrisBlock.TYPE_NEMO : blocks =  new Nemo(4, 1); break;
			}
			if(blocks!=null && isGhost){
				blocks.setPosX(tetrisBlock.getPosX());
				blocks.setPosY(tetrisBlock.getPosY());
				blocks.rotation(tetrisBlock.getRotationIndex());
			}
			return blocks;
		}	
		
		/**
		 * 블럭을 홀드시킨다.
		 */
		public void playBlockHold(){
			if(isHold) return;
			
			if(hold==null){
				hold = getBlockClone(shap,false);
				this.nextTetrisBlock();
			}else{
				TetrisBlock tmp = getBlockClone(shap,false);
				shap = getBlockClone(hold,false);
				hold = getBlockClone(tmp,false);
				this.initController();
			}
			
			isHold = true;
		}
		
		
		/**
		 * 가장 밑에 줄에 블럭을 생성한다.
		 * @param numOfLine
		 */
		boolean stop = false;
		public void addBlockLine(int numOfLine){
			stop = true;
			// 내리기가 있을 때까지 대기한다.
			// 내리기를 모두 실행한 후 다시 시작한다.
			Block block;
			int rand = (int) (Math.random() * maxX);
			for (int i = 0; i < numOfLine; i++) {
				this.dropBoard(maxY - 1, -1);
				for (int col = 0; col < maxX; col++) {
					if (col != rand) {
						block = new Block(0, 0, Color.GRAY);
						block.setPosGridXY(col, maxY - 1);
						blockList.add(block);
						map[maxY - 1][col] = block;
					}
				}
				//만약 내려오는 블럭과 겹치면 블럭을 위로 올린다.
				boolean up = false;
				for(int j=0 ; j<shap.getBlock().length ; j++){
					Block sBlock = shap.getBlock(j);
					if(map[sBlock.getY()][sBlock.getX()]!=null){
						up = true;
						break;
					}
				}
				if(up){
					controller.moveDown(-1);
				}
			}
			
			this.repaint();
			synchronized (this) {
				stop = false;
				this.notify();
			}
		}
		
		public void keyReleased(KeyEvent e) {}
		public void keyTyped(KeyEvent e) {}
		public void keyPressed(KeyEvent e) {
			if(!isPlay) return;
			if(e.getKeyCode() == KeyEvent.VK_LEFT){
				controller.moveLeft();
			}else if(e.getKeyCode() == KeyEvent.VK_RIGHT){
				controller.moveRight();
			}else if(e.getKeyCode() == KeyEvent.VK_DOWN){
				controller.moveDown();
			}else if(e.getKeyCode() == KeyEvent.VK_UP){
				controller.nextRotationLeft();
			}else if(e.getKeyCode() == KeyEvent.VK_SPACE){
				controller.moveQuickDown(shap.getPosY(), true);
				this.fixingTetrisBlock();
			}else if(e.getKeyCode() == KeyEvent.VK_SHIFT){ 
				playBlockHold();
			}
			this.repaint();
		}

		//public void mouseClicked(MouseEvent e) {}
		//public void mouseEntered(MouseEvent e) {}
		//public void mouseExited(MouseEvent e) {}
		//public void mousePressed(MouseEvent e) {
			//this.requestFocus();
		//}
		//public void mouseReleased(MouseEvent e) {}

		public boolean isPlay(){return isPlay;}
		public void setPlay(boolean isPlay){this.isPlay = isPlay;}
		public void changeSpeed(Integer speed) {comboSpeed.setSelectedItem(speed);}
	

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
