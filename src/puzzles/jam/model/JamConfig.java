package puzzles.jam.model;

// TODO: implement your JamConfig for the common solver

import puzzles.common.solver.Configuration;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class JamConfig implements Configuration {

    private static int height; //max rows
    private static int width; //max cols
    private int numCars;
    private ArrayList<Car> cars = new ArrayList<Car>();

    private char[][] grid;

    public JamConfig(String file) throws IOException {
        try(BufferedReader read = new BufferedReader(new FileReader(file)))
        {
            String[] field = read.readLine().split("\\s+");
            height = Integer.parseInt(field[0]);
            width = Integer.parseInt(field[1]);
            grid = new char[height][width];
            for (char[] r: grid)
            {
                Arrays.fill(r,'.');
            }
            numCars = Integer.parseInt(read.readLine());
            String line;
            while((line = read.readLine()) != null)
            {
                String[] data = line.split("\\s+");
                int startR = Integer.parseInt(data[1]);
                int startC = Integer.parseInt(data[2]);
                int endR = Integer.parseInt(data[3]);
                int endC = Integer.parseInt(data[4]);
                Car current = new Car(startR, startC, endR, endC, data[0]);
                cars.add(current);
                for (int r = startR; r <= endR; r++)
                {
                    for (int c = startC; c <= endC; c++)
                    {
                        grid[r][c] = data[0].charAt(0);
                    }
                }
            }
        }
    }

    public JamConfig(int startR, int endR, int startC, int endC, JamConfig other, int index, String move)
    {
        Car newC = new Car(startR,startC,endR,endC, other.cars.get(index).getLetter());
        this.cars.addAll(other.cars);
        if(cars.size()>0) cars.remove(index);
        cars.add(index,newC);

        this.grid = new char[height][width];
        for(int r=0;r<height;r++) {
            if (width >= 0) System.arraycopy(other.grid[r], 0, this.grid[r], 0, width);
        }
        if (move.equals("R"))
        {
            grid[endR][endC] = grid[startR][startC];
            grid[startR][startC-1] = '.';
        }
        if (move.equals("L"))
        {
            grid[startR][startC] = grid[endR][endC];
            grid[endR][endC+1] = '.';
        }
        if (move.equals("U"))
        {
            grid[endR][endC] = grid[startR][startC];
            grid[startR-1][startC] = '.';
        }
        if (move.equals("D"))
        {
            grid[startR][startC] = grid[endR][endC];
            grid[endR+1][endC] = '.';
        }


    }


    @Override
    public boolean isSolution() {

        for(int r = 0; r < height;r++)
        {
            if (grid[r][width-1] == 'X')
                return true;
        }
        return false;
    }

    public int getWidth()
    {
        return width;
    }
    public int getHeight()
    {
        return height;
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
                if((!(endC + 1 >= width)) && (collision(endC + 1, endR)))
                {
                    JamConfig j1 = new JamConfig(startR,endR,startC+1,endC+1, this, i, "R");
                    neighbors.add(j1);
                }
                if(!(startC - 1 < 0) && collision(startC - 1, startR))
                {
                    JamConfig j2 = new JamConfig(startR,endR,startC-1,endC-1, this, i, "L");
                    neighbors.add(j2);
                }
            }
            else {
                if (!(endR + 1 >= height) && collision(endC, endR + 1)) {
                    neighbors.add(new JamConfig(startR + 1, endR + 1, startC, endC, this, i, "U"));
                }
                if(!(startR - 1 < 0) && collision(startC, startR - 1)) {
                    neighbors.add(new JamConfig(startR - 1, endR - 1, startC, endC, this, i, "D"));
                }
            }
        }
        return neighbors;
    }

    @Override
    public boolean equals(Object other) {
        boolean result = true;
        if (other instanceof JamConfig j) {
            for(int r = 0; r < height; r++)
                for(int c = 0; c < width; c++)
                    if(grid[r][c] != j.grid[r][c])
                    {
                        result = false;
                        break;
                    }
        }
        return result;
    }

    public boolean collision(int x, int y)
    {
        return grid[y][x]=='.';
    }

    @Override
    public int hashCode() {
        return (this.toString()).hashCode();
    }

    public char[][] getGrid()
    {
        return grid;
    }

    public ArrayList<Car> getCars()
    {
        return cars;
    }

    @Override
    public String toString() {
        String s = "";
        for(int r = 0; r < height; r++)
        {
            for(int c = 0; c < width; c++)
            {
                s += grid[r][c] + " ";
            }
            if(r != height-1)
            s += "\n";
        }
        return s;
    }

    public Car getCar(String letter)
    {
        for(int i = 0; i < numCars; i++)
        {
            if (cars.get(i).getLetter().equals(letter))
            {
                return cars.get(i);
            }
        }
        return null;
    }
}
