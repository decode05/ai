
import java.util.*;

public class CSP_missionary {

    public static void main(String[] args) {
        int numMissionaries = 3;
        int numCannibals = 3;

        List<List<Integer>> solution = solveMissionariesAndCannibals(numMissionaries, numCannibals);

        if (solution != null) {
            System.out.println("Solution found:");
            for (List<Integer> step : solution) {
                System.out.println(step);
            }
        } else {
            System.out.println("No solution found.");
        }
    }

    public static List<List<Integer>> solveMissionariesAndCannibals(int numMissionaries, int numCannibals) {
        Queue<State> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();
        Map<String, String> parentMap = new HashMap<>();

        State initialState = new State(numMissionaries, numMissionaries, 0, 0, 0);
        queue.add(initialState);
        visited.add(hash(initialState));

        while (!queue.isEmpty()) {
            State currentState = queue.poll();

            if (currentState.numMissionariesOnLeftBank == 0 && currentState.numCannibalsOnLeftBank == 0) {
                return reconstructPath(parentMap, currentState);
            }

            List<State> successors = generateSuccessors(currentState, numMissionaries, numCannibals);

            for (State successor : successors) {
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

    public static List<State> generateSuccessors(State state, int numMissionaries, int numCannibals) {
        List<State> successors = new ArrayList<>();

        // Move one missionary from left to right
        if (state.boatPosition == 0 && state.numMissionariesOnLeftBank >= 1) {
            successors.add(new State(
                state.numMissionariesOnLeftBank - 1, state.numCannibalsOnLeftBank, 1,
                state.numMissionariesOnRightBank + 1, state.numCannibalsOnRightBank
            ));
        }

        // Move one cannibal from left to right
        if (state.boatPosition == 0 && state.numCannibalsOnLeftBank >= 1) {
            successors.add(new State(
                state.numMissionariesOnLeftBank, state.numCannibalsOnLeftBank - 1, 1,
                state.numMissionariesOnRightBank, state.numCannibalsOnRightBank + 1
            ));
        }

        // Move two missionaries from left to right
        if (state.boatPosition == 0 && state.numMissionariesOnLeftBank >= 2) {
            successors.add(new State(
                state.numMissionariesOnLeftBank - 2, state.numCannibalsOnLeftBank, 1,
                state.numMissionariesOnRightBank + 2, state.numCannibalsOnRightBank
            ));
        }

        // Move two cannibals from left to right
        if (state.boatPosition == 0 && state.numCannibalsOnLeftBank >= 2) {
            successors.add(new State(
                state.numMissionariesOnLeftBank, state.numCannibalsOnLeftBank - 2, 1,
                state.numMissionariesOnRightBank, state.numCannibalsOnRightBank + 2
            ));
        }

        // Move one missionary and one cannibal from left to right
        if (state.boatPosition == 0 && state.numMissionariesOnLeftBank >= 1 && state.numCannibalsOnLeftBank >= 1) {
            successors.add(new State(
                state.numMissionariesOnLeftBank - 1, state.numCannibalsOnLeftBank - 1, 1,
                state.numMissionariesOnRightBank + 1, state.numCannibalsOnRightBank + 1
            ));
        }

        // Move one missionary from right to left
        if (state.boatPosition == 1 && state.numMissionariesOnRightBank >= 1) {
            successors.add(new State(
                state.numMissionariesOnLeftBank + 1, state.numCannibalsOnLeftBank, 0,
                state.numMissionariesOnRightBank - 1, state.numCannibalsOnRightBank
            ));
        }

        // Move one cannibal from right to left
        if (state.boatPosition == 1 && state.numCannibalsOnRightBank >= 1) {
            successors.add(new State(
                state.numMissionariesOnRightBank, state.numCannibalsOnLeftBank + 1, 0,
                state.numMissionariesOnRightBank, state.numCannibalsOnRightBank - 1
                ));
                }
                    // Move two missionaries from right to left
    if (state.boatPosition == 1 && state.numMissionariesOnRightBank >= 2) {
        successors.add(new State(
            state.numMissionariesOnLeftBank + 2, state.numCannibalsOnLeftBank, 0,
            state.numMissionariesOnRightBank - 2, state.numCannibalsOnRightBank
        ));
    }

    // Move two cannibals from right to left
    if (state.boatPosition == 1 && state.numCannibalsOnRightBank >= 2) {
        successors.add(new State(
            state.numMissionariesOnLeftBank, state.numCannibalsOnLeftBank + 2, 0,
            state.numMissionariesOnRightBank, state.numCannibalsOnRightBank - 2
        ));
    }

    // Move one missionary and one cannibal from right to left
    if (state.boatPosition == 1 && state.numMissionariesOnRightBank >= 1 && state.numCannibalsOnRightBank >= 1) {
        successors.add(new State(
            state.numMissionariesOnLeftBank + 1, state.numCannibalsOnLeftBank + 1, 0,
            state.numMissionariesOnRightBank - 1, state.numCannibalsOnRightBank - 1
        ));
    }

    return successors;
}

public static String hash(State state) {
    return state.numMissionariesOnLeftBank + "-" + state.numCannibalsOnLeftBank + "-" +
           state.boatPosition + "-" + state.numMissionariesOnRightBank + "-" + state.numCannibalsOnRightBank;
}

public static List<List<Integer>> reconstructPath(Map<String, String> parentMap, State currentState) {
    List<List<Integer>> path = new ArrayList<>();
    String currentStateHash = hash(currentState);

    while (parentMap.containsKey(currentStateHash)) {
        String parentHash = parentMap.get(currentStateHash);
        State parentState = parseHash(parentHash);
        List<Integer> step = new ArrayList<>();
        step.add(parentState.numMissionariesOnLeftBank);
        step.add(parentState.numCannibalsOnLeftBank);
        step.add(parentState.boatPosition);
        step.add(parentState.numMissionariesOnRightBank);
        step.add(parentState.numCannibalsOnRightBank);
        path.add(0, step);
        currentStateHash = parentHash;
    }

    return path;
}

public static State parseHash(String hash) {
    String[] parts = hash.split("-");
    return new State(
        Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Integer.parseInt(parts[2]),
        Integer.parseInt(parts[3]), Integer.parseInt(parts[4])
    );
}

static class State {
    int numMissionariesOnLeftBank;
    int numCannibalsOnLeftBank;
    int boatPosition; // 0 for left bank, 1 for right bank
    int numMissionariesOnRightBank;
    int numCannibalsOnRightBank;

    public State(int numMissionariesOnLeftBank, int numCannibalsOnLeftBank, int boatPosition,
                 int numMissionariesOnRightBank, int numCannibalsOnRightBank) {
        this.numMissionariesOnLeftBank = numMissionariesOnLeftBank;
        this.numCannibalsOnLeftBank = numCannibalsOnLeftBank;
        this.boatPosition = boatPosition;
        this.numMissionariesOnRightBank = numMissionariesOnRightBank;
        this.numCannibalsOnRightBank = numCannibalsOnRightBank;
    }
}
}
                

