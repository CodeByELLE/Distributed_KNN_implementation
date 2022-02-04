package DS;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class readFile {
	public static ArrayList<node> read_file() throws FileNotFoundException {

		// read CSV file
		String fileName = "jester.csv";
		File file = new File(fileName);
		ArrayList<node> A = new ArrayList<node>();
		ArrayList<Integer> ports = new ArrayList<Integer>();
		ArrayList<ArrayList<Double>> profiles = new ArrayList<ArrayList<Double>>();

		int ii = 0;
		int c = 0;
		int iii = 0;

		int this_port;
		Scanner inputStream = new Scanner(file);
		// put each line of the dataset into a node object
		while (inputStream.hasNext()) {
			String data = inputStream.next();
			String[] spliteddata = data.split(",");
			double vect[] = new double[spliteddata.length];
			// System.out.print(spliteddata.length+" ");
			ArrayList<Double> temp = new ArrayList<Double>();
			for (int i = 0; i < spliteddata.length; i++)

			{
				// System.out.println(spliteddata[i]);
				temp.add(Double.parseDouble(spliteddata[i]));

			}
			int initial_port = 2612;
			this_port = initial_port + iii;
			ports.add(this_port);
			profiles.add(temp);
			iii = iii + 1;

		}

		// fill the A array with the node instances
		for (int i = 0; i < profiles.size(); i++) {

			node temp3 = new node(ports.get(i), profiles.get(i),
					driver.extract_sample(ports, driver.k_neighbors, ports.get(i)));
			A.add(temp3);
		}

		// System.out.println("Finish "+A.size());

		return A;
	}

	public static void main(String[] args) throws FileNotFoundException {

		System.out.println("Hello");
		ArrayList<node> p = read_file();
		System.out.println(p.size());

	}

}
