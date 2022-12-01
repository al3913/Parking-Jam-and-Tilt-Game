package puzzles.jam.model;

public class Car {
    private int startR;
    private int endR;
    private int startC;
    private int endC;
    private String direction;
    private String letter;

    public Car(int startR, int startC, int endR, int endC, String letter)
    {
        this.startR = startR;
        this.startC = startC;
        this.endR = endR;
        this.endC = endC;
        this.letter = letter;
    }

    public void setDirection()
    {
        if(startR == endR)
        {
            direction = "H";
        }
        else {
            direction = "V";
        }
    }

    public String getDirection()
    {
        return direction;
    }

    public String getLetter()
    {
        return letter;
    }

    public int getStartRow()
    {
        return startR;
    }

    public int getStartCol()
    {
        return startC;
    }

    public int getEndRow()
    {
        return endR;
    }

    public int getEndCol()
    {
        return endC;
    }

}
