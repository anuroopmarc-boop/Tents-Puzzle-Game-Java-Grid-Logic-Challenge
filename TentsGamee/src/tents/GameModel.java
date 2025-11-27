package tents;
public class GameModel {
    int size;
    CellType[][] grid;

    public GameModel(int size) {
        this.size = size;
        grid = new CellType[size][size];
        reset();
    }

    public void reset() {
        for (int r = 0; r < size; r++)
            for (int c = 0; c < size; c++)
                grid[r][c] = CellType.EMPTY;

        // Example tree placements
        grid[1][2] = CellType.TREE;
        grid[3][4] = CellType.TREE;
        grid[5][1] = CellType.TREE;
    }

    public void toggleTent(int r, int c) {
        if (grid[r][c] == CellType.EMPTY)
            grid[r][c] = CellType.TENT;
        else if (grid[r][c] == CellType.TENT)
            grid[r][c] = CellType.EMPTY;
    }

    public boolean isValidTent(int r, int c) {
        if (r < 0 || c < 0 || r >= size || c >= size)
            return false;

        return grid[r][c] == CellType.EMPTY;
    }
}
