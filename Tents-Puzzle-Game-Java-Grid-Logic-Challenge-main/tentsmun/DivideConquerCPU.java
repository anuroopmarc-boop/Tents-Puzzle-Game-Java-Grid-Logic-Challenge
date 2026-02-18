import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class DivideConquerCPU {

    public static boolean solve(GameState state) {
        List<Point> trees = state.getTrees();
        if (trees.isEmpty())
            return true;

        List<List<Point>> regions = divideIntoRegions(state, trees);

        boolean allSolved = true;
        for (List<Point> region : regions) {
            if (!solveRegion(state, region)) {
                allSolved = false;
            }
        }

        return allSolved && state.isPuzzleComplete();
    }

    private static List<List<Point>> divideIntoRegions(GameState state, List<Point> trees) {
        int tSize = trees.size();
        boolean[][] adj = new boolean[tSize][tSize];

        List<List<Point>> treeValidSpots = new ArrayList<>();
        for (Point tree : trees) {
            treeValidSpots.add(getValidTentSpots(state, tree));
        }

        for (int i = 0; i < tSize; i++) {
            for (int j = i + 1; j < tSize; j++) {
                if (areTreesDependent(treeValidSpots.get(i), treeValidSpots.get(j))) {
                    adj[i][j] = adj[j][i] = true;
                }
            }
        }

        List<List<Point>> regions = new ArrayList<>();
        boolean[] visited = new boolean[tSize];
        for (int i = 0; i < tSize; i++) {
            if (!visited[i]) {
                List<Point> region = new ArrayList<>();
                List<Integer> queue = new ArrayList<>();
                queue.add(i);
                visited[i] = true;
                int head = 0;
                while (head < queue.size()) {
                    int curr = queue.get(head++);
                    region.add(trees.get(curr));
                    for (int next = 0; next < tSize; next++) {
                        if (adj[curr][next] && !visited[next]) {
                            visited[next] = true;
                            queue.add(next);
                        }
                    }
                }
                regions.add(region);
            }
        }
        return regions;
    }

    private static boolean areTreesDependent(List<Point> spots1, List<Point> spots2) {
        for (Point p1 : spots1) {
            for (Point p2 : spots2) {

                if (p1.equals(p2))
                    return true;

                if (Math.abs(p1.x - p2.x) <= 1 && Math.abs(p1.y - p2.y) <= 1)
                    return true;
            }
        }
        return false;
    }

    private static boolean solveRegion(GameState state, List<Point> region) {
        List<Point> remainingTrees = new ArrayList<>(region);

        while (!remainingTrees.isEmpty()) {

            remainingTrees.removeIf(tree -> isTreeSatisfied(state, tree));
            if (remainingTrees.isEmpty())
                break;

            List<List<Point>> allValidSpots = new ArrayList<>();
            for (Point tree : remainingTrees) {
                allValidSpots.add(getValidTentSpots(state, tree));
            }

            quickSort(remainingTrees, allValidSpots, 0, remainingTrees.size() - 1);

            Point mostConstrainedTree = remainingTrees.get(0);
            List<Point> validSpots = allValidSpots.get(0);

            if (validSpots.size() == 1) {

                Point spot = validSpots.get(0);
                state.placeTent(spot.x, spot.y);

                remainingTrees.remove(0);
            } else {

                return false;
            }
        }
        return true;
    }

    private static void quickSort(List<Point> trees, List<List<Point>> spots, int low, int high) {
        if (low < high) {
            int pi = partition(trees, spots, low, high);
            quickSort(trees, spots, low, pi - 1);
            quickSort(trees, spots, pi + 1, high);
        }
    }

    private static int partition(List<Point> trees, List<List<Point>> spots, int low, int high) {
        int pivot = spots.get(high).size();
        int i = (low - 1);
        for (int j = low; j < high; j++) {
            if (spots.get(j).size() < pivot) {
                i++;
                swap(trees, spots, i, j);
            }
        }
        swap(trees, spots, i + 1, high);
        return i + 1;
    }

    private static void swap(List<Point> trees, List<List<Point>> spots, int i, int j) {
        Point tempTree = trees.get(i);
        trees.set(i, trees.get(j));
        trees.set(j, tempTree);

        List<Point> tempSpots = spots.get(i);
        spots.set(i, spots.get(j));
        spots.set(j, tempSpots);
    }

    private static boolean isTreeSatisfied(GameState state, Point tree) {
        int[][] dirs = { { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 } };
        for (int[] d : dirs) {
            int nr = tree.x + d[0];
            int nc = tree.y + d[1];
            if (state.inBounds(nr, nc) && state.getCell(nr, nc) == GameState.TENT) {
                return true;
            }
        }
        return false;
    }

    private static List<Point> getValidTentSpots(GameState state, Point tree) {
        List<Point> spots = new ArrayList<>();
        int[][] dirs = { { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 } };
        for (int[] d : dirs) {
            int nr = tree.x + d[0];
            int nc = tree.y + d[1];
            if (state.inBounds(nr, nc) && state.getCell(nr, nc) == GameState.EMPTY) {
                if (isLegalPlacement(state, nr, nc)) {
                    spots.add(new Point(nr, nc));
                }
            }
        }
        return spots;
    }

    public static boolean makeMove(GameState state) {

        List<Point> trees = state.getTrees();
        List<List<Point>> regions = divideIntoRegions(state, trees);

        for (List<Point> region : regions) {
            List<Point> remainingTrees = new ArrayList<>(region);
            remainingTrees.removeIf(tree -> isTreeSatisfied(state, tree));
            if (remainingTrees.isEmpty())
                continue;

            List<List<Point>> allValidSpots = new ArrayList<>();
            for (Point tree : remainingTrees) {
                allValidSpots.add(getValidTentSpots(state, tree));
            }

            quickSort(remainingTrees, allValidSpots, 0, remainingTrees.size() - 1);

            if (!allValidSpots.isEmpty() && allValidSpots.get(0).size() == 1) {
                Point spot = allValidSpots.get(0).get(0);
                state.placeTent(spot.x, spot.y);
                return true;
            }
        }
        return false;
    }

    private static boolean isLegalPlacement(GameState state, int r, int c) {
        if (!state.inBounds(r, c))
            return false;
        if (state.getCell(r, c) != GameState.EMPTY)
            return false;

        // Adjacency check (tents cannot touch)
        for (int dr = -1; dr <= 1; dr++) {
            for (int dc = -1; dc <= 1; dc++) {
                if (dr == 0 && dc == 0)
                    continue;
                int nr = r + dr;
                int nc = c + dc;
                if (state.inBounds(nr, nc) && state.getCell(nr, nc) == GameState.TENT) {
                    return false;
                }
            }
        }

        // Row/Col limits check
        if (state.getRowUsed(r) >= state.getRowTarget(r))
            return false;
        if (state.getColUsed(c) >= state.getColTarget(c))
            return false;

        // Must be next to some tree
        boolean hasTree = false;
        int[][] dirs = { { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 } };
        for (int[] d : dirs) {
            int nr = r + d[0];
            int nc = c + d[1];
            if (state.inBounds(nr, nc) && state.getCell(nr, nc) == GameState.TREE) {
                hasTree = true;
                break;
            }
        }
        return hasTree;
    }

}
