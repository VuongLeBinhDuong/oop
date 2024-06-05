
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;

public class ButtonPanel extends JPanel
{
	public static final long serialVersionUID = 1L;
	private static final int BUTTON_WIDTH = 200, BUTTON_HEIGHT = 150;
	private JLabel[] buttons;
	private SortButtonListener listener;
	private int number = 5;

	public ButtonPanel(SortButtonListener listener, String sort_type)
	{
		super();

		this.listener = listener;

		buttons = new JLabel[number];
		for (int i = 0; i < buttons.length; i++)
			buttons[i] = new JLabel();

		initButtons(buttons[0], "create_button", 0);

		if (sort_type.equals("bubble")) initButtons(buttons[1], "bubble_sort_button", 1);
		else if (sort_type.equals("selection")) initButtons(buttons[1], "selection_button", 1);
		else if (sort_type.equals("merge")) initButtons(buttons[1], "merge_button", 1);
		else if (sort_type.equals("quick")) initButtons(buttons[1], "quick_button", 1);
		else if (sort_type.equals("insertion")) initButtons(buttons[1], "insertion_button", 1);
		else if (sort_type.equals("shell")) initButtons(buttons[1], "shell_button", 1);

		initButtons(buttons[2], "stop_button", 3);
		initButtons(buttons[3], "continue_button", 4);
		initButtons(buttons[4], "back_button", 2);

		// add button to the panel
		setLayout(null);
		for (int i = 0; i < buttons.length; i++)
		{
			buttons[i].setBounds(50,  50 + (BUTTON_HEIGHT + 5) * i, BUTTON_WIDTH, BUTTON_HEIGHT);
			add(buttons[i]);
		}
	}


	private void initButtons(JLabel button, String name, int id)
	{
		button.setIcon(new ImageIcon(String.format("./src/buttons/%s.png", name)));
		button.addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent e) {}

			public void mouseEntered(MouseEvent e) {
				button.setIcon(new ImageIcon(String.format("./src/buttons/%s_entered.png", name)));
			}

			public void mouseExited(MouseEvent e) {
				button.setIcon(new ImageIcon(String.format("./src/buttons/%s.png", name)));
			}

			public void mousePressed(MouseEvent e) {
				button.setIcon(new ImageIcon(String.format("./src/buttons/%s_pressed.png", name)));
			}

			public void mouseReleased(MouseEvent e) {
				listener.sortButtonClicked(id);
				button.setIcon(new ImageIcon(String.format("./src/buttons/%s_entered.png", name)));
			}
		});
	}


	public interface SortButtonListener
	{
		void sortButtonClicked(int id);
	}
}
