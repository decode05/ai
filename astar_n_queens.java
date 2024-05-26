import java.util.*;

class Board implements Comparable<Board> {
    private int[] queens; // queens[i] represents the column position of the queen in row i
    private int g; // cost to reach this board
    private int h; // heuristic cost from this board to the goal
    private int n; // size of the board

    public Board(int n) {
        this.n = n;
        this.queens = new int[n];
        Arrays.fill(queens, -1);
        this.g = 0;
        this.h = 0;
    }

    public Board(int[] queens, int g) {
        this.queens = queens.clone();
        this.g = g;
        this.n = queens.length;
        this.h = calculateHeuristic();
    }

    private int calculateHeuristic() {
        int conflicts = 0;
        for (int i = 0; i < n; i++) {
            if (queens[i] == -1) continue;
            for (int j = i + 1; j < n; j++) {
                if (queens[j] == -1) continue;
                if (queens[i] == queens[j] || Math.abs(queens[i] - queens[j]) == Math.abs(i - j)) {
                    conflicts++;
                }
            }
        }
        return conflicts;
    }

    public List<Board> generateSuccessors() {
        List<Board> successors = new ArrayList<>();
        int row = -1;
        for (int i = 0; i < n; i++) {
            if (queens[i] == -1) {
                row = i;
                break;
            }
        }
        if (row == -1) return successors;
        for (int col = 0; col < n; col++) {
            int[] newQueens = queens.clone();
            newQueens[row] = col;
            successors.add(new Board(newQueens, g + 1));
        }
        return successors;
    }

    public boolean isGoal() {
        return h == 0 && queens[n - 1] != -1;
    }

    @Override
    public int compareTo(Board other) {
        return Integer.compare(this.g + this.h, other.g + other.h);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Board board = (Board) obj;
        return Arrays.equals(queens, board.queens);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(queens);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (queens[i] == j) {
                    sb.append("Q ");
                } else {
                    sb.append(". ");
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}

public class astar_n_queens {
    public static void main(String[] args) {
        int n = 8; // Change this value for different sizes of the board
        Board initialBoard = new Board(n);
        PriorityQueue<Board> openSet = new PriorityQueue<>();
        Set<Board> closedSet = new HashSet<>();
        openSet.add(initialBoard);

        while (!openSet.isEmpty()) {
            Board current = openSet.poll();
            if (current.isGoal()) {
                System.out.println("Solution found:\n" + current);
                return;
            }
            closedSet.add(current);
            for (Board neighbor : current.generateSuccessors()) {
                if (!closedSet.contains(neighbor) && !openSet.contains(neighbor)) {
                    openSet.add(neighbor);
                }
            }
        }
        System.out.println("No solution found");
    }
}
