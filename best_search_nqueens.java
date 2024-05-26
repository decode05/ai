import java.util.*;

class best_search_nqueens {
    static class BoardState implements Comparable<BoardState> {
        int[] board;
        int conflicts;
        int row;

        public BoardState(int[] board, int conflicts, int row) {
            this.board = board;
            this.conflicts = conflicts;
            this.row = row;
        }

        @Override
        public int compareTo(BoardState other) {
            return Integer.compare(this.conflicts, other.conflicts);
        }
    }

    static int calculateConflicts(int[] board) {
        int conflicts = 0;
        int N = board.length;
        for (int i = 0; i < N; i++) {
            for (int j = i + 1; j < N; j++) {
                if (board[i] == board[j] || Math.abs(board[i] - board[j]) == Math.abs(i - j)) {
                    conflicts++;
                }
            }
        }
        return conflicts;
    }

    static List<int[]> generateSuccessors(int[] board, int row) {
        List<int[]> successors = new ArrayList<>();
        int N = board.length;
        for (int col = 0; col < N; col++) {
            int[] newBoard = Arrays.copyOf(board, N);
            newBoard[row] = col;
            successors.add(newBoard);
        }
        return successors;
    }

    static int[] bestFirstSearch(int N) {
        PriorityQueue<BoardState> priorityQueue = new PriorityQueue<>();
        int[] initialBoard = new int[N]; // Initialized with zeros by default
        priorityQueue.add(new BoardState(initialBoard, calculateConflicts(initialBoard), 0));

        while (!priorityQueue.isEmpty()) {
            BoardState currentState = priorityQueue.poll();

            if (currentState.conflicts == 0 && currentState.row == N) {
                return currentState.board; // Found a solution
            }

            if (currentState.row < N) {
                List<int[]> successors = generateSuccessors(currentState.board, currentState.row);
                for (int[] successor : successors) {
                    int newConflicts = calculateConflicts(successor);
                    priorityQueue.add(new BoardState(successor, newConflicts, currentState.row + 1));
                }
            }
        }

        return null; // No solution found
    }

    public static void main(String[] args) {
        int N = 8;
        int[] solution = bestFirstSearch(N);
        if (solution != null) {
            System.out.println("Solution for " + N + "-Queens:");
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < N; j++) {
                    if (solution[i] == j) {
                        System.out.print("Q ");
                    } else {
                        System.out.print(". ");
                    }
                }
                System.out.println();
            }
        } else {
            System.out.println("No solution found for " + N + "-Queens.");
        }
    }
}
