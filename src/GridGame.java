import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class GridGame extends JPanel implements ActionListener, KeyListener{
	JFrame frame = new JFrame();
	
	Timer t = new Timer(1000 / 60, this);
	
	Grid grid = new Grid();
	
	Block activeBlock = new Block();
	ArrayList<Block> inactiveBlocks = new ArrayList<Block>();
	
	public static void main(String[] args) {
		new GridGame().start();
	}
	
	public void start(){
		setPreferredSize(new Dimension(500, 500));
		frame.add(this);
		frame.setVisible(true);
		frame.pack();
		frame.addKeyListener(this);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		activeBlock = new Block();
		
		t.start();
	}

	int x = 0;
	
	@Override
	public void paintComponent(Graphics g){
		for(Block b : inactiveBlocks){
			b.draw(g);
		}
		activeBlock.draw(g);
		grid.draw(g);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		activeBlock.update(inactiveBlocks);
		if(!activeBlock.active){
			inactiveBlocks.add(activeBlock);
			activeBlock = new Block();
		}
		repaint();
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
			t.stop();
			System.exit(0);
		}
		
		if(e.getKeyCode() == KeyEvent.VK_LEFT){
			activeBlock.moveLeft();
		}
		
		if(e.getKeyCode() == KeyEvent.VK_RIGHT){
			activeBlock.moveRight();
		}
		
		if(e.getKeyCode() == KeyEvent.VK_DOWN){
			activeBlock.moveDown();
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
}

class Grid{
	private int yoffset = 0;
	private int xoffset = 0; 
	
	public void draw(Graphics g){
		g.setColor(Color.BLACK);
		for(int i = 0; i < 25; i++){
			g.fillRect(0, yoffset, 500, 1);
			yoffset += 20;
		}
		yoffset = 0;
		
		for(int i = 0; i < 25; i++){
			g.fillRect(xoffset, 0, 1, 500);
			xoffset += 20;
		}
		xoffset = 0;
	}
}

class Block{
	public int xPosition = 0;
	public int yPosition = 0;
	public Color color;
	
	int xMove = 20;
	int yMove = 20;
	
	private int floor = 475;
	public boolean active = true;
	
	private boolean moveLeft = false;
	private boolean moveRight = false;
	
	int speed = 1000;
	long dropTime = -1;
	
	public Block(){
		xPosition = 225;
		yPosition = 0;
		
		switch(new Random().nextInt(4)){
		case 0:
			color = Color.BLUE;
			break;
		case 1:
			color = Color.RED;
			break;
		case 2:
			color = Color.GREEN;
			break;
		case 3:
			color = Color.YELLOW;
			break;
		}
		 
	}
	
	public void moveLeft(){
		moveLeft = true;
	}
	
	public void moveRight(){
		moveRight = true;
	}
	
	public void moveDown(){
		
		yPosition += yMove;
	}
	
	public void draw(Graphics g){
		g.setColor(color);
		g.fillRect(xPosition - 4, yPosition, 20, 20);
	}
	
	public void update(ArrayList<Block> blocks){
		if(yPosition >= floor){
			active = false;
		}
		
		if(checkBlock(xPosition, yPosition, blocks)){
			yPosition -= yMove;
			active = false;
		}

		if(moveLeft && !checkBlock(xPosition - xMove, yPosition, blocks)){
			xPosition -= xMove;
		}
		
		if(moveRight && !checkBlock(xPosition + xMove, yPosition, blocks)){
			xPosition += xMove;
		}
		moveLeft = false;
		moveRight = false;
		if(active){
			if(System.currentTimeMillis() - dropTime >= speed){
				yPosition += yMove;
				dropTime = System.currentTimeMillis();
			}
		}
	}
	
	private boolean checkBlock(int x, int y, ArrayList<Block> blocks){
		for(Block b : blocks){
			if(x == b.xPosition && y >= b.yPosition){
				return true;
			}
		}
		
		return false;
	}
}

