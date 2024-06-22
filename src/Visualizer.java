
import javax.swing.JOptionPane;
import java.awt.image.BufferStrategy;
import java.awt.Color;
import java.awt.Graphics;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class Visualizer
{
	private static final int PADDING = 10;
	private static final int MAX_BAR_HEIGHT = 500, MIN_BAR_HEIGHT = 30;
	private Integer[] array;
	private int capacity, speed;
	private Bar[] bars;
	private boolean hasArray;

	// statistic
	private long startTime, time;
	private int comp, swapping;

	private Color originalColor, swappingColor, comparingColor;

	private BufferStrategy bs;
	private Graphics g;

	private SortedListener listener;

	protected int stopBubbleFlag = 0;
	protected int stopInsertionFlag = 0;

	private volatile boolean pausedSelection = false;
	private final Object lockSelection = new Object();


	public Visualizer(int capacity, int fps, SortedListener listener)
	{
		this.capacity = capacity;
		this.speed = (int) (1000.0/fps);
		this.listener = listener;
		startTime = time = comp = swapping = 0;

		originalColor = ColorManager.BAR_GREEN;
		comparingColor = Color.YELLOW;
		swappingColor = ColorManager.BAR_RED;

		bs = listener.getBufferStrategy();

		hasArray = false;
	}

	public void enterArrayManually(int canvasWidth, int canvasHeight) {
		array = new Integer[capacity];
		bars = new Bar[capacity];
		hasArray = true;

		// Initial position
		double x = PADDING;
		int y = canvasHeight - PADDING;

		// Width of all bars
		double width = (double) (canvasWidth - PADDING * 2) / capacity;

		// Prompt the user to enter values
		String input = JOptionPane.showInputDialog(null,
				"Enter " + capacity + " integers separated by spaces:",
				"Enter Array",
				JOptionPane.PLAIN_MESSAGE);

		// If the user cancels or inputs nothing, exit the function
		if (input == null || input.trim().isEmpty()) {
			JOptionPane.showMessageDialog(null,
					"No input provided. Operation canceled.",
					"Input Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		// Split the input string into individual numbers
		StringTokenizer tokenizer = new StringTokenizer(input);
		if (tokenizer.countTokens() != capacity) {
			JOptionPane.showMessageDialog(null,
					"Please enter exactly " + capacity + " integers.",
					"Input Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		// Get graphics
		g = bs.getDrawGraphics();
		g.setColor(ColorManager.CANVAS_BACKGROUND);
		g.fillRect(0, 0, canvasWidth, canvasHeight);

		// Parse the input values
		try {
			for (int i = 0; i < capacity; i++) {
				int value = Integer.parseInt(tokenizer.nextToken().trim());

				// Ensure the value is within the valid range
				if (value < MIN_BAR_HEIGHT || value > MAX_BAR_HEIGHT) {
					throw new NumberFormatException("Value out of range.");
				}

				array[i] = value;

				Bar bar = new Bar((int) x, y, (int) width, value, originalColor);
				bar.draw(g);
				bars[i] = bar;

				// Move to the next bar
				x += width;
			}
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(null,
					"Invalid input. Please enter integers between " +
							MIN_BAR_HEIGHT + " and " + MAX_BAR_HEIGHT + ".",
					"Input Error",
					JOptionPane.ERROR_MESSAGE);
			hasArray = false;
			return;
		}

		bs.show();
		g.dispose();
	}

	public void displaySortStatistics() throws InterruptedException {
		if (!isCreated()) return;

		// Arrays to hold the statistics
		long[] sortTimes = new long[6];
		int[] comparisons = new int[6];
		int[] swaps = new int[6];
		String[] sortNames = {"Bubble Sort", "Selection Sort", "Insertion Sort", "Quick Sort", "Merge Sort", "Shell Sort"};

		// Perform each sort and collect statistics
		for (int i = 0; i < 6; i++) {
			// Make a deep copy of the original array
			Integer[] tempArray = array.clone();
			// Reset counters
			comp = swapping = 0;

			// Choose the sorting method
			switch (i) {
				case 0:
					startTime = System.nanoTime();
					bubbleSortInternal(tempArray);
					sortTimes[i] = System.nanoTime() - startTime;
					break;
				case 1:
					startTime = System.nanoTime();
					selectionSortInternal(tempArray);
					sortTimes[i] = System.nanoTime() - startTime;
					break;
				case 2:
					startTime = System.nanoTime();
					insertionSortInternal(tempArray);
					sortTimes[i] = System.nanoTime() - startTime;
					break;
				case 3:
					startTime = System.nanoTime();
					quickSortInternal(tempArray);
					sortTimes[i] = System.nanoTime() - startTime;
					break;
				case 4:
					startTime = System.nanoTime();
					mergeSortInternal(tempArray);
					sortTimes[i] = System.nanoTime() - startTime;
					break;
				case 5:
					startTime = System.nanoTime();
					shellSortInternal(tempArray);
					sortTimes[i] = System.nanoTime() - startTime;
					break;
			}

			// Record comparisons and swaps
			comparisons[i] = comp;
			swaps[i] = swapping;
		}

		StringBuilder result = new StringBuilder();
		result.append("<html><body><h2>Sorting Statistics</h2>");
		result.append("<table border=\"1\" cellspacing=\"0\" cellpadding=\"5\">");
		result.append("<tr><th>Sort Algorithm</th><th>Time (µs)</th><th>Comparisons</th><th>Swaps</th></tr>");

		for (int i = 0; i < 6; i++) {
			// Convert nanoTime to microseconds (10^-6 seconds)
			long timeMicros = TimeUnit.NANOSECONDS.toMicros(sortTimes[i]);
			result.append(String.format("<tr><td>%s</td><td>%d</td><td>%d</td><td>%d</td></tr>",
					sortNames[i],
					timeMicros,
					comparisons[i],
					swaps[i]));
		}

		result.append("</table></body></html>");

		// Display the results in a dialog box
		JOptionPane.showMessageDialog(null, result.toString(), "Sort Statistics", JOptionPane.INFORMATION_MESSAGE);
	}


	private void bubbleSortInternal(Integer[] array) {
		// Bubble sort implementation for statistics collection
		for (int i = array.length - 1; i >= 0; i--) {
			int count = 0;
			for (int j = 0; j < i; j++) {
				if (array[j] > array[j + 1]) {
					// Swap elements
					int temp = array[j];
					array[j] = array[j + 1];
					array[j + 1] = temp;
					count++;
					swapping++;
				}
				comp++;
			}
			if (count == 0)  // the array is sorted
				break;
		}
	}

	private void selectionSortInternal(Integer[] array) {
		// Selection sort implementation for statistics collection
		for (int i = array.length - 1; i >= 0; i--) {
			int max = array[i], index = i;
			for (int j = 0; j <= i; j++) {
				if (max < array[j]) {
					max = array[j];
					index = j;
				}
				comp++;
			}
			int temp = array[i];
			array[i] = array[index];
			array[index] = temp;
			swapping++;
		}
	}

	private void insertionSortInternal(Integer[] array) {
		// Insertion sort implementation for statistics collection
		for (int i = 1; i < array.length; i++) {
			int index = i - 1, element = array[i];
			while (index >= 0 && element < array[index]) {
				array[index + 1] = array[index];
				index--;
				comp++;
				swapping++;
			}
			comp++;
			array[index + 1] = element;
		}
	}

	private void quickSortInternal(Integer[] array) throws InterruptedException {
		// Quick sort implementation for statistics collection
		quickSortInternal(array, 0, array.length - 1);
	}

	private void quickSortInternal(Integer[] array, int start, int end) {
		if (start < end) {
			int pivot = partition(array, start, end);
			quickSortInternal(array, start, pivot - 1);
			quickSortInternal(array, pivot + 1, end);
		}
	}

	private int partition(Integer[] array, int start, int end) {
		int pivot = array[end];
		int index = start - 1;
		for (int i = start; i < end; i++) {
			if (array[i] < pivot) {
				index++;
				int temp = array[index];
				array[index] = array[i];
				array[i] = temp;
				swapping++;
			}
			comp++;
		}
		int temp = array[index + 1];
		array[index + 1] = array[end];
		array[end] = temp;
		swapping++;
		return index + 1;
	}

	private void mergeSortInternal(Integer[] array) throws InterruptedException {
		// Merge sort implementation for statistics collection
		mergeSortInternal(array, 0, array.length - 1);
	}

	private void mergeSortInternal(Integer[] array, int left, int right) {
		if (left < right) {
			int middle = (left + right) / 2;
			mergeSortInternal(array, left, middle);
			mergeSortInternal(array, middle + 1, right);
			merge(array, left, middle, right);
		}
	}

	private void merge(Integer[] array, int left, int middle, int right) {
		int n1 = middle - left + 1;
		int n2 = right - middle;
		Integer[] leftArr = new Integer[n1];
		Integer[] rightArr = new Integer[n2];
		System.arraycopy(array, left, leftArr, 0, n1);
		System.arraycopy(array, middle + 1, rightArr, 0, n2);

		int l = 0, r = 0, k = left;
		while (l < n1 && r < n2) {
			if (leftArr[l] <= rightArr[r]) {
				array[k] = leftArr[l];
				l++;
			} else {
				array[k] = rightArr[r];
				r++;
			}
			k++;
			comp++;
			swapping++;
		}
		while (l < n1) {
			array[k] = leftArr[l];
			l++;
			k++;
			swapping++;
		}
		while (r < n2) {
			array[k] = rightArr[r];
			r++;
			k++;
			swapping++;
		}
	}

	private void shellSortInternal(Integer[] array) {
		// Shell sort implementation for statistics collection
		int n = array.length;
		for (int gap = n / 2; gap > 0; gap /= 2) {
			for (int i = gap; i < n; i++) {
				int temp = array[i];
				int j;
				for (j = i; j >= gap && array[j - gap] > temp; j -= gap) {
					array[j] = array[j - gap];
					swapping++;
					comp++;
				}
				array[j] = temp;
			}
		}
	}


	public void createRandomArray(int canvasWidth, int canvasHeight) {
        array = new Integer[capacity];
        bars = new Bar[capacity];
        hasArray = true;

        // initial position
        double x = PADDING;
        int y = canvasHeight - PADDING;

        // width of all bars
        double width = (double) (canvasWidth - PADDING * 2) / capacity;

        // get graphics
        g = bs.getDrawGraphics();
        g.setColor(ColorManager.CANVAS_BACKGROUND);
        g.fillRect(0, 0, canvasWidth, canvasHeight);

        // Kiểm tra nếu phạm vi giá trị nhỏ hơn số lượng phần tử cần tạo
        if (MAX_BAR_HEIGHT - MIN_BAR_HEIGHT + 1 < capacity) {
            JOptionPane.showMessageDialog(null, "Phạm vi giá trị không đủ lớn để tạo ra mảng với các giá trị khác nhau!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Tạo danh sách các giá trị từ MIN_BAR_HEIGHT đến MAX_BAR_HEIGHT
        List<Integer> values = new ArrayList<>();
        for (int i = MIN_BAR_HEIGHT; i <= MAX_BAR_HEIGHT; i++) {
            values.add(i);
        }

        // Shuffle để ngẫu nhiên hóa thứ tự các giá trị
        Collections.shuffle(values);

        // Chọn `capacity` giá trị đầu tiên từ danh sách đã xáo trộn
        for (int i = 0; i < capacity; i++) {
            array[i] = values.get(i);

            Bar bar = new Bar((int) x, y, (int) width, array[i], originalColor);
            bar.draw(g);
            bars[i] = bar;

            // move to the next bar
            x += width;
        }

        bs.show();
        g.dispose();
    }

    // Phương thức từ nhánh 'thaihung' nếu cần giữ lại logic tạo mảng với các giá trị trùng lặp
    public void createRandomArrayDuplicates(int canvasWidth, int canvasHeight) {
        array = new Integer[capacity];
        bars = new Bar[capacity];
        hasArray = true;

        // initial position
        double x = PADDING;
        int y = canvasHeight - PADDING;

        // width of all bars
        double width = (double) (canvasWidth - PADDING * 2) / capacity;

        // get graphics
        g = bs.getDrawGraphics();
        g.setColor(ColorManager.CANVAS_BACKGROUND);
        g.fillRect(0, 0, canvasWidth, canvasHeight);

        Random rand = new Random();
        for (int i = 0; i < capacity; i++) {
            int value = rand.nextInt(MAX_BAR_HEIGHT - MIN_BAR_HEIGHT + 1) + MIN_BAR_HEIGHT;
            array[i] = value;

            Bar bar = new Bar((int) x, y, (int) width, value, originalColor);
            bar.draw(g);
            bars[i] = bar;

            // move to the next bar
            x += width;
        }

        bs.show();
        g.dispose();
    }


	// return a color for a bar
	private Color getBarColor(int value)
	{
		return ColorManager.BAR_BLUE;
	}


	private void redrawBars() {
		for (int i = 0; i < bars.length; i++) {
			bars[i].draw(g);
		}
		bs.show();
	}

	private void checkPaused() throws InterruptedException {
		synchronized (lockSelection) {
			while (pausedSelection) {
				lockSelection.wait();
			}
		}
	}

	public void pauseSelection() {
		synchronized (lockSelection) {
			pausedSelection = true;
		}
	}

	public void resumeSelection() {
		synchronized (lockSelection) {
			pausedSelection = false;
			lockSelection.notifyAll();
		}
	}

	// handle stop buttons
	private synchronized void handlePause() throws InterruptedException {
		wait();
		redrawBars();  // Redraw bars immediately after resuming
	}

	// handle continue button
	public synchronized void resume() {
		notifyAll();
	}

	// swap 2 elements given 2 indexes
	private void swap(int i, int j)
	{
		// swap the elements
		int temp = array[j];
		array[j] = array[i];
		array[i] = temp;

		// clear the bar
		bars[i].clear(g);
		bars[j].clear(g);

		// swap the drawings
		bars[j].setValue(bars[i].getValue());
		bars[i].setValue(temp);

		colorPair(i, j, swappingColor);
	}


	private void colorPair(int i, int j, Color color)
	{
		Color color1 = bars[i].getColor(), color2 = bars[j].getColor();
		// drawing
		bars[i].setColor(color);
		bars[i].draw(g);

		bars[j].setColor(color);
		bars[j].draw(g);

		bs.show();

		// delay
		try {
			TimeUnit.MILLISECONDS.sleep(speed);
		} catch (Exception ex) {}

		// put back to original color
		bars[i].setColor(color1);
		bars[i].draw(g);

		bars[j].setColor(color2);
		bars[j].draw(g);

		bs.show();
	}


	// color the bar in speed time and put it
	// back to its original color
	private void colorBar(int index, Color color)
	{
		Bar bar = bars[index];
		Color oldColor = bar.getColor();

		bar.setColor(color);
		bar.draw(g);
		bs.show();

		try {
			TimeUnit.MILLISECONDS.sleep(speed);
		} catch (Exception ex) {}

		bar.setColor(oldColor);
		bar.draw(g);

		bs.show();
	}


	// swiping effect when the sorting is finished
	private void finishAnimation()
	{
		// swiping to green
		for (int i = 0; i < bars.length; i++)
		{
			colorBar(i, comparingColor);
			bars[i].setColor(getBarColor(i));
			bars[i].draw(g);
			bs.show();
		}

		// show elapsed time and comparisons
		listener.onArraySorted(time, comp, swapping);
	}


	// for restore purpose
	public void drawArray()
	{
		if (!hasArray)
			return;

		g = bs.getDrawGraphics();

		for (int i = 0; i < bars.length; i++)
		{
			bars[i].draw(g);
		}

		bs.show();
		g.dispose();
	}

	/* BUBBLE SORT */
	public void bubbleSort() throws InterruptedException {
		if (!isCreated()) return;

		// get graphics
		g = bs.getDrawGraphics();

		// calculate elapsed time
		startTime = System.nanoTime();
		Sort.bubbleSort(array.clone());
		time = System.nanoTime() - startTime;

		comp = swapping = 0;
		int count = 0;
		for (int i = array.length - 1; i >= 0; i--) {
			count = 0;
			for (int j = 0; j < i; j++) {
				colorPair(j, j + 1, comparingColor);

				if (array[j] > array[j + 1]) {
					swap(j, j + 1);
					count++;
					swapping++;
				}

				comp++;
				// Check for pause
				if (stopBubbleFlag == 1) {
					handlePause();
				}
			}

			bars[i].setColor(getBarColor(i));
			bars[i].draw(g);
			bs.show();

			if (count == 0)  // the array is sorted
				break;
		}
		finishAnimation();

		g.dispose();
	}


	/* SELECTION SORT */
	public void selectionSort() throws InterruptedException {
		if (!isCreated())
			return;

		// get graphics
		g = bs.getDrawGraphics();

		// calculate elapsed time
		startTime = System.nanoTime();
		Sort.selectionSort(array.clone());
		time = System.nanoTime() - startTime;

		comp = swapping = 0;
		for (int i = array.length-1; i >= 0; i--)
		{
			// find the max
			int max = array[i], index = i;
			for (int j = 0; j <= i; j++)
			{
				if (max < array[j])
				{
					max = array[j];
					index = j;
				}
				colorPair(index, j, comparingColor);
				comp++;
			}

			swap(i, index);
			swapping++;

			// Check for pause
			checkPaused();

			bars[i].setColor(getBarColor(i));
			bars[i].draw(g);
			bs.show();
		}

		finishAnimation();

		g.dispose();
	}



	/* INSERTION SORT */
	public void insertionSort() throws InterruptedException {
		if (!isCreated())
			return;

		// gett graphics
		g = bs.getDrawGraphics();

		// calculate elapsed time
		startTime = System.nanoTime();
		Sort.insertionSort(array.clone());
		time = System.nanoTime() - startTime;

		comp = swapping = 0;

		Bar bar;
		for (int i = 1; i < array.length; i++)
		{
			bars[i].setColor(getBarColor(i));

			// find the insertion location by comparing to its predecessor
			int index = i-1, element = array[i];
			while (index >= 0 && element < array[index])
			{
				array[index+1] = array[index];

				bar = bars[index+1];
				bar.clear(g);
				bar.setValue(bars[index].getValue());
				colorBar(index+1, swappingColor);

				index--;
				comp++;
				swapping++;

				// Check for pause
				if (stopInsertionFlag == 1) {
					try {
						handlePause();
					} catch (InterruptedException e) {
						throw new RuntimeException(e);
					}
				}
			}
			comp++;

			index++;

			// insert the element
			array[index] = element;

			bar = bars[index];
			bar.clear(g);
			bar.setValue(element);
			bar.setColor(getBarColor(index));
			bar.draw(g);

			bs.show();
		}

		finishAnimation();

		g.dispose();
	}


	/* QUICK SORT */
	public void quickSort() throws InterruptedException {
		if (!isCreated())
			return;

		g = bs.getDrawGraphics();

		// calculate elapsed time
		startTime = System.nanoTime();
		Sort.quickSort(array.clone());
		time = System.nanoTime() - startTime;

		comp = swapping = 0;

		quickSort(0, array.length-1);

		finishAnimation();
		g.dispose();
	}


	// recursive quicksort
	private void quickSort(int start, int end) throws InterruptedException {
		if (start < end)
		{
			// place pivot in correct spot
			int pivot = partition(start, end);

			// coloring
			bars[pivot].setColor(getBarColor(pivot));
			bars[pivot].draw(g);
			bs.show();

			// sort the left half
			quickSort(start, pivot-1);

			// sort the right half
			quickSort(pivot+1, end);
		}
	}


	// quick sort partition
	private int partition(int start, int end) throws InterruptedException {
		// pivot is the last element
		int pivot = array[end];

		// mark it as pivot
		Bar bar = bars[end];
		Color oldColor = bar.getColor();
		bar.setColor(comparingColor);
		bar.draw(g);
		bs.show();

		int index = start-1;
		for (int i = start; i < end; i++)
		{
			if (array[i] < pivot)
			{
				index++;
				swap(index, i);
				swapping++;

				// Check for pause
				try {
					checkPaused();
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				}
			}
			comp++;
		}

		bar.setColor(oldColor);
		bar.draw(g);
		bs.show();

		// move pivot to correct location
		index++;
		swap(index, end);
		swapping++;

		// Check for pause
		try {
			checkPaused();
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}

		return index;
	}

	/* MERGE SORT */
	public void mergeSort() throws InterruptedException {
		if (!isCreated())
			return;

		g = bs.getDrawGraphics();

		// calculate elapsed time
		startTime = System.nanoTime();
		Sort.mergeSort(array.clone());
		time = System.nanoTime() - startTime;

		comp = swapping = 0;

		mergeSort(0, array.length-1);

		finishAnimation();
		g.dispose();
	}


	// recursive mergeSort
	private void mergeSort(int left, int right) throws InterruptedException {
		if (left >= right)
			return;

		// find the middle
		int middle = (right + left) / 2;

		// sort the left half
		mergeSort(left, middle);

		// sort the second half
		mergeSort(middle+1, right);

		// merge them
		try {
			merge(left, middle, right);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}


	// merge for mergeSort
	private void merge(int left, int middle, int right) throws InterruptedException {
		Color mergeColor = getBarColor(middle);

		// number of items in the first half
		int n1 = middle - left + 1;
		int n2 = right - middle;  // second half

		// create array for those parts
		int[] leftArr = new int[n1];
		for (int i = 0; i < n1; i++)
			leftArr[i] = array[left+i];

		int[] rightArr = new int[n2];
		for (int i = 0; i < n2; i++)
			rightArr[i] = array[middle+i+1];

		// starting index
		int l = 0, r = 0, k = left;  // k: for the original array

		// merge
		while (l < n1 && r < n2)
		{
			Bar bar = bars[k];
			bar.clear(g);
			if (leftArr[l] < rightArr[r]) {
				array[k] = leftArr[l];
				bar.setValue(leftArr[l]);
				l++;
			} else {
				array[k] = rightArr[r];
				bar.setValue(rightArr[r]);
				r++;
			}

			bar.setColor(mergeColor);
			colorBar(k, swappingColor);
			k++;
			comp++;
			swapping++;

			// Check for pause
			try {
				checkPaused();
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		}


		// add the remaining in the two arrays if there are any
		while (l < n1)
		{
			Bar bar = bars[k];
			bar.clear(g);

			array[k] = leftArr[l];
			bar.setValue(leftArr[l]);
			bar.setColor(mergeColor);
			colorBar(k, swappingColor);
			l++;
			k++;
			swapping++;

			try {
				checkPaused();
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		}

		while (r < n2)
		{
			Bar bar = bars[k];
			bar.clear(g);

			array[k] = rightArr[r];
			bar.setValue(rightArr[r]);
			bar.setColor(mergeColor);
			colorBar(k, swappingColor);
			r++;
			k++;
			swapping++;

			try {
				checkPaused();
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		}
	}

	/* SHELL SORT */
	public void shellSort() throws InterruptedException {
		if (!isCreated()) return;

		g = bs.getDrawGraphics();

		startTime = System.nanoTime();
		Sort.shellSort(array.clone());
		time = System.nanoTime() - startTime;

		comp = swapping = 0;

		int n = array.length;

		for (int gap = n / 2; gap > 0; gap /= 2) {
			for (int i = gap; i < n; i++) {
				int temp = array[i];
				int j;

				for (j = i; j >= gap && array[j - gap] > temp; j -= gap) {
					// Tô màu các thanh đang so sánh
					colorPair(j, i, comparingColor);
					array[j] = array[j - gap];

					// Xóa và cập nhật giá trị của thanh
					bars[j].clear(g);
					bars[j].setValue(bars[j - gap].getValue());
					bars[j].draw(g);

					bs.show();
					swapping++;
					comp++;

					// Check for pause
					checkPaused();
				}

				array[j] = temp;
				bars[j].clear(g);
				bars[j].setValue(temp);
				bars[j].setColor(getBarColor(j));
				bars[j].draw(g);
				bs.show();
			}
		}

		finishAnimation();
		g.dispose();
	}


	// check if array is created
	private boolean isCreated()
	{
		if (!hasArray)
			JOptionPane.showMessageDialog(null, "You need to create an array!", "No Array Created Error", JOptionPane.ERROR_MESSAGE);
		return hasArray;
	}


	public void setCapacity(int capacity)
	{
		this.capacity = capacity;
	}

	public void setFPS(int fps)
	{
		this.speed = (int) (1000.0/fps);
	}

	public interface SortedListener
	{
		void onArraySorted(long elapsedTime, int comparison, int swapping);
		BufferStrategy getBufferStrategy();
	}
}