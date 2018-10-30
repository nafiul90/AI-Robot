package bd.edu.seu.fall2018ai.assignment1.algorithm;

import bd.edu.seu.fall2018ai.assignment1.model.*;
import bd.edu.seu.fall2018ai.assignment1.model.Action;
import javafx.scene.shape.Rectangle;
import javafx.util.Pair;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import static java.lang.Math.abs;
import static java.lang.Math.sqrt;

/**
 * // TODO make sure you type in your name and ID in the following line. Feel free to rename this class if you want to.
 * @author Nafiul Islam (2016000000090)
 */
public class MySearchAlgorithmImpl extends SearchAlgorithm {

    @Override
    public List<Action> search(Robot2D source,
                               Destination2D destination,
                               List<Shape2D> obstacleList,
                               double mapWidth, double mapHeight) {

        List<Pair<Double,Double>> path=aStar(source,destination,obstacleList, mapWidth,  mapHeight);
        List<Action> actionList = new ArrayList<>();
        for(int i=path.size()-1;i>=0;i--) {
            double dis = sqrt(Math.pow(source.getCenter().getX() - destination.getCenter().getX(), 2) + Math.pow(source.getCenter().getY() - destination.getCenter().getY(), 2));
            if(dis <= source.getRadius()+destination.getRadius()){
                break;
            }
            double x=path.get(i).getKey();
            double y=path.get(i).getValue();
            if (y < source.getCenter().getY()) {
                //System.out.println("y is smaller");
                Action randomAction = Action.values()[2];
                source.applyAction(randomAction);
                actionList.add(randomAction);
            }
            if (x > source.getCenter().getX()) {
                //System.out.println("x is greater");
                Action randomAction = Action.values()[0];
                source.applyAction(randomAction);
                actionList.add(randomAction);
            }
            if (y > source.getCenter().getY()) {
                //System.out.println("y is greater");
                Action randomAction = Action.values()[3];
                source.applyAction(randomAction);
                actionList.add(randomAction);
            }
            if (x < source.getCenter().getX()) {
                //System.out.println("x is smaller");
                Action randomAction = Action.values()[1];
                source.applyAction(randomAction);
                actionList.add(randomAction);

            }
        }
        return actionList;
    }

