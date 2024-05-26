import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.HashSet;

public class BFS_missionary {

    private static final int BOAT_CAPACITY = 2;

    public static void main(String[] args) {
        int missionaries = 3;
        int cannibals = 3;

        State initialState = new State(missionaries, cannibals, true);
        solve(initialState);
    }

    private static void solve(State initialState) {
        Set<State> visited = new HashSet<>();
        Queue<State> queue = new LinkedList<>();

        queue.add(initialState);
        visited.add(initialState);

        while (!queue.isEmpty()) {
            State current = queue.poll();

            if (current.isGoal()) {
                System.out.println("Solution Found!");
                printPath(current);
                return;
            }

            for (State nextState : generateSuccessors(current)) {
                if (!visited.contains(nextState)) {
                    queue.add(nextState);
                    visited.add(nextState);
                }
            }
        }

        System.out.println("No Solution Found!");
    }

    private static void printPath(State state) {
        if (state == null) {
            return;
        }
        printPath(state.parent);
        System.out.println(state);
    }

    private static Set<State> generateSuccessors(State state) {
        Set<State> successors = new HashSet<>();

        int missionariesOnLeftBank = state.missionariesLeft;
        int cannibalsOnLeftBank = state.cannibalsLeft;
        boolean boatOnLeftBank = state.boatLeft;

        // Try moving 1 or 2 missionaries
        for (int missionariesToMove = 1; missionariesToMove <= Math.min(missionariesOnLeftBank, BOAT_CAPACITY); missionariesToMove++) {
            int remainingMissionaries = missionariesOnLeftBank - missionariesToMove;
            int cannibalsToMove = BOAT_CAPACITY - missionariesToMove;

            // Ensure cannibals aren't outnumbered (and can move that many)
            if (cannibalsToMove <= cannibalsOnLeftBank && remainingMissionaries >= cannibalsOnLeftBank) {
                successors.add(new State(remainingMissionaries, cannibalsOnLeftBank - cannibalsToMove, !boatOnLeftBank, state));
            }
        }

        // Try moving only cannibals (up to boat capacity)
        for (int cannibalsToMove = 1; cannibalsToMove <= Math.min(cannibalsOnLeftBank, BOAT_CAPACITY); cannibalsToMove++) {
            int remainingCannibals = cannibalsOnLeftBank - cannibalsToMove;
            successors.add(new State(missionariesOnLeftBank, remainingCannibals, !boatOnLeftBank, state));
        }

        return successors;
    }

    static class State {
        int missionariesLeft;
        int cannibalsLeft;
        boolean boatLeft; // True: boat on left bank, False: right bank
        State parent; // For tracing solution path

        public State(int missionariesLeft, int cannibalsLeft, boolean boatLeft) {
            this.missionariesLeft = missionariesLeft;
            this.cannibalsLeft = cannibalsLeft;
            this.boatLeft = boatLeft;
            this.parent = null;
        }

        public State(int missionariesLeft, int cannibalsLeft, boolean boatLeft, State parent) {
            this.missionariesLeft = missionariesLeft;
            this.cannibalsLeft = cannibalsLeft;
            this.boatLeft = boatLeft;
            this.parent = parent;
        }

        public boolean isGoal() {
            return missionariesLeft == 0 && cannibalsLeft == 0 && !boatLeft;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("Left Bank: ");
            sb.append(missionariesLeft).append(" missionaries, ");
            sb.append(cannibalsLeft).append(" cannibals, ");
            sb.append(boatLeft ? "Boat" : "No Boat");
            return sb.toString();
        }
    }
}
