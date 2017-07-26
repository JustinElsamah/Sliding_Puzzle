import java.util.ArrayList;
import java.util.List;

/**
 * Created by justinelsemah on 2017-07-24.
 */
public class Board {

    private final int[][] blocks;
    private int hamming;
    private int manhattan;
    private boolean isGoal;
    private final int[] indexOfZero = new int[2];
    private final int[][] twinBlocks;
    private final int[] arrData;

    // construct a board from an n-by-n array of blocks
    public Board(int[][] blocks) {
        this.blocks = blocks;
        hamming = -1;
        manhattan = 0;
        int row;
        int column;
        int counter = -1;
        arrData = new int[6];
        twinBlocks = new int[this.dimension()][this.dimension()];

        // run through every block
        for (int i = 0; i < this.dimension(); i++) {
            for (int j = 0; j < this.dimension(); j++) {
                // hamming implementation
                if (blocks[i][j] != i * this.dimension() + (j + 1)) {
                    hamming++;
                }
                // manhattan implementation
                if (blocks[i][j] != 0) {
                    row = blocks[i][j] / this.dimension();
                    column = blocks[i][j] % this.dimension();
                    if (column == 0) {
                        row--;
                        column = this.dimension() - 1;
                    } else {
                        column--;
                    }
                    manhattan += Math.abs(i - row);
                    manhattan += Math.abs(j - column);

                }// finding index of zero
                else {
                    indexOfZero[0] = i;
                    indexOfZero[1] = j;
                }
                // twin implementation
                if (this.blocks[i][j] != 0 && counter < 3) {
                    arrData[++counter] = i;
                    arrData[++counter] = j;
                    arrData[++counter] = this.blocks[i][j];
                }
                twinBlocks[i][j] = this.blocks[i][j];
            }
        }
    }

    // (where blocks[i][j] = block in row i, column j)
    // board dimension
    public int dimension() {
        return this.blocks.length;
    }

    // number of blocks out of place
    public int hamming() {
        return hamming;
    }


    // sum of Manhattan distances between blocks and goal
    public int manhattan() {
        return manhattan;
    }

    // is this board the goal board?
    public boolean isGoal() {
        if (this.hamming() == 0) {
            return true;
        }
        return false;
    }

    // a board that is obtained by exchanging any pair of blocks
    public Board twin() {
        twinBlocks[arrData[0]][arrData[1]] = arrData[5];
        twinBlocks[arrData[3]][arrData[4]] = arrData[2];
        Board blocks = new Board(twinBlocks);
        return blocks;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == null) {
            return false;
        }
        if (!Board.class.isAssignableFrom(y.getClass())) {
            return false;
        }
        final Board that = (Board) y;
        if (this.dimension() != that.dimension()) {
            return false;
        }
        for (int i = 0; i < this.dimension(); i++) {
            for (int j = 0; j < this.dimension(); j++) {
                if (this.blocks[i][j] != that.blocks[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        List<Board> list = new ArrayList<>();
        List<int[][]> arrays = new ArrayList<>();

        if (indexOfZero[0] != 0) {
            arrays.add(new int[this.dimension()][this.dimension()]);
        }
        if (indexOfZero[0] != this.dimension() - 1) {
            arrays.add(new int[this.dimension()][this.dimension()]);
        }
        if (indexOfZero[1] != 0) {
            arrays.add(new int[this.dimension()][this.dimension()]);
        }
        if (indexOfZero[1] != this.dimension() - 1) {
            arrays.add(new int[this.dimension()][this.dimension()]);
        }

        for (int i = 0; i < this.dimension(); i++) {
            for (int j = 0; j < this.dimension(); j++) {
                for (int[][] arr : arrays) {
                    arr[i][j] = blocks[i][j];
                }
            }
        }

        int counter = 0;
        if (indexOfZero[0] != 0) {
            arrays.get(counter)[indexOfZero[0]][indexOfZero[1]] = arrays.get(counter)[indexOfZero[0] - 1][indexOfZero[1]];
            arrays.get(counter)[indexOfZero[0] - 1][indexOfZero[1]] = 0;
            list.add(new Board(arrays.get(counter)));
            counter++;
        }
        if (indexOfZero[0] != this.dimension() - 1) {
            arrays.get(counter)[indexOfZero[0]][indexOfZero[1]] = arrays.get(counter)[indexOfZero[0] + 1][indexOfZero[1]];
            arrays.get(counter)[indexOfZero[0] + 1][indexOfZero[1]] = 0;
            list.add(new Board(arrays.get(counter)));
            counter++;
        }
        if (indexOfZero[1] != 0) {
            arrays.get(counter)[indexOfZero[0]][indexOfZero[1]] = arrays.get(counter)[indexOfZero[0]][indexOfZero[1] - 1];
            arrays.get(counter)[indexOfZero[0]][indexOfZero[1] - 1] = 0;
            list.add(new Board(arrays.get(counter)));
            counter++;
        }
        if (indexOfZero[1] != this.dimension() - 1) {
            arrays.get(counter)[indexOfZero[0]][indexOfZero[1]] = arrays.get(counter)[indexOfZero[0]][indexOfZero[1] + 1];
            arrays.get(counter)[indexOfZero[0]][indexOfZero[1] + 1] = 0;
            list.add(new Board(arrays.get(counter)));
        }

        return list;

    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(this.dimension() + "\n");
        for (int i = 0; i < this.dimension(); i++) {
            for (int j = 0; j < this.dimension(); j++) {
                s.append(String.format("%2d ", blocks[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    public static void main(String[] args) {
        Board board1 = new Board(new int[][]{{1, 3, 2}, {4, 5, 6}, {7, 8, 0}});
        System.out.println(board1);
        for (Board board : board1.neighbors()) {
            System.out.println(board);
        }
    }
}
