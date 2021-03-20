package drawing;

import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class MyFrame extends JFrame {
	public MyFrame(int rows, int columns, int span, int margin, ArrayList<ArrayList<Point>> toRender) {
		super("Płytka PCB");
		JPanel panel = new MyPanel(rows, columns, span, margin, toRender);

		add(panel);

		pack();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
}