    public List<Pair<Double,Double>> aStar(Robot2D source,
                                            Destination2D destination,
                                            List<Shape2D> obstacleList,
                                            double mapWidth, double mapHeight){
        HashMap< Pair<Double,Double> ,Pair<Double,Double> > cameFrom= new HashMap<Pair<Double,Double> ,Pair<Double,Double>>();
        HashMap< Pair<Double,Double> ,Double > gScore= new HashMap<Pair<Double,Double> ,Double>();
        HashMap< Pair<Double,Double> ,Double > fScore= new HashMap<Pair<Double,Double> ,Double>();
        final int MAX_ACTIONS =20000;
        int count=0;
        List<Pair<Double,Double>> closedList = new ArrayList<>();
        List<Pair<Double,Double>> openList = findChildren(source,obstacleList,mapHeight,mapWidth);
        System.out.println(openList);
        double dis = sqrt(Math.pow(source.getCenter().getX()-destination.getCenter().getX(), 2) + Math.pow(source.getCenter().getY()-destination.getCenter().getY(), 2));
        gScore.put(new Pair<Double,Double>(source.getCenter().getX(),source.getCenter().getY()),0.0);
        fScore.put(new Pair<Double,Double>(source.getCenter().getX(),source.getCenter().getY()),dis);
        closedList.add(new Pair<>(source.getCenter().getX(), source.getCenter().getY()));
        while (!openList.isEmpty()) {
            dis = sqrt(Math.pow(source.getCenter().getX() - destination.getCenter().getX(), 2) + Math.pow(source.getCenter().getY() - destination.getCenter().getY(), 2));

            count++;
//

            double minHurestic = 999999999;
            double x = 0;
            double y = 0;


            for (Pair<Double, Double> pair : openList) {

                double hurestic = sqrt(Math.pow(destination.getCenter().getX() - pair.getKey(), 2) + Math.pow(destination.getCenter().getY() - pair.getValue(), 2));
                if (hurestic < minHurestic) {
                    minHurestic = hurestic;
                    x = pair.getKey();
                    y = pair.getValue();
                }
            }
            Pair<Double, Double> current = new Pair<>(x, y);
            System.out.println(current);
//            if(count<MAX_ACTIONS){
//                return reconstractPath(cameFrom, current);
//            }
            if (current.getKey() == destination.getCenter().getX() && current.getValue() == destination.getCenter().getY()) {
                return reconstractPath(cameFrom, current);
            }
            openList.remove(current);
            closedList.add(current);
            System.out.println(current);

            List<Pair<Double, Double>> neibour = findChildren(new Robot2D(new Point2D(current.getKey(), current.getValue()), source.getRadius()), obstacleList, mapHeight, mapWidth);
            for (Pair<Double, Double> n : neibour) {
                System.out.println(n);
                if (closedList.contains(n)) continue;
                double tentitiveGScore=0;
                if(gScore.containsKey(current)) tentitiveGScore = gScore.get(current) + sqrt(Math.pow(current.getKey() - n.getKey(), 2) + Math.pow(current.getValue() - n.getValue(), 2));
                if (!openList.contains(n)) {
                    openList.add(n);
                } else if (gScore.containsKey(n) && tentitiveGScore >= gScore.get(n)) continue;

                cameFrom.put(n, current);
                gScore.put(n, tentitiveGScore);
                if(gScore.containsKey(n) && gScore.containsKey(current))
                fScore.put(n, gScore.get(n) + gScore.get(current) + sqrt(Math.pow(destination.getCenter().getX() - n.getKey(), 2) + Math.pow(destination.getCenter().getY() - n.getValue(), 2)));

            }


        }

        return null;

    }

    private List<Pair<Double,Double>> reconstractPath(HashMap<Pair<Double,Double>,Pair<Double,Double>> cameFrom, Pair<Double,Double> current) {
        List<Pair<Double,Double>> totalPath=new ArrayList<>();
        while(cameFrom.containsKey(current)){
            current=cameFrom.get(current);
            totalPath.add(current);
        }
        return totalPath;
    }

    List<Pair<Double,Double>> findChildren(Robot2D source,List<Shape2D> obstacleList,double mapHeight, double mapWidth){
        System.out.println("infunction 2");
        List<Pair<Double,Double>> openList=new ArrayList<>();

        double b=source.getCenter().getY()-1;
        for(int i=0;i<3;i++){
            boolean collids=false;
            double a = source.getCenter().getX()-1;
            for(int j=0;j<3;j++){
                Pair<Double,Double> pair=new Pair<>(a,b);
                if (a - source.getRadius() < 0 ||
                    b - source.getRadius() < 0 ||
                    a + source.getRadius() >= mapWidth ||
                    b + source.getRadius() >= mapHeight)
                collids = true;
                for (Shape2D shape2D : obstacleList) {

                    if (shape2D instanceof Circle2D) {
                        double dis = sqrt(Math.pow(a-((Circle2D) shape2D).getCenter().getX(), 2) + Math.pow(b-((Circle2D) shape2D).getCenter().getY(), 2));
                        if (dis > source.getRadius() + ((Circle2D) shape2D).getRadius()) {
                        }else{
                            collids=true;
                        }
                    }
                    if (shape2D instanceof Rectangle2D) {
                        Circle2D circle2D=new Circle2D(new Point2D(a,b),source.getRadius());
                        if(shape2D.collidesWith(circle2D)){
                            collids=true;
                        }
                    }
                }

                if(!collids){
                    if(b!=source.getCenter().getY() || a!=source.getCenter().getX())
                    openList.add(pair);
                }else{
                    collids=false;
                }
                a++;
            }
            b++;
        }

        return openList;
    }
}
