package io.github.arnabmaji19.coinman;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class MovingObjectMaker {

    private static final Random random = new Random();
    private final int CREATION_DELAY;
    private List<MovingObject> movingObjects;
    private List<MovingObject> objectsToBeRemoved;

    private int count;
    private int screenHeight;
    private int screenWidth;

    public MovingObjectMaker(int CREATION_DELAY, int screenHeight, int screenWidth) {
        this.CREATION_DELAY = CREATION_DELAY;
        this.count = 0;
        this.screenHeight = screenHeight;
        this.screenWidth = screenWidth;
        this.movingObjects = new ArrayList<>();
        this.objectsToBeRemoved = new ArrayList<>();
    }

    public static Random getRandom() {
        return random;
    }

    public List<MovingObject> getMovingObjects() {
        return movingObjects;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public void createObjectInInterval(){
        // create object in a delay
        if (this.count < CREATION_DELAY) count++;
        else {
            createObject(); // create new object in each interval
            count = 0; // reset interval count
        }
    }

    public void removeLaterIfNecessary(MovingObject object){
        if (object.getXPosition() < (- object.getObjectWidth())) objectsToBeRemoved.add(object); // mark objects to be removed
    }

    public void removeUnnecessaryObjects(){
        if (objectsToBeRemoved.size() == 0) return;
        for (MovingObject object : objectsToBeRemoved) movingObjects.remove(object);
        objectsToBeRemoved.clear(); // clear the list
    }

    protected abstract void createObject();
}
