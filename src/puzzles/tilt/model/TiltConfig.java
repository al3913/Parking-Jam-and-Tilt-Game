package puzzles.tilt.model;

import puzzles.common.solver.Configuration;

import java.util.*;

public class TiltConfig implements Configuration {

    private final static char BLOCK = '*';
    private final static char EMPTY = '.';
    private final static char HOLE = 'O';
    private final static char BLUE = 'B';
    private final static char GREEN = 'G';

    private static int DIM;
    public char[][] board;

    public TiltConfig(int dim, char[][] b) {
        DIM = dim;
        this.board = new char[dim][dim];
        for(int r=0;r<dim;r++) {
            System.arraycopy(b[r], 0, board[r], 0, dim);
        }
    }

    public TiltConfig() {}

    @Override
    public boolean isSolution() {
        for(int r=0;r<DIM;r++) {
            for(int c=0;c<DIM;c++) {
                if(board[r][c]==GREEN)
                    return false;
            }
        }
        return true;
    }

    @Override
    public Collection<Configuration> getNeighbors() {
        List<Configuration> neighbors = new ArrayList<>();
        neighbors.add(this.tiltNorth());
        neighbors.add(this.tiltEast());
        neighbors.add(this.tiltSouth());
        neighbors.add(this.tiltWest());
        neighbors.removeIf(Objects::isNull); //Remove moves that return null
        return neighbors;
    }

    @Override
    public boolean equals(Object other) {
        boolean b = true;
        if (other instanceof TiltConfig t) {
            for (int r = 0; r < DIM; r++) {
                for (int c = 0; c < DIM; c++) {
                    if (board[r][c] != t.board[r][c]) {
                        b = false;
                        break;
                    }
                }
            }
        }
        return b;
    }

    @Override
    public int hashCode() {
        return (this.toString()).hashCode();
    }

    @Override
    public String toString() {
        String s = "";
        for(int r=0; r<DIM; r++) {
            for(int c=0; c<DIM; c++) {
                s+=(board[r][c] + " ");
            }
            s+="\n";
        }
        return s;
    }

