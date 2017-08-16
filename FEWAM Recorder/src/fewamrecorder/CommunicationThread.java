package fewamrecorder;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class CommunicationThread extends Thread {

	String ip;
	int port;
	Socket socket;
	
	//for remote input
	Robot robo;
	
	public CommunicationThread(String ip, int port) {
		this.ip = ip;
		this.port = port;
		try {
			robo = new Robot();
		} catch(AWTException e) {
			System.out.println("error creating robot");
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		try {
			socket = new Socket(ip, port);
			//BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			//scanner instead, until file transfers
			Scanner scanner = new Scanner(socket.getInputStream());
			PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);
			
			pw.println("ready");
			
			if(!scanner.nextLine().equals("ready")) System.out.println("communication error");
			
			System.out.println("communication established");
			
			robo.setAutoDelay(10);
			
			//wait for streamer to send something
			while(true) {
				
				String next = scanner.nextLine();
				
				if(next.equals("moveto")) {
					//streamer will send something in format of: "x,y"
					System.out.println("moving mouse");
					
					String[] xy = scanner.nextLine().split(",");
					
					System.out.println("mouse moving to " + java.util.Arrays.toString(xy));
					
					robo.mouseMove((int) (Float.parseFloat(xy[0]) * (float) Toolkit.getDefaultToolkit().getScreenSize().getWidth()), (int) (Float.parseFloat(xy[1]) * (float) Toolkit.getDefaultToolkit().getScreenSize().getHeight()));
					
					pw.println("done");
				} /*else if(next.equals("click")) {
					//streamer will send something in format of: "x,y"
					System.out.println("clicking mouse");
					
					String[] xy = scanner.nextLine().split(",");
					
					System.out.println("clicking mouse at " + java.util.Arrays.toString(xy));
					
					robo.mouseMove((int) (Float.parseFloat(xy[0]) * (float) Toolkit.getDefaultToolkit().getScreenSize().getWidth()), (int) (Float.parseFloat(xy[1]) * (float) Toolkit.getDefaultToolkit().getScreenSize().getHeight()));
					
					String mouseBtn = scanner.nextLine();
					
					if(mouseBtn.equals("mouse_button_left")) {
						robo.mousePress(InputEvent.BUTTON1_MASK);
						robo.mouseRelease(InputEvent.BUTTON1_MASK);
					} else if(mouseBtn.equals("mouse_button_right")) {
						robo.mousePress(InputEvent.BUTTON2_MASK);
						robo.mouseRelease(InputEvent.BUTTON2_MASK);
					} else {
						System.out.println("error, received: " + mouseBtn);
					}
					
					pw.println("done");
					
				}*/	else if(next.equals("mouse_down")) {

					//streamer will send something in format of: "x,y"
					System.out.println("mouse going down");
					
					String[] xy = scanner.nextLine().split(",");
					
					System.out.println("mouse going down at " + java.util.Arrays.toString(xy));
					
					robo.mouseMove((int) (Float.parseFloat(xy[0]) * (float) Toolkit.getDefaultToolkit().getScreenSize().getWidth()), (int) (Float.parseFloat(xy[1]) * (float) Toolkit.getDefaultToolkit().getScreenSize().getHeight()));
					
					String mouseBtn = scanner.nextLine();
					
					if(mouseBtn.equals("mouse_button_left")) {
						robo.mousePress(InputEvent.BUTTON1_MASK);
					} else if(mouseBtn.equals("mouse_button_right")) {
						robo.mousePress(InputEvent.BUTTON2_MASK);
					} else {
						System.out.println("error, received: " + mouseBtn);
					}
					
					pw.println("done");
					
				} else if(next.equals("mouse_up")) {

					//streamer will send something in format of: "x,y"
					System.out.println("lifting mouse");
					
					String[] xy = scanner.nextLine().split(",");
					
					System.out.println("lifting mouse at " + java.util.Arrays.toString(xy));
					
					robo.mouseMove((int) (Float.parseFloat(xy[0]) * (float) Toolkit.getDefaultToolkit().getScreenSize().getWidth()), (int) (Float.parseFloat(xy[1]) * (float) Toolkit.getDefaultToolkit().getScreenSize().getHeight()));
					
					String mouseBtn = scanner.nextLine();
					
					if(mouseBtn.equals("mouse_button_left")) {
						robo.mouseRelease(InputEvent.BUTTON1_MASK);
					} else if(mouseBtn.equals("mouse_button_right")) {
						robo.mouseRelease(InputEvent.BUTTON2_MASK);
					} else {
						System.out.println("error, received: " + mouseBtn);
					}
					
					pw.println("done");
					
				} else if(next.equals("keypress")) {
					
					int keycode = Integer.parseInt(scanner.nextLine());
					
					System.out.println("pressing key: " + (char) keycode);
					
					robo.keyPress(keycode);
					
					pw.println("done");
				} else if(next.equals("keyrelease")) {
					
					int keycode = Integer.parseInt(scanner.nextLine());
					
					System.out.println("releasing key: " + (char) keycode);
					
					robo.keyRelease(keycode);
					
					pw.println("done");
				} else {
					System.out.println("unknown command: " + next);
				}
				
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
