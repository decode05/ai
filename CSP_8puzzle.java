
import java.util.*;

public class CSP_8puzzle {

    public static void main(String[] args) {
        int[][] initialState = {
            {1, 0, 3},
            {4, 5, 6},
            {7, 8, 2} // 0 represents the blank tile
        };

        int[][] goalState = {
            {1, 2, 3},
            {4, 5, 6},
            {7, 8, 0}
        };

        System.out.println("Initial State:");
        printBoard(initialState);

        List<int[][]> solution = solveEightPuzzle(initialState, goalState);

        if (solution != null) {
            System.out.println("Solution found:");
            for (int[][] state : solution) {
                printBoard(state);
            }
        } else {
            System.out.println("No solution found.");
        }
    }

    public static List<int[][]> solveEightPuzzle(int[][] initialState, int[][] goalState) {
        Queue<int[][]> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();
        Map<String, String> parentMap = new HashMap<>();

        queue.add(initialState);
        visited.add(hash(initialState));

        while (!queue.isEmpty()) {
            int[][] currentState = queue.poll();

            if (Arrays.deepEquals(currentState, goalState)) {
                return reconstructPath(parentMap, currentState);
            }

            List<int[][]> successors = generateSuccessors(currentState);

            for (int[][] successor : successors) {
                String hash = hash(successor);
                if (!visited.contains(hash)) {
                    queue.add(successor);
                    visited.add(hash);
                    parentMap.put(hash, hash(currentState));
                }
            }
        }

        return null; // No solution found
    }

    public static List<int[][]> generateSuccessors(int[][] state) {
        List<int[][]> successors = new ArrayList<>();

        int zeroRow = -1;
        int zeroCol = -1;

        // Find the position of the blank tile (0)
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (state[i][j] == 0) {
                    zeroRow = i;
                    zeroCol = j;
                    break;
                }
            }
        }

        // Generate successors by moving the blank tile
        int[][] moves = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
        for (int[] move : moves) {
            int newRow = zeroRow + move[0];
            int newCol = zeroCol + move[1];
            if (newRow >= 0 && newRow < 3 && newCol >= 0 && newCol < 3) {
                int[][] successor = cloneState(state);
                successor[zeroRow][zeroCol] = successor[newRow][newCol];
                successor[newRow][newCol] = 0;
                successors.add(successor);
            }
        }

        return successors;
    }

    public static int[][] cloneState(int[][] state) {
        int[][] newState = new int[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                newState[i][j] = state[i][j];
            }
        }
        return newState;
    }

    public static String hash(int[][] state) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                sb.append(state[i][j]);
            }
        }
        return sb.toString();
    }

    public static List<int[][]> reconstructPath(Map<String, String> parentMap, int[][] currentState) {
        List<int[][]> path = new ArrayList<>();
        String currentStateHash = hash(currentState);

        while (parentMap.containsKey(currentStateHash)) {
            int[][] parentState = parseHash(parentMap.get(currentStateHash));
            path.add(0, parentState);
            currentStateHash = parentMap.get(currentStateHash);
        }

        path.add(0, currentState); // Add initial state
        return path;
    }

    public static int[][] parseHash(String hash) {
        int[][] state = new int[3][3];
        int index = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                state[i][j] = Character.getNumericValue(hash.charAt(index++));
            }
        }
        return state;
    }

    public static void printBoard(int[][] board) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }
}
