	public class Job{

		private int startTime;
		private int jobID;
		private int estRunTime;
		private int core;
		private int memory;
		private int disk;

		// Job constructor
		public Job(int startTime, int jobID, int estRunTime, int core, int memory, int disk){
			this.startTime = startTime;
			this.jobID = jobID;
			this.estRunTime = estRunTime;
			this.core = core;
			this.memory = memory;
			this.disk = disk;
		}

		// start time of job
		public int getStartTime(){
			return this.startTime;
		}
	
		// id of job
		public int getID(){
			return this.jobID;
		}
	
		// run time of job
		public int getRunTime(){
			return this.estRunTime;
		}
	
		// cores for this job
		public int getCoreReq(){
			return this.core;
		}
		
		// memory required
		public int getMemoryReq(){
			return this.memory;
		}
	
		// disk space required
		public int getDiskReq(){
			return this.disk;
		}
	}