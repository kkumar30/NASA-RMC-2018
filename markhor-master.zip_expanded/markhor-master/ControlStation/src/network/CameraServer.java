package network;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.imageio.ImageIO;

import gui.ImagePanel;

public class CameraServer implements Runnable
{
	public ServerSocket server;
	private ImagePanel ipanel;
	
	public CameraServer(ImagePanel ipanel)
	{
		this.ipanel = ipanel;
		try
		{
			server = new ServerSocket(11001);
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void run()
	{
		while(true)
		{
			System.out.println("GotSomething!");
			Socket sock;
			try 
			{
				sock = this.server.accept();
				InputStream in = sock.getInputStream();
				Integer bytesRead = 0;
				byte[] buffer = new byte[65536];
				while((bytesRead = in.read(buffer)) != -1)
				{
					System.out.println("Read" + bytesRead.toString() + " bytes:[" + new String(buffer) + "]");
				}
				BufferedImage jpg = ImageIO.read(new ByteArrayInputStream(buffer));
				//ImageIO.write(jpg, "JPG", new File("robot.jpg"));
				ipanel.setNewImage(jpg);
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
			
			
		}
	}
}
