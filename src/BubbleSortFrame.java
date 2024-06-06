import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.List;

public class BubbleSortFrame extends SortFrame {

    private static final long serialVersionUID = 1L;

    class RunThread extends Thread {
        public void run() {
            try {
                visualizer.bubbleSort();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    class StopThread extends Thread {
        public void run() {
            visualizer.stopBubbleFlag = 1;
        }
    }

    class ContinueThread extends Thread {
        public void run() {
            visualizer.stopBubbleFlag = 0;
            visualizer.resume();
        }
    }

    public BubbleSortFrame() {
        super("Bubble Sort Algorithm Visualizer");
        initializeButtonPanel();
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

        }
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                new BubbleSortFrame().setVisible(true);
            }
        });
    }
}