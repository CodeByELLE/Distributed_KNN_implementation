package DS;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;

public class machine {

	// the machine class contains 3 main elements

	// the data node
	node data;

	// the listening thread
	listening lth;

	// the processing thread
	processing pth;

	machine(node nn) {
		this.data = nn;
		this.lth = new listening(this.data);
		this.pth = new processing(this.data);

		// starting the listening and the processing threads
		lth.start();
		pth.start();

	}

	public static String connect_to_node(int port, String s) throws IOException {
		DatagramSocket ds = new DatagramSocket();
		String str = s;
		InetAddress ip = InetAddress.getByName("127.0.0.1");
		DatagramPacket dp = new DatagramPacket(str.getBytes(), str.length(), ip, port);
		ds.send(dp);

		byte[] buf = new byte[1024];
		DatagramPacket dp1 = new DatagramPacket(buf, 1024);
		ds.receive(dp1);
		String str1 = new String(dp1.getData(), 0, dp1.getLength());

		return str1;

	}

	public static ArrayList<Integer> connect_to_node_a(int port, String s) throws IOException, ClassNotFoundException {
		DatagramSocket ds = new DatagramSocket();
		String str = s;
		InetAddress ip = InetAddress.getByName("127.0.0.1");
		DatagramPacket dp = new DatagramPacket(str.getBytes(), str.length(), ip, port);
		ds.send(dp);

		byte[] recvBuf = new byte[5000];
		DatagramPacket dp1 = new DatagramPacket(recvBuf, recvBuf.length);
		ds.receive(dp1);

		ByteArrayInputStream byteStream = new ByteArrayInputStream(recvBuf);

		ObjectInputStream iss2 = new ObjectInputStream(new BufferedInputStream(byteStream));

		Object o = iss2.readObject();

		ArrayList<Integer> st = (ArrayList<Integer>) o;

		return st;

	}

	public static ArrayList<Double> connect_to_node_p(int port, String s) throws IOException, ClassNotFoundException {
		DatagramSocket ds = new DatagramSocket();
		String str = s;
		InetAddress ip = InetAddress.getByName("127.0.0.1");
		DatagramPacket dp = new DatagramPacket(str.getBytes(), str.length(), ip, port);
		ds.send(dp);

		byte[] recvBuf = new byte[30000];
		DatagramPacket dp1 = new DatagramPacket(recvBuf, recvBuf.length);
		ds.receive(dp1);

		ByteArrayInputStream byteStream = new ByteArrayInputStream(recvBuf);

		ObjectInputStream iss2 = new ObjectInputStream(new BufferedInputStream(byteStream));

		Object o = iss2.readObject();

		ArrayList<Double> st = (ArrayList<Double>) o;

		return st;

	}

	public static void sendTo(Object o, int desPort)

	{
		try

		{
			InetAddress address = InetAddress.getByName("localhost");
			DatagramSocket ds = new DatagramSocket();
			DatagramPacket packet;
			ByteArrayOutputStream byteStream = new ByteArrayOutputStream(30000);
			ObjectOutputStream os = new ObjectOutputStream(new BufferedOutputStream(byteStream));
			os.flush();
			os.writeObject(o);
			os.flush();
			// retrieves byte array
			byte[] sendBuf = byteStream.toByteArray();
			packet = new DatagramPacket(sendBuf, sendBuf.length, address, desPort);
			ds.send(packet);
			os.close();

		} catch (UnknownHostException e) {
			System.err.println("Exception:  " + e);
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// the update function

	public static int updateNN(Map<Integer, Double> B, int j, double d) {
		double maxValueInMap = (Collections.max(B.values()));

		// check if the distance D is larger than all the neighbors of B then exist
		if (d >= maxValueInMap) {
			return 0;
		}
		// check if the B contains the the element key j , if it's smaller than it
		// replace it
		else if (B.containsKey(j)) {
			if (B.get(j) <= d) {
				return 0;
			}
			B.put(j, d);

			return 1;
		} else {
			int Element = 0;
			for (Entry<Integer, Double> entry : B.entrySet()) {
				if (entry.getValue() == maxValueInMap) {
					Element = entry.getKey();
				}

			}

			B.remove(Element);
			B.put(j, d);

			return 1;
		}
	}

	// the euclidean distance function
	public static double Euclidean(ArrayList<Double> x, ArrayList<Double> y) {

		double ds = 0.0;
		for (int n = 0; n < x.size(); n++)
			ds += Math.pow(x.get(n) - y.get(n), 2.0);

		ds = Math.sqrt(ds);
		DecimalFormat df = new DecimalFormat("0.00");
		return round(ds, 2);
	}

	public static double round(double value, int places) {
		if (places < 0)
			throw new IllegalArgumentException();

		long factor = (long) Math.pow(10, places);
		value = value * factor;
		long tmp = Math.round(value);
		return (double) tmp / factor;
	}

}
