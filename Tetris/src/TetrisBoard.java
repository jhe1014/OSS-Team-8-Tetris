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
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.swing.SwingConstants;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.UIManager;

public class TetrisBoard extends JFrame{
	
	private static final long serialVersionUID = 1L;

	private JPanel contentPane;

	private int port;
	public String ip = "null", nickname = "null";

	// ���� �Է��ؽ�Ʈ ���� ������ ����
	private String tfsip = "";
	private String tfspt = "";
	private String tfsnick = "";
	
	// Ŭ���̾�Ʈ �Է��ؽ�Ʈ ���� ������ ����
	private String tfcip = ""; 
	private String tfcpt = ""; 
	private String tfcnick = "";
	
	private boolean svst = false; // ������ �����ߴ��� Ȯ���ϱ� ���� ���� ����
	private boolean ctst = false; // Ŭ���̾�Ʈ�� �����ߴ��� Ȯ���ϱ� ���� ���� ����
	
	private boolean ready = false;
	private boolean start = false;
	
	JLabel lblIpValue = new JLabel();
	JLabel lblPortValue = new JLabel();
	JLabel lblNickNameValue = new JLabel();
	
	JTextField txsip = new JTextField("127.0.0.1"); // ����â���� ip �Է��ϴ� ĭ
	JTextField txspt = new JTextField(""); // ����â���� ��Ʈ��ȣ �Է��ϴ� ĭ
	JTextField txsnick = new JTextField(""); // ����â���� �г��� �Է��ϴ� ĭ
	
	JTextField txcip = new JTextField("127.0.0.1"); // Ŭ���̾�Ʈâ���� ip �Է��ϴ� ĭ
	JTextField txcpt = new JTextField(""); // Ŭ���̾�Ʈâ���� ��Ʈ��ȣ �Է��ϴ� ĭ
	JTextField txcnick = new JTextField(""); // Ŭ���̾�Ʈâ���� �г��� �Է��ϴ� ĭ
	
	private JTextField CTF; // ä�ø޽��� �Է�ĭ
	JTextArea ChatArea; // ä�ø޽��� ���� ĭ
	JScrollPane Chatrange;
	
	JPanel panel_My;
	JButton btnStart = new JButton("�����ϱ�");
	JLabel panel_Your = new JLabel();
	
	ServerSocket ssocket; // ���� ����
	Socket socket; // Ŭ���̾�Ʈ ����
	
	private Thread th;
	public static final int BLOCK_SIZE = 20;
	public static final int BOARD_X = 120;
	public static final int BOARD_Y = 50;
	private static final int FRAME_X = 100, FRAME_Y = 100; // �����ų �� X��ǥ�� �ٲ���ϴ� ���ŷο�... �Ф�
	private int minX=0, minY=0, maxX=10, maxY=21, down=50, up=0;
	private ArrayList<Block> blockList;
	private ArrayList<TetrisBlock> nextBlocks;
	private Block map[][];
	private TetrisController controller;
	private TetrisBlock shap;
	private TetrisBlock hold;
	private int speed = 1;
	
	private boolean isPlay = false;
	private boolean isHold = false;
	private int removeLineCount = 0;
	private int removeLineCombo = 0;
	
