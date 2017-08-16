package fewamstreamer;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.awt.event.*;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Display extends JFrame {
	
	public static int height = 600, width = 800;
	
	/*static BufferedImage bg = new BufferedImage(height, width, BufferedImage.TYPE_INT_RGB);
	static Graphics bgGraphics = bg.createGraphics();*/
	//^image as entire background, slightly smoother than drawing directly, will use if I add buttons
	static BufferedImage currentFrame = new BufferedImage(800, 600, BufferedImage.TYPE_INT_RGB);
	static BufferedImage tempFrame;
	
	static JPanel panel; //because mouseevents wont register without the jpanel
	
	static Socket socket;
	static Scanner scanner;
	static PrintWriter writer;
	
	static Graphics graphics;
	
	public Display(Socket socket) {
		this.setTitle("VideoStream");
		this.setSize(width, height);
		graphics = this.getContentPane().getGraphics();
		//this.setUndecorated(true);
		this.getContentPane().getSize();
		//this.setExtendedState(MAXIMIZED_BOTH);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		panel = new JPanel();
		panel.setSize(this.getWidth(), this.getHeight());
		panel.setLocation(0, 0);
		panel.setVisible(true);
		this.height = this.getHeight();
		this.width = this.getWidth();
		panel.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				/*writer.println("click");
				writer.println(e.getX() + "," + e.getY());
				writer.println("mouse_button_left");*/
			}

			@Override
			public void mousePressed(MouseEvent e) {
				writer.println("mouse_down");
				writer.println((float) e.getX() / (float) width + "," + (float) e.getY() / (float) height);
				writer.println("mouse_button_left");
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				System.out.println("sending x:" + e.getX() + ", y:" + e.getY());
				writer.println("mouse_up");
				writer.println((float) e.getX() / (float) width + "," + (float) e.getY() / (height));
				writer.println("mouse_button_left");
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		panel.addMouseMotionListener(new MouseMotionListener() {

			@Override
			public void mouseDragged(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseMoved(MouseEvent e) {
				writer.println("moveto");
				writer.println((float) e.getX() / (float) width + "," + (float) e.getY() / (float) height);
				System.out.println("sent motionevent x:" + (float) e.getX() / (float) width + ", y:" + (float) e.getY() / (float) height);		
				System.out.println("x:" + (float) e.getX() + ", y:" + (float) e.getY());
				System.out.println("width:" + (float) width + ", height:" + (float) height);
			}
			
		});
		
		
		this.addKeyListener(new KeyListener(){

			@Override
			public void keyTyped(KeyEvent e) {	}

			@Override
			public void keyPressed(KeyEvent e) {
				writer.println("keypress");
				writer.println(e.getKeyCode());
			}

			@Override
			public void keyReleased(KeyEvent e) {
				writer.println("keyrelease");
				writer.println(e.getKeyCode());
			}
			
		});
		this.setLayout(null);
		this.add(panel);
		this.socket = socket;
		try {
			this.scanner = new Scanner(this.socket.getInputStream());
			this.writer = new PrintWriter(this.socket.getOutputStream(), true);
			this.writer.println("ready");
			System.out.println("received: " + scanner.nextLine());
			this.writer.println("moveto");
			this.writer.println("150,150");
			if(!scanner.nextLine().equals("done")) System.out.println("didnt say done");
		} catch (IOException e) {
			System.out.println("error with scanner or writer");
			e.printStackTrace();
		}
	}
	
	public static void start() {
		//while(true) { if(!currentFrame.equals(tempFrame)) { tempFrame = currentFrame; } }
	}
	
	public void showDisplay() {
		this.setVisible(true);
	}
	
	public static void update() {	}
	
	public void paint(Graphics g) {
		/*g.drawImage(currentFrame, 0, 0, this.panel.getWidth(), this.panel.getHeight(), null);*/
		this.getContentPane().getGraphics().drawImage(currentFrame, 0, 0, this.getContentPane().getWidth(), this.getContentPane().getHeight(), null);
		//g.drawImage(currentFrame, 0, 0, this.getWidth(), this.getHeight(), null);
		//g.drawImage(currentFrame.getScaledInstance(800, -1, Image.SCALE_SMOOTH), 0, 0, null);
		this.panel.setSize(this.getSize());
		this.panel.setLocation(0, 0);
		this.getContentPane().add(panel);
		this.height = (int) this.getContentPane().getHeight();
		this.width = (int) this.getContentPane().getWidth();
		repaint();
	}
}
