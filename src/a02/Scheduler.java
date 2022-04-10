package a02;

import net.datastructures.ArrayList;
import net.datastructures.HeapPriorityQueue;

public class Scheduler {
    static ArrayList<ArrayList<String>> jobList = new ArrayList<>();
    static HeapPriorityQueue<Integer, Job> jobQueue = new HeapPriorityQueue<>();
    static int maxWaitingTime;
    static final String jobFile = "jobs.txt"; //change this to use a different file.

    /**
     * sends commands to parse the text file, set maxWaitingTime, 
     * get a job queued, and finally run the cpu
     * 
     * @param waitingTime max time a job is waiting for priority increases. 
     */
    public static void schedulerLauncher(int waitingTime) {
        maxWaitingTime = waitingTime;
        jobList = FileIO.parseJobs(jobFile);
        queueJob();
        runCPU();
    }//schedulerLauncher method

    /**
     * launches various methods to simulate a cpu scheduler. 
     */
    public static void runCPU() {
        Job curJob = jobQueue.removeMin().getValue(); // pulls the highest priority job from the queue
        int curJobLength = curJob.processJob(); //processes one time slice on current job
        System.out.printf("%s (priority: %d, time remaining: %d)\n", curJob.name, curJob.priority, curJobLength);
        if (curJobLength != 0) {
            jobQueue.insert(curJob.priority, curJob); //if the job is unfinished, put it back in the queue
        }
        incrementTime(curJob); //increment time waited for other jobs. 
        queueJob(); //pull another line from jobs.txt and put it in the queue
        if (jobQueue.isEmpty()) {
            System.out.println("Done!"); //if we're done we're done :D
        } else {
            runCPU(); //or run it again!
        }
    }//runCPU method

    /**
     * walks through the job queue and increments time waited for each job not currently working
     * clears working status from the workingJob to prepare for next time slice
     * 
     * @param workingJob currently worked job
     */
    public static void incrementTime(Job workingJob) {
        HeapPriorityQueue<Integer, Job> queueIncrememented = new HeapPriorityQueue<>();
        Job curJob;
        while (jobQueue.size() > 0) {
            curJob = jobQueue.removeMin().getValue();
            curJob.incrementTimeWaiting();
            queueIncrememented.insert(curJob.priority, curJob);
        }
        jobQueue = queueIncrememented;
        workingJob.working = false;
    }//incrementTime method

    /**
     * pulls in a new array of job data and checks validity of data. 
     * recognizes no new job slice and returns without performing further actions
     * if valid data, creates new job and enters it into the queue.
     */
    public static void queueJob() {
        if (!jobList.isEmpty()) {
            ArrayList<String> curJobData = jobList.get(0); //pull the next entry from the jobs.txt depository
            if (curJobData.size() == 1) {
                jobList.remove(0); //remove the skip marker
                return; //if it's a "no new job slice" the length of the array containing job information will be one so we just return.
            } else {
                String name = curJobData.get(0); //name is stored at [0] in job info array
                int length = Integer.parseInt(curJobData.get(1)); //length is stord at [1] in job info array
                int priority = Integer.parseInt(curJobData.get(2)); //priority is stored at [2] in job info array
                if (priority > -21 && priority < 20 && length > 0 && length < 101) { //make sure entries outside min/max length and priority are filtered out
                    Job curJob = new Job(name, length, priority, maxWaitingTime);
                    jobQueue.insert(curJob.priority, curJob);
                    jobList.remove(0);
                } else {
                    jobList.remove(0);
                }
            }
        }
    }//queueJob method

    /**
     * debug method for testing file parsing logic. 
     */
    public static void testFIleParsing() {
        jobList = FileIO.parseJobs(jobFile);
        for (ArrayList<String> stringList : jobList) {
            for (String s : stringList) {
                System.out.printf("%s ", s);
            }
            System.out.println();
        }
    }//testFileParsing method
}//Scheduler Class