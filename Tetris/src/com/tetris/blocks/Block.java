package com.tetris.blocks;

import java.awt.Color;
import java.awt.Graphics;

public class Block {
	private int size = 20;
	private int width = size, height = size;
	private int gap = 3;
	private int fixGridX, fixGridY;
	private int posGridX, posGridY;
	private Color color;
	
	
	/**
	 * 
	 * @param fixGridX : �簢�� ���� X �׸�����ǥ
	 * @param fixGridY : �簢�� ���� Y �׸�����ǥ
	 * @param color : �簢�� ����
	 */
	public Block(int fixGridX, int fixGridY, Color color) {
		this.fixGridX = fixGridX;
		this.fixGridY = fixGridY;
		this.color=color;
	}
	

	/**
	 * �簢���� �׷��ش�.
	 * @param g
	 */
	public void drawColorBlock(Graphics g){
	    g.setColor(color);
		g.fillRect((fixGridX+posGridX)*size, (fixGridY+posGridY)*size, width, height);
		g.setColor(Color.BLACK);
		g.drawRect((fixGridX+posGridX)*size, (fixGridY+posGridY)*size, width, height);
		g.drawLine((fixGridX+posGridX)*size, (fixGridY+posGridY)*size, (fixGridX+posGridX)*size+width, (fixGridY+posGridY)*size+height);
		g.drawLine((fixGridX+posGridX)*size, (fixGridY+posGridY)*size+height, (fixGridX+posGridX)*size+width, (fixGridY+posGridY)*size);
		g.setColor(color);
		g.fillRect((fixGridX+posGridX)*size+gap, (fixGridY+posGridY)*size+gap, width-gap*2, height-gap*2);
		g.setColor(Color.BLACK);
		g.drawRect((fixGridX+posGridX)*size+gap, (fixGridY+posGridY)*size+gap, width-gap*2, height-gap*2);
	}
	
	/**
	 * ���� ���� ������ǥ�� �����ش�.
	 * @return ������� X������ǥ
	 */
	public int getX(){return posGridX + fixGridX;}	
	
	
	/**
	 * ���� ���� ������ǥ�� �����ش�.
	 * @return ������� Y������ǥ
	 */
	public int getY(){return posGridY + fixGridY;}

	
	/**
	 * Getter Setter
	 */
	public int getPosGridX(){return this.posGridX;}
	public int getPosGridY(){return this.posGridY;}
	public void setPosGridX(int posGridX) {this.posGridX = posGridX;}
	public void setPosGridY(int posGridY) {this.posGridY = posGridY;}
	public void setPosGridXY(int posGridX, int posGridY){this.posGridX = posGridX;this.posGridY = posGridY;}
	public void setFixGridX(int fixGridX) {this.fixGridX = fixGridX;}
	public void setFixGridY(int fixGridY) {this.fixGridY = fixGridY;}
	public void setFixGridXY(int fixGridX, int fixGridY){this.fixGridX = fixGridX;this.fixGridY = fixGridY;}
}

