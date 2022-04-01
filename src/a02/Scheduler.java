package a02;

import net.datastructures.ArrayList;
import net.datastructures.HeapPriorityQueue;

public class Scheduler {
    static ArrayList<ArrayList<String>> jobList = new ArrayList<>();
    static HeapPriorityQueue<Integer, Job> jobQueue = new HeapPriorityQueue<>();
    static int maxWaitingTime;

    public static void launcher() {
        jobList = FileIO.parseJobs();
        queueJob();
        runCPU();
    }

    public static void runCPU() {
        Job curJob = jobQueue.removeMin().getValue();
        --curJob.length;

    }

    public static void incrementTime() {
        HeapPriorityQueue<Integer, Job> queueIncrememented = new HeapPriorityQueue<>();
        Job curJob;
        while (jobQueue.size() > 0) {
            curJob = jobQueue.removeMin().getValue();
            ++curJob.timeWaiting;
            if (curJob.timeWaiting > maxWaitingTime) {
                --curJob.priority;
            }
            queueIncrememented.insert(curJob.priority, curJob);
        }
    }

    public static void queueJob() {
        ArrayList<String> curJobData = jobList.get(0);
        if (curJobData.size() == 1) {
            return;
        } else {
            Job curJob = new Job(curJobData.get(0), Integer.parseInt(curJobData.get(1)),
                    Integer.parseInt(curJobData.get(2)));
            jobQueue.insert(curJob.priority, curJob);
            jobList.remove(0);
        }
    }
}
