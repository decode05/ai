
import java.util.*;

public class CSP_waterjug {

    public static void main(String[] args) {
        int jug1Capacity = 5;
        int jug2Capacity = 3;
        int targetVolume = 4;

        List<String> solution = solveWaterJug(jug1Capacity, jug2Capacity, targetVolume);

        if (solution != null) {
            System.out.println("Solution found:");
            for (String step : solution) {
                System.out.println(step);
            }
        } else {
            System.out.println("No solution found.");
        }
    }

    public static List<String> solveWaterJug(int jug1Capacity, int jug2Capacity, int targetVolume) {
        Queue<State> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();
        Map<String, String> parentMap = new HashMap<>();

        queue.add(new State(0, 0));
        visited.add(hash(0, 0));

        while (!queue.isEmpty()) {
            State currentState = queue.poll();

            if (currentState.jug1Volume == targetVolume || currentState.jug2Volume == targetVolume) {
                return reconstructPath(parentMap, currentState);
            }

            List<State> successors = generateSuccessors(currentState, jug1Capacity, jug2Capacity);

            for (State successor : successors) {
                String hash = hash(successor.jug1Volume, successor.jug2Volume);
                if (!visited.contains(hash)) {
                    queue.add(successor);
                    visited.add(hash);
                    parentMap.put(hash, hash(currentState.jug1Volume, currentState.jug2Volume));
                }
            }
        }

        return null; // No solution found
    }

    public static List<State> generateSuccessors(State state, int jug1Capacity, int jug2Capacity) {
        List<State> successors = new ArrayList<>();

        // Fill jug 1
        successors.add(new State(jug1Capacity, state.jug2Volume));

        // Fill jug 2
        successors.add(new State(state.jug1Volume, jug2Capacity));

        // Empty jug 1
        successors.add(new State(0, state.jug2Volume));

        // Empty jug 2
        successors.add(new State(state.jug1Volume, 0));

        // Pour jug 1 into jug 2
        int transferAmount = Math.min(state.jug1Volume, jug2Capacity - state.jug2Volume);
        successors.add(new State(state.jug1Volume - transferAmount, state.jug2Volume + transferAmount));

        // Pour jug 2 into jug 1
        transferAmount = Math.min(state.jug2Volume, jug1Capacity - state.jug1Volume);
        successors.add(new State(state.jug1Volume + transferAmount, state.jug2Volume - transferAmount));

        return successors;
    }

    public static String hash(int jug1Volume, int jug2Volume) {
        return jug1Volume + "-" + jug2Volume;
    }

    public static List<String> reconstructPath(Map<String, String> parentMap, State currentState) {
        List<String> path = new ArrayList<>();
        String currentStateHash = hash(currentState.jug1Volume, currentState.jug2Volume);

        while (parentMap.containsKey(currentStateHash)) {
            String parentHash = parentMap.get(currentStateHash);
            State parentState = parseHash(parentHash);
            String step = String.format("Fill Jug 1: %d, Fill Jug 2: %d", parentState.jug1Volume, parentState.jug2Volume);
            path.add(0, step);
            currentStateHash = parentHash;
        }

        return path;
    }

    public static State parseHash(String hash) {
        String[] parts = hash.split("-");
        return new State(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
    }

    static class State {
        int jug1Volume;
        int jug2Volume;

        public State(int jug1Volume, int jug2Volume) {
            this.jug1Volume = jug1Volume;
            this.jug2Volume = jug2Volume;
        }
    }
}
