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
        setDirection();
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

    public String getColor(String carLetter) {
        return switch (carLetter) {
            case "A" -> "#6AF369";
            case "B" -> "#D5510A";
            case "C" -> "#0A0BF6";
            case "D" -> "#BF10C3";
            case "E" -> "#5D4EE7";
            case "F" -> "#023A08";
            case "G" -> "#545755";
            case "H" -> "#837C7C";
            case "I" -> "#E1D263";
            case "J" -> "#453819";
            case "K" -> "#517247";
            case "L" -> "#FFFFFF";
            case "O" -> "#FFFF00";
            case "P" -> "#8D00FF";
            case "Q" -> "#0171FF";
            case "R" -> "#119F21";
            case "S" -> "#000000";
            case "X" -> "#FF0000";
            default -> "#606365";
        };
    }
}
