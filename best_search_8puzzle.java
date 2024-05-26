import java.util.*;

class best_search_8puzzle {
    static class BoardState implements Comparable<BoardState> {
        int[] board;
        int g;
        int h;
        BoardState parent;

        public BoardState(int[] board, int g, int h, BoardState parent) {
            this.board = board.clone();
            this.g = g;
            this.h = h;
            this.parent = parent;
        }

        @Override
        public int compareTo(BoardState other) {
            return Integer.compare(this.h, other.h);
        }
    }

    static int calculateManhattanDistance(int[] board) {
        int distance = 0;
        int size = (int) Math.sqrt(board.length);
        for (int i = 0; i < board.length; i++) {
            if (board[i] != 0) {
                int targetX = (board[i] - 1) % size;
                int targetY = (board[i] - 1) / size;
                int x = i % size;
                int y = i / size;
                distance += Math.abs(x - targetX) + Math.abs(y - targetY);
            }
        }
        return distance;
    }

    static List<int[]> generateSuccessors(int[] board) {
        List<int[]> successors = new ArrayList<>();
        int size = (int) Math.sqrt(board.length);
        int blankIndex = -1;
        for (int i = 0; i < board.length; i++) {
            if (board[i] == 0) {
                blankIndex = i;
                break;
            }
        }

        int[] dRow = {1, -1, 0, 0};
        int[] dCol = {0, 0, 1, -1};
        int blankRow = blankIndex / size;
        int blankCol = blankIndex % size;

        for (int k = 0; k < 4; k++) {
            int newRow = blankRow + dRow[k];
            int newCol = blankCol + dCol[k];
            if (newRow >= 0 && newRow < size && newCol >= 0 && newCol < size) {
                int newIndex = newRow * size + newCol;
                int[] newBoard = board.clone();
                newBoard[blankIndex] = newBoard[newIndex];
                newBoard[newIndex] = 0;
                successors.add(newBoard);
            }
        }

        return successors;
    }

    static boolean isGoalState(int[] board) {
        for (int i = 0; i < board.length - 1; i++) {
            if (board[i] != i + 1) {
                return false;
            }
        }
        return board[board.length - 1] == 0;
    }

    static void printSolution(BoardState state) {
        Stack<BoardState> path = new Stack<>();
        while (state != null) {
            path.push(state);
            state = state.parent;
        }
        while (!path.isEmpty()) {
            BoardState current = path.pop();
            for (int i = 0; i < current.board.length; i++) {
                System.out.print(current.board[i] + " ");
                if ((i + 1) % 3 == 0) {
                    System.out.println();
                }
            }
            System.out.println();
        }
    }

    static void bestFirstSearch(int[] initialBoard) {
        PriorityQueue<BoardState> priorityQueue = new PriorityQueue<>();
        Set<String> visited = new HashSet<>();
        int initialHeuristic = calculateManhattanDistance(initialBoard);
        priorityQueue.add(new BoardState(initialBoard, 0, initialHeuristic, null));
        visited.add(Arrays.toString(initialBoard));

        while (!priorityQueue.isEmpty()) {
            BoardState currentState = priorityQueue.poll();

            if (isGoalState(currentState.board)) {
                System.out.println("Solution found:");
                printSolution(currentState);
                return;
            }

            for (int[] successor : generateSuccessors(currentState.board)) {
                if (!visited.contains(Arrays.toString(successor))) {
                    int g = currentState.g + 1;
                    int h = calculateManhattanDistance(successor);
                    priorityQueue.add(new BoardState(successor, g, h, currentState));
                    visited.add(Arrays.toString(successor));
                }
            }
        }

        System.out.println("No solution found.");
    }

    public static void main(String[] args) {
        int[] initialBoard = {
            1, 0, 3,
            4, 2, 5,
            7, 8, 6
        };

        bestFirstSearch(initialBoard);
    }
}
