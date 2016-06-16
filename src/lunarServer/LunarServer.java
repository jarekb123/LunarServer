package lunarServer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.StringTokenizer;

import java.util.Base64;
import java.util.Properties;

public class LunarServer extends Thread {
	
	ServerSocket serverSocket = null;
	PrintWriter output;
	BufferedReader input;
	
	public LunarServer() throws IOException
	{
		serverSocket = new ServerSocket(5555);
	}
	@Override
	public void run()
	{
		String received = null;
		while(true)
		{
			
				Socket socket;
				try {
					socket = serverSocket.accept();
					System.out.println("connected");
					input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					output = new PrintWriter(socket.getOutputStream(), true);
					
					
						while((received = input.readLine()) != null)
						{
							System.out.println("Received: " + received);
							StringTokenizer token = new StringTokenizer(received);
							String command = token.nextToken();
							switch(command)
							{
							case "loadlevel" :
								int level = Integer.parseInt(token.nextToken());
								sendlevel(level);
								break;
							}
						}
						
						
					socket.close();
				
				} catch (IOException e) {
					e.printStackTrace();
				}
					
			
		}
	}
	private void sendlevel(int level)
	{
		System.setProperty("file.encoding","UTF-8");
		 InputStream is;
		 
       try 
       {
	       	Properties properties=new Properties();
	
	       	is = new FileInputStream(new File("level"+level+".properties"));
			properties.load(is);
			int number=Integer.parseInt(properties.getProperty("levelNo"));
			double gravity=Double.parseDouble(properties.getProperty("gravity"));
			int bonusNumber=Integer.parseInt(properties.getProperty("bonusNo"));
			double maxVx=Double.parseDouble(properties.getProperty("maxVx"));
			double maxVy=Double.parseDouble(properties.getProperty("maxVy"));
			String landingsx = properties.getProperty("landingsx");
			String landingsy = properties.getProperty("landingsy");
			String imgPath = properties.getProperty("imgPath");
			//gameMap.loadMap(filename);
			
			String toSend = "sendlevel " + imgPath 
					+ " " + gravity+ " " +bonusNumber
					+ " " +maxVx+ " " +maxVy;
			output.println(toSend);
			
			String x = properties.getProperty("x");
			String y = properties.getProperty("y");
			
			toSend = "landings coordinates";
			output.println(toSend);
			output.println(landingsx);
			output.println(landingsy);
			
			toSend = "map coordinates";
			output.println(toSend);
			output.println(x);
			output.println(y);
			
			
			toSend = "image bytes start";
			output.println(toSend);
			
			// kodowanie obrazka na base64
			File file = new File(imgPath);
			FileInputStream file_input = new FileInputStream(file);
			int length = (int) file.length();
			System.out.println("file_len: "+length);
			byte file_byte[] = new byte[length];
			file_input.read(file_byte);
			file_input.close();
			String base64_coded = Base64.getEncoder().encodeToString(file_byte);
		//	System.out.println(base64_coded);
			output.println(base64_coded);
			
			toSend = "image bytes end";
			output.println(toSend);
			
       } 
       catch (FileNotFoundException e)
       {
      	 e.printStackTrace();	
       } 
       catch (IOException e) 
       {
      	 e.printStackTrace();
       }
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("Game Server Lunar 1.0");
		
		try {
			Thread t = new LunarServer();
			
			t.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}
