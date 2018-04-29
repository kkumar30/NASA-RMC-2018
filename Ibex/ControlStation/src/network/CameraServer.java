package network;

import java.awt.image.BufferedImage;
import java.io.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.Buffer;
import java.util.Base64;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import gui.ImagePanel;
import gui.*;

import static gui.GUI.pose;

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


				FileOutputStream fos = new FileOutputStream(new File("pictures/contours.jpg"));
				int c = 0;

				while ((c=iStream.read())!=-1){

					fos.write(c);
				}





				BufferedImage jpg = ImageIO.read(new File("pictures/contours.jpg"));
				ipanel.setNewImage(jpg);
				try{
				TimeUnit.SECONDS.sleep(2);
				} catch (Exception e){
					System.out.println(e);
				}
				BufferedImage poseImg = null;
				if (pose == 0.0){poseImg = ImageIO.read(new File("pictures/pose/0_pose.jpg"));}
				else if (pose == 1.0){ poseImg = ImageIO.read(new File("pictures/pose/1_pose.jpg"));}
				else if (pose == 2.0){ poseImg = ImageIO.read(new File("pictures/pose/2_pose.jpg"));}
				else if (pose == 3.0){ poseImg = ImageIO.read(new File("pictures/pose/3_pose.jpg"));}
				else if (pose == 4.0){ poseImg = ImageIO.read(new File("pictures/pose/4_pose.jpg"));}
				else if (pose == 5.0){ poseImg = ImageIO.read(new File("pictures/pose/5_pose.jpg"));}
				else if (pose == 6.0){ poseImg = ImageIO.read(new File("pictures/pose/6_pose.jpg"));}
				else if (pose == 7.0){ poseImg = ImageIO.read(new File("pictures/pose/7_pose.jpg"));}

				ipanel.setNewImage(poseImg);
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