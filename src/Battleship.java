import Main.Handlers.*;
import Main.Models.*;

import java.io.*;
import java.net.*;

import javax.swing.*;

public class Battleship
{
	public static void main(String[] args)
	{
//		initializeGui();
		
		String serverName = "ec2-18-207-150-67.compute-1.amazonaws.com";
		int port = 8989;
		
		String username = JOptionPane.showInputDialog(new JFrame(), "Enter username: ");
		System.out.println("Connecting to " + serverName + " on port " + port);
		
		try (Socket conn = new Socket(serverName, port);
				BufferedReader read = new BufferedReader(new InputStreamReader(conn.getInputStream())))
		{
			
			System.out.println("Just connected to " + conn.getRemoteSocketAddress());
			
			ServerHandler sh = new ServerHandler(conn);
			BattleshipGui gui = new BattleshipGui(sh);
			
			sh.SendLoginMessage(username);
			
			String input = read.readLine();
			
			int loop = 0;
			
			while (input != null)
			{
				Message message = MessageFactory.parse(input);
				if(message.type == "Chat")
				{
					ChatMessage chat = (ChatMessage) message;
					gui.AppendTextArea(chat.chatMessage);
				}
				
				System.out.println("Message type: "+ message.type);
				System.out.println(message);
				
				input = read.readLine();
				loop++;
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
