package fewamrecorder;

import java.util.Scanner;

import javax.swing.ImageIcon;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.net.UnknownHostException;

public class FEWAMRecorder {
	public static void main(String[] args) throws IOException, AWTException, InterruptedException {
		//throwing everything just for later
		//TODO: stop throwing everything
		
		System.out.println("start? (y/n)");
		
		Scanner scanner = new Scanner(System.in);
		if(scanner.nextLine().equals("n")) { return; }
		//lol
		
		System.out.print("enter the stream port: ");
		int stream_port = Integer.parseInt(scanner.nextLine());
		
		System.out.print("enter the communication port: ");
		int communication_port = Integer.parseInt(scanner.nextLine());
		
		System.out.print("enter the ip address: ");
		String ip = scanner.nextLine();
		
		scanner.close();
		
		new CommunicationThread(ip, communication_port).start();
		
		Socket socket = new Socket(ip, stream_port);
		ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
		
		Robot robo = new Robot();
		Rectangle rect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
		
		while(true) { oos.writeObject(new ImageIcon(robo.createScreenCapture(rect))); /*Thread.sleep(100);*/ oos.reset(); }
	}
	
}