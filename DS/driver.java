package DS;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class driver {
	volatile static int ii = 0;
	volatile static int starting = 0;
	volatile static int step1 = 0;
	volatile static int start_step2 = 0;
	volatile static int step2 = 0;
	volatile static int start_step3 = 0;
	volatile static int iter_done = 0;
	volatile static int next_iter = 0;
	volatile static int you_can_start = 0;
	volatile static int c = 10;
	static int k_neighbors = 4;
	volatile static int machines_num = 0;
	volatile static int c_counter = 10;

	public static void main(String[] args) throws ClassNotFoundException, IOException, InterruptedException {

		// Scan the number K of neighbours
		System.out.println("Enter the number of neighbours :");
		Scanner sc = new Scanner(System.in);
		driver.k_neighbors = sc.nextInt();

		// Initializing the machines arraylist
		ArrayList<machine> machines = new ArrayList<machine>();

		// Calling the readfile function to create nodes from the csv file with a unique
		// port
		ArrayList<node> all_nodes = readFile.read_file();

		// Create separate machines from nodes
		for (int i = 0; i < all_nodes.size(); i++) {
			machine m1 = new machine(all_nodes.get(i));
			machines.add(m1);
		}

		machines_num = machines.size();
		// System.out.println("The number of nodes is "+machines.size());

		ServerSocket srv = null;
		srv = new ServerSocket(2611);
		int j = 0;
		while (j < machines.size()) {

			Socket s;
			String serverHost = "localhost";

			try {

				Socket connexion = null;
				// Waiting for connections
				connexion = srv.accept();

				ObjectOutputStream oos = oos = new ObjectOutputStream(connexion.getOutputStream());
				ObjectInputStream ois = ois = new ObjectInputStream(connexion.getInputStream());

				// creating a unique thread for each connection
				Thread t = new ClientHandler(connexion, ois, oos);

				t.start();

				// oos.close();
				// connexion.close();

			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			j = j + 1;
		}

		System.out.println("All machines are created");

		while (driver.c >= driver.c_counter) {
			driver.you_can_start = 0;

			// Waiting all the machines to send the start signal
			while (driver.starting < machines.size()) {

			}

			driver.starting = 0;

			driver.ii = 0;
			driver.step1 = 0;
			driver.start_step2 = 0;
			driver.step2 = 0;
			driver.start_step3 = 0;
			driver.iter_done = 0;
			driver.next_iter = 0;
			driver.c = 0;
			driver.you_can_start = 1;

			// Waiting all machines to finish the first step
			while (driver.step1 < machines.size()) {

			}

			driver.start_step2 = 1;

			// Printing the actual B vector for all nodes
			for (int i = 0; i < machines.size(); i++) {
				System.out.println("The B of the  " + machines.get(i).data.port + " is:   " + machines.get(i).data.b);
			}

			// Printing the actual Reverse vector for all nodes
			for (int i = 0; i < machines.size(); i++) {
				System.out.println(
						"The reverse of the  " + machines.get(i).data.port + " is:   " + machines.get(i).data.r);
			}

			// Waiting the machines to finish the step2
			while (driver.step2 < machines.size()) {

			}

			// Printing the actual BUR for all nodes
			for (int i = 0; i < machines.size(); i++) {
				System.out
						.println("The BUR of the  " + machines.get(i).data.port + " is:   " + machines.get(i).data.bUr);
			}

			driver.start_step3 = 1;

			// Waiting for machines to finish there iterations

			while (driver.iter_done < machines.size()) {
			}

			// print the total of received C
			System.out.println("All machines finished their iterations the C total is: " + driver.c);

			if (driver.c >= driver.c_counter) {
				driver.next_iter = 1;

			} else {
				// send a signal to stop the iterations
				driver.next_iter = 2;
			}

		}

		// Finishing the while loop

		// Printing the final B for all nodes
		for (int i = 0; i < machines.size(); i++) {
			System.out.println("The Final B of the  " + machines.get(i).data.port + " is:   " + machines.get(i).data.b);
		}

	}

	static HashMap<Integer, Double> extract_sample(ArrayList<Integer> v, int k, int h) {

		ArrayList<Integer> ran_index = new ArrayList<Integer>();
		int i = 0;
		while (i < k) {
			int r = randomNumberInRange(0, v.size() - 1);
			if (!ran_index.contains(v.get(r)) && v.get(r) != h) {
				// System.out.println(v.get(r));
				ran_index.add(v.get(r));

				i = i + 1;
			}
		}
		;
		HashMap<Integer, Double> p = new HashMap<Integer, Double>();
		for (int j = 0; j < k; j++) {
			p.put(ran_index.get(j), (double) 999);
		}

		return p;

	}

	public static int randomNumberInRange(int min, int max) {
		Random random = new Random();
		return random.nextInt((max - min) + 1) + min;
	}

	// some synchronized functions used to updates the static variables from threads

	public static synchronized void incrementstep2() {
		driver.step2 = driver.step2 + 1;

	}

	public static synchronized void incrementstep1() {
		driver.step1 = driver.step1 + 1;

	}

	public static synchronized void incrementc(int c) {
		driver.c = driver.c + c;

	}

	public static synchronized void incrementiter() {
		driver.iter_done = driver.iter_done + 1;

	}

	public static synchronized void incrementstart() {
		driver.starting = driver.starting + 1;

	}

	public static synchronized void incrementstartt() {
		driver.you_can_start = driver.you_can_start + 1;

	}

	public static synchronized void decrementstartt() {
		driver.you_can_start = driver.you_can_start - 1;

	}

}
