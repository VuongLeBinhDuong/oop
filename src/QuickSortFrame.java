

import java.awt.EventQueue;

public class QuickSortFrame extends SortFrame {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public QuickSortFrame() {
        super("Quick Sort Algorithm Visualizer");
        initializeButtonPanel();
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
                setVisible(false);
                new MainMenu();
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