import java.util.*;

class State implements Comparable<State> {
    int missionariesLeft, cannibalsLeft, boat;
    int cost, heuristic;

    State(int missionariesLeft, int cannibalsLeft, int boat, int cost, int heuristic) {
        this.missionariesLeft = missionariesLeft;
        this.cannibalsLeft = cannibalsLeft;
        this.boat = boat;
        this.cost = cost;
        this.heuristic = heuristic;
    }

    int getTotalCost() {
        return cost + heuristic;
    }

    @Override
    public int compareTo(State other) {
        return Integer.compare(this.getTotalCost(), other.getTotalCost());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        State state = (State) o;
        return missionariesLeft == state.missionariesLeft &&
               cannibalsLeft == state.cannibalsLeft &&
               boat == state.boat;
    }

    @Override
    public int hashCode() {
        return Objects.hash(missionariesLeft, cannibalsLeft, boat);
    }
}

public class astar_missionary {
    private static int heuristic(int missionariesLeft, int cannibalsLeft) {
        return missionariesLeft + cannibalsLeft;
    }

    private static List<String> getPath(Map<State, State> cameFrom, State end) {
        List<String> path = new ArrayList<>();
        State current = end;
        while (cameFrom.containsKey(current)) {
            State prev = cameFrom.get(current);
            String action = getAction(prev, current);
            path.add(action);
            current = prev;
        }
        Collections.reverse(path);
        return path;
    }

    private static String getAction(State prev, State current) {
        int deltaM = prev.missionariesLeft - current.missionariesLeft;
        int deltaC = prev.cannibalsLeft - current.cannibalsLeft;
        if (prev.boat == 1) {
            return "Move " + deltaM + " missionaries and " + deltaC + " cannibals to the right";
        } else {
            return "Move " + (-deltaM) + " missionaries and " + (-deltaC) + " cannibals to the left";
        }
    }

    public static List<String> solveMissionariesAndCannibals() {
        PriorityQueue<State> openSet = new PriorityQueue<>();
        Map<State, State> cameFrom = new HashMap<>();
        Map<State, Integer> gScore = new HashMap<>();
        State start = new State(3, 3, 1, 0, heuristic(3, 3));

        openSet.add(start);
        gScore.put(start, 0);

        while (!openSet.isEmpty()) {
            State current = openSet.poll();

            if (isGoalState(current)) {
                return getPath(cameFrom, current);
            }

            List<State> neighbors = getNeighbors(current);
            for (State neighbor : neighbors) {
                int tentativeGScore = gScore.get(current) + 1;
                if (tentativeGScore < gScore.getOrDefault(neighbor, Integer.MAX_VALUE)) {
                    cameFrom.put(neighbor, current);
                    gScore.put(neighbor, tentativeGScore);
                    neighbor.cost = tentativeGScore;
                    neighbor.heuristic = heuristic(neighbor.missionariesLeft, neighbor.cannibalsLeft);
                    if (!openSet.contains(neighbor)) {
                        openSet.add(neighbor);
                    }
                }
            }
        }

        return Collections.emptyList(); // No solution found
    }

    private static boolean isGoalState(State state) {
        return state.missionariesLeft == 0 && state.cannibalsLeft == 0 && state.boat == 0;
    }

    private static List<State> getNeighbors(State state) {
        List<State> neighbors = new ArrayList<>();
        int[][] moves = {{1, 0}, {2, 0}, {0, 1}, {0, 2}, {1, 1}};

        for (int[] move : moves) {
            int m = move[0];
            int c = move[1];

            if (state.boat == 1) { // Boat on the left side
                int newML = state.missionariesLeft - m;
                int newCL = state.cannibalsLeft - c;
                if (isValidState(newML, newCL, newML, newCL)) {
                    neighbors.add(new State(newML, newCL, 0, state.cost, 0));
                }
            } else { // Boat on the right side
                int newML = state.missionariesLeft + m;
                int newCL = state.cannibalsLeft + c;
                if (isValidState(newML, newCL, newML, newCL)) {
                    neighbors.add(new State(newML, newCL, 1, state.cost, 0));
                }
            }
        }

        return neighbors;
    }

    private static boolean isValidState(int missionariesLeft, int cannibalsLeft, int missionariesRight, int cannibalsRight) {
        if (missionariesLeft < 0 || cannibalsLeft < 0 || missionariesRight < 0 || cannibalsRight < 0 || 
            missionariesLeft > 3 || cannibalsLeft > 3 || missionariesRight > 3 || cannibalsRight > 3) {
            return false;
        }
        if (missionariesLeft > 0 && missionariesLeft < cannibalsLeft) {
            return false;
        }
        if (missionariesRight > 0 && missionariesRight < cannibalsRight) {
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        List<String> solution = solveMissionariesAndCannibals();
        if (solution.isEmpty()) {
            System.out.println("No solution found.");
        } else {
            for (String step : solution) {
                System.out.println(step);
            }
        }
    }
}
