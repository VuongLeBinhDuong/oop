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

        setSize(800, 600);

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
                int choice = JOptionPane.showConfirmDialog(null, "Are you sure you want to exit?", "Exit Confirmation", JOptionPane.YES_NO_OPTION);
                if (choice == JOptionPane.YES_OPTION) {
                    System.exit(0);
                } else {
                    // Clear all components and recreate a new MainMenu instance
                    getContentPane().removeAll();
                    getContentPane().repaint();
                    new MainMenu();
                }
            }
        });

        // Enter fullscreen mode
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        if (gd.isFullScreenSupported()) {
            gd.setFullScreenWindow(this);
        }
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
        gbcTitle.insets = new Insets(20, 0, 30, 0);

        JLabel title = new JLabel("Sorting Visualization");
        title.setFont(new Font("Arial", Font.BOLD, 40));
        titlePanel.add(title, gbcTitle);

        // Create a JPanel to hold the introduction text, prompt label, and combo box
        JPanel selectionPanel = new JPanel(new GridBagLayout());
        selectionPanel.setBackground(Color.WHITE);

        GridBagConstraints gbcSelection = new GridBagConstraints();
        gbcSelection.gridx = 0;
        gbcSelection.gridy = 0;
        gbcSelection.insets = new Insets(30, 0, 10, 0);

        JLabel introductionLabel = new JLabel("<html>Welcome to Sorting Visualization Tool!<br><br>"
                + "Sorting is an essential concept in computer science and algorithms.<br>"
                + "With this tool, you can visualize various sorting algorithms in action.<br>"
                + "Observe how each algorithm rearranges elements to achieve the final sorted order.<br>"
                + "Select a sorting algorithm from the list below and click Submit to begin.<br><br>"
                + "Enjoy exploring the fascinating world of sorting algorithms!</html>");
        introductionLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        selectionPanel.add(introductionLabel, gbcSelection);

        gbcSelection.gridy++;
        JLabel promptLabel = new JLabel("Please select a sorting algorithm:");
        promptLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        selectionPanel.add(promptLabel, gbcSelection);

        gbcSelection.gridy++;
        gbcSelection.insets = new Insets(0, 0, 70, 0);
        JComboBox<String> sortComboBox = new JComboBox<>();
        sortComboBox.addItem("Bubble");
        sortComboBox.addItem("Selection");
        sortComboBox.addItem("Insertion");
        sortComboBox.addItem("Quick");
        sortComboBox.addItem("Merge");
        sortComboBox.addItem("Shell");
        sortComboBox.setPreferredSize(new Dimension(300, 30));
        selectionPanel.add(sortComboBox, gbcSelection);
        sortComboBox.setSelectedIndex(-1); // No item selected initially

        sortComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedSort = (String) sortComboBox.getSelectedItem();
                selected = selectedSort;
            }
        });

        // Create a JPanel to hold the button
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setBackground(Color.WHITE);

        GridBagConstraints gbcButton = new GridBagConstraints();
        gbcButton.gridx = 0;
        gbcButton.gridy = 0;
        gbcButton.insets = new Insets(30, 0, 30, 0);

        JButton button = new JButton("Submit");
        button.setPreferredSize(new Dimension(100, 40));
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selected == null){
                    JOptionPane.showMessageDialog(null, "Choose a type of sort before submit");
                    getContentPane().removeAll();
                    getContentPane().repaint();
                    new MainMenu();
                }
                else {
                    getContentPane().removeAll();
                    getContentPane().repaint();
                    switch (selected) {
                        case "Bubble":
                            new BubbleSortFrame().setVisible(true);
                            setVisible(false); // Hide the main menu
                            break;
                        case "Selection":
                            new SelectionSortFrame().setVisible(true);
                            setVisible(false); // Hide the main menu
                            break;
                        case "Insertion":
                            new InsertionSortFrame().setVisible(true);
                            setVisible(false); // Hide the main menu
                            break;
                        case "Quick":
                            new QuickSortFrame().setVisible(true);
                            setVisible(false); // Hide the main menu
                            break;
                        case "Merge":
                            new MergeSortFrame().setVisible(true);
                            setVisible(false); // Hide the main menu
                            break;
                        case "Shell":
                            //new ShellSortFrame().setVisible(true);
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
        buttonPanel.add(button, gbcButton);

        // Add the panels to the center container with vertical spacing
        center.setLayout(new GridBagLayout());
        GridBagConstraints gbcCenter = new GridBagConstraints();
        gbcCenter.gridx = 0;
        gbcCenter.gridy = 0;
        gbcCenter.fill = GridBagConstraints.HORIZONTAL;
        gbcCenter.anchor = GridBagConstraints.CENTER;
        gbcCenter.insets = new Insets(10, 10, 10, 10);

        center.add(titlePanel, gbcCenter);

        gbcCenter.gridy++;
        center.add(selectionPanel, gbcCenter);

        gbcCenter.gridy++;
        center.add(buttonPanel, gbcCenter);

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
                + "Here are the steps to use this visualization:\n\n"
                + "1. Choose a sorting algorithm from the drop-down menu.\n"
                + "2. Click the 'Submit' button to start the visualization.\n"
                + "3. Observe the sorting process as the algorithm sorts the data.\n\n"
                + "You can visualize popular sorting algorithms such as Bubble Sort, Selection Sort, Insertion Sort, Quick Sort, and Merge Sort. "
                + "Understanding sorting algorithms visually can enhance your comprehension and analysis of their performance.");

        helpText.setFont(new Font("Arial", Font.PLAIN, 14)); // Increased font size
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
                + "algorithmic efficiency and performance. Sorting algorithms included in this visualization are Bubble Sort, "
                + "Selection Sort, Insertion Sort, Quick Sort, and Merge Sort.");

        aboutText.setFont(new Font("Arial", Font.PLAIN, 14)); // Increased font size
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
        mainScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        mainScrollPane.setBackground(Color.WHITE); // Set background color of the main scroll pane

        center.setLayout(new BorderLayout()); // Set layout for center panel
        center.add(mainScrollPane, BorderLayout.CENTER); // Add main scroll pane to center panel

        center.revalidate(); // Revalidate the center panel
        center.repaint(); // Repaint the center panel

        // Scroll to the top
        mainScrollPane.getVerticalScrollBar().setValue(0);
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
        mainScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        mainScrollPane.setBackground(Color.WHITE); // Set background color of the main scroll pane

        center.setLayout(new BorderLayout()); // Set layout for center panel
        center.add(mainScrollPane, BorderLayout.CENTER); // Add main scroll pane to center panel

        center.revalidate(); // Revalidate the center panel
        center.repaint(); // Repaint the center panel
        // Scroll to the top
        mainScrollPane.getVerticalScrollBar().setValue(0);
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
        mainScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        mainScrollPane.setBackground(Color.WHITE); // Set background color of the main scroll pane

        center.setLayout(new BorderLayout()); // Set layout for center panel
        center.add(mainScrollPane, BorderLayout.CENTER); // Add main scroll pane to center panel

        center.revalidate(); // Revalidate the center panel
        center.repaint(); // Repaint the center panel

        // Scroll to the top
        mainScrollPane.getVerticalScrollBar().setValue(0);
    }


}