    public TiltConfig tiltNorth() {
        TiltConfig north = new TiltConfig(DIM, this.board);
        for(int r=1;r<DIM;r++) {
            for(int c=0;c<DIM;c++) {
                if(north.board[r][c]==GREEN || north.board[r][c]==BLUE) { //When a disc is found
                    int row = r-1; //Check row above
                    while(row>=0) { //While in the bounds of the array
                        if(north.board[row][c]==HOLE) { //If object above is a hole
                            if(north.board[r][c]==GREEN) north.board[r][c] = EMPTY; //GREEN GOES IN
                            if(north.board[r][c]==BLUE) return null; //Can't put blue in, returns null
                            break; //Piece has found its spot -> break out of while
                        }
                        else if(north.board[row][c]!=EMPTY) { //If it's not an empty space
                            if (row + 1 != r) { //It isn't the same as the row before
                                north.board[row + 1][c] = north.board[r][c]; //Piece moves to the row before object
                                north.board[r][c] = EMPTY; //Previous position is set to empty
                            }
                            break; //Piece has found its spot -> break out of while
                        }
                        else if(row==0 && north.board[row][c]==EMPTY) { //If row is the top of the board and it's empty
                            north.board[row][c] = north.board[r][c]; //Piece is put at the top
                            north.board[r][c] = EMPTY; //Previous position is set to empty
                            break; //Piece has found its spot -> break out of while
                        }
                        row--; //If row is empty, go to next row
                    }
                }
            }
        }
        if(this.equals(north)) return null; //If no pieces moved, returns null
        return north; //Return board
    }
    public TiltConfig tiltSouth() {
        TiltConfig south = new TiltConfig(DIM, this.board);
        for(int r=DIM-1;r>=0;r--) {
            for(int c=0;c<DIM;c++) {
                if(south.board[r][c]==GREEN || south.board[r][c]==BLUE) { //When a disc is found
                    int row = r+1; //Check row below
                    while(row<=DIM-1) { //While in the bounds of the array
                        if(south.board[row][c]==HOLE) { //If object above is a hole
                            if(south.board[r][c]==GREEN) south.board[r][c] = EMPTY; //GREEN GOES IN
                            if(south.board[r][c]==BLUE) return null; //Can't put blue in, returns null
                            break; //Piece has found its spot -> break out of while
                        }
                        else if(south.board[row][c]!=EMPTY) { //If it's not an empty space
                            if (row - 1 != r) { //It isn't the same as the row before
                                south.board[row - 1][c] = south.board[r][c]; //Piece moves to the row before object
                                south.board[r][c] = EMPTY; //Previous position is set to empty
                            }
                            break; //Piece has found its spot -> break out of while
                        }
                        else if(row==DIM-1 && south.board[row][c]==EMPTY) { //If row is the bottom of the board and it's empty
                            south.board[row][c] = south.board[r][c]; //Piece is put at the bottom
                            south.board[r][c] = EMPTY; //Previous position is set to empty
                            break; //Piece has found its spot -> break out of while
                        }
                        row++; //If row is empty, go to next row
                    }
                }
            }
        }
        if(this.equals(south)) return null; //If no pieces moved, returns null
        return south; //Return board
    }
    public TiltConfig tiltEast() {
        TiltConfig east = new TiltConfig(DIM, this.board);
        for(int r=0;r<DIM;r++) {
            for(int c=DIM-1;c>=0;c--) {
                if(east.board[r][c]==GREEN || east.board[r][c]==BLUE) { //When a disc is found
                    int col = c+1; //Check col to the right
                    while(col<=DIM-1) { //While in the bounds of the array
                        if(east.board[r][col]==HOLE) { //If object above is a hole
                            if(east.board[r][c]==GREEN) east.board[r][c] = EMPTY; //GREEN GOES IN
                            if(east.board[r][c]==BLUE) return null; //Can't put blue in, returns null
                            break; //Piece has found its spot -> break out of while
                        }
                        else if(east.board[r][col]!=EMPTY) { //If it's not an empty space
                            if (col - 1 != c) { //It isn't the same as the col before
                                east.board[r][col-1] = east.board[r][c]; //Piece moves to the col before object
                                east.board[r][c] = EMPTY; //Previous position is set to empty
                            }
                            break; //Piece has found its spot -> break out of while
                        }
                        else if(col==DIM-1 && east.board[r][col]==EMPTY) { //If col is the right side of the board, and it's empty
                            east.board[r][col] = east.board[r][c]; //Piece is put on the right side
                            east.board[r][c] = EMPTY; //Previous position is set to empty
                            break; //Piece has found its spot -> break out of while
                        }
                        col++; //If col is empty, go to next col
                    }
                }
            }
        }
        if(this.equals(east)) return null; //If no pieces moved, returns null
        return east; //Return board
    }
    public TiltConfig tiltWest() {
        TiltConfig west = new TiltConfig(DIM, this.board);
        for(int r=0;r<DIM;r++) {
            for(int c=1;c<DIM;c++) {
                if(west.board[r][c]==GREEN || west.board[r][c]==BLUE) { //When a disc is found
                    int col = c-1; //Check col to the left
                    while(col>=0) { //While in the bounds of the array
                        if(west.board[r][col]==HOLE) { //If object above is a hole
                            if(west.board[r][c]==GREEN) west.board[r][c] = EMPTY; //GREEN GOES IN
                            if(west.board[r][c]==BLUE) return null; //Can't put blue in, returns null
                            break; //Piece has found its spot -> break out of while
                        }
                        else if(west.board[r][col]!=EMPTY) { //If it's not an empty space
                            if (col + 1 != c) { //It isn't the same as the col before
                                west.board[r][col+1] = west.board[r][c]; //Piece moves to the col before object
                                west.board[r][c] = EMPTY; //Previous position is set to empty
                            }
                            break; //Piece has found its spot -> break out of while
                        }
                        else if(col==0 && west.board[r][col]==EMPTY) { //If col is the left side of the board, and it's empty
                            west.board[r][col] = west.board[r][c]; //Piece is put on the left side
                            west.board[r][c] = EMPTY; //Previous position is set to empty
                            break; //Piece has found its spot -> break out of while
                        }
                        col--; //If col is empty, go to next col
                    }
                }
            }
        }
        if(this.equals(west)) return null; //If no pieces moved, returns null
        return west; //Return board
    }

    public int getDIM() { return DIM; }
}
