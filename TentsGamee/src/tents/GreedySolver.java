package tents;
public class GreedySolver {
    private GameModel model;

    public GreedySolver(GameModel model) {
        this.model = model;
    }

    public void solve() {
        for (int r = 0; r < model.size; r++) {
            for (int c = 0; c < model.size; c++) {
                if (model.grid[r][c] == CellType.TREE) {
                    placeGreedyTent(r, c);
                }
            }
        }
    }

    private void placeGreedyTent(int r, int c) {
        int[][] dirs = { {1,0}, {-1,0}, {0,1}, {0,-1} };

        for (int[] d : dirs) {
            int nr = r + d[0];
            int nc = c + d[1];

            if (model.isValidTent(nr, nc)) {
                model.grid[nr][nc] = CellType.TENT;
                return;
            }
        }
    }
}
