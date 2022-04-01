package a02;
public class Job {
    String name;
    int length;
    int priority;
    int timeWaiting;
    

    public Job(String name, int length, int priority) {
        this.name = name;
        this.length = length;
        this.priority = priority;
        timeWaiting = 0;
    }
}
