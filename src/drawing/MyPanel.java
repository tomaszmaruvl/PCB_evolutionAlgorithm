package drawing;

import java.awt.*;
import java.awt.geom.*;
import java.util.ArrayList;

import javax.swing.JPanel;

public class MyPanel extends JPanel {

	public Color[] colors = { Color.BLACK, Color.BLUE, Color.GREEN, Color.ORANGE, Color.PINK, Color.RED,
			new Color(0, 255, 255), new Color(179, 99, 238), new Color(96, 49, 45), Color.MAGENTA };
	public int rows;
	public int columns;
	public int grid_span;
	public int margin;
	public int row_lenght;
	public int column_length;
	public ArrayList<ArrayList<Point>> toRender;

	public MyPanel(int rows, int columns, int grid_span, int margin, ArrayList<ArrayList<Point>> toRender) {
		this.row_lenght = columns * grid_span;
		this.column_length = rows * grid_span;
		int height = column_length + 2 * margin;
		int width = row_lenght + 2 * margin;
		setPreferredSize(new Dimension(height, width));
		this.grid_span = grid_span;
		this.rows = rows;
		this.columns = columns;
		this.margin = margin;
		this.toRender = toRender;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Stroke dashed = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[] { 2 }, 0);
		Stroke normal = new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setStroke(dashed);

		for (int i = 0; i <= columns; i++) {
			Line2D line = new Line2D.Double(margin, margin + i * grid_span, margin + column_length,
					margin + i * grid_span);
			g2d.draw(line);
		}

		for (int i = 0; i <= rows; i++) {
			Line2D line = new Line2D.Double(margin + i * grid_span, margin, margin + i * grid_span,
					margin + row_lenght);
			g2d.draw(line);
		}

		g2d.setStroke(normal);

		for (int i = 0; i < toRender.size(); i++) {
			g2d.setColor(this.colors[i]);
			ArrayList<Point> trackToRender = toRender.get(i);
			for (int j = 1; j < trackToRender.size(); j++) {
				Point previousPoint = trackToRender.get(j - 1);
				Point actual = trackToRender.get(j);
				if (j == 1) g2d.draw(getDotFromCoordinates(previousPoint));
				if(j == trackToRender.size()-1) g2d.draw(getDotFromCoordinates(actual));
				g2d.draw(getLineFromCoordinates(previousPoint, actual));
			}
		}
	}

	public Line2D getLineFromCoordinates(Point p1, Point p2) {
		int x1 = p1.x;
		int y1 = p1.y;
		int x2 = p2.x;
		int y2 = p2.y;
		return new Line2D.Double(margin + x1 * grid_span, margin + y1 * grid_span, margin + x2 * grid_span,
				margin + y2 * grid_span);
	}

	public Ellipse2D getDotFromCoordinates(Point point) {
		int r = grid_span/2;
		return new Ellipse2D.Double(margin -r/2 + point.x  * grid_span, margin -r/2 + point.y * grid_span,r, r);
	}
}