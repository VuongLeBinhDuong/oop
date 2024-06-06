
import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.List;

public class BubbleSortFrame extends SortFrame {

	private List<Thread> threads;

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
			visualizer.stopFlag = 1;
		}
	}
	
	class ContinueThread extends Thread {
		public void run() {
			visualizer.stopFlag = 0;
			visualizer.resume();
			
		}
	}
	
	public void interruptAllThreads() {
        for (Thread thread : threads) {
            
                thread.interrupt();
            
        }
    }

	public BubbleSortFrame() {
        super("Bubble Sort Algorithm Visualizer");
        threads = new ArrayList<>();
        initializeButtonPanel();
    }

    @Override
    public void sortButtonClicked(int id) {
        switch (id) {
            case 0:  // create button
                visualizer.createRandomArray(canvas.getWidth(), canvas.getHeight());
                break;
            case 1:  // sort button
            	interruptAllThreads();
                RunThread runThread = new RunThread();
                runThread.start();
                threads.add(runThread);
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