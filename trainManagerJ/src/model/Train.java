package model;

public class Train {
    private int id;
    private String trainName;
    private int capacity;
    private int delayMinutes;

    public Train(int id, String trainName, int capacity) {
        this.id = id;
        this.trainName = trainName;
        this.capacity = capacity;
        this.delayMinutes = 0;
    }

    public int getId() { return id; }

    public String getTrainName() { return trainName; }
    public void setTrainName(String trainName) { this.trainName = trainName; }

    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }

    public int getDelayMinutes() { return delayMinutes; }
    public void setDelayMinutes(int delayMinutes) { this.delayMinutes = delayMinutes; }

    @Override
    public String toString() {
        String delayText = delayMinutes > 0 ? " | delayed by " + delayMinutes + " min" : "";
        return "Train " + id + " - " + trainName + " | seats " + capacity + delayText;
    }
}
