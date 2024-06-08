import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.NumberFormat;

public abstract class SortFrame extends JFrame implements PropertyChangeListener,
        ChangeListener, Visualizer.SortedListener,
        ButtonPanel.SortButtonListener, MyCanvas.VisualizerProvider {

    private static final int WIDTH = 1600;
    protected static final int HEIGHT = 1000;
    private static final int CAPACITY = 50, FPS = 100;

    public static int screenWidth;
    public static int screenHeight;

    protected JPanel mainPanel, inputPanel, sliderPanel, inforPanel;
    protected ButtonPanel buttonPanel;
    protected JLabel capacityLabel, fpsLabel, timeLabel, compLabel, swapLabel;
    protected JFormattedTextField capacityField;
    protected JSlider fpsSlider;
    protected MyCanvas canvas;
    protected Visualizer visualizer;
    protected boolean isChecked;

    public SortFrame(String title) {
        super(title);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        screenWidth = screenSize.width;
        screenHeight = screenSize.height;

        Container cp = getContentPane();
        cp.setLayout(new BorderLayout());
        this.mainPanel = initialize();
        cp.add(mainPanel, BorderLayout.CENTER);

        setSize(screenWidth, screenHeight);

        setLocationRelativeTo(null);
        setResizable(false);
        setBackground(Color.WHITE);
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); // Prevent default close operation

        // Add a WindowListener to handle the close operation
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int choice = JOptionPane.showConfirmDialog(SortFrame.this, "Are you sure you want to exit?", "Exit Confirmation", JOptionPane.YES_NO_OPTION);
                if (choice == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });

    }

    protected JPanel initialize() {
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
        return mainPanel;
    }

    protected void initializeButtonPanel() {
        buttonPanel = new ButtonPanel(this);
        int y = (int)(0.14*screenHeight);
        int width = (int)(0.16*screenWidth);
        buttonPanel.setBounds(0, y, width, screenHeight);
        buttonPanel.setBackground(ColorManager.BACKGROUND);
        mainPanel.add(buttonPanel);
    }

    protected void initializeCanvas() {
        canvas = new MyCanvas(this);
        int cWidth = (int)(0.77*screenWidth);
        int cHeight = (int)(0.82*screenHeight);
        canvas.setFocusable(false);
        canvas.setMaximumSize(new Dimension(cWidth, cHeight));
        canvas.setMinimumSize(new Dimension(cWidth, cHeight));
        canvas.setPreferredSize(new Dimension(cWidth, cHeight));
        canvas.setBounds((int)(0.2*screenWidth), (int)(0.11*screenHeight), cWidth, cHeight);
        mainPanel.add(canvas);
        pack();
    }

    protected void initializeVisualizer() {
        visualizer = new Visualizer(CAPACITY, FPS, this);
        visualizer.createRandomArray(canvas.getWidth(), canvas.getHeight());
    }

    protected void initializeInputPanel() {
        // Create the capacity label
        capacityLabel = new JLabel("Capacity");
        capacityLabel.setForeground(ColorManager.TEXT);
        capacityLabel.setFont(new Font(null, Font.BOLD, 15));

        // Formatter for the capacity input field
        NumberFormat format = NumberFormat.getNumberInstance();
        MyFormatter formatter = new MyFormatter(format);
        formatter.setValueClass(Integer.class);
        formatter.setMinimum(0);
        formatter.setMaximum(400);
        formatter.setAllowsInvalid(false);
        formatter.setCommitsOnValidEdit(true);

        // Capacity input field setup
        capacityField = new JFormattedTextField(formatter);
        capacityField.setValue(CAPACITY);
        capacityField.setColumns(3);
        capacityField.setFont(new Font(null, Font.PLAIN, 15));
        capacityField.setForeground(ColorManager.TEXT);
        capacityField.setBackground(ColorManager.CANVAS_BACKGROUND);
        capacityField.setCaretColor(ColorManager.BAR_YELLOW);
        capacityField.setBorder(BorderFactory.createLineBorder(ColorManager.FIELD_BORDER, 1));
        capacityField.addPropertyChangeListener("value", this);

        // Associate the label with the input field
        capacityLabel.setLabelFor(capacityField);

        // Create the "Different Value" checkbox
        JCheckBox differentValueCheckbox = new JCheckBox("Unique Value Array");
        differentValueCheckbox.setFont(new Font(null, Font.PLAIN, 15));
        differentValueCheckbox.setForeground(ColorManager.TEXT);
        differentValueCheckbox.setBackground(ColorManager.BACKGROUND);
        differentValueCheckbox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                isChecked = (e.getStateChange() == ItemEvent.SELECTED);
            }
        });

        // Panel to hold the capacity label, input field, and checkbox in a row
        JPanel inputRowPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        inputRowPanel.setBackground(ColorManager.BACKGROUND);
        inputRowPanel.add(capacityLabel);
        inputRowPanel.add(capacityField);
        inputRowPanel.add(differentValueCheckbox);

        // Outer panel to position inputRowPanel properly within the main panel
        inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
        inputPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        inputPanel.setBackground(ColorManager.BACKGROUND);
        inputPanel.add(inputRowPanel);

        // Maintain the existing size constraints and positioning
        inputPanel.setBounds((int) (0.015 * screenWidth), (int) (0.018 * screenHeight), (int) (0.25 * screenWidth), (int) (0.05 * screenHeight));
        mainPanel.add(inputPanel);
    }


    protected void initializeSliderPanel() {
        fpsLabel = new JLabel("Frames Per Second");
        fpsLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        fpsLabel.setFont(new Font("Arial", Font.BOLD, 15));
        fpsLabel.setForeground(ColorManager.TEXT);
        fpsLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        fpsSlider = new JSlider(JSlider.HORIZONTAL, 50, 450, FPS);
        fpsSlider.setMajorTickSpacing(100);
        fpsSlider.setMinorTickSpacing(20);
        fpsSlider.setPaintTicks(true);
        fpsSlider.setPaintLabels(true);
        fpsSlider.setPaintTrack(true);
        fpsSlider.setForeground(ColorManager.TEXT);
        fpsSlider.setBackground(ColorManager.BACKGROUND);
        fpsSlider.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ColorManager.FIELD_BORDER, 1),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        fpsSlider.addChangeListener(this);

        // Create a custom label formatter for better appearance
        fpsSlider.setLabelTable(fpsSlider.createStandardLabels(100));
        fpsSlider.setFont(new Font("Arial", Font.PLAIN, 12));

        sliderPanel = new JPanel();
        sliderPanel.setLayout(new BoxLayout(sliderPanel, BoxLayout.Y_AXIS));
        sliderPanel.setBackground(ColorManager.BACKGROUND);
        sliderPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding
        sliderPanel.add(fpsLabel);
        sliderPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Add spacing between label and slider
        sliderPanel.add(fpsSlider);

        sliderPanel.setBounds((int)(0.01*screenWidth), (int)(0.047*screenHeight), (int)(0.17*screenWidth), (int)(0.2*screenHeight)); // Increase height for better spacing
        mainPanel.add(sliderPanel);
    }


    protected void initializeInforPanel() {
        timeLabel = new JLabel("Elapsed Time: 0 µs");
        timeLabel.setFont(new Font(null, Font.PLAIN, 18));
        timeLabel.setForeground(ColorManager.TEXT_GREEN);

        compLabel = new JLabel("Comparisons: 0");
        compLabel.setFont(new Font(null, Font.PLAIN, 18));
        compLabel.setForeground(ColorManager.TEXT_RED);

        swapLabel = new JLabel("Swaps: 0");
        swapLabel.setFont(new Font(null, Font.PLAIN, 18));
        swapLabel.setForeground(ColorManager.TEXT_YELLOW);

        inforPanel = new JPanel(new GridLayout(1, 0));
        inforPanel.setAlignmentX(JPanel.CENTER_ALIGNMENT);
        inforPanel.add(timeLabel);
        inforPanel.add(compLabel);
        inforPanel.add(swapLabel);
        inforPanel.setBackground(ColorManager.BACKGROUND);
        inforPanel.setBounds((int)(0.4*screenWidth), (int)(0.05*screenHeight), (int)(0.5*screenWidth), (int)(0.018*screenHeight));
        mainPanel.add(inforPanel);
    }

    public boolean isChecked() {
        return isChecked;
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