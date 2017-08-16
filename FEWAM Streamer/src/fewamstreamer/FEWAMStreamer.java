package fewamstreamer;

import java.awt.AWTException;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

import javax.swing.ImageIcon;

public class FEWAMStreamer {
	
	Display display;
	
	public static void main(String[] args) throws IOException, AWTException, ClassNotFoundException {
		
		//TODO: add gui and fps monitor/changer
		//TODO: dont throw everything
		
		Scanner scanner = new Scanner(System.in);
		System.out.println("start? (y/n)");
		if(scanner.nextLine().equals("n")) return;
		
		System.out.println("stream port: ");
		int stream_port = Integer.parseInt(scanner.nextLine());
		
		System.out.println("communication port:");
		int communication_port = Integer.parseInt(scanner.nextLine());
		
		scanner.close();
		
		ServerSocket ss = new ServerSocket(stream_port);
		ServerSocket ss2 = new ServerSocket(communication_port);
		
		Socket stream_socket = ss.accept();
		Socket communication_socket = ss2.accept();		
		
		ss.close();
		ss2.close();
		
		ObjectInputStream ois = new ObjectInputStream(stream_socket.getInputStream());
		
		Display display = new Display(communication_socket);
		
		display.currentFrame = null;
		
		display.start();
		display.showDisplay();
		
		while(true) {
			/*ImageIcon ii = (ImageIcon) ois.readObject();
			BufferedImage bi = new BufferedImage(ii.getIconWidth(), ii.getIconHeight(), BufferedImage.TYPE_INT_RGB);
			Graphics bigr = bi.createGraphics();
			bigr.drawImage(ii.getImage(), 0, 0, ii.getIconWidth(), ii.getIconHeight(), null);
			bigr.dispose();*/
			
			ImageIcon icon = (ImageIcon) ois.readObject();
			BufferedImage bi = new BufferedImage(icon.getIconWidth(),icon.getIconHeight(), BufferedImage.TYPE_INT_RGB);
			Graphics g = bi.createGraphics();
			icon.paintIcon(null, g, 0,0);
			g.dispose();
			
			display.currentFrame = bi;
			System.gc();
		}
	}
}
