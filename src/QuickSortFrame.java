

import java.awt.EventQueue;

public class QuickSortFrame extends SortFrame {

    public QuickSortFrame() {
        super("Quick Sort Algorithm Visualizer");
        initializeButtonPanel();
    }

    @Override
    protected void initializeButtonPanel() {
        buttonPanel = new ButtonPanel(this, "quick");
        buttonPanel.setBounds(0, 150, 250, HEIGHT);
        buttonPanel.setBackground(ColorManager.BACKGROUND);
        mainPanel.add(buttonPanel);
    }

    @Override
    public void sortButtonClicked(int id) {
        switch (id) {
            case 0:  // create button
                visualizer.createRandomArray(canvas.getWidth(), canvas.getHeight());
                break;
            case 1:  // sort button
                visualizer.quickSort();
                break;
            case 2: // back button
                getContentPane().removeAll();
                getContentPane().repaint();
                new MainMenu();
                setVisible(false);
                break;
        }
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                new QuickSortFrame().setVisible(true);
            }
        });
    }
}