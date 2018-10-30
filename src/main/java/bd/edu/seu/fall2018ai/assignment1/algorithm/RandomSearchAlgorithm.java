package bd.edu.seu.fall2018ai.assignment1.algorithm;

import bd.edu.seu.fall2018ai.assignment1.model.Action;
import bd.edu.seu.fall2018ai.assignment1.model.Destination2D;
import bd.edu.seu.fall2018ai.assignment1.model.Robot2D;
import bd.edu.seu.fall2018ai.assignment1.model.Shape2D;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomSearchAlgorithm extends SearchAlgorithm {
    @Override
    public List<Action> search(Robot2D source,
                               Destination2D destination,
                               List<Shape2D> obstacleList,
                               double mapWidth, double mapHeight) {
        List<Action> actionList = new ArrayList<>();

        final int MAX_ACTIONS = 100000;
        Random random = new Random();

        int direction=0;

        for (int i = 0; i < 1000; i++) {
            Robot2D currentState = new Robot2D(source.getCenter(), source.getRadius(), source.getOrientation());
            //Action randomAction = Action.values()[random.nextInt(Action.values().length)];
            Action randomAction = Action.values()[direction];
            if(direction==1) direction=0;
            else direction=1;
            System.out.println(source.getOrientation()+ " Center "+source.getCenter());
            source.applyAction(randomAction);

            boolean collides = false;

            if (source.getCenter().getSquaredDistance(destination.getCenter()) < destination.getRadius() * destination.getRadius())
                break;

//            if (source.getCenter().getX() - source.getRadius() < 0 ||
//                    source.getCenter().getY() - source.getRadius() < 0 ||
//                    source.getCenter().getX() + source.getRadius() >= mapWidth ||
//                    source.getCenter().getY() + source.getRadius() >= mapHeight)
//                collides = true;
//
//            if (!collides)
//                for (Shape2D shape2D : obstacleList) {
//                    if (shape2D.collidesWith(source)) {
//                        collides = true;
//                        break;
//                    }
//                }

            if (collides) {
                source = currentState;
            } else {
                actionList.add(randomAction);
            }
        }

        return actionList;
    }
}
