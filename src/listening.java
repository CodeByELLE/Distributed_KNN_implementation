package DS;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

public class listening extends Thread {

	node data;

	listening(node p) {
		this.data = p;
	}

	synchronized public void f() throws InterruptedException, ClassNotFoundException, IOException {
		DatagramSocket ds = new DatagramSocket(data.port);
		while (true) {
			// Starting the listening server and wait for requests
			byte[] buf = new byte[1024];
			DatagramPacket dp = new DatagramPacket(buf, 1024);
			ds.receive(dp);
			String str = new String(dp.getData(), 0, dp.getLength());

			DatagramSocket ds1 = new DatagramSocket();

			InetAddress ip = InetAddress.getByName("127.0.0.1");

			// decode the received message
			// the message contains the port and a specific message that depends on the
			// required task
			// 1 - B_Bar for BUR requests
			// 2 - neighbor : for requests that ask the node to add to there R
			// 3 - profile : for profile requests
			String message = str.split(" ")[0];

			String str1 = str.split(" ")[1];
			int temp = Integer.parseInt(str1);
			if (message.equals("B_Bar")) {
				// send the BUR to the sender machine
				machine.sendTo(data.bUr, dp.getPort());

			} else if (message.equals("neignbor")) {
				data.r.add(temp);
				String res = "ok";

				DatagramPacket dp1 = new DatagramPacket(res.getBytes(), res.length(), ip, dp.getPort());
				ds1.send(dp1);
			} else if (message.equals("profile")) {
				// send the profile to the sender machine
				machine.sendTo(data.profile, dp.getPort());
			}

		}

	}

	public void run() {

		try {
			try {
				f();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
