package DS;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class node implements Serializable {

	// the port of the node
	int port;

	ArrayList<Double> profile;
	HashMap<Integer, Double> b = new HashMap<Integer, Double>();
	ArrayList<Integer> r = new ArrayList<Integer>();
	ArrayList<Integer> bUr = new ArrayList<Integer>();

	node(int p, ArrayList<Double> pro, HashMap<Integer, Double> bb) {
		this.port = p;
		this.profile = pro;
		this.b = bb;
	}

	node(int p, ArrayList<Double> pro) {
		this.port = p;
		this.profile = pro;
	}

}
