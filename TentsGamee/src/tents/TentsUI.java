package tents;
import javax.swing.*;
import java.awt.*;

public class TentsUI extends JFrame {
    private GameModel model;
    private GreedySolver solver;

    public TentsUI() {
        setTitle("Tents Puzzle Game");
        setSize(700, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        model = new GameModel(8);   // 8x8 grid
        solver = new GreedySolver(model);

        add(new GridPanel(model));
        add(createBottomPanel(), BorderLayout.SOUTH);
    }

    private JPanel createBottomPanel() {
        JPanel panel = new JPanel();

        JButton solveBtn = new JButton("Solve (Greedy)");
        solveBtn.addActionListener(e -> {
            solver.solve();
            repaint();
        });

        JButton resetBtn = new JButton("Reset");
        resetBtn.addActionListener(e -> {
            model.reset();
            repaint();
        });

        panel.add(solveBtn);
        panel.add(resetBtn);

        return panel;
    }
}
