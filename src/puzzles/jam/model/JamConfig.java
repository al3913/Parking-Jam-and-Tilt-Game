package puzzles.jam.model;

// TODO: implement your JamConfig for the common solver

import puzzles.common.solver.Configuration;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class JamConfig implements Configuration {

    public int height; //max rows
    public int width; //max cols
    public int numCars;
    private ArrayList<Car> cars = new ArrayList<Car>();

    public JamConfig(String file) throws IOException {
        try(BufferedReader read = new BufferedReader(new FileReader(file)))
        {
            String[] field = read.readLine().split("\\s+");
            height = Integer.parseInt(field[0]);
            width = Integer.parseInt(field[1]);
            String line = read.readLine();
            numCars = Integer.parseInt(read.readLine());
            while((line = read.readLine()) != null)
            {
                String[] data = line.split("\\s+");
                Car current = new Car(Integer.parseInt(data[1]), Integer.parseInt(data[2]), Integer.parseInt(data[3]), Integer.parseInt(data[4]), data[0]);
                cars.add(current);
            }
        }
    }

    private JamConfig(int startR, int endR, int startC, int endC, String letter, int index)
    {
        Car newC = new Car(startR,startC,endR,endC, letter);
        cars.remove(index);
        cars.add(index,newC);
    }

    @Override
    public boolean isSolution() {
        return false;
    }

    @Override
    public Collection<Configuration> getNeighbors() {
        Collection<Configuration> neighbors = new ArrayList<>();
        for(int i = 0; i < cars.size(); i++)
        {
            Car car = cars.get(i);
            String direct = car.getDirection();
            int startR = car.getStartRow();
            int endR = car.getEndRow();
            int startC = car.getStartCol();
            int endC = car.getEndCol();
            if(direct.equals("H"))
            {
                if(!(endC + 1 > width) && collision(endC + 1, endR, direct))
                {
                    neighbors.add(new JamConfig(startR,endR,startC+1,endC+1, car.getLetter(), i));
                }
                if(!(startC - 1 < 0) && collision(startC - 1, startR, direct))
                {
                    neighbors.add(new JamConfig(startR,endR,startC-1,endC-1, car.getLetter(), i));

                }
            }
            else {
                if(!(endR + 1 > height) && collision(endC, endR + 1, direct))
                    neighbors.add(new JamConfig(startR+1,endR+1,startC,endC, car.getLetter(), i));

                if(!(startR - 1 < 0) && collision(startC, startR - 1, direct))
                    neighbors.add(new JamConfig(startR-1,endR-1,startC,endC, car.getLetter(), i));
            }
        }
        return neighbors;
    }

    @Override
    public boolean equals(Object other) {
        return false;
    }

    public boolean collision(int x, int y, String orient)
    {
        for(Car c: cars)
        {
            if (orient.equals("H"))
                if ((x >= c.getStartCol()) && (x <= c.getEndCol()) && (y >= c.getStartRow()) && (y <= c.getEndRow()))
                    return false;
            if (orient.equals("V"))
                if ((y >= c.getStartRow()) && (y <= c.getEndRow()) && (x >= c.getStartCol()) && (x <= c.getEndCol()))
                    return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return cars.hashCode();
    }

    @Override
    public String toString() {
        return null;
    }
}
