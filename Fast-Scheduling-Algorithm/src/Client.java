import java.net.*;
import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


import java.io.*;

public class Client {

	// Socket and streams
	private static Socket soc = null;
	private BufferedReader input = null;
	private DataOutputStream outStream = null;
	private BufferedReader inStream = null;

	// Basic commands used in this client
	private final String HELO = "HELO";
	private final String AUTH = "AUTH";
	private final String REDY = "REDY";
	private final String JCPL = "JCPL";
	private final String NONE = "NONE";
	private final String QUIT = "QUIT";
	private final String JOBN = "JOBN";
	private final String OK   = "OK";
	private final String SCHD = "SCHD";

	// Socket is connected or not
	boolean connected = false;

	// constructor to put ip address and port
	public Client(String address, int port) throws IOException {

		// connect to the server
		connect(address, port);

		// takes input from keyboard
		input = new BufferedReader(new InputStreamReader(System.in));

		// sends output to the server
		outStream = new DataOutputStream(soc.getOutputStream());

		// gets input from server
		inStream = new BufferedReader(new InputStreamReader(soc.getInputStream()));
	}

	public static void main(String args[]) throws IOException {
		// Specify Server IP address and Port
		Client client = new Client("127.0.0.1", 50000);
		client.run();
	}

	private static void connect(String address, int port) {
		// try connect to server
			try {
				//System.out.println("Connecting to server: " + address + ":" + port);
				soc = new Socket(address, port);
				//System.out.println("Connected.");
			} catch (IOException e) {
				//System.out.println(e.getMessage());
				//System.out.println("Failed to conect to server");
			}
	}

	// Start the handshake for server
	void handshake() {
		sendMsg(HELO);
		readMsg();
		
		// Send user details
		sendMsg(AUTH + " " + System.getProperty("user.name"));
		readMsg();
		
		// done handshake
		connected = true;
	}

	private void run () {

		handshake();

		ArrayList<Server> servers = new ArrayList<Server>();
		ArrayList<Job> jobs = new ArrayList<Job>();

		// ready to recieve commands
		sendMsg(REDY);

		// server message information
		String msg = readMsg();
		
		while (connected){
			// Job completed we tell ds-server we are ready
			if (msg.contains(JCPL)){
				sendMsg(REDY);
				msg = readMsg(); 
				// there are no more jobs left
			} else if (msg.contains(NONE)){
				connected = false;
				sendMsg(QUIT);
			}else {

				// Get next message
				if (msg.contains(OK)){ 
					sendMsg(REDY);
					msg = readMsg();
				}

				// JOB FROM SERVER
				if (msg.contains(JOBN)){
					jobs.add(newJob(msg)); // create job 

					// the job arrayList will only ever have 1 item in it at a time...
					sendMsg(getsCapable(jobs.get(0)));
					msg = readMsg();

					sendMsg(OK);

					// list of capable servers are added to arrayList of server objects
					msg = readMsg();
					servers = serverCreator(msg);
					sendMsg(OK);
					msg = readMsg();

					sendMsg(fast(servers, jobs)); // FAST ALGORITHM SCHEDULING
					msg = readMsg();

					// only need one job at a time
					jobs.remove(0);
				} 
			} 
		}

		
		// close the connection
		try {

			// QUIT hand-shake, must receive confirmation from server for quit
			if (readMsg().contains(QUIT)){
				input.close();
				outStream.close();
				soc.close();
			}
				
		} catch (IOException i) {
			//System.out.println(i);
		}

		// Exit the program
		System.exit(1);
	}

	public String fast(ArrayList<Server> servers, ArrayList<Job> job){

		// Server information string
		String info = "";

		int reqMultiplier = 2;

		for (Server s: servers) {

			// find best fit for job
			if (s.getDisk() >= job.get(0).getDiskReq()/reqMultiplier &&
			s.getCores() >= job.get(0).getCoreReq()/reqMultiplier &&
				s.getMemory() >= job.get(0).getMemoryReq()/reqMultiplier) {
					info = s.getType() + " " + s.getID();
					return SCHD + " " + job.get(0).getID() + " " + info;
				}
				else {
					// Send job to first server
					info = servers.get(0).getType() + " " + servers.get(0).getID();
				}
		}
		// There is only one job in queue so schedule it
		return SCHD + " " + job.get(0).getID() + " " + info;
	}

	// takes server input and creates arrayList of CAPABLE SERVER OBJECTS
	public ArrayList<Server> serverCreator(String server){

		// trim spaces
		server = server.trim();

		// split strings by newline
		String[] lines = server.split("\\r?\\n");

		ArrayList<Server> newList = new ArrayList<Server>();
 		
		for (String line : lines) {

			// split each line by white space
			String[] splitStr = line.split("\\s+");

			//Construct new server
			Server s = new Server(splitStr[0], Integer.parseInt(splitStr[1]), splitStr[2], Integer.parseInt(splitStr[3]), Integer.parseInt(splitStr[4]), Integer.parseInt(splitStr[5]), Integer.parseInt(splitStr[6]), Integer.parseInt(splitStr[7]), Integer.parseInt(splitStr[8]) );
			newList.add(s);
        }

		return newList;
	}

	
	// create a new job object
	public Job newJob(String job){

		// trim spaces
		job = job.trim();

		// split string up by white space
		String[] splitStr = job.split("\\s+");

		Job j = new Job(Integer.parseInt(splitStr[1]), Integer.parseInt(splitStr[2]), Integer.parseInt(splitStr[3]),  Integer.parseInt(splitStr[4]) ,Integer.parseInt(splitStr[5]), Integer.parseInt(splitStr[6]));

		// returns job object
		return j;
	}

	// Send new message to server
	private void sendMsg (String outStr) {
		byte[] byteMsg = outStr.getBytes();
		try {
			outStream.write(byteMsg);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// System.out.println("SEND: " + outStr);		
	}

	// Recieve new message from server
	private String readMsg () {
		String inStr = "";
		char[] cbuf = new char[Short.MAX_VALUE];

		try {
			inStream.read(cbuf);
		} catch (IOException e) {
			e.printStackTrace();
		}

		inStr = new String(cbuf, 0, cbuf.length);

		// System.out.println("RECI: " + inStr);
		return inStr;
	}

	// Find available servers
	public String getsCapable(Job j){
		return("GETS Capable " + j.getCoreReq() + " " + j.getMemoryReq() + " " + j.getDiskReq());
	}

	// read XML from server
	public static ArrayList<Server> readXML(String fileName){
        ArrayList<Server> serverList = new ArrayList<Server>();
		
		try {
			File systemXML = new File(fileName);

			// Setup XML document parser
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(systemXML);

			// String converting to normalized form
			doc.getDocumentElement().normalize();
			NodeList servers = doc.getElementsByTagName("server");
			for (int i = 0; i < servers.getLength(); i++) {
				Element server = (Element) servers.item(i);

				String type = server.getAttribute("type");
				int limit = Integer.parseInt(server.getAttribute("limit"));
				int bootupTime = Integer.parseInt(server.getAttribute("bootupTime"));
				float hourlyRate = Float.parseFloat(server.getAttribute("hourlyRate"));
				int coreCount = Integer.parseInt(server.getAttribute("coreCount"));
				int memory = Integer.parseInt(server.getAttribute("memory"));
				int disk = Integer.parseInt(server.getAttribute("disk"));
				
				Server s = new Server(type,limit,bootupTime,hourlyRate,coreCount,memory,disk);
				serverList.add(s);
			}

			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return serverList;
    }
	
}