	private DatagramSocket sock;
	private int myport = 7000;
	private int otherport = 8000;
	private InetAddress address = null;
	InetAddress local = null;
	
	 
	public TetrisBoard() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(FRAME_X, FRAME_Y, 800, 700);
		setResizable(false);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(176, 224, 230));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.setBounds(0, 0, 784, 26);
		contentPane.add(menuBar);
		
		JMenu mnNewMenu = new JMenu("���� �ϱ�");
		menuBar.add(mnNewMenu);
		
		JMenuItem mntmNewMenuItem_Server = new JMenuItem("������ �����ϱ�");
		mntmNewMenuItem_Server.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (svst == false && ctst == false) {
					Serverframe svf = new Serverframe();
					svf.setVisible(true);
				}
				
				else { // ��� �� �� ���ζ� ���� ���� ��� ���� �޽��� ���
					JOptionPane.showMessageDialog(null, "�̹� ���� ���Դϴ�!", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		mnNewMenu.add(mntmNewMenuItem_Server);
		
		JMenuItem mntmNewMenuItem_Client = new JMenuItem("Ŭ���̾�Ʈ�� �����ϱ�");
		mntmNewMenuItem_Client.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (svst == false && ctst == false) {
					Clientframe ctf = new Clientframe();
					ctf.setVisible(true);
				}
				
				else { // ��� �� �� ���ζ� ���� ���� ��� ���� �޽��� ���
					JOptionPane.showMessageDialog(null, "�̹� ���� ���Դϴ�!", "Error", JOptionPane.ERROR_MESSAGE);
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
		
		JLabel lblNickname = new JLabel("�г��� : ");
		lblNickname.setBounds(5, 20, 50, 15);
		panel.add(lblNickname);
		
		lblNickNameValue.setBounds(60, 20, 108, 15);
		lblNickNameValue.setText(nickname);
		panel.add(lblNickNameValue);
		
		BufferedImage bi = new BufferedImage(200,420, BufferedImage.TYPE_INT_RGB);
		
		panel_My = new JPanel() {
				public void paintComponent(Graphics g) {
					g.clearRect(0, 0, this.getWidth(), this.getHeight()+1);
					g.setColor(Color.BLACK);
					g.fillRect(0, 0, this.getWidth(), this.getHeight()+1);
					
					//�׸��� ǥ��
					g.setColor(Color.darkGray);
					for(int i=1;i<maxY;i++) g.drawLine(0 + BLOCK_SIZE*(minX), 0+BLOCK_SIZE*(i+minY), 0 + (maxX+minX)*BLOCK_SIZE, 0+BLOCK_SIZE*(i+minY));
					for(int i=1;i<maxX;i++) g.drawLine(0 + BLOCK_SIZE*(i+minX), 0 + BLOCK_SIZE*minY, 0 + BLOCK_SIZE*(i+minX), 0 + BLOCK_SIZE*(minY+maxY));
				
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
		//panel_My.setFocusable(true); 
		contentPane.add(panel_My);
		
		panel_My.addKeyListener(new KeyListener() {
			public void keyPressed(KeyEvent e) {
					//System.out.println("Ű�̺�Ʈ");
					if(!isPlay) return;
					if(e.getKeyCode() == KeyEvent.VK_LEFT){
						controller.moveLeft();
						//System.out.println("����");
					}else if(e.getKeyCode() == KeyEvent.VK_RIGHT){
						controller.moveRight(); 
					}else if(e.getKeyCode() == KeyEvent.VK_DOWN){
						controller.moveDown();
					}else if(e.getKeyCode() == KeyEvent.VK_UP){
						controller.nextRotationLeft();
					}else if(e.getKeyCode() == KeyEvent.VK_SPACE){
						controller.moveQuickDown(shap.getPosY(), true);
						fixingTetrisBlock();
					}else if(e.getKeyCode() == KeyEvent.VK_SHIFT){ 
						playBlockHold();
					}
					repaint();
				}
					public void keyReleased(KeyEvent arg0) {}
					public void keyTyped(KeyEvent e) {}
		});
	
		JLabel lblHold = new JLabel("H O L D");
		lblHold.setBounds(25, 80, 78, 21);
		lblHold.setFont(new Font("����", Font.BOLD, 20));
		contentPane.add(lblHold);
		
		JPanel panel_Hold = new JPanel() {
			public void paintComponent (Graphics g) {
				g.clearRect(0, 0, this.getWidth(), this.getHeight()+1);
				
				g.setColor(Color.BLACK);
				g.fillRect(0, 0, this.getWidth(), this.getHeight()+1);
				
				// �׸��� ǥ��
				g.setColor(Color.darkGray);
				for(int i=1;i<5;i++) g.drawLine(BLOCK_SIZE*(minX-1) ,0 + BLOCK_SIZE*(i), BLOCK_SIZE*(minX+5),0 + BLOCK_SIZE*(i));
				for(int i=1;i<5;i++) g.drawLine(BLOCK_SIZE*(minY+i) ,0 + BLOCK_SIZE*(i-4), BLOCK_SIZE*(minY+i),0 + BLOCK_SIZE*(minY+5)-1);
			
				if(hold!=null){
					int x=0, y=0, newY=3;
					x = hold.getPosX();
					y = hold.getPosY();
					hold.setPosX(2+minX);
					hold.setPosY(newY+minY);
					hold.drawBlock(g);
					hold.setPosX(x);
					hold.setPosY(y);
				}
				
				
			}
		};
		//panel_Hold.setBackground(Color.DARK_GRAY);
		panel_Hold.setBounds(10, 105, 100, 100);
		contentPane.add(panel_Hold);
		
		JLabel lblNext = new JLabel("N E X T");
		lblNext.setBounds(352, 80, 78, 21);
		lblNext.setFont(new Font("����", Font.BOLD, 20));
		contentPane.add(lblNext);
		
		JPanel panel_Next = new JPanel() {
			public void paintComponent(Graphics g) {
				g.clearRect(0, 0, this.getWidth(), this.getHeight()+1);
				
				g.setColor(Color.BLACK);
				g.fillRect(0, 0, this.getWidth(), this.getHeight()+1);
				
				g.setColor(Color.DARK_GRAY);
				for(int i=1;i<5;i++) g.drawLine(0 + BLOCK_SIZE*(minX), 0+BLOCK_SIZE*(i+minY), 0 + (maxX+minX)*BLOCK_SIZE, 0+BLOCK_SIZE*(i+minY));
				for(int i=1;i<5;i++) g.drawLine(0 + BLOCK_SIZE*(i+minX), 0 + BLOCK_SIZE*minY, 0 + BLOCK_SIZE*(i+minX), 0 + BLOCK_SIZE*(minY+maxY));

				
				if(nextBlocks!=null){
					int x=0, y=0, newY=3;
					for(int i = 0 ; i<nextBlocks.size() ; i++){
						TetrisBlock block = nextBlocks.get(i);
						x = block.getPosX();
						y = block.getPosY(); 
						block.setPosX(2+minX);
						block.setPosY(newY+minY-1);
						if(newY==3) newY=6;
						block.drawBlock(g);
						block.setPosX(x);
						block.setPosY(y);
						newY+=3;
					}
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
						block.setPosX(1);
						block.setPosY(newY+0);
						if(newY==3) newY=5;
						block.drawBlock(g);
						block.setPosX(x);
						block.setPosY(y);
						newY+=3;
					}
				}
			}
		};
		//panel_NextList.setBackground(Color.DARK_GRAY);
		panel_NextList.setBounds(330, 225, 100, 240);
		contentPane.add(panel_NextList);
		
		panel_Your.setBackground(Color.BLACK);
		panel_Your.setOpaque(true);
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
				if(ctst == true && svst == false) { // Ŭ���̾�Ʈ�� ������ �ִ� ��� 
					C_send();
				}
				
				else if (svst == true && ctst == false) { // ������ ������ �ִ� ���
					S_send();
				}
				
				else {
					JOptionPane.showMessageDialog(null, "������ Ŭ���̾�Ʈ�� ���ӵǾ������ʽ��ϴ�!", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		JButton btnSend = new JButton("Send"); // ä�� �޽��� ���� ��ư
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(ctst == true && svst == false) { // Ŭ���̾�Ʈ�� ������ �ִ� ��� 
					C_send();
				}
				
				else if (svst == true && ctst == false) { // ������ ������ �ִ� ���
					S_send();
				}
				
				else {
					JOptionPane.showMessageDialog(null, "������ Ŭ���̾�Ʈ�� ���ӵǾ������ʽ��ϴ�!", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		btnSend.setBounds(630, 100, 130, 27);
		panel_Chat.add(btnSend);
	
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(ctst == true && svst == false) { 
					ready = true;
					
					try {
						   /*Robot robot = new Robot();
						   Rectangle area = new Rectangle(panel_My.getX(), panel_My.getY(), panel_My.getWidth(), panel_My.getHeight());
						   BufferedImage bi = robot.createScreenCapture(area);
						   ImageIcon imc = new ImageIcon(bi);
						   panel_Your.setIcon(imc);*/
						   
						   PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);
						   pw.println("ready");	
						   panel_My.requestFocus();
						 
					} catch (Exception e) {
						System.out.println("�غ� ���� ���� ����");
					}
				}
				
				else if (svst == true && ctst == false) {
					if(ready) {
						try {
							PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);
							pw.println("start");
						} catch (Exception e) {
							System.out.println("���� ���� ���� ����");
						}
						
						gameStart(speed);
						panel_My.requestFocus();
						imgReceive(myport,panel_Your);
						imgSend(otherport,panel_My);
					}
				}
				
				else {
					JOptionPane.showMessageDialog(null, "������ Ŭ���̾�Ʈ�� ���ӵǾ������ʽ��ϴ�!", "Error", JOptionPane.ERROR_MESSAGE);
				}
				
				
			}
		});
		
		btnStart.setFont(new Font("����", Font.PLAIN, 15));
		btnStart.setBackground(UIManager.getColor("Button.background"));
		btnStart.setBounds(630, 0, 130, 51);
		panel_Chat.add(btnStart);
		
		JButton btnEnd = new JButton("������");
		btnEnd.setFont(new Font("����", Font.PLAIN, 15));
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
					ChatArea.append("���� �����\n");
					
						socket = ssocket.accept();
						cip = socket.getInetAddress().getHostAddress();
						ChatArea.append("["+ cip +" ���� ������ ������...]\n");
						
						while(true) {
							br = new BufferedReader(
									new InputStreamReader (socket.getInputStream()));
							String msg = br.readLine();
							if(msg.equals("ready")) { // Ŭ���̾�Ʈ�� �غ� ������ ���
								ready = true;
							}
							ChatArea.append(msg + "\n");
							Chatrange.getVerticalScrollBar().setValue(Chatrange.getVerticalScrollBar().getMaximum());
						}
						
				} catch (Exception e) {
					ChatArea.append("[" + cip + "�� ������ �����Ͽ����ϴ�.]");
				
					//if (!ssocket.isClosed()) {stopServer();};
					//closeAll();
					
					//System.out.println("���� ����\n");
				}
			}
			
			private void closeAll() {
				try {
					if(br!=null) br.close();
					if(socket!=null) socket.close();
					if(ssocket!=null) ssocket.close();
				} catch(Exception e) {
					System.out.println("closeAll()���� ���� : " + e);
				}
			}
		};
		thread.start();
	}
	
	void stopServer() {
		 try {
	    	  ChatArea.append("Ŭ���̾�Ʈ�� ���� ����.\n");
	    	  ChatArea.setText("");
	    
	         if(ssocket!=null && !ssocket.isClosed()) {
	            ssocket.close();
	         }
	      } catch (IOException e) {}
	}
	
	void S_send() { // ���� �޽��� ������
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
					panel_My.requestFocus();
					Chatrange.getVerticalScrollBar().setValue(Chatrange.getVerticalScrollBar().getMaximum());
				} catch(Exception e2) {
					ChatArea.append("Ŭ���̾�Ʈ�� ������ ������\n");
				}
			}
				//S_send(text);
		};
		thread.start();
	}
	
	void startClient() { // Ŭ���̾�ŸƮ �������� �޼ҵ�
	      Thread thread = new Thread() { //������ ����
	         @Override
	         public void run() { // run�޼ҵ� ������
	        	 try {
	               socket = new Socket(); 
	               socket.connect(new InetSocketAddress(tfcip, Integer.parseInt(tfcpt)));  //���� ������� IP ,��Ʈ��ȣ ���ε�
	               ChatArea.append("[���� �Ϸ�: " + socket.getRemoteSocketAddress() + "]\n");
	            } catch(Exception e) {
	            	ChatArea.append("[���� ��� �ȵ�]\n");
	            	
	               if(!socket.isClosed()) { stopClient(); }

	               return;
	            }	    
	            C_receive(); //�������� ���� ������ �ޱ�	            
	         }
	      };   	      
	      thread.start();
	   }
	
	   void stopClient() { //Ŭ���̾�Ʈ ���� �޼ҵ�
	      try {
	    	  ChatArea.append("������ ���� ����.\n");
	    	  ChatArea.setText("");
	    
	         if(socket!=null && !socket.isClosed()) {
	            socket.close();
	         }
	      } catch (IOException e) {}
	   }   
	   
	   void C_send() { // Ŭ���̾�Ʈ �޽��� ������
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
						panel_My.requestFocus();
						Chatrange.getVerticalScrollBar().setValue(Chatrange.getVerticalScrollBar().getMaximum());
					} catch(Exception e2) {
						ChatArea.append("Ŭ���̾�Ʈ�� ������ ������\n");
					}
						//C_send(text);
				}
			};
			thread.start();
	   }

	   void C_receive() { // �������� ���� ������ �޴� �޼ҵ�
	      while(true) {
	         try {
	        	BufferedReader br = new BufferedReader(
							new InputStreamReader (socket.getInputStream()));
					while (true) {
						String msg = br.readLine();
						if(msg.equals("start")) { //������ �����ϱ⸦ ���� ���
							start = true;
							if(ctst == true && svst == false) { 
								if(start && ready) {
									gameStart(speed);
									imgReceive(otherport,panel_Your);
									imgSend(myport,panel_My);
								}
							}
						}
						ChatArea.append(msg + "\n");
						Chatrange.getVerticalScrollBar().setValue(Chatrange.getVerticalScrollBar().getMaximum());
					}
	         } catch (Exception e) {
	            stopClient(); //Ŭ���̾�Ʈ ����
	            break;
	         }
	      }
	   }
	   
	   public void imgSend(final int portNum ,final JPanel PANEL) { // �ڽ��� ����ȭ�� ���濡�� �۽�
		   Thread th = new Thread() {
			   public void run() {
				   try {
					   byte outbuff[] = new byte[80000];
					   while(isPlay) {
						   Robot robot = new Robot();
						   System.out.println("�̹���������");
						   Rectangle area = new Rectangle(120+FRAME_X, 105+FRAME_Y, 200, 420);
						   BufferedImage bufferedImage = robot.createScreenCapture(area);
						   ImageIcon imc = new ImageIcon(bufferedImage);
						   //panel_Your.setIcon(imc);
						   //PANEL.setIcon(imc);
						   
						  /* byte[][] temp = controller.getIndex();
						   byte[] pos = new byte[2];
						   byte sendarr[] = new byte[8];
						   byte sendarr2[] = new byte[maxX*maxY];
						   
						   for(int i=0; i<comb.length; i++) {
							   for(int j=0; j<comb[i].length; j++) {
								   comb[i][j] = 0;
							   }
						   }						   
						   
						   for(int i=0; i<comb.length; i++) {
							   for(int j=0; j<comb[i].length; j++) {
								   if(fixed[i][j]==1) {
									   comb[i][j] = 1;
								   }
								 //  if(getData[i][j]==1) {
								//	   comb[i][j] = 1;
								 //  }
							   }
						   }
						   					   
						   	   
						   //����� �ڵ�
						   
						   for(int i=0; i<maxY; i++) {
							   for(int j=0; j<maxX; j++) {
								   sendarr2[i*maxX+j] = comb[i][j];
							   }
						   }*/
						/*
						   int count=0;
						   for(int i=0; i<sendarr2.length; i++) {
							   System.out.print(sendarr2[i]);
							   
							   if(count==maxX) {
								   System.out.println();
								   count=0;
							   }
							   count++;
						   }
						   */
						   
						   /*
						   System.out.println("sendarr");
						   for(int i=0; i<8; i++) {
							   
							   System.out.print(sendarr[i]+" ");
						   }
						   */
						   
						   
						   /*System.out.println();
						   ByteArrayOutputStream baos = new ByteArrayOutputStream();
						   //ByteArrayInputStream baos1 = new ByteArrayInputStream(sendarr);
						   //ImageIO.write(bufferedImage, "jpg", baos);
						   
						   
						   //baos.write(sendarr, 0, 8);
						   baos.write(sendarr2, 0, maxX*maxY);
						   outbuff = baos.toByteArray();*/
						   
						   
						   
						   //����
						 
						   
						   
//						   System.out.println("----------------------read--------");
//						   byte[] temp1 = baos1.readAllBytes();
//						   for(int i=0; i<temp1.length; i++) {
//							   System.out.print(temp1[i]);
//						   }
						   /*DatagramPacket dp = new DatagramPacket(outbuff, outbuff.length, address, portNum);
						   
						   dp.setData(outbuff, 0, outbuff.length);
						   sock.send(dp);*/
						 
						   ByteArrayOutputStream baos = new ByteArrayOutputStream();
						   ImageIO.write(bufferedImage, "jpg", baos);
						   
						   outbuff = baos.toByteArray();
						   
						   DatagramPacket dp = new DatagramPacket(outbuff, outbuff.length, address, portNum);
						   sock.send(dp);
						   }
				   } catch(Exception e){
					   System.out.println("�̹��� ���� ���� �߻�");
					   e.printStackTrace();
				   }
			   }
		   };
		   th.start();
	   }
	   
	   public void imgReceive(final int portNum, final JLabel PANEL) { // �̹��� ���� �ް� ����
		   Thread th = new Thread() {
			 public void run() {
				 try {
					byte[] rcvbyte = new byte[80000];
		 			DatagramPacket dp = new DatagramPacket(rcvbyte, rcvbyte.length);
		 			BufferedImage bf;
		 			ImageIcon imc;
		 			address = InetAddress.getByName("192.168.0.128");
		 			sock = new DatagramSocket(portNum);
		 			
				 	while(isPlay) {

				 		/*byte temp[] = new byte[255];
				 		//byte temp2[][] = new byte[4][2];
				 		sock.receive(dp);
				 		ByteArrayInputStream bais = new ByteArrayInputStream(rcvbyte);
				 		System.out.println("�̹��� ������ ip&port :" + dp.getSocketAddress() +
				 				" ���Ź���ũ�� : " + dp.getLength());

				 		//temp = bais.readAllBytes();
				 		bais.read(temp, 0, maxX*maxY);
				 		System.out.println();
				 		//System.out.println("���Ź���Ȯ��");
						for(int i=0; i<8; i++) {
							//System.out.print(temp[i]);
						}
						
						for(int i=0; i<4; i++) {
							//System.out.println("~~~");
							for(int j=0; j<2; j++) {
								temp2[i][j] = temp[i*2+j];
							}
						//	System.out.println();
						}

						
						
						
						for(int i=0; i<maxY; i++) {
							//System.out.println("~~~");
							for(int j=0; j<maxX; j++) {
								reassemble[i][j] = temp[i*maxX+j];
								//System.out.print(" "+temp2[i][j]);
//								temp3[i][j] = temp[i*2+j];
							}
						//	System.out.println();
						}						
						
							
				 			Thread.sleep(15);
				 			//}*/
				 		
				 		System.out.println("�̹��� ������");
				 		sock.receive(dp);
				 		ByteArrayInputStream bais = new ByteArrayInputStream(rcvbyte);
				 		bf = ImageIO.read(bais);
				 		
				 		if(bf != null) {
				 			imc = new ImageIcon(bf);
				 			//panel_Your.setIcon(imc);
				 			PANEL.setIcon(imc);
				 			Thread.sleep(15);
				 			}
				 		}
				 	} catch (Exception e) {
				 			e.printStackTrace();
				 			System.out.println("�̹��� ���� ���� �߻�");
				 		}
				 }
		   };
		   th.start();
	   }
	   
	   void gameStart(int speed){
			//���� ���� �����带 ������Ų��.
			if(th!=null){
				try {isPlay = false;th.join();} 
				catch (InterruptedException e) {e.printStackTrace();}
			}
			
			//�ʼ���
			map = new Block[maxY][maxX];
			blockList = new ArrayList<Block>();
			nextBlocks = new ArrayList<TetrisBlock>();
			
			//��������
			shap = getRandomTetrisBlock();
			hold = null;
			isHold = false;
			controller = new TetrisController(shap,maxX-1,maxY-1,map);
			for(int i=0 ; i<5 ; i++){
				nextBlocks.add(getRandomTetrisBlock());
			}
			
			//������ ����
			isPlay = true;
			th = new Thread() {
				public void run() {
					int countMove = (21-speed)*5;
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
								
								if(controller!=null && !controller.moveDown()) fixingTetrisBlock();
							}
							repaint();
							continue;
						}
						
						countMove--;
						if (countMove == 0) {
							countMove = (21-speed)*5;
							if (controller != null && !controller.moveDown()) countDown = down;
						}
						
						if (countUp != 0) {
							countUp--;
							if (countUp == 0) {
								countUp = up;
								addBlockLine(1);
							}
						}
						repaint();
					}//while()
				}//run()
			};
			th.start();
		}
		
		/**
		 * ��(���̱�, ��)�� ���Ϸ� �̵��Ѵ�.
		 * @param lineNumber	
		 * @param num -1 or 1
		 */
		public void dropBoard(int lineNumber, int num){
			
			// ���� ����Ʈ����.
			this.dropMap(lineNumber,num);
			
			//��ǥ�ٲ��ֱ�(1��ŭ����)
			this.changeTetrisBlockLine(lineNumber,num);
			
			//�ٽ� üũ�ϱ�
			this.checkMap();
		}
		
		
		/**
		 * lineNumber�� ���� ���ε��� ��� numĭ�� ������.
		 * @param lineNumber
		 * @param num ĭ�� -1,1
		 */
		private void dropMap(int lineNumber, int num) {
			if(num==1){
				//���پ� ������
				for(int i= lineNumber ; i>0 ;i--){
					for(int j=0 ; j<map[i].length ;j++){
						map[i][j] = map[i-1][j];
					}
				}
				
				//�� ������ null�� �����
				for(int j=0 ; j<map[0].length ;j++){
					map[0][j] = null;
				}
			}
			else if(num==-1){
				//���پ� �ø���
				for(int i= 1 ; i<=lineNumber ;i++){
					for(int j=0 ; j<map[i].length ;j++){
						map[i-1][j] = map[i][j];
					}
				}
				
				//removeLine�� null�� �����
				for(int j=0 ; j<map[0].length ;j++){
					map[lineNumber][j] = null;
				}
			}
		}
		
		
		/**
		 * lineNumber�� ���� ���ε��� ��� num��ŭ �̵���Ų��.
		 * @param lineNumber 
		 * @param num	�̵��� ����
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
		 * ��Ʈ���� ���� ������Ų��. 
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
			
			// drawList �߰�
			for (Block block : shap.getBlock()) {
				blockList.add(block);
			}
			
			// check
			isCombo = checkMap();

			if(isCombo) removeLineCombo++;
			else removeLineCombo = 0;
			
			//���� ��Ʈ���� ���� �����´�.
			this.nextTetrisBlock();
			
			//Ȧ�尡�ɻ��·� ������ش�.
			isHold = false;
		}//fixingTetrisBlock()
		
		
		/**
		 * 
		 * @return true-����⼺��, false-��������
		 */
		private boolean checkMap(){
			boolean isCombo = false;
			int count = 0;
			Block mainBlock;
			
			for(int i=0 ; i<blockList.size() ;i++){
				mainBlock = blockList.get(i);
				
				// map�� �߰�
				if(mainBlock.getY()<0 || mainBlock.getY() >=maxY) continue;
				
				if(mainBlock.getY()<maxY && mainBlock.getX()<maxX) 
					map[mainBlock.getY()][mainBlock.getX()] = mainBlock;

				// ���� �� á�� ���. ������ �����Ѵ�.
				if (mainBlock.getY() == 1 && mainBlock.getX() > 2 && mainBlock.getX() < 7) {
					this.gameEndCallBack();
					break;
				}
				
				//1�ٰ��� üũ
				count = 0;
				for (int j = 0; j < maxX; j++) {
					if(map[mainBlock.getY()][j] != null) count++;
				}
				
				//block�� �ش� line�� �����.
				if (count == maxX) {
					removeLineCount++;
					this.removeBlockLine(mainBlock.getY());
					isCombo = true;
				}
			}
			return isCombo;
		}
		
		/**
		 * ��Ʈ���� �� ����Ʈ���� ��Ʈ���� ���� �޾ƿ´�.
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
		 * lineNumber ������ �����ϰ�, drawlist���� �����ϰ�, map�� �Ʒ��� ������.
		 * @param lineNumber ��������
		 */
		private void removeBlockLine(int lineNumber) {
			// 1���� ������
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
		
		
		/**TODO : ���������ݺ�
		 * ������ ����Ǹ� ����Ǵ� �޼ҵ�
		 */
		public void gameEndCallBack(){
			this.isPlay = false;
		}
		
		
		/**
		 * �������� ��Ʈ���� ���� �����ϰ� ��ȯ�Ѵ�.
		 * @return ��Ʈ���� ��
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
		
		public TetrisBlock getBlockClone(TetrisBlock tetrisBlock){
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
			return blocks;
		}	
		
		/**
		 * ���� Ȧ���Ų��.
		 */
		public void playBlockHold(){
			if(isHold) return;
			
			if(hold==null){
				hold = getBlockClone(shap);
				this.nextTetrisBlock();
			}else{
				TetrisBlock tmp = getBlockClone(shap);
				shap = getBlockClone(hold);
				hold = getBlockClone(tmp);
				this.initController();
			}
			
			isHold = true;
		}
		
		
		/**
		 * ���� �ؿ� �ٿ� ���� �����Ѵ�.
		 * @param numOfLine
		 */
		boolean stop = false;
		public void addBlockLine(int numOfLine){
			stop = true;
			// �����Ⱑ ���� ������ ����Ѵ�.
			// �����⸦ ��� ������ �� �ٽ� �����Ѵ�.
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
				//���� �������� ���� ��ġ�� ���� ���� �ø���.
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

		public boolean isPlay(){return isPlay;}
		public void setPlay(boolean isPlay){this.isPlay = isPlay;}
	

	class Serverframe extends JFrame{ // ������ �����ϱ� ������ �� �Է�â
		
		public Serverframe() {
			super("Serverframe");
			setResizable(false);
			setSize(400, 250);
			getContentPane().setLayout(null);
			
			JLabel svip = new JLabel("IP�ּ�");
			svip.setFont(new Font("����", Font.BOLD, 15));
			svip.setBounds(31, 27, 78, 21);
			getContentPane().add(svip);
			
			JLabel svpt = new JLabel("��Ʈ��ȣ");
			svpt.setFont(new Font("����", Font.BOLD, 15));
			svpt.setBounds(31, 63, 105, 21);
			getContentPane().add(svpt);
			
			JLabel svnick = new JLabel("�г���");
			svnick.setFont(new Font("����", Font.BOLD, 15));
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
			
			btnOKButton.setFont(new Font("����", Font.BOLD, 18));
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
			btnCancelButton.setFont(new Font("����", Font.BOLD, 18));
			btnCancelButton.setBounds(217, 150, 120, 35);
			getContentPane().add(btnCancelButton);
		}
	}
		
		class Clientframe extends JFrame { // Ŭ���̾�Ʈ�� �����ϱ� ������ �� �Է�â
			public Clientframe() {
				super("Clientframe");
				setResizable(false);
				setSize(400, 250);
				getContentPane().setLayout(null);
				
				JLabel ctip = new JLabel("���IP");
				ctip.setFont(new Font("����", Font.BOLD, 15));
				ctip.setBounds(31, 27, 78, 21);
				getContentPane().add(ctip);
				
				JLabel ctpt = new JLabel("��Ʈ��ȣ");
				ctpt.setFont(new Font("����", Font.BOLD, 15));
				ctpt.setBounds(31, 63, 105, 21);
				getContentPane().add(ctpt);
				
				JLabel ctnick = new JLabel("�г���");
				ctnick.setFont(new Font("����", Font.BOLD, 15));
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
						
						try {
							local = InetAddress.getLocalHost();
						} catch (UnknownHostException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						
						lblIpValue.setText(local.getHostAddress());
					    lblPortValue.setText(tfcpt);
					    lblNickNameValue.setText(tfcnick);
					    
					    btnStart.setText("�غ��ϱ�");
					    
					    startClient();
						
						setVisible(false);
					}
				});
				
				btnOKButton.setFont(new Font("����", Font.BOLD, 18));
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
				btnCancelButton.setFont(new Font("����", Font.BOLD, 18));
				btnCancelButton.setBounds(217, 150, 120, 35);
				getContentPane().add(btnCancelButton);
			}
		}
}
