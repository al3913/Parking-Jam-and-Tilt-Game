package puzzles.jam.model;

/**
 * Class that represents a Car on the board
 *
 *
 * @author Andy Lin
 */
public class Car {
    private int startR;
    private int endR;
    private int startC;
    private int endC;
    private String direction;
    private String letter;

    /**
     * Car constructor
     * @param startR = far left or far top row location
     * @param startC = far left or far top col location
     * @param endR = far right or far bottom row location
     * @param endC = far right or far bottom col location
     * @param letter = letter for the car
     */
    public Car(int startR, int startC, int endR, int endC, String letter)
    {
        this.startR = startR;
        this.startC = startC;
        this.endR = endR;
        this.endC = endC;
        this.letter = letter;
        setDirection();
    }

    /**
     * Sets the direction for the car
     */
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

    /**
     * gets the direction
     * @return direction
     */
    public String getDirection()
    {
        return direction;
    }

    /**
     * gets the letter
     * @return letter
     */
    public String getLetter()
    {
        return letter;
    }

    /**
     * gets the start row
     * @return startR
     */
    public int getStartRow()
    {
        return startR;
    }

    /**
     * gets the start col
     * @return startC;
     */
    public int getStartCol()
    {
        return startC;
    }

    /**
     * gets the end row
     * @return endR;
     */
    public int getEndRow()
    {
        return endR;
    }

    /**
     * gets the end col
     * @return endC
     */
    public int getEndCol()
    {
        return endC;
    }

    /**
     * Gets the color based on the letter of the car
     * @param carLetter letter representation of the car
     * @return color hexcode
     */
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
