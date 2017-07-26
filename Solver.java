import edu.princeton.cs.algs4.MinPQ;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Solver {

    private class Node implements Comparable<Node> {
        private Board board;
        private int moves;
        private Node prev;
        private int manhattan;

        private Node(Board board, int moves, Node prev) {
            this.board = board;
            this.moves = moves;
            this.prev = prev;
            this.manhattan = board.manhattan();
        }

        @Override
        public int compareTo(Node that) {
            return (this.manhattan + this.moves) - (that.manhattan + that.moves);
        }
    }

    private final int moves;
    private final boolean isSolvable;
    private final Node goalNode;

    // finds a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        Node dequeued;
        Node dequeuedTwin;
        MinPQ<Node> minPQ = new MinPQ<>();
        MinPQ<Node> minPQTwin = new MinPQ<>();

        if (initial == null) {
            throw new IllegalArgumentException();
        }

        minPQ.insert(new Node(initial, 0, null));
        minPQTwin.insert(new Node(initial.twin(), 0, null));
        while (true) {
            dequeued = minPQ.delMin();
            dequeuedTwin = minPQTwin.delMin();
            if (dequeued.board.isGoal()) {
                moves = dequeued.moves;
                isSolvable = true;
                goalNode = dequeued;
                break;
            } else if (dequeuedTwin.board.isGoal()) {
                moves = -1;
                isSolvable = false;
                goalNode = null;
                break;
            }
            for (Board board : dequeued.board.neighbors()) {
                try {
                    if (!board.equals(dequeued.prev.board)) {
                        minPQ.insert(new Node(board, dequeued.moves + 1, dequeued));
                    }
                } catch (NullPointerException ignored) {
                    minPQ.insert(new Node(board, dequeued.moves + 1, dequeued));
                }
            }
            for (Board board : dequeuedTwin.board.neighbors()) {
                try {
                    if (!board.equals(dequeuedTwin.prev.board)) {
                        minPQTwin.insert(new Node(board, dequeuedTwin.moves + 1, dequeuedTwin));
                    }
                } catch (NullPointerException ignored) {
                    minPQTwin.insert(new Node(board, dequeuedTwin.moves + 1, dequeuedTwin));
                }
            }
        }
    }

    // min number of moves to solve board; -1 if unsolvable
    public int moves() {
        return moves;
    }

    // is the board solvable?
    public boolean isSolvable() {
        return isSolvable;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (goalNode == null) {
            return null;
        }
        Node tempNode = goalNode;

        List<Board> list = new ArrayList<>();
        while (tempNode != null) {
            list.add(tempNode.board);
            tempNode = tempNode.prev;
        }
        Collections.reverse(list);
        return list;

    }
}