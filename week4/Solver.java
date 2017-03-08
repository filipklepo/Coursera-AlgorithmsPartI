import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class Solver {

    private Collection<Board> solution;
    private int moves = -1;

    private static class AStarNode implements Comparable<AStarNode> {

        Board board;
        AStarNode previous;
        int moves;

        public AStarNode(Board board, AStarNode previous, int moves) {
            this.board = board;
            this.previous = previous;
            this.moves = moves;
        }

        @Override
        public int compareTo(AStarNode o) {
            return Integer.compare(this.board.manhattan() + this.moves, o.board.manhattan() + o.moves);
        }
    }

    public Solver(Board initial) {
        Objects.requireNonNull(initial);

        solve(initial);
    }

    private void solve(Board initial) {
        Board twin = initial.twin();
        MinPQ<AStarNode> initialPq = new MinPQ<>();
        MinPQ<AStarNode> twinPq = new MinPQ<>();

        initialPq.insert(new AStarNode(initial, null, 0));
        twinPq.insert(new AStarNode(twin, null, 0));

        while (true) {
            AStarNode initalMinimum = initialPq.delMin();
            AStarNode twinMinimum = twinPq.delMin();

            if (initalMinimum.board.isGoal()) {
                Collection<Board> solution = new ArrayList<>();
                insertSolutionStep(solution, initalMinimum);

                this.solution = solution;
                this.moves = initalMinimum.moves;
                break;
            } else if (twinMinimum.board.isGoal()) {
                break;
            }

            // fill priority queues with neighbors of current minimums
            for (Board initialBoardNeighbor : initalMinimum.board.neighbors()) {
                if (Objects.nonNull(initalMinimum.previous) &&
                        initalMinimum.previous.board.equals(initialBoardNeighbor)) continue;

                initialPq.insert(new AStarNode(initialBoardNeighbor, initalMinimum, initalMinimum.moves + 1));
            }
            for (Board twinBoardNeighbor : twinMinimum.board.neighbors()) {
                if (Objects.nonNull(twinMinimum.previous) &&
                        twinMinimum.previous.board.equals(twinBoardNeighbor)) continue;

                twinPq.insert(new AStarNode(twinBoardNeighbor, twinMinimum, twinMinimum.moves + 1));
            }
        }
    }

    public boolean isSolvable() {
        return Objects.nonNull(solution);
    }

    public int moves() {
        return moves;
    }

    public Iterable<Board> solution() {
        return new ArrayList<>(solution);
    }

    private void insertSolutionStep(Collection<Board> soultion, AStarNode current) {
        if (Objects.isNull(current)) {
            return;
        }

        insertSolutionStep(soultion, current.previous);
        soultion.add(current.board);
    }

    public static void main(String[] args) {
    }
}
