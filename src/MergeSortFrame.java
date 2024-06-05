

import java.awt.EventQueue;

public class MergeSortFrame extends SortFrame {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MergeSortFrame() {
        super("Merge Sort Algorithm Visualizer");
        initializeButtonPanel();
    }

//    @Override
//    protected void initializeButtonPanel() {
//        buttonPanel = new ButtonPanel(this, "merge");
//        buttonPanel.setBounds(0, 150, 250, HEIGHT);
//        buttonPanel.setBackground(ColorManager.BACKGROUND);
//        mainPanel.add(buttonPanel);
//    }

    @Override
    public void sortButtonClicked(int id) {
        switch (id) {
            case 0:  // create button
                visualizer.createRandomArray(canvas.getWidth(), canvas.getHeight());
                break;
            case 1:  // sort button
                visualizer.mergeSort();
                break;
            case 2: // back button
                break;
        }
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MergeSortFrame().setVisible(true);
            }
        });
    }
}