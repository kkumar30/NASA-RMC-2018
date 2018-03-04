package network;

import java.awt.image.BufferedImage;
import java.io.*;
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
				DataOutputStream toPserver = new DataOutputStream(sock.getOutputStream());
				DataInputStream fromPserver = new DataInputStream(sock.getInputStream());


				File dlfile = new File("/home/thagen/NASA-RMC-2018/Ibex/ControlStation/pictures/contours.jpg");
				dlfile.createNewFile();

				DataOutputStream dos = new DataOutputStream(new FileOutputStream(dlfile));
				while (fromPserver.available() > 0)
				{
					dos.writeByte(fromPserver.readByte());
				}

				BufferedImage jpg = ImageIO.read(dlfile);

				ipanel.setNewImage(jpg);
				sock.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}


		}
	}
}