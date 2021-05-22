public class Server {
	private String type; 
	private String state;
	private int limit; 
	private int bootTime;
	private float hourlyRate; 
	private int memory; 
	private int disk;
	private int startTime;
	private int runTime;
	private int waitTime;
	private int coreCount; 
	private int id;


		// We need 2 constructors for stage 1 and stage 2

		public Server(String type, int id, String state, int bootTime, int coreCount, int memory, int disk, int waitTime, int runTime ){
			this.type = type;
			this.bootTime = bootTime;
			this.coreCount = coreCount;
			this.memory = memory;
			this.disk = disk;
			this.type = type;
			this.id = id;
			this.state = state;
			this.waitTime = waitTime;
			this.runTime = runTime;
		}

		public Server(String type, int limit, int bootTime, float hourlyRate, int coreCount, int memory, int disk){
			this.type = type;
			this.limit = limit;
			this.bootTime = bootTime;
			this.hourlyRate = hourlyRate;
			this.coreCount = coreCount;
			this.memory = memory;
			this.disk = disk;
		}
		
		// returns a String for server state/status
		public String getState(){
			return this.state;
		}
		
		// type: an identifier of job category based on run time
		public String getType() {
			return this.type;
		}

		// returns int for server ID
		public int getID(){
			return this.id;
		}
		
		// limit: the number of servers of a particular type
		public int getLimit() {
			return this.limit; // the number of servers of a particular type
		}
		
		// bootupTime: the amount of time taken to boot a server of a particular type
		public int getBootupTime() {
			return this.bootTime;
		}
		
		// hourlyRate: the monetary cost for renting a server of a particular type per
		public Float getHourlyRate() {
			return this.hourlyRate;
		}

		// returns an int for the start time
		public int getStartTime(){
			return this.startTime;
		}

		// returns an int for wait time for server
		public int getWaitTime(){
			return this.waitTime;
		}

		// returns an int for run time for server
		public int getRunTime(){
			return this.runTime;
		}

		// core: the number of CPU cores
		public int getCores() {
			return this.coreCount;
		}

		// memory: the amount of RAM (in MB)
		public int getMemory() {
			return this.memory;
		}

		// disk: the amount of disk space (in MB)
		public int getDisk() {
			return this.disk;
		}
}
