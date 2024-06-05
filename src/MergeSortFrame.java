

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

    class RunThread extends Thread {
        public void run() {
            try {
                visualizer.mergeSort();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    class StopThread extends Thread {
        public void run() {
            visualizer.pauseSelection();
        }
    }

    class ContinueThread extends Thread {
        public void run() {
            visualizer.resumeSelection();
        }
    }

    @Override
    public void sortButtonClicked(int id) {
        switch (id) {
            case 0:  // create button
                visualizer.createRandomArray(canvas.getWidth(), canvas.getHeight());
                break;
            case 1:  // sort button
                RunThread runThread = new RunThread();
                runThread.start();
                break;
            case 2:  // back button
                new MainMenu();
                break;
            case 3:  // stop button
                StopThread stopThread = new StopThread();
                stopThread.start();
                break;
            case 4:  // continue button
                ContinueThread continueThread = new ContinueThread();
                continueThread.start();
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