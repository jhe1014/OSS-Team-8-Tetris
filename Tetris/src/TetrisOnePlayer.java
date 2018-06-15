
import java.awt.EventQueue;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import com.tetris.blocks.*;
import com.tetris.controller.*;
import com.tetris.shape.*;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;


public class TetrisOnePlayer extends JFrame {

	private JPanel contentPane;
	public static final int BLOCK_SIZE = 20;
	public static final int BOARD_X = 120;
	public static final int BOARD_Y = 50;
	private int minX=0, minY=0, maxX=10, maxY=21, down=50, up=0;
	private ArrayList<Block> blockList;
	private ArrayList<TetrisBlock> nextBlocks;
	private Block map[][];
	private TetrisController controller;
	private TetrisBlock shap;
	private TetrisBlock hold;
	private int speed = 1;
	
	private Thread th;
	private boolean isPlay = false;
	private boolean isHold = false;
	private int removeLineCount = 0;
	private int removeLineCombo = 0;


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
		
		JPanel PanelHold = new JPanel() {
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
		PanelHold.setBackground(Color.DARK_GRAY);
		PanelHold.setBounds(10, 105, 100, 100);
		contentPane.add(PanelHold);
		
		JPanel MainPanel = new JPanel() {
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
		
		MainPanel.setBackground(Color.DARK_GRAY);
		MainPanel.setBounds(120, 65, 200, 420);
		
		contentPane.add(MainPanel);
		
		MainPanel.addKeyListener(new KeyListener() {
			public void keyPressed(KeyEvent e) {
					//System.out.println("Ű�̺�Ʈ");
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
						fixingTetrisBlock();
					}else if(e.getKeyCode() == KeyEvent.VK_SHIFT){ 
						playBlockHold();
					}
					repaint();
				}
					public void keyReleased(KeyEvent arg0) {}
					public void keyTyped(KeyEvent e) {}
		});
		
		JPanel PanelNext = new JPanel() {
			public void paintComponent(Graphics g) {
				g.clearRect(0, 0, this.getWidth(), this.getHeight()+1);
				
				g.setColor(Color.BLACK);
				g.fillRect(0, 0, this.getWidth(), this.getHeight()+1);
				
				g.setColor(Color.DARK_GRAY);
				for(int i=1;i<5;i++) g.drawLine(BLOCK_SIZE*(minX-1) ,0 + BLOCK_SIZE*(i), BLOCK_SIZE*(minX+5),0 + BLOCK_SIZE*(i));
				for(int i=1;i<5;i++) g.drawLine(BLOCK_SIZE*(minY+i) ,0 + BLOCK_SIZE*(i-4), BLOCK_SIZE*(minY+i),0 + BLOCK_SIZE*(minY+5)-1);

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
		PanelNext.setBackground(Color.DARK_GRAY);
		PanelNext.setBounds(330, 105, 100, 100);
		contentPane.add(PanelNext);
		
		JPanel PanelNextBoard = new JPanel() {
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
		lblTetrisGame.setFont(new Font("����", Font.BOLD, 30));
		
		JButton btnStart = new JButton("�����ϱ�");
		btnStart.setBounds(336, 520, 125, 29);
		contentPane.add(btnStart);
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
					if(isPlay) {
						JOptionPane.showMessageDialog(null, "�̹� �÷������Դϴ�!", "Error", JOptionPane.ERROR_MESSAGE);
						MainPanel.requestFocus();
					}
					
					else {
						gameStart(speed);
						MainPanel.requestFocus();
					}
				}
		});
		
		JButton btnEnd = new JButton("������");
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
		lblHold.setFont(new Font("����", Font.BOLD, 20));
		contentPane.add(lblHold);
		
		JLabel lblNext = new JLabel("N E X T");
		lblNext.setBounds(352, 80, 78, 21);
		lblNext.setFont(new Font("����", Font.BOLD, 20));
		contentPane.add(lblNext);
		
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
}
