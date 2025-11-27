package tents;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GridPanel extends JPanel {
    private GameModel model;
    private int cellSize = 60;

    public GridPanel(GameModel model) {
        this.model = model;

        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                int row = e.getY() / cellSize;
                int col = e.getX() / cellSize;

                model.toggleTent(row, col);
                repaint();
            }
        });

        setPreferredSize(new Dimension(model.size * cellSize, model.size * cellSize));
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (int r = 0; r < model.size; r++) {
            for (int c = 0; c < model.size; c++) {

                g.drawRect(c * cellSize, r * cellSize, cellSize, cellSize);

                if (model.grid[r][c] == CellType.TREE) {
                    g.setColor(Color.GREEN);
                    g.fillOval(c * cellSize + 15, r * cellSize + 15, 30, 30);
                    g.setColor(Color.BLACK);
                }

                if (model.grid[r][c] == CellType.TENT) {
                    g.setColor(Color.ORANGE);
                    g.fillRect(c * cellSize + 10, r * cellSize + 10, 40, 40);
                    g.setColor(Color.BLACK);
                }
            }
        }
    }
}
