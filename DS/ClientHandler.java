package DS;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientHandler extends Thread {

	final ObjectInputStream ois;
	final ObjectOutputStream oos;
	final Socket s;

	// Constructor
	public ClientHandler(Socket s, ObjectInputStream dis, ObjectOutputStream dos) {
		this.s = s;
		this.ois = dis;
		this.oos = dos;
	}

	public void run() {

		try {
			f();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	synchronized public void f() throws InterruptedException {

		String ss = "";
		int iii = 0;
		while (true) {

			try {

				driver.incrementstart();

				// waiting the machine to finish a task
				ss = (String) ois.readObject();

				// Waiting the main server to send a signal to start the next step
				while (driver.you_can_start != 1) {

				}

				driver.incrementstep1();

				while (driver.start_step2 == 0) {
				}

				oos.writeObject("Go , start calculating the bUr");
				ss = (String) ois.readObject();

				driver.incrementstep2();
				//System.out.println("step2 " + iii + " in handler");
				while (driver.start_step3 == 0) {
				}

				oos.writeObject("Go ON , Start the local calculations");
				ss = (String) ois.readObject();

				// received the C counted by the machine
				int c_temp = Integer.parseInt(ss);

				// Sum that C with the global C
				driver.incrementc(c_temp);
				driver.incrementiter();

				//System.out.println("step3 " + iii + " in handler");

				while (driver.next_iter == 0) {
					// Waiting the server to send the next iteration decision
				}

				if (driver.next_iter == 1) {

					// tell the machine to start the next iteration
					oos.writeObject("continue");

				} else if (driver.next_iter == 2) {
					// tell the machine to stop calculations

					oos.writeObject("dont");
					oos.close();
					s.close();
					break;
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

}
