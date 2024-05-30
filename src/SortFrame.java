package test2;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.NumberFormat;

public abstract class SortFrame extends JFrame implements PropertyChangeListener,
        ChangeListener, Visualizer.SortedListener,
        ButtonPanel.SortButtonListener, MyCanvas.VisualizerProvider {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final int WIDTH = 1280;
	protected static final int HEIGHT = 720;
    private static final int CAPACITY = 50, FPS = 100;

    protected JPanel mainPanel, inputPanel, sliderPanel, inforPanel;
    protected ButtonPanel buttonPanel;
    protected JLabel capacityLabel, fpsLabel, timeLabel, compLabel, swapLabel;
    protected JFormattedTextField capacityField;
    protected JSlider fpsSlider;
    protected MyCanvas canvas;
    protected Visualizer visualizer;

    public SortFrame(String title) {
        super(title);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMaximumSize(new Dimension(WIDTH, HEIGHT + 200));
        setMinimumSize(new Dimension(WIDTH, HEIGHT + 20));
        setPreferredSize(new Dimension(WIDTH, HEIGHT + 20));
        setLocationRelativeTo(null);
        setResizable(false);
        setBackground(ColorManager.BACKGROUND);

        initialize();
    }

    protected void initialize() {
        mainPanel = new JPanel();
        mainPanel.setLayout(null);
        mainPanel.setBackground(ColorManager.BACKGROUND);
        add(mainPanel);

        initializeButtonPanel();
        initializeCanvas();
        initializeVisualizer();
        initializeInputPanel();
        initializeSliderPanel();
        initializeInforPanel();
    }

    protected void initializeButtonPanel() {
        buttonPanel = new ButtonPanel(this, "quick");
        buttonPanel.setBounds(0, 150, 250, HEIGHT);
        buttonPanel.setBackground(ColorManager.BACKGROUND);
        mainPanel.add(buttonPanel);
    }

    protected void initializeCanvas() {
        canvas = new MyCanvas(this);
        int cWidth = WIDTH - 250 - 10;
        int cHeight = HEIGHT - 80;
        canvas.setFocusable(false);
        canvas.setMaximumSize(new Dimension(cWidth, cHeight));
        canvas.setMinimumSize(new Dimension(cWidth, cHeight));
        canvas.setPreferredSize(new Dimension(cWidth, cHeight));
        canvas.setBounds(250, 60, cWidth, cHeight);
        mainPanel.add(canvas);
        pack();
    }

    protected void initializeVisualizer() {
        visualizer = new Visualizer(CAPACITY, FPS, this);
        visualizer.createRandomArray(canvas.getWidth(), canvas.getHeight());
    }

    protected void initializeInputPanel() {
        capacityLabel = new JLabel("Capacity");
        capacityLabel.setForeground(ColorManager.TEXT);
        capacityLabel.setFont(new Font(null, Font.BOLD, 15));

        NumberFormat format = NumberFormat.getNumberInstance();
        MyFormatter formatter = new MyFormatter(format);
        formatter.setValueClass(Integer.class);
        formatter.setMinimum(0);
        formatter.setMaximum(200);
        formatter.setAllowsInvalid(false);
        formatter.setCommitsOnValidEdit(true);

        capacityField = new JFormattedTextField(formatter);
        capacityField.setValue(CAPACITY);
        capacityField.setColumns(3);
        capacityField.setFont(new Font(null, Font.PLAIN, 15));
        capacityField.setForeground(ColorManager.TEXT);
        capacityField.setBackground(ColorManager.CANVAS_BACKGROUND);
        capacityField.setCaretColor(ColorManager.BAR_YELLOW);
        capacityField.setBorder(BorderFactory.createLineBorder(ColorManager.FIELD_BORDER, 1));
        capacityField.addPropertyChangeListener("value", this);

        capacityLabel.setLabelFor(capacityField);

        inputPanel = new JPanel(new GridLayout(1, 0));
        inputPanel.add(capacityLabel);
        inputPanel.add(capacityField);
        inputPanel.setBackground(ColorManager.BACKGROUND);
        inputPanel.setBounds(25, 20, 170, 30);
        mainPanel.add(inputPanel);
    }

    protected void initializeSliderPanel() {
        fpsLabel = new JLabel("Frames Per Second");
        fpsLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        fpsLabel.setFont(new Font(null, Font.BOLD, 15));
        fpsLabel.setForeground(ColorManager.TEXT);

        fpsSlider = new JSlider(JSlider.HORIZONTAL, 50, 350, FPS);
        fpsSlider.setMajorTickSpacing(100);
        fpsSlider.setMinorTickSpacing(20);
        fpsSlider.setPaintTicks(true);
        fpsSlider.setPaintLabels(true);
        fpsSlider.setPaintTrack(true);
        fpsSlider.setForeground(ColorManager.TEXT);
        fpsSlider.addChangeListener(this);

        sliderPanel = new JPanel();
        sliderPanel.setLayout(new BoxLayout(sliderPanel, BoxLayout.Y_AXIS));
        sliderPanel.setBackground(ColorManager.BACKGROUND);
        sliderPanel.add(fpsLabel);
        sliderPanel.add(fpsSlider);

        sliderPanel.setBounds(10, 80, 220, 100);
        mainPanel.add(sliderPanel);
    }

    protected void initializeInforPanel() {
        timeLabel = new JLabel("Elapsed Time: 0 µs");
        timeLabel.setFont(new Font(null, Font.PLAIN, 15));
        timeLabel.setForeground(ColorManager.TEXT_RED);

        compLabel = new JLabel("Comparisons: 0");
        compLabel.setFont(new Font(null, Font.PLAIN, 15));
        compLabel.setForeground(ColorManager.TEXT_YELLOW);

        swapLabel = new JLabel("Swaps: 0");
        swapLabel.setFont(new Font(null, Font.PLAIN, 15));
        swapLabel.setForeground(ColorManager.TEXT_GREEN);

        inforPanel = new JPanel(new GridLayout(1, 0));
        inforPanel.setAlignmentX(JPanel.CENTER_ALIGNMENT);
        inforPanel.add(timeLabel);
        inforPanel.add(compLabel);
        inforPanel.add(swapLabel);
        inforPanel.setBackground(ColorManager.BACKGROUND);
        inforPanel.setBounds(410, 20, 800, 30);
        mainPanel.add(inforPanel);
    }

    @Override
    public void propertyChange(PropertyChangeEvent e) {
        int value = ((Number) capacityField.getValue()).intValue();
        visualizer.setCapacity(value);
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        if (!fpsSlider.getValueIsAdjusting()) {
            int value = (int) fpsSlider.getValue();
            visualizer.setFPS(value);
        }
    }

    @Override
    public void onDrawArray() {
        if (visualizer != null)
            visualizer.drawArray();
    }

    @Override
    public void onArraySorted(long elapsedTime, int comp, int swapping) {
        timeLabel.setText("Elapsed Time: " + (int) (elapsedTime / 1000.0) + " µs");
        compLabel.setText("Comparisons: " + comp);
		swapLabel.setText("Swaps: " + swapping);
	}


	// return the graphics for drawing
	public BufferStrategy getBufferStrategy()
	{
		BufferStrategy bs = canvas.getBufferStrategy();
		if (bs == null)
		{
			canvas.createBufferStrategy(2);
			bs = canvas.getBufferStrategy();
		}

		return bs;
	}
}