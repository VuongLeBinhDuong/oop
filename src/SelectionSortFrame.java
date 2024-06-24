

import java.awt.EventQueue;

public class SelectionSortFrame extends SortFrame {

    public SelectionSortFrame() {
        super("Selection Sort Algorithm Visualizer");
        initializeButtonPanel();
    }


    class RunThread extends Thread {
        public void run() {
            try {
                visualizer.selectionSort();
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
        boolean isCheck = isChecked();
        switch (id) {
            case 0:  // create button
                if (isCheck){
                    visualizer.createRandomArray(canvas.getWidth(), canvas.getHeight());
                }
                else {
                    visualizer.createRandomArrayDuplicates(canvas.getWidth(), canvas.getHeight());
                }
                break;
            case 1:  // sort button
                RunThread runThread = new RunThread();
                runThread.start();
                break;
            case 2:  // back button
                getContentPane().removeAll();
                getContentPane().repaint();
                setVisible(false);
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
            case 5:
                visualizer.enterArrayManually(canvas.getWidth(), canvas.getHeight());
                break;
            case 6:
                try {
                    visualizer.displaySortStatistics();  // Show the statistics of different sorts
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                new SelectionSortFrame().setVisible(true);
            }
        });
    }
}