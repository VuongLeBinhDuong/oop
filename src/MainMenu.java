import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.awt.event.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.util.List;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class MainMenu extends JFrame {
    public static final int WIDTH = 1280, HEIGHT = 720;
    public static final int CAPACITY = 50, FPS = 100;
    private JPanel center;
    private String selected;
    public static int screenWidth;
    public static int screenHeight;
    public static void main(String[] args)
    {
        new MainMenu();
    }

    public MainMenu() {
        Container cp = getContentPane();
        cp.setLayout(new BorderLayout());
        this.center = createCenter();
        cp.add(center, BorderLayout.CENTER);
        setTitle("Sorting Visualization");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        screenWidth = screenSize.width;
        screenHeight = screenSize.height;
        setSize(screenWidth, screenHeight);

        setLocationRelativeTo(null);
        setResizable(false);
        setBackground(Color.WHITE);

        // Create and set the menu bar

        createMenuBar();
        createMainMenu();

        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); // Prevent default close operation

        // Add a WindowListener to handle the close operation
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int choice = JOptionPane.showConfirmDialog(MainMenu.this, "Are you sure you want to exit?", "Exit Confirmation", JOptionPane.YES_NO_OPTION);
                if (choice == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });

    }

    public void createMainMenu() {
        // Clear existing components from the center container
        center.removeAll();

        // Create a JPanel to hold the title
        JPanel titlePanel = new JPanel(new GridBagLayout());
        titlePanel.setBackground(Color.WHITE);

        GridBagConstraints gbcTitle = new GridBagConstraints();
        gbcTitle.gridx = 0;
        gbcTitle.gridy = 0;
        gbcTitle.insets = new Insets(20, 0, 5, 0); // Reduced bottom inset

        JLabel title = new JLabel("Sorting Visualization");
        title.setFont(new Font("Arial", Font.BOLD, 60));
        titlePanel.add(title, gbcTitle);

        // Create a JPanel to hold the introduction text
        JPanel selectionPanel = new JPanel(new GridBagLayout());
        selectionPanel.setBackground(Color.WHITE);

        GridBagConstraints gbcSelection = new GridBagConstraints();
        gbcSelection.gridx = 0;
        gbcSelection.gridy = 0;
        gbcSelection.insets = new Insets(5, 20, 10, 20); // Adjust insets to add padding on left and right and reduced top inset
        gbcSelection.fill = GridBagConstraints.BOTH; // Ensure the component expands both horizontally and vertically
        gbcSelection.weightx = 1.0; // Ensure the introductionLabel expands horizontally
        gbcSelection.weighty = 1.0; // Ensure the introductionLabel expands vertically

        JLabel introductionLabel = new JLabel("<html>" +
                "Sorting algorithms are used to sort a data structure according to a specific order relationship, such as numerical order or lexicographical order.<br><br>" +
                "This operation is one of the most important and widespread in computer science. For a long time, new methods have been developed to make this procedure faster and faster.<br><br>" +
                "There are currently hundreds of different sorting algorithms, each with its own specific characteristics. They are classified according to two metrics: space complexity and time complexity.<br><br>" +
                "Those two kinds of complexity are represented with asymptotic notations, mainly with the symbols O, Θ, Ω, representing respectively the upper bound, the tight bound, and the lower bound of the algorithm's complexity, specifying in brackets an expression in terms of n, the number of the elements of the data structure.<br><br>" +
                "Most of them fall into two categories:<br><br>" +
                "<b>Logarithmic:</b><br>" +
                "<ul>" +
                "<li>The complexity is proportional to the binary logarithm (i.e., to the base 2) of n.</li>" +
                "<li>An example of a logarithmic sorting algorithm is Quick sort, with space and time complexity O(n × log n).</li>" +
                "</ul><br>" +
                "<b>Quadratic:</b><br>" +
                "<ul>" +
                "<li>The complexity is proportional to the square of n.</li>" +
                "<li>An example of a quadratic sorting algorithm is Bubble sort, with a time complexity of O(n<sup>2</sup>).</li>" +
                "</ul><br>" +
                "Space and time complexity can also be further subdivided into 3 different cases: best case, average case, and worst case.<br><br>" +
                "Sorting algorithms can be difficult to understand, and it's easy to get confused. We believe visualizing sorting algorithms can be a great way to better understand their functioning while having fun!" +
                "</html>");

        introductionLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        selectionPanel.add(introductionLabel, gbcSelection);

        gbcSelection.gridy++;
        gbcSelection.weightx = 0; // Reset weightx for subsequent components
        gbcSelection.weighty = 0; // Reset weighty for subsequent components
        gbcSelection.fill = GridBagConstraints.HORIZONTAL; // Fill horizontally for subsequent components

        // Create a new JPanel to hold the prompt label, combo box, and button
        JPanel promptPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbcPrompt = new GridBagConstraints();
        gbcPrompt.gridx = 0;
        gbcPrompt.gridy = 0;
        gbcPrompt.insets = new Insets(10, 0, 10, 10); // Adjust insets as needed

        JLabel promptLabel = new JLabel("Please select a sorting algorithm:");
        promptLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        promptPanel.add(promptLabel, gbcPrompt);

        gbcPrompt.gridx++;
        gbcPrompt.insets = new Insets(10, 10, 10, 10); // Adjust insets as needed

        JComboBox<String> sortComboBox = new JComboBox<>();
        sortComboBox.addItem("Bubble Sort");
        sortComboBox.addItem("Selection Sort");
        sortComboBox.addItem("Insertion Sort");
        sortComboBox.addItem("Quick Sort");
        sortComboBox.addItem("Merge Sort");
        sortComboBox.addItem("Shell Sort");
        sortComboBox.setPreferredSize(new Dimension(150, 30)); // Set smaller width
        promptPanel.add(sortComboBox, gbcPrompt);
        sortComboBox.setSelectedIndex(-1); // No item selected initially

        sortComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedSort = (String) sortComboBox.getSelectedItem();
                selected = selectedSort;
            }
        });

        gbcPrompt.gridx++;
        gbcPrompt.insets = new Insets(10, 0, 10, 0); // Adjust insets as needed

        JButton button = new JButton("Submit");
        button.setPreferredSize(new Dimension(100, 30)); // Align height with combo box
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selected == null) {
                    JOptionPane.showMessageDialog(MainMenu.this, "Choose a type of sort before submit");
                } else {
                    getContentPane().removeAll();
                    getContentPane().repaint();
                    switch (selected) {
                        case "Bubble Sort":
                            new BubbleSortFrame().setVisible(true);
                            setVisible(false); // Hide the main menu
                            break;
                        case "Selection Sort":
                            new SelectionSortFrame().setVisible(true);
                            setVisible(false); // Hide the main menu
                            break;
                        case "Insertion Sort":
                            new InsertionSortFrame().setVisible(true);
                            setVisible(false); // Hide the main menu
                            break;
                        case "Quick Sort":
                            new QuickSortFrame().setVisible(true);
                            setVisible(false); // Hide the main menu
                            break;
                        case "Merge Sort":
                            new MergeSortFrame().setVisible(true);
                            setVisible(false); // Hide the main menu
                            break;
                        case "Shell Sort":
                            new ShellSortFrame().setVisible(true);
                            setVisible(false); // Hide the main menu
                            break;
                        default:
                            break;
                    }
                }
                center.revalidate();
                center.repaint();
            }
        });
        promptPanel.add(button, gbcPrompt);

        // Add the promptPanel to the selectionPanel
        selectionPanel.add(promptPanel, gbcSelection);

        // Add the panels to the center container with vertical spacing
        center.setLayout(new GridBagLayout());
        GridBagConstraints gbcCenter = new GridBagConstraints();
        gbcCenter.gridx = 0;
        gbcCenter.gridy = 0;
        gbcCenter.fill = GridBagConstraints.BOTH; // Fill both horizontally and vertically
        gbcCenter.anchor = GridBagConstraints.CENTER;
        gbcCenter.insets = new Insets(10, 10, 10, 10);
        gbcCenter.weightx = 1.0;
        gbcCenter.weighty = 1.0;

        center.add(titlePanel, gbcCenter);

        gbcCenter.gridy++;
        center.add(selectionPanel, gbcCenter);

        // Revalidate and repaint the center container
        center.revalidate();
        center.repaint();
    }



    JPanel createCenter() {
        JPanel center = new JPanel();
        center.setLayout(new GridBagLayout());
        center.setBackground(Color.WHITE);
        center.removeAll();
        center.revalidate();
        center.repaint();
        return center;
    }

    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10)); // Add padding

        JMenu homeMenu = new JMenu("Home");
        homeMenu.setBorder(BorderFactory.createEmptyBorder());
        homeMenu.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                center.removeAll();
                createMainMenu();
                center.revalidate();
                center.repaint();
            }
        });
        menuBar.add(homeMenu);
        menuBar.add(Box.createHorizontalStrut(20));

        JMenu aboutMenu = new JMenu("About");
        aboutMenu.setBorder(BorderFactory.createEmptyBorder());
        aboutMenu.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                center.removeAll();
                createAbout();
                center.revalidate();
                center.repaint();
            }
        });
        menuBar.add(aboutMenu);
        menuBar.add(Box.createHorizontalStrut(20));

        JMenu sortMenuItem = new JMenu("Categories");
        sortMenuItem.setBorder(BorderFactory.createEmptyBorder());
        JMenuItem bubbleSort = new JMenuItem("Bubble Sort");
        JMenuItem insertionSort = new JMenuItem("Insertion Sort");
        JMenuItem selectionSort = new JMenuItem("Selection Sort");
        JMenuItem mergeSort = new JMenuItem("Merge Sort");
        JMenuItem quickSort = new JMenuItem("Quick Sort");
        JMenuItem shellSort = new JMenuItem("Shell Sort");

        bubbleSort.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                center.removeAll();
                bubbleSortInformation();
                center.revalidate();
                center.repaint();
            }
        });

        insertionSort.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                center.removeAll();
                insertionSortInformation();
                center.revalidate();
                center.repaint();
            }
        });

        selectionSort.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                center.removeAll();
                selectionSortInformation();
                center.revalidate();
                center.repaint();
            }
        });

        mergeSort.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                center.removeAll();
                mergeSortInformation();
                center.revalidate();
                center.repaint();
            }
        });

        quickSort.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                center.removeAll();
                quickSortInformation();
                center.revalidate();
                center.repaint();
            }
        });

        shellSort.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                center.removeAll();
                shellSortInformation();
                center.revalidate();
                center.repaint();
            }
        });

        sortMenuItem.add(bubbleSort);
        sortMenuItem.add(insertionSort);
        sortMenuItem.add(selectionSort);
        sortMenuItem.add(mergeSort);
        sortMenuItem.add(quickSort);
        sortMenuItem.add(shellSort);

        menuBar.add(sortMenuItem);
        menuBar.add(Box.createHorizontalStrut(20));

        JMenu helpMenu = new JMenu("Help");
        helpMenu.setBorder(BorderFactory.createEmptyBorder());
        helpMenu.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                center.removeAll();
                createHelp();
                center.revalidate();
                center.repaint();
            }
        });

        menuBar.add(helpMenu);

        // Add horizontal strut to create space between menu items and exit button
        menuBar.add(Box.createHorizontalStrut(1390));

        setJMenuBar(menuBar);
    }

    public void createHelp() {
        center.removeAll();

        JTextArea helpText = new JTextArea();
        helpText.setText("Welcome to Sorting Visualization!\n\n"
                + "This application provides an interactive platform for visualizing various sorting algorithms in action. "
                + "Here are the detailed steps and functionalities available in this visualization tool:\n\n"
                + "1. **Choose a sorting algorithm:**\n"
                + "   Select from a range of popular sorting algorithms including:\n"
                + "   - Bubble Sort: A simple, but inefficient, sorting algorithm that repeatedly steps through the list to be sorted.\n"
                + "   - Selection Sort: A comparison-based algorithm that divides the list into a sorted and an unsorted region.\n"
                + "   - Insertion Sort: Efficient for small data sets and adaptive to the structure of the input data.\n"
                + "   - Quick Sort: A highly efficient sorting algorithm that uses divide-and-conquer.\n"
                + "   - Merge Sort: An efficient, stable, comparison-based, divide-and-conquer sorting algorithm.\n"
                + "   - Shell Sort: An extension of Insertion Sort that allows the exchange of items that are far apart.\n\n"
                + "2. **Create an array:**\n"
                + "   Click the 'Create Array' button to generate a random array of data that you can sort. "
                + "   You can customize the size of the array and the range of the numbers using the provided input fields.\n"
                + "   - Size: Adjust the number of elements in the array.\n"
                + "   - Range: Define the minimum and maximum values for the elements in the array.\n\n"
                + "3. **Enter an array:**\n"
                + "   Alternatively, manually enter an array of integers separated by spaces into the input field provided. "
                + "   Click 'Sort' to visualize the chosen algorithm sorting the entered array.\n\n"
                + "4. **Compare Algorithms:**\n"
                + "   Use the 'Compare' button to run each of the selected sorting algorithms on the same randomly generated array. "
                + "   This feature allows you to compare the performance and efficiency of different sorting algorithms in real-time.\n\n"
                + "5. **Start sorting:**\n"
                + "   Once you have created or entered an array, press the 'Sort' button to initiate the sorting visualization. "
                + "   You will see the array elements animated, showing the step-by-step process of how the algorithm sorts the data.\n\n"
                + "6. **Pause/Continue:**\n"
                + "   You can pause the visualization at any time by clicking the 'Pause' button, which is useful for closely analyzing a particular step. "
                + "   Press 'Continue' to resume the sorting process from where you left off.\n\n"
                + "7. **Adjust speed:**\n"
                + "   Use the slider to control the speed of the sorting animation. "
                + "   Slower speeds can help you understand complex algorithms better by allowing you to observe each step carefully.\n\n"
                + "8. **Step-by-step Execution:**\n"
                + "   For deeper analysis, you can step through the sorting process one action at a time. This can be especially useful for educational purposes.\n\n"
                + "9. **Detailed Algorithm Information:**\n"
                + "   Hover over each sorting algorithm in the menu to see a brief description and complexity details. "
                + "   This can help you understand the algorithm's efficiency and suitability for different types of data sets.\n\n"
                + "Understanding sorting algorithms visually can significantly enhance your comprehension and analysis of their performance and efficiency. "
                + "Use this tool to explore and learn the characteristics of different sorting algorithms in a dynamic and interactive environment.");

        helpText.setFont(new Font("Arial", Font.PLAIN, 14));
        helpText.setEditable(false);
        helpText.setLineWrap(true);
        helpText.setWrapStyleWord(true);
        helpText.setMargin(new Insets(20, 20, 20, 20));

        JScrollPane scrollPane = new JScrollPane(helpText);
        scrollPane.setPreferredSize(new Dimension(WIDTH - 100, HEIGHT - 100));

        center.setLayout(new BorderLayout());
        center.add(scrollPane, BorderLayout.CENTER);

        center.revalidate();
        center.repaint();
    }


    public void createAbout() {
        center.removeAll();

        JTextArea aboutText = new JTextArea();
        aboutText.setText("Sorting Visualization Project\n\n"
                + "Version: 1.0\n\n"
                + "Author: [Your Name]\n\n"
                + "Description:\n\n"
                + "This project aims to provide an educational tool for understanding sorting algorithms. "
                + "Developed using Java Swing, it offers a user-friendly interface to visualize the step-by-step execution "
                + "of popular sorting algorithms. By observing the sorting process in real-time, users can gain insights into "
                + "algorithmic efficiency and performance.\n\n"
                + "Key Features:\n"
                + "1. **Interactive Algorithm Selection:**\n"
                + "   Choose from a variety of sorting algorithms including Bubble Sort, Selection Sort, Insertion Sort, Quick Sort, Merge Sort, and Shell Sort. "
                + "   Each algorithm is explained in detail, highlighting its operational mechanics and time complexity.\n\n"
                + "2. **Customizable Array Generation:**\n"
                + "   Generate arrays with customizable sizes and number ranges to observe how different algorithms handle varying data sets. "
                + "   This feature allows users to test the performance and behavior of algorithms on different types of data.\n\n"
                + "3. **Dynamic Visualization:**\n"
                + "   Watch the sorting process unfold in real-time with animations that clearly illustrate each algorithm’s approach to sorting. "
                + "   The visualization aids in understanding the efficiency and flow of each sorting technique.\n\n"
                + "4. **Speed Control:**\n"
                + "   Adjust the speed of the sorting visualization to either speed up the process for quicker observations or slow it down for detailed analysis.\n\n"
                + "5. **Educational Insights:**\n"
                + "   The tool is designed to be educational, making it perfect for students and enthusiasts looking to deepen their understanding of sorting algorithms. "
                + "   It also includes detailed explanations and complexity analysis of each sorting method.\n\n"
                + "6. **Step-by-step Execution:**\n"
                + "   Pause the animation and step through the sorting process one step at a time to closely examine how algorithms work. "
                + "   This feature is particularly useful for in-depth learning and analysis.\n\n"
                + "7. **User Experience:**\n"
                + "   The interface is designed with usability in mind, ensuring a seamless and intuitive experience for users. "
                + "   The clear layout and interactive controls make it easy to navigate and use the tool effectively.\n\n"
                + "Future Enhancements:\n"
                + "We plan to expand the tool with additional features such as more sorting algorithms, comparative analysis tools, "
                + "and the ability to visualize the sorting process with different data structures like linked lists and trees.\n\n"
                + "We hope this project enhances your understanding of sorting algorithms and inspires further exploration in the field of computer science.");

        aboutText.setFont(new Font("Arial", Font.PLAIN, 14));
        aboutText.setEditable(false);
        aboutText.setLineWrap(true);
        aboutText.setWrapStyleWord(true);
        aboutText.setMargin(new Insets(20, 20, 20, 20));

        JScrollPane scrollPane = new JScrollPane(aboutText);
        scrollPane.setPreferredSize(new Dimension(WIDTH - 100, HEIGHT - 100));

        center.setLayout(new BorderLayout());
        center.add(scrollPane, BorderLayout.CENTER);

        center.revalidate();
        center.repaint();
    }


    public void bubbleSortInformation() {
        center.removeAll();

        // Create a panel to hold the text area and image display
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(Color.WHITE); // Set background color to white

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;

        JEditorPane infoText = new JEditorPane();
        infoText.setContentType("text/html"); // Set content type to HTML
        String info = "<html><font size=\"5\"><b>Bubble Sort:</b></font><br><br>"
                + "Bubble Sort is a simple sorting algorithm that repeatedly steps through the list, compares adjacent elements, and swaps them if they are in the wrong order. "
                + "The pass through the list is repeated until the list is sorted. The algorithm gets its name because smaller elements 'bubble' to the top of the list with each pass.<br><br>"
                + "<font size=\"5\"><b>How does Bubble Sort Work?</b></font><br><br>"
                + "Let us understand the working of bubble sort with the help of the following illustration:<br><br>"
                + "Input: arr[] = {6, 0, 3, 5}</html>";
        infoText.setText(info);
        infoText.setFont(new Font("Arial", Font.PLAIN, 14)); // Increase font size for readability
        infoText.setEditable(false);
        infoText.setMargin(new Insets(20, 20, 20, 20));
        infoText.setPreferredSize(new Dimension(800, 250)); // Set preferred size for text area

        contentPanel.add(infoText, gbc); // Add editor pane to content panel

        // Step 1
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        String info1 = "<html><b>First Pass: <br><br></b>" + "The largest element is placed in its correct position, i.e., the end of the array.</html>";
        JEditorPane infoText1 = new JEditorPane();
        infoText1.setContentType("text/html"); // Set content type to HTML
        infoText1.setText(info1);
        infoText1.setFont(new Font("Arial", Font.PLAIN, 14)); // Increase font size for readability
        infoText1.setEditable(false);
        infoText1.setMargin(new Insets(20, 20, 20, 20));
        infoText1.setPreferredSize(new Dimension(800, 100)); // Set preferred size for text area

        contentPanel.add(infoText1, gbc); // Add editor pane to content panel

        // Step 1 Image
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        String imagePath1 = "./src/bubble_sort/bubble_step1.jpg";
        ImageIcon imageIcon1 = new ImageIcon(imagePath1);
        JLabel imageLabel1 = new JLabel();
        imageLabel1.setIcon(new ImageIcon(imageIcon1.getImage().getScaledInstance(800, 400, Image.SCALE_DEFAULT)));
        JPanel imagePanel1 = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        imagePanel1.setBackground(Color.WHITE);
        imagePanel1.add(imageLabel1);

        contentPanel.add(imagePanel1, gbc); // Add image panel to content panel

        // Step 2
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        String info2 = "<html><b>Second Pass: <br><br></b>" + "Place the second largest element at correct position</html>";
        JEditorPane infoText2 = new JEditorPane();
        infoText2.setContentType("text/html"); // Set content type to HTML
        infoText2.setText(info2); // Fix assignment of text
        infoText2.setFont(new Font("Arial", Font.PLAIN, 14)); // Increase font size for readability
        infoText2.setEditable(false);
        infoText2.setMargin(new Insets(20, 20, 20, 20));
        infoText2.setPreferredSize(new Dimension(800, 100)); // Set preferred size for text area

        contentPanel.add(infoText2, gbc); // Add editor pane to content panel

        // Step 2 Image
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.CENTER;
        String imagePath2 = "./src/bubble_sort/bubble_step2.jpg";
        ImageIcon imageIcon2 = new ImageIcon(imagePath2);
        JLabel imageLabel2 = new JLabel();
        imageLabel2.setIcon(new ImageIcon(imageIcon2.getImage().getScaledInstance(800, 400, Image.SCALE_DEFAULT)));
        JPanel imagePanel2 = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        imagePanel2.setBackground(Color.WHITE);
        imagePanel2.add(imageLabel2);

        contentPanel.add(imagePanel2, gbc); // Add image panel to content panel

        // Step 3
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.WEST;
        String info3 = "<html><b>Third Pass: <br><br></b>" + "Place the remaining two elements at their correct positions.</html>";
        JEditorPane infoText3 = new JEditorPane();
        infoText3.setContentType("text/html"); // Set content type to HTML
        infoText3.setText(info3); // Fix assignment of text
        infoText3.setFont(new Font("Arial", Font.PLAIN, 14)); // Increase font size for readability
        infoText3.setEditable(false);
        infoText3.setMargin(new Insets(20, 20, 20, 20));
        infoText3.setPreferredSize(new Dimension(800, 100)); // Set preferred size for text area

        contentPanel.add(infoText3, gbc); // Add editor pane to content panel

        // Step 3 Image
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.CENTER;
        String imagePath3 = "./src/bubble_sort/bubble_step3.jpg";
        ImageIcon imageIcon3 = new ImageIcon(imagePath3);
        JLabel imageLabel3 = new JLabel();
        imageLabel3.setIcon(new ImageIcon(imageIcon3.getImage().getScaledInstance(800, 400, Image.SCALE_DEFAULT)));
        JPanel imagePanel3 = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        imagePanel3.setBackground(Color.WHITE);
        imagePanel3.add(imageLabel3);

        contentPanel.add(imagePanel3, gbc); // Add image panel to content panel

        // summary
        gbc.gridy = 7;
        gbc.anchor = GridBagConstraints.WEST;
        String summary = "<html><b>Total no. of passes: n-1</b><br><br>" +
                "<b>Total no. of comparisons: n*(n-1)/2</b></html>";
        JEditorPane infoText4 = new JEditorPane();
        infoText4.setContentType("text/html"); // Set content type to HTML
        infoText4.setText(summary); // Fix assignment of text
        infoText4.setFont(new Font("Arial", Font.PLAIN, 14)); // Increase font size for readability
        infoText4.setEditable(false);
        infoText4.setMargin(new Insets(20, 20, 20, 20));
        infoText4.setPreferredSize(new Dimension(800, 100)); // Set preferred size for text area

        contentPanel.add(infoText4, gbc); // Add editor pane to content panel

        // Create a scroll pane for the entire content panel
        JScrollPane mainScrollPane = new JScrollPane(contentPanel);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                mainScrollPane.getViewport().setViewPosition( new Point(0, 0) );
            }
        });
        mainScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        mainScrollPane.setBackground(Color.WHITE); // Set background color of the main scroll pane

        center.setLayout(new BorderLayout()); // Set layout for center panel
        center.add(mainScrollPane, BorderLayout.CENTER); // Add main scroll pane to center panel

        center.revalidate(); // Revalidate the center panel
        center.repaint(); // Repaint the center panel

    }


    public void insertionSortInformation() {
        center.removeAll();

        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(Color.WHITE); // Set background color to white

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;

        JEditorPane infoText = new JEditorPane();
        infoText.setContentType("text/html");
        infoText.setText("<html>"
                + "<h2>Insertion Sort:</h2>"
                + "Insertion Sort is a simple sorting algorithm that builds the final sorted list one item at a time. "
                + "It takes each element from the list and inserts it into its correct position in the sorted part of the list. "
                + "The algorithm repeatedly compares the current element with the elements before it and moves them one position "
                + "to the right until it finds the correct position for the current element.<br><br>"
                + "<font size=\"5\"><b>How does Insertion Sort Work?</b></font><br><br>"
                + "Consider an array having elements: {23, 1, 10, 5, 2}"
                + "</html>");
        infoText.setFont(new Font("Arial", Font.PLAIN, 14)); // Increase font size for readability
        infoText.setEditable(false);
        infoText.setMargin(new Insets(20, 20, 20, 20));
        infoText.setPreferredSize(new Dimension(800, 250)); // Set preferred size for text area

        contentPanel.add(infoText, gbc); // Add editor pane to content panel

        // Step 1 Image
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        String imagePath1 = "./src/insertion_sort/insertion_sort.jpg";
        ImageIcon imageIcon1 = new ImageIcon(imagePath1);
        JLabel imageLabel1 = new JLabel();
        imageLabel1.setIcon(new ImageIcon(imageIcon1.getImage().getScaledInstance(800, 400, Image.SCALE_DEFAULT)));
        JPanel imagePanel1 = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        imagePanel1.setBackground(Color.WHITE);
        imagePanel1.add(imageLabel1);

        contentPanel.add(imagePanel1, gbc); // Add image panel to content panel

        // Step 1
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        String info1 = "<html><b>First Pass:</b><br>" +
                "Current element is 23<br>" +
                "The first element in the array is assumed to be sorted.<br>" +
                "The sorted part until 0th index is : [23]<br><br>" +
                "<b>Second Pass:</b><br>" +
                "Compare 1 with 23 (current element with the sorted part).<br>" +
                "Since 1 is smaller, insert 1 before 23.<br>" +
                "The sorted part until 1st index is: [1, 23]<br><br>" +
                "<b>Third Pass:</b><br>" +
                "Compare 10 with 1 and 23 (current element with the sorted part).<br>" +
                "Since 10 is greater than 1 and smaller than 23, insert 10 between 1 and 23.<br>" +
                "The sorted part until 2nd index is: [1, 10, 23]<br><br>" +
                "<b>Fourth Pass:</b><br>" +
                "Compare 5 with 1, 10, and 23 (current element with the sorted part).<br>" +
                "Since 5 is greater than 1 and smaller than 10, insert 5 between 1 and 10.<br>" +
                "The sorted part until 3rd index is: [1, 5, 10, 23]<br><br>" +
                "<b>Fifth Pass:</b><br>" +
                "Compare 2 with 1, 5, 10, and 23 (current element with the sorted part).<br>" +
                "Since 2 is greater than 1 and smaller than 5 insert 2 between 1 and 5.<br>" +
                "The sorted part until 4th index is: [1, 2, 5, 10, 23]<br><br>" +
                "<b>Final Array:</b><br>" +
                "The sorted array is: [1, 2, 5, 10, 23]<br><br>" +
                "<b>Time Complexity: O(N^2)</b><br>" +
                "<b>Auxiliary Space: O(1)</b>" +
                "</html>";
        JEditorPane infoText1 = new JEditorPane();
        infoText1.setContentType("text/html"); // Set content type to HTML
        infoText1.setText(info1);
        infoText1.setFont(new Font("Arial", Font.PLAIN, 14)); // Increase font size for readability
        infoText1.setEditable(false);
        infoText1.setMargin(new Insets(20, 20, 20, 20));
        infoText1.setPreferredSize(new Dimension(800, 600)); // Set preferred size for text area

        contentPanel.add(infoText1, gbc); // Add editor pane to content panel

        JScrollPane mainScrollPane = new JScrollPane(contentPanel);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                mainScrollPane.getViewport().setViewPosition( new Point(0, 0) );
            }
        });
        mainScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        mainScrollPane.setBackground(Color.WHITE); // Set background color of the main scroll pane

        center.setLayout(new BorderLayout());
        center.add(mainScrollPane, BorderLayout.CENTER);

        center.revalidate();
        center.repaint();

        // Scroll to the top
        mainScrollPane.getVerticalScrollBar().setValue(0);
    }


    public void selectionSortInformation() {
        center.removeAll();

        // Create a panel to hold the text area and image display
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(Color.WHITE); // Set background color to white

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;

        JEditorPane infoText = new JEditorPane();
        infoText.setContentType("text/html"); // Set content type to HTML
        String info = "<html><font size=\"5\"><b>Selection Sort:</b></font><br><br>"
                + "Selection Sort is a simple sorting algorithm that divides the input list into two parts: "
                + "the sublist of items already sorted, which is built up from left to right at the front (left) of the list, "
                + "and the sublist of items remaining to be sorted that occupy the rest of the list. "
                + "Initially, the sorted sublist is empty and the unsorted sublist is the entire input list. "
                + "The algorithm proceeds by finding the smallest (or largest, depending on sorting order) element in the unsorted sublist, "
                + "exchanging (swapping) it with the leftmost unsorted element (putting it in sorted order), "
                + "and moving the sublist boundaries one element to the right.<br><br>"
                + "<font size=\"5\"><b>How does Selection Sort Work?</b></font><br><br>"
                + "Let us understand the working of selection sort with the help of the following illustration:<br><br>"
                + "Input: arr[] = {64, 25, 12, 22, 11}</html>";
        infoText.setText(info);
        infoText.setFont(new Font("Arial", Font.PLAIN, 14)); // Increase font size for readability
        infoText.setEditable(false);
        infoText.setMargin(new Insets(20, 20, 20, 20));
        infoText.setPreferredSize(new Dimension(800, 300)); // Set preferred size for text area

        contentPanel.add(infoText, gbc); // Add editor pane to content panel

        // Step 1
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        String info1 = "<html><b>First Pass: <br><br></b>"
                + "The smallest element is selected from the unsorted part and swapped with the leftmost element.<br>"
                + "Array after pass 1: {11, 25, 12, 22, 64}</html>";
        JEditorPane infoText1 = new JEditorPane();
        infoText1.setContentType("text/html"); // Set content type to HTML
        infoText1.setText(info1);
        infoText1.setFont(new Font("Arial", Font.PLAIN, 14)); // Increase font size for readability
        infoText1.setEditable(false);
        infoText1.setMargin(new Insets(20, 20, 20, 20));
        infoText1.setPreferredSize(new Dimension(800, 100)); // Set preferred size for text area

        contentPanel.add(infoText1, gbc); // Add editor pane to content panel

        // Step 1 Image
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        String imagePath1 = "./src/selection_sort/selection_step1.jpg";
        ImageIcon imageIcon1 = new ImageIcon(imagePath1);
        JLabel imageLabel1 = new JLabel();
        imageLabel1.setIcon(new ImageIcon(imageIcon1.getImage().getScaledInstance(800, 400, Image.SCALE_DEFAULT)));
        JPanel imagePanel1 = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        imagePanel1.setBackground(Color.WHITE);
        imagePanel1.add(imageLabel1);

        contentPanel.add(imagePanel1, gbc); // Add image panel to content panel

        // Step 2
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        String info2 = "<html><b>Second Pass: <br><br></b>"
                + "The next smallest element is selected from the unsorted part and swapped with the leftmost unsorted element.<br>"
                + "Array after pass 2: {11, 12, 25, 22, 64}</html>";
        JEditorPane infoText2 = new JEditorPane();
        infoText2.setContentType("text/html"); // Set content type to HTML
        infoText2.setText(info2); // Fix assignment of text
        infoText2.setFont(new Font("Arial", Font.PLAIN, 14)); // Increase font size for readability
        infoText2.setEditable(false);
        infoText2.setMargin(new Insets(20, 20, 20, 20));
        infoText2.setPreferredSize(new Dimension(800, 100)); // Set preferred size for text area

        contentPanel.add(infoText2, gbc); // Add editor pane to content panel

        // Step 2 Image
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.CENTER;
        String imagePath2 = "./src/selection_sort/selection_step2.jpg";
        ImageIcon imageIcon2 = new ImageIcon(imagePath2);
        JLabel imageLabel2 = new JLabel();
        imageLabel2.setIcon(new ImageIcon(imageIcon2.getImage().getScaledInstance(800, 400, Image.SCALE_DEFAULT)));
        JPanel imagePanel2 = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        imagePanel2.setBackground(Color.WHITE);
        imagePanel2.add(imageLabel2);

        contentPanel.add(imagePanel2, gbc); // Add image panel to content panel

        // Step 3
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.WEST;
        String info3 = "<html><b>Third Pass: <br><br></b>"
                + "The next smallest element is selected from the unsorted part and swapped with the leftmost unsorted element.<br>"
                + "Array after pass 3: {11, 12, 22, 25, 64}</html>";
        JEditorPane infoText3 = new JEditorPane();
        infoText3.setContentType("text/html"); // Set content type to HTML
        infoText3.setText(info3); // Fix assignment of text
        infoText3.setFont(new Font("Arial", Font.PLAIN, 14)); // Increase font size for readability
        infoText3.setEditable(false);
        infoText3.setMargin(new Insets(20, 20, 20, 20));
        infoText3.setPreferredSize(new Dimension(800, 100)); // Set preferred size for text area

        contentPanel.add(infoText3, gbc); // Add editor pane to content panel

        // Step 3 Image
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.CENTER;
        String imagePath3 = "./src/selection_sort/selection_step3.jpg";
        ImageIcon imageIcon3 = new ImageIcon(imagePath3);
        JLabel imageLabel3 = new JLabel();
        imageLabel3.setIcon(new ImageIcon(imageIcon3.getImage().getScaledInstance(800, 400, Image.SCALE_DEFAULT)));
        JPanel imagePanel3 = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        imagePanel3.setBackground(Color.WHITE);
        imagePanel3.add(imageLabel3);

        contentPanel.add(imagePanel3, gbc); // Add image panel to content panel

        // Step 4
        gbc.gridy = 7;
        gbc.anchor = GridBagConstraints.WEST;
        String info4 = "<html><b>Fourth Pass: <br><br></b>"
                + "The next smallest element is selected from the unsorted part and swapped with the leftmost unsorted element.<br>"
                + "Array after pass 4: {11, 12, 22, 25, 64}</html>";
        JEditorPane infoText4 = new JEditorPane();
        infoText4.setContentType("text/html"); // Set content type to HTML
        infoText4.setText(info4); // Fix assignment of text
        infoText4.setFont(new Font("Arial", Font.PLAIN, 14)); // Increase font size for readability
        infoText4.setEditable(false);
        infoText4.setMargin(new Insets(20, 20, 20, 20));
        infoText4.setPreferredSize(new Dimension(800, 100)); // Set preferred size for text area

        contentPanel.add(infoText4, gbc); // Add editor pane to content panel

        // Step 4 Image
        gbc.gridy = 8;
        gbc.anchor = GridBagConstraints.CENTER;
        String imagePath4 = "./src/selection_sort/selection_step4.jpg";
        ImageIcon imageIcon4 = new ImageIcon(imagePath4);
        JLabel imageLabel4 = new JLabel();
        imageLabel4.setIcon(new ImageIcon(imageIcon4.getImage().getScaledInstance(800, 400, Image.SCALE_DEFAULT)));
        JPanel imagePanel4 = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        imagePanel4.setBackground(Color.WHITE);
        imagePanel4.add(imageLabel4);

        contentPanel.add(imagePanel4, gbc); // Add image panel to content panel

        gbc.gridy = 9;
        gbc.anchor = GridBagConstraints.WEST;
        String info5 = "<html><b>Fifth Pass: <br><br></b>"
                + "At last the largest value present in the array automatically get placed at the last position in the array<br>"
                + "The resulted array is the sorted array.</html>";
        JEditorPane infoText5 = new JEditorPane();
        infoText5.setContentType("text/html"); // Set content type to HTML
        infoText5.setText(info5); // Fix assignment of text
        infoText5.setFont(new Font("Arial", Font.PLAIN, 14)); // Increase font size for readability
        infoText5.setEditable(false);
        infoText5.setMargin(new Insets(20, 20, 20, 20));
        infoText5.setPreferredSize(new Dimension(800, 100)); // Set preferred size for text area

        contentPanel.add(infoText5, gbc); // Add editor pane to content panel

        // Step 4 Image
        gbc.gridy = 10;
        gbc.anchor = GridBagConstraints.CENTER;
        String imagePath5 = "./src/selection_sort/selection_step5.jpg";
        ImageIcon imageIcon5 = new ImageIcon(imagePath5);
        JLabel imageLabel5 = new JLabel();
        imageLabel5.setIcon(new ImageIcon(imageIcon5.getImage().getScaledInstance(800, 400, Image.SCALE_DEFAULT)));
        JPanel imagePanel5 = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        imagePanel5.setBackground(Color.WHITE);
        imagePanel5.add(imageLabel5);

        contentPanel.add(imagePanel5, gbc); // Add image panel to content panel


        // summary
        gbc.gridy = 11;
        gbc.anchor = GridBagConstraints.WEST;
        String summary = "<html><b>Total no. of passes: n-1</b><br><br>" +
                "<b>Total no. of comparisons: n*(n-1)/2</b></html>";
        JEditorPane infoText6 = new JEditorPane();
        infoText6.setContentType("text/html"); // Set content type to HTML
        infoText6.setText(summary); // Fix assignment of text
        infoText6.setFont(new Font("Arial", Font.PLAIN, 14)); // Increase font size for readability
        infoText6.setEditable(false);
        infoText6.setMargin(new Insets(20, 20, 20, 20));
        infoText6.setPreferredSize(new Dimension(800, 100)); // Set preferred size for text area

        contentPanel.add(infoText6, gbc); // Add editor pane to content panel

        // Create a scroll pane for the entire content panel
        JScrollPane mainScrollPane = new JScrollPane(contentPanel);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                mainScrollPane.getViewport().setViewPosition( new Point(0, 0) );
            }
        });
        mainScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        mainScrollPane.setBackground(Color.WHITE); // Set background color of the main scroll pane

        center.setLayout(new BorderLayout()); // Set layout for center panel
        center.add(mainScrollPane, BorderLayout.CENTER); // Add main scroll pane to center panel

        center.revalidate(); // Revalidate the center panel
        center.repaint(); // Repaint the center panel

        // Scroll to the top
        mainScrollPane.getVerticalScrollBar().setValue(0);
    }


    public void mergeSortInformation() {
        center.removeAll();

        // Create a panel to hold the text area and image display
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(Color.WHITE); // Set background color to white

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;

        // Merge Sort Introduction
        JEditorPane introText = new JEditorPane();
        introText.setContentType("text/html"); // Set content type to HTML
        String introInfo = "<html><font size=\"5\"><b>Merge Sort:</b></font><br><br>"
                + "Merge Sort is a divide-and-conquer algorithm that divides the input array into two halves, "
                + "recursively sorts each half, and then merges the sorted halves to produce a single sorted array. "
                + "The merge operation is the key to Merge Sort, where two sorted arrays are combined into a single sorted array.<br><br>"
                + "<font size=\"5\"><b>How does Merge Sort Work?</b></font><br><br>"
                + "Let us look at the working of Merge Sort with the following example:<br><br>"
                + "Divide:<br>"
                + "[38, 27, 43, 10] is divided into [38, 27] and [43, 10].<br>"
                + "[38, 27] is divided into [38] and [27].<br>"
                + "[43, 10] is divided into [43] and [10].<br><br>"
                + "Conquer:<br>"
                + "[38] is already sorted.<br>"
                + "[27] is already sorted.<br>"
                + "[43] is already sorted.<br>"
                + "[10] is already sorted.<br><br>"
                + "Merge:<br>"
                + "Merge [38] and [27] to get [27, 38].<br>"
                + "Merge [43] and [10] to get [10,43].<br>"
                + "Merge [27, 38] and [10,43] to get the final sorted list [10, 27, 38, 43].<br><br>"
                + "Therefore, the sorted list is [10, 27, 38, 43].</html>";
        introText.setText(introInfo);
        introText.setFont(new Font("Arial", Font.PLAIN, 14)); // Increase font size for readability
        introText.setEditable(false);
        introText.setMargin(new Insets(20, 20, 20, 20));
        introText.setPreferredSize(new Dimension(800, 600)); // Set preferred size for text area

        contentPanel.add(introText, gbc); // Add introduction editor pane to content panel

        // Step 1 Image
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        String imagePath1 = "./src/merge_sort/merge_sort_step1.jpg"; // Replace with actual image path
        ImageIcon imageIcon1 = new ImageIcon(imagePath1);
        JLabel imageLabel1 = new JLabel();
        imageLabel1.setIcon(new ImageIcon(imageIcon1.getImage().getScaledInstance(500, 500, Image.SCALE_DEFAULT)));
        JPanel imagePanel1 = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 10));
        imagePanel1.setBackground(Color.WHITE);
        imagePanel1.add(imageLabel1);

        contentPanel.add(imagePanel1, gbc); // Add image panel to content panel

        // Step 2 Image
        gbc.gridy = 2;
        String imagePath2 = "./src/merge_sort/merge_sort_step2.jpg"; // Replace with actual image path
        ImageIcon imageIcon2 = new ImageIcon(imagePath2);
        JLabel imageLabel2 = new JLabel();
        imageLabel2.setIcon(new ImageIcon(imageIcon2.getImage().getScaledInstance(500, 500, Image.SCALE_DEFAULT)));
        JPanel imagePanel2 = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 10));
        imagePanel2.setBackground(Color.WHITE);
        imagePanel2.add(imageLabel2);

        contentPanel.add(imagePanel2, gbc); // Add image panel to content panel

        // Step 3 Image
        gbc.gridy = 3;
        String imagePath3 = "./src/merge_sort/merge_sort_step3.jpg"; // Replace with actual image path
        ImageIcon imageIcon3 = new ImageIcon(imagePath3);
        JLabel imageLabel3 = new JLabel();
        imageLabel3.setIcon(new ImageIcon(imageIcon3.getImage().getScaledInstance(500, 500, Image.SCALE_DEFAULT)));
        JPanel imagePanel3 = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 10));
        imagePanel3.setBackground(Color.WHITE);
        imagePanel3.add(imageLabel3);

        contentPanel.add(imagePanel3, gbc); // Add image panel to content panel

        // Step 4 Image
        gbc.gridy = 4;
        String imagePath4 = "./src/merge_sort/merge_sort_step4.jpg"; // Replace with actual image path
        ImageIcon imageIcon4 = new ImageIcon(imagePath4);
        JLabel imageLabel4 = new JLabel();
        imageLabel4.setIcon(new ImageIcon(imageIcon4.getImage().getScaledInstance(500, 500, Image.SCALE_DEFAULT)));
        JPanel imagePanel4 = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 10));
        imagePanel4.setBackground(Color.WHITE);
        imagePanel4.add(imageLabel4);

        contentPanel.add(imagePanel4, gbc); // Add image panel to content panel

        // Create a scroll pane for the entire content panel
        JScrollPane mainScrollPane = new JScrollPane(contentPanel);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                mainScrollPane.getViewport().setViewPosition( new Point(0, 0) );
            }
        });
        mainScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        mainScrollPane.setBackground(Color.WHITE); // Set background color of the main scroll pane

        center.setLayout(new BorderLayout()); // Set layout for center panel
        center.add(mainScrollPane, BorderLayout.CENTER); // Add main scroll pane to center panel

        center.revalidate(); // Revalidate the center panel
        center.repaint(); // Repaint the center panel

        // Scroll to the top
        mainScrollPane.getVerticalScrollBar().setValue(0);
    }


    public void quickSortInformation() {
        center.removeAll();

        // Create a panel to hold the text area and image display
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(Color.WHITE); // Set background color to white

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;

        // Quick Sort Introduction
        JEditorPane introText = new JEditorPane();
        introText.setContentType("text/html"); // Set content type to HTML
        String introInfo = "<html><font size=\"5\"><b>Quick Sort:</b></font><br><br>"
                + "Quick Sort is a popular sorting algorithm that follows the divide-and-conquer paradigm. "
                + "It works by selecting a 'pivot' element from the array and partitioning the other elements into two sub-arrays "
                + "according to whether they are less than or greater than the pivot. "
                + "The sub-arrays are then recursively sorted. "
                + "This process continues until the base case of an empty or single-element array is reached, "
                + "which is inherently sorted.<br><br>"
                + "<font size=\"5\"><b>How does Quick Sort Work?</b></font><br><br>"
                + "Let us understand the working of Quick Sort with the help of the following illustration:<br><br>"
                + "Input: arr[] = {10, 80, 30, 90, 40}</html>";
        introText.setText(introInfo);
        introText.setFont(new Font("Arial", Font.PLAIN, 14)); // Increase font size for readability
        introText.setEditable(false);
        introText.setMargin(new Insets(20, 20, 20, 20));
        introText.setPreferredSize(new Dimension(800, 300)); // Set preferred size for text area

        contentPanel.add(introText, gbc); // Add introduction editor pane to content panel

        // Step 1: Partitioning
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        String partitionInfo1 = "<html><b>Partition Algorithm:</b><br><br>" +
                "Compare 10 with the pivot and as it is less than pivot arrange it accordingly.</b></html>";
        JEditorPane partitionText1 = new JEditorPane();
        partitionText1.setContentType("text/html"); // Set content type to HTML
        partitionText1.setText(partitionInfo1);
        partitionText1.setFont(new Font("Arial", Font.PLAIN, 14)); // Increase font size for readability
        partitionText1.setEditable(false);
        partitionText1.setMargin(new Insets(20, 20, 20, 20));
        partitionText1.setPreferredSize(new Dimension(800, 100)); // Set preferred size for text area

        contentPanel.add(partitionText1, gbc); // Add partitioning editor pane to content panel

        // Step 1 Image
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        String imagePath1 = "./src/quick_sort/quick_partition_step1.jpg"; // Replace with actual image path
        ImageIcon imageIcon1 = new ImageIcon(imagePath1);
        JLabel imageLabel1 = new JLabel();
        imageLabel1.setIcon(new ImageIcon(imageIcon1.getImage().getScaledInstance(800, 400, Image.SCALE_DEFAULT)));
        JPanel imagePanel1 = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        imagePanel1.setBackground(Color.WHITE);
        imagePanel1.add(imageLabel1);

        contentPanel.add(imagePanel1, gbc); // Add image panel to content panel
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        String partitionInfo2 = "<html>Compare 80 with the pivot. It is greater than pivot.</html>";
        JEditorPane partitionText2 = new JEditorPane();
        partitionText2.setContentType("text/html"); // Set content type to HTML
        partitionText2.setText(partitionInfo2);
        partitionText2.setFont(new Font("Arial", Font.PLAIN, 14)); // Increase font size for readability
        partitionText2.setEditable(false);
        partitionText2.setMargin(new Insets(20, 20, 20, 20));
        partitionText2.setPreferredSize(new Dimension(800, 100)); // Set preferred size for text area

        contentPanel.add(partitionText2, gbc); // Add partitioning editor pane to content panel

        // Step 1 Image
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.CENTER;
        String imagePath2 = "./src/quick_sort/quick_partition_step2.jpg"; // Replace with actual image path
        ImageIcon imageIcon2 = new ImageIcon(imagePath2);
        JLabel imageLabel2 = new JLabel();
        imageLabel2.setIcon(new ImageIcon(imageIcon2.getImage().getScaledInstance(800, 400, Image.SCALE_DEFAULT)));
        JPanel imagePanel2 = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        imagePanel2.setBackground(Color.WHITE);
        imagePanel2.add(imageLabel2);

        contentPanel.add(imagePanel2, gbc); // Add image panel to content panel

        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.WEST;
        String partitionInfo3 = "<html>Compare 30 with pivot. It is less than pivot so arrange it accordingly.</html>";
        JEditorPane partitionText3 = new JEditorPane();
        partitionText3.setContentType("text/html"); // Set content type to HTML
        partitionText3.setText(partitionInfo3);
        partitionText3.setFont(new Font("Arial", Font.PLAIN, 14)); // Increase font size for readability
        partitionText3.setEditable(false);
        partitionText3.setMargin(new Insets(20, 20, 20, 20));
        partitionText3.setPreferredSize(new Dimension(800, 100)); // Set preferred size for text area

        contentPanel.add(partitionText3, gbc); // Add partitioning editor pane to content panel

        // Step 1 Image
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.CENTER;
        String imagePath3 = "./src/quick_sort/quick_partition_step3.jpg"; // Replace with actual image path
        ImageIcon imageIcon3 = new ImageIcon(imagePath3);
        JLabel imageLabel3 = new JLabel();
        imageLabel3.setIcon(new ImageIcon(imageIcon3.getImage().getScaledInstance(800, 400, Image.SCALE_DEFAULT)));
        JPanel imagePanel3 = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        imagePanel3.setBackground(Color.WHITE);
        imagePanel3.add(imageLabel3);

        contentPanel.add(imagePanel3, gbc); // Add image panel to content panel

        gbc.gridy = 7;
        gbc.anchor = GridBagConstraints.WEST;
        String partitionInfo4 = "<html>Compare 90 with the pivot. It is greater than the pivot.</html>";
        JEditorPane partitionText4 = new JEditorPane();
        partitionText4.setContentType("text/html"); // Set content type to HTML
        partitionText4.setText(partitionInfo4);
        partitionText4.setFont(new Font("Arial", Font.PLAIN, 14)); // Increase font size for readability
        partitionText4.setEditable(false);
        partitionText4.setMargin(new Insets(20, 20, 20, 20));
        partitionText4.setPreferredSize(new Dimension(800, 100)); // Set preferred size for text area

        contentPanel.add(partitionText4, gbc); // Add partitioning editor pane to content panel

        // Step 1 Image
        gbc.gridy = 8;
        gbc.anchor = GridBagConstraints.CENTER;
        String imagePath4 = "./src/quick_sort/quick_partition_step4.jpg"; // Replace with actual image path
        ImageIcon imageIcon4 = new ImageIcon(imagePath4);
        JLabel imageLabel4 = new JLabel();
        imageLabel4.setIcon(new ImageIcon(imageIcon4.getImage().getScaledInstance(800, 400, Image.SCALE_DEFAULT)));
        JPanel imagePanel4 = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        imagePanel4.setBackground(Color.WHITE);
        imagePanel4.add(imageLabel4);

        contentPanel.add(imagePanel4, gbc); // Add image panel to content panel

        gbc.gridy = 9;
        gbc.anchor = GridBagConstraints.WEST;
        String partitionInfo5 = "<html>Arrange the pivot in its correct position.</html>";
        JEditorPane partitionText5 = new JEditorPane();
        partitionText5.setContentType("text/html"); // Set content type to HTML
        partitionText5.setText(partitionInfo5);
        partitionText5.setFont(new Font("Arial", Font.PLAIN, 14)); // Increase font size for readability
        partitionText5.setEditable(false);
        partitionText5.setMargin(new Insets(20, 20, 20, 20));
        partitionText5.setPreferredSize(new Dimension(800, 100)); // Set preferred size for text area

        contentPanel.add(partitionText5, gbc); // Add partitioning editor pane to content panel

        // Step 1 Image
        gbc.gridy = 10;
        gbc.anchor = GridBagConstraints.CENTER;
        String imagePath5 = "./src/quick_sort/quick_partition_step5.jpg"; // Replace with actual image path
        ImageIcon imageIcon5 = new ImageIcon(imagePath5);
        JLabel imageLabel5 = new JLabel();
        imageLabel5.setIcon(new ImageIcon(imageIcon5.getImage().getScaledInstance(800, 400, Image.SCALE_DEFAULT)));
        JPanel imagePanel5 = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        imagePanel5.setBackground(Color.WHITE);
        imagePanel5.add(imageLabel5);

        contentPanel.add(imagePanel5, gbc); // Add image panel to content panel

        // Step 6: Quick Sort
        gbc.gridy = 11;
        gbc.anchor = GridBagConstraints.WEST;
        String sortInfo1 = "<html><b>Illustration of Quicksort:</b><br><br>" +
                "As the partition process is done recursively, it keeps on putting the pivot in its actual position in the sorted array. Repeatedly putting pivots in their actual position makes the array sorted. Follow the below images to understand how the recursive implementation of the partition algorithm helps to sort the array.<br>" +
                "Initial partition on the main array:</html>";
        JEditorPane sortText1 = new JEditorPane();
        sortText1.setContentType("text/html"); // Set content type to HTML
        sortText1.setText(sortInfo1); // Fix assignment of text
        sortText1.setFont(new Font("Arial", Font.PLAIN, 14)); // Increase font size for readability
        sortText1.setEditable(false);
        sortText1.setMargin(new Insets(20, 20, 20, 20));
        sortText1.setPreferredSize(new Dimension(800, 100)); // Set preferred size for text area

        contentPanel.add(sortText1, gbc); // Add sorting editor pane to content panel

        // Step 6 Image
        gbc.gridy = 12;
        gbc.anchor = GridBagConstraints.CENTER;
        String imagePath6 = "./src/quick_sort/quick_sort_step1.jpg"; // Replace with actual image path
        ImageIcon imageIcon6 = new ImageIcon(imagePath6);
        JLabel imageLabel6 = new JLabel();
        imageLabel6.setIcon(new ImageIcon(imageIcon6.getImage().getScaledInstance(800, 400, Image.SCALE_DEFAULT)));
        JPanel imagePanel6 = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        imagePanel6.setBackground(Color.WHITE);
        imagePanel6.add(imageLabel6);

        contentPanel.add(imagePanel6, gbc); // Add image panel to content panel

        gbc.gridy = 13;
        gbc.anchor = GridBagConstraints.WEST;
        String sortInfo2 = "<html>Partitioning of the subarrays:</html>";
        JEditorPane sortText2 = new JEditorPane();
        sortText2.setContentType("text/html"); // Set content type to HTML
        sortText2.setText(sortInfo2); // Fix assignment of text
        sortText2.setFont(new Font("Arial", Font.PLAIN, 14)); // Increase font size for readability
        sortText2.setEditable(false);
        sortText2.setMargin(new Insets(20, 20, 20, 20));
        sortText2.setPreferredSize(new Dimension(800, 100)); // Set preferred size for text area

        contentPanel.add(sortText2, gbc); // Add sorting editor pane to content panel

        // Step 6 Image
        gbc.gridy = 14;
        gbc.anchor = GridBagConstraints.CENTER;
        String imagePath7 = "./src/quick_sort/quick_sort_step2.jpg"; // Replace with actual image path
        ImageIcon imageIcon7 = new ImageIcon(imagePath7);
        JLabel imageLabel7 = new JLabel();
        imageLabel7.setIcon(new ImageIcon(imageIcon7.getImage().getScaledInstance(800, 400, Image.SCALE_DEFAULT)));
        JPanel imagePanel7 = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        imagePanel7.setBackground(Color.WHITE);
        imagePanel7.add(imageLabel7);

        contentPanel.add(imagePanel7, gbc); // Add image panel to content panel

        // Create a scroll pane for the entire content panel
        JScrollPane mainScrollPane = new JScrollPane(contentPanel);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                mainScrollPane.getViewport().setViewPosition( new Point(0, 0) );
            }
        });
        mainScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        mainScrollPane.setBackground(Color.WHITE); // Set background color of the main scroll pane

        center.setLayout(new BorderLayout()); // Set layout for center panel
        center.add(mainScrollPane, BorderLayout.CENTER); // Add main scroll pane to center panel

        center.revalidate(); // Revalidate the center panel
        center.repaint(); // Repaint the center panel

    }

    public void shellSortInformation() {
        center.removeAll();

        // Create a panel to hold the text area and image display
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(Color.WHITE); // Set background color to white

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;

        // Shell Sort Introduction
        JEditorPane introText = new JEditorPane();
        introText.setContentType("text/html"); // Set content type to HTML
        String introInfo = "<html><font size=\"5\"><b>Shell Sort:</b></font><br><br>"
                + "Shell Sort is an in-place comparison-based sorting algorithm that divides the input array into "
                + "smaller subarrays, each of which is sorted individually using insertion sort. The unique feature "
                + "of Shell Sort is that it compares elements that are far apart and gradually reduces the gap between elements.<br><br>"
                + "<font size=\"5\"><b>How does Shell Sort Work?</b></font><br><br>"
                + "<b>Algorithm:</b><br><br>"
                + "<b>Step 1 - Start:</b> Begin with the array to be sorted.<br>"
                + "<b>Step 2 - Initialize the gap size, say h:</b> Determine the initial gap size, h.<br>"
                + "<b>Step 3 - Divide the list into smaller sublists:</b> Each sublist has elements that are h positions apart.<br>"
                + "<b>Step 4 - Sort these sublists using insertion sort:</b> Apply insertion sort on each sublist.<br>"
                + "<b>Step 5 - Repeat step 2 until the list is sorted:</b> Reduce the gap and repeat steps 2-4 until the gap is 1.<br>"
                + "<b>Step 6 - Print the sorted list:</b> Output the sorted array.<br>"
                + "<b>Step 7 - Stop:</b> The sorting process is complete.<br><br>"
                + "Below is the implementation of ShellSort."
                + "</html>";
        introText.setText(introInfo);
        introText.setFont(new Font("Arial", Font.PLAIN, 14)); // Increase font size for readability
        introText.setEditable(false);
        introText.setMargin(new Insets(20, 20, 20, 20));
        introText.setPreferredSize(new Dimension(800, 400)); // Set preferred size for text area

        contentPanel.add(introText, gbc); // Add introduction editor pane to content panel

        // Step 1 Image
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        String imagePath1 = "./src/shell_sort/shell_sort_step1.jpg"; // Replace with actual image path
        ImageIcon imageIcon1 = new ImageIcon(imagePath1);
        JLabel imageLabel1 = new JLabel();
        imageLabel1.setIcon(new ImageIcon(imageIcon1.getImage().getScaledInstance(800, 400, Image.SCALE_DEFAULT)));
        JPanel imagePanel1 = new JPanel(new FlowLayout(FlowLayout.CENTER,0 , 10));
        imagePanel1.setBackground(Color.WHITE);
        imagePanel1.add(imageLabel1);

        contentPanel.add(imagePanel1, gbc); // Add image panel to content panel

        // Step 2 Image
        gbc.gridy = 2;
        String imagePath2 = "./src/shell_sort/shell_sort_step2.jpg"; // Replace with actual image path
        ImageIcon imageIcon2 = new ImageIcon(imagePath2);
        JLabel imageLabel2 = new JLabel();
        imageLabel2.setIcon(new ImageIcon(imageIcon2.getImage().getScaledInstance(800, 400, Image.SCALE_DEFAULT)));
        JPanel imagePanel2 = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 10));
        imagePanel2.setBackground(Color.WHITE);
        imagePanel2.add(imageLabel2);

        contentPanel.add(imagePanel2, gbc); // Add image panel to content panel

        // Step 3 Image
        gbc.gridy = 3;
        String imagePath3 = "./src/shell_sort/shell_sort_step3.jpg"; // Replace with actual image path
        ImageIcon imageIcon3 = new ImageIcon(imagePath3);
        JLabel imageLabel3 = new JLabel();
        imageLabel3.setIcon(new ImageIcon(imageIcon3.getImage().getScaledInstance(800, 400, Image.SCALE_DEFAULT)));
        JPanel imagePanel3 = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 10));
        imagePanel3.setBackground(Color.WHITE);
        imagePanel3.add(imageLabel3);

        contentPanel.add(imagePanel3, gbc); // Add image panel to content panel

        // Step 4 Image
        gbc.gridy = 4;
        String imagePath4 = "./src/shell_sort/shell_sort_step4.jpg"; // Replace with actual image path
        ImageIcon imageIcon4 = new ImageIcon(imagePath4);
        JLabel imageLabel4 = new JLabel();
        imageLabel4.setIcon(new ImageIcon(imageIcon4.getImage().getScaledInstance(800, 400, Image.SCALE_DEFAULT)));
        JPanel imagePanel4 = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 10));
        imagePanel4.setBackground(Color.WHITE);
        imagePanel4.add(imageLabel4);

        contentPanel.add(imagePanel4, gbc); // Add image panel to content panel

        // Step 5 Image
        gbc.gridy = 5;
        String imagePath5 = "./src/shell_sort/shell_sort_step5.jpg"; // Replace with actual image path
        ImageIcon imageIcon5 = new ImageIcon(imagePath5);
        JLabel imageLabel5 = new JLabel();
        imageLabel5.setIcon(new ImageIcon(imageIcon5.getImage().getScaledInstance(800, 400, Image.SCALE_DEFAULT)));
        JPanel imagePanel5 = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 10));
        imagePanel5.setBackground(Color.WHITE);
        imagePanel5.add(imageLabel5);

        contentPanel.add(imagePanel5, gbc); // Add image panel to content panel

        // Step 6 Image
        gbc.gridy = 6;
        String imagePath6 = "./src/shell_sort/shell_sort_step6.jpg"; // Replace with actual image path
        ImageIcon imageIcon6 = new ImageIcon(imagePath6);
        JLabel imageLabel6 = new JLabel();
        imageLabel6.setIcon(new ImageIcon(imageIcon6.getImage().getScaledInstance(800, 400, Image.SCALE_DEFAULT)));
        JPanel imagePanel6 = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 10));
        imagePanel6.setBackground(Color.WHITE);
        imagePanel6.add(imageLabel6);

        contentPanel.add(imagePanel6, gbc); // Add image panel to content panel

        // Create a scroll pane for the entire content panel
        JScrollPane mainScrollPane = new JScrollPane(contentPanel);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                mainScrollPane.getViewport().setViewPosition( new Point(0, 0) );
            }
        });
        mainScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        mainScrollPane.setBackground(Color.WHITE); // Set background color of the main scroll pane

        center.setLayout(new BorderLayout()); // Set layout for center panel
        center.add(mainScrollPane, BorderLayout.CENTER); // Add main scroll pane to center panel

        center.revalidate(); // Revalidate the center panel
        center.repaint(); // Repaint the center panel

    }


}

