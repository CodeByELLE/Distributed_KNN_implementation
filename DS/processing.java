package DS;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class processing extends Thread {

	node data;

	processing(node p) {
		this.data = p;
	}

	synchronized public void f() throws InterruptedException, ClassNotFoundException, IOException {

		Socket sock = null;
		String serverHost = new String("localhost");
		System.out.println("Machine " + data.port + " created");

		// Connect the node to the server
		sock = new Socket(serverHost, 2611);
		ObjectInputStream ois = new ObjectInputStream(sock.getInputStream());
		ObjectOutputStream oos = new ObjectOutputStream(sock.getOutputStream());

		int nex_it = 1;
		String res;
		int iter_num = 0;
		while (nex_it == 1) {
			// System.out.println("Iteration "+iter_num+" : "+data.port);
			iter_num = iter_num + 1;

			// Tell the other nodes that i'm your neighbour
			data.r = new ArrayList<Integer>();
			for (int i : data.b.keySet()) {
				String str = "neignbor" + " " + data.port;
				// Call the function to connect nodes in UDP mode
				res = machine.connect_to_node(i, str);

			}

			// Tell the server that i'm done with my neighbours
			oos.writeObject(data.port + " i am done with my neighbours");

			try {

				String s = (String) ois.readObject();

				// Calculate the BUR
				data.bUr = generate_BuR(data.b, data.r);
				oos.writeObject(data.port + " I calculated my BUR");
				s = (String) ois.readObject();

				// connecting with the BUR of my BUR
				ArrayList<Integer> b_of_all1 = new ArrayList<Integer>();
				for (int i = 0; i < data.bUr.size(); i++) {

					String str = "B_Bar" + " " + data.port;
					// get the BUR
					b_of_all1.addAll(machine.connect_to_node_a(data.bUr.get(i), str));

				}

				Set<Integer> b_of_all2 = new HashSet<Integer>(b_of_all1);
				ArrayList<Integer> b_of_all = new ArrayList<Integer>(b_of_all2);
				ArrayList<Integer> my_port = new ArrayList<Integer>();
				my_port.add(data.port);
				b_of_all.removeAll(my_port);

				// comparing the actual node with all the other nodes
				ArrayList<Double> dest_profile = new ArrayList<Double>();

				int c = 0;
				for (int i = 0; i < b_of_all.size(); i++) {

					String str = "profile" + " " + data.port;
					// Received the profile of the CS element
					dest_profile = machine.connect_to_node_p(b_of_all.get(i), str);

					// call the update function using the Euclidean similarity

					c = c + machine.updateNN(data.b, b_of_all.get(i), machine.Euclidean(data.profile, dest_profile));

				}

				// Print the C on that machine and send it to the server
				System.out.println(data.port + " I am done updates with " + c + " changes");
				oos.writeObject(c + "");
				s = (String) ois.readObject();

				if (s.equals("continue")) {
					// next iteration
					nex_it = 1;
				} else {
					// stop the loop
					nex_it = 0;
				}

			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// the function that generates that BUR
	ArrayList<Integer> generate_BuR(HashMap<Integer, Double> b, ArrayList<Integer> r) {

		ArrayList<Integer> u = new ArrayList<Integer>();

		for (Integer key : b.keySet()) {

			u.add(key);

		}

		for (int i = 0; i < r.size(); i++) {
			u.add(r.get(i));

		}

		Set<Integer> set = new HashSet<Integer>(u);
		ArrayList<Integer> u1 = new ArrayList<Integer>(set);
		return u1;
	}

}
