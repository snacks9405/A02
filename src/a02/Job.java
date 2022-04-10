package a02;

/**
 * class defining what it means to be a Job and offers basic functions to manage priority, length, and time waiting
 */
public class Job {
    String name; 
    int length;
    int priority; 
    int timeWaiting; 
    int maxWaitingTime; 
    boolean working = false; 

    /**
     * constructor for a Job
     * 
     * @param name name of job
     * @param length length of job in time slices
     * @param priority priority of job
     * @param maxWaitingTime how long job has been waiting to be processed
     */
    public Job(String name, int length, int priority, int maxWaitingTime) {
        this.name = name;
        this.length = length;
        this.priority = priority;
        this.maxWaitingTime = maxWaitingTime;
        timeWaiting = 0; //initialize to zero on construction
    }//Job constructor

    /**
     * if the job isn't currently being worked, increments time waiting up to maxTimeWaiting,
     * also adjusts priority accordingly.
     */
    public void incrementTimeWaiting() {
        if (!working) { //check if the job is working
            ++timeWaiting; 
            if (timeWaiting > maxWaitingTime) { //if timeWaiting is greater than max, raise priority (up to highest priority -20)....
                if (priority > -20) {
                    --priority;
                }
                timeWaiting = 0; //and reset timeWaiting
            }
        }
    }//incrementTimeWaiting method

    /**
     * process one slice of remaining length of the job 
     * 
     * @return remaining length of time required to complete job
     */
    public int processJob() {
        working = true; //flag this job as currently being worked 
        return --length;
    }//processJob method
}//Job class