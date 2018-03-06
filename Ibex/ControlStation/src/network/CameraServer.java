package network;

import java.awt.image.BufferedImage;
import java.io.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Base64;

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
//				DataInputStream fromPserver = new DataInputStream(sock.getInputStream());
				BufferedInputStream iStream = new BufferedInputStream(sock.getInputStream());

				FileOutputStream fos = new FileOutputStream(new File("/home/thagen/NASA-RMC-2018/Ibex/ControlStation/pictures/contours.jpg"));

				int c = 0;
				while ((c = iStream.read()) != -1) {
					fos.write(c);
				}


//
//				File dlfile = new File("/home/thagen/NASA-RMC-2018/Ibex/ControlStation/pictures/contours.jpg");
//				DataOutputStream dos = new DataOutputStream(new FileOutputStream(dlfile));
//				while(fromPserver.available()>1)
//				{
//					dos.writeByte(fromPserver.readByte());
//				}


//				dos.write(imgData);

				BufferedImage jpg = ImageIO.read(new File("/home/thagen/NASA-RMC-2018/Ibex/ControlStation/pictures/contours.jpg"));


				ipanel.setNewImage(jpg);
				fos.close();
				iStream.close();

				sock.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}


		}
	}
}