
import java.util.*;

class best_search_missionary {

    static class State implements Comparable<State> {
        int missionariesLeft, cannibalsLeft, boat; // boat=0 means boat is on the left bank, boat=1 means boat is on the right bank
        int depth;
        State parent;

        public State(int missionariesLeft, int cannibalsLeft, int boat, int depth, State parent) {
            this.missionariesLeft = missionariesLeft;
            this.cannibalsLeft = cannibalsLeft;
            this.boat = boat;
            this.depth = depth;
            this.parent = parent;
        }

        @Override
        public int compareTo(State other) {
            return Integer.compare(this.depth + heuristic(this), other.depth + heuristic(other));
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            State state = (State) obj;
            return missionariesLeft == state.missionariesLeft && cannibalsLeft == state.cannibalsLeft && boat == state.boat;
        }

        @Override
        public int hashCode() {
            return Objects.hash(missionariesLeft, cannibalsLeft, boat);
        }
    }

    static int heuristic(State state) {
        return state.missionariesLeft + state.cannibalsLeft;
    }

    static boolean isValidState(State state) {
        int missionariesRight = 3 - state.missionariesLeft;
        int cannibalsRight = 3 - state.cannibalsLeft;

        if (state.missionariesLeft < 0 || state.missionariesLeft > 3 || state.cannibalsLeft < 0 || state.cannibalsLeft > 3)
            return false;
        if (state.missionariesLeft > 0 && state.missionariesLeft < state.cannibalsLeft)
            return false;
        if (missionariesRight > 0 && missionariesRight < cannibalsRight)
            return false;
        return true;
    }

    static boolean isGoalState(State state) {
        return state.missionariesLeft == 0 && state.cannibalsLeft == 0 && state.boat == 1;
    }

    static List<State> generateSuccessors(State state) {
        List<State> successors = new ArrayList<>();
        int direction = state.boat == 0 ? -1 : 1; // If boat is on the left bank, it goes right, and vice versa

        // Try all possible moves: (1,0), (2,0), (1,1), (0,1), (0,2)
        int[][] moves = {{1, 0}, {2, 0}, {1, 1}, {0, 1}, {0, 2}};

        for (int[] move : moves) {
            State newState = new State(
                state.missionariesLeft + direction * move[0],
                state.cannibalsLeft + direction * move[1],
                1 - state.boat,
                state.depth + 1,
                state
            );
            if (isValidState(newState)) {
                successors.add(newState);
            }
        }

        return successors;
    }

    static void printSolution(State state) {
        Stack<State> path = new Stack<>();
        while (state != null) {
            path.push(state);
            state = state.parent;
        }
        while (!path.isEmpty()) {
            State current = path.pop();
            System.out.printf("Left Bank: %dM %dC, Boat: %s, Right Bank: %dM %dC\n",
                current.missionariesLeft, current.cannibalsLeft,
                current.boat == 0 ? "Left" : "Right",
                3 - current.missionariesLeft, 3 - current.cannibalsLeft);
        }
    }

    static void bestFirstSearch() {
        PriorityQueue<State> priorityQueue = new PriorityQueue<>();
        Set<State> visited = new HashSet<>();
        State initialState = new State(3, 3, 0, 0, null);
        priorityQueue.add(initialState);
        visited.add(initialState);

        while (!priorityQueue.isEmpty()) {
            State currentState = priorityQueue.poll();

            if (isGoalState(currentState)) {
                System.out.println("Solution found:");
                printSolution(currentState);
                return;
            }

            for (State successor : generateSuccessors(currentState)) {
                if (!visited.contains(successor)) {
                    priorityQueue.add(successor);
                    visited.add(successor);
                }
            }
        }

        System.out.println("No solution found.");
    }

    public static void main(String[] args) {
        bestFirstSearch();
    }
}
