
import java.util.*;

class JugState implements Comparable<JugState> {
    int jug1, jug2, cost, heuristic;

    JugState(int jug1, int jug2, int cost, int heuristic) {
        this.jug1 = jug1;
        this.jug2 = jug2;
        this.cost = cost;
        this.heuristic = heuristic;
    }

    int getTotalCost() {
        return cost + heuristic;
    }

    @Override
    public int compareTo(JugState other) {
        return Integer.compare(this.getTotalCost(), other.getTotalCost());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JugState jugState = (JugState) o;
        return jug1 == jugState.jug1 && jug2 == jugState.jug2;
    }

    @Override
    public int hashCode() {
        return Objects.hash(jug1, jug2);
    }
}

public class atar_waterjug {
    private static int heuristic(int jug1, int jug2, int target) {
        return Math.abs(jug1 - target) + Math.abs(jug2 - target);
    }

    private static List<String> getPath(Map<JugState, JugState> cameFrom, JugState end) {
        List<String> path = new ArrayList<>();
        JugState current = end;
        while (cameFrom.containsKey(current)) {
            JugState prev = cameFrom.get(current);
            if (prev.jug1 != current.jug1) {
                if (current.jug1 == 0) path.add("Empty jug1");
                else path.add("Fill jug1");
            } else if (prev.jug2 != current.jug2) {
                if (current.jug2 == 0) path.add("Empty jug2");
                else path.add("Fill jug2");
            } else if (current.jug1 > prev.jug1) path.add("Pour from jug2 to jug1");
            else path.add("Pour from jug1 to jug2");
            current = prev;
        }
        Collections.reverse(path);
        return path;
    }

    public static List<String> solveWaterJug(int jug1Capacity, int jug2Capacity, int target) {
        PriorityQueue<JugState> openSet = new PriorityQueue<>();
        Map<JugState, JugState> cameFrom = new HashMap<>();
        Map<JugState, Integer> gScore = new HashMap<>();
        JugState start = new JugState(0, 0, 0, heuristic(0, 0, target));

        openSet.add(start);
        gScore.put(start, 0);

        while (!openSet.isEmpty()) {
            JugState current = openSet.poll();

            if (current.jug1 == target || current.jug2 == target) {
                return getPath(cameFrom, current);
            }

            List<JugState> neighbors = getNeighbors(current, jug1Capacity, jug2Capacity, target);
            for (JugState neighbor : neighbors) {
                int tentativeGScore = gScore.get(current) + 1;
                if (tentativeGScore < gScore.getOrDefault(neighbor, Integer.MAX_VALUE)) {
                    cameFrom.put(neighbor, current);
                    gScore.put(neighbor, tentativeGScore);
                    neighbor.cost = tentativeGScore;
                    neighbor.heuristic = heuristic(neighbor.jug1, neighbor.jug2, target);
                    if (!openSet.contains(neighbor)) {
                        openSet.add(neighbor);
                    }
                }
            }
        }

        return Collections.emptyList(); // No solution found
    }

    private static List<JugState> getNeighbors(JugState state, int jug1Capacity, int jug2Capacity, int target) {
        List<JugState> neighbors = new ArrayList<>();
        int jug1 = state.jug1;
        int jug2 = state.jug2;

        // Fill jug1
        neighbors.add(new JugState(jug1Capacity, jug2, state.cost, 0));
        // Fill jug2
        neighbors.add(new JugState(jug1, jug2Capacity, state.cost, 0));
        // Empty jug1
        neighbors.add(new JugState(0, jug2, state.cost, 0));
        // Empty jug2
        neighbors.add(new JugState(jug1, 0, state.cost, 0));
        // Pour jug1 -> jug2
        int pour1to2 = Math.min(jug1, jug2Capacity - jug2);
        neighbors.add(new JugState(jug1 - pour1to2, jug2 + pour1to2, state.cost, 0));
        // Pour jug2 -> jug1
        int pour2to1 = Math.min(jug2, jug1Capacity - jug1);
        neighbors.add(new JugState(jug1 + pour2to1, jug2 - pour2to1, state.cost, 0));

        return neighbors;
    }

    public static void main(String[] args) {
        int jug1Capacity = 5;
        int jug2Capacity = 7;
        int target = 6;

        List<String> solution = solveWaterJug(jug1Capacity, jug2Capacity, target);
        if (solution.isEmpty()) {
            System.out.println("No solution found.");
        } else {
            for (String step : solution) {
                System.out.println(step);
            }
        }
    }
}

