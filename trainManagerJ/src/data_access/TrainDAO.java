package data_access;

import model.Train;
import java.util.ArrayList;

public class TrainDAO {
    private ArrayList<Train> trains;

    public TrainDAO() {
        trains = new ArrayList<>();
    }

    public void addTrain(Train train) {
        trains.add(train);
    }

    public void removeTrain(int id) {
        Train train = findById(id);

        if(train != null) {
            trains.remove(train);
        }
    }

    public Train findById(int id) {
        for(Train train : trains) {
            if(train.getId() == id) {
                return train;
            }
        }

        return null;
    }

    public ArrayList<Train> findAll() {
        return trains;
    }
}
