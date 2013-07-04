import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Path;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class Test {
	public static void main(final String[] args) throws Exception {
		final Display display = Display.getDefault();
		final Shell shell = new Shell(display);
		final Canvas canvas = new org.eclipse.swt.widgets.Canvas(shell, SWT.NONE);

		canvas.addPaintListener(new PaintListener() {
			private int	height;
			private int	width;
			private float	xfactor;
			private int	xoff;
			private float	yfactor;
			private int	yoff;

			@Override
			public void paintControl(final PaintEvent e) {
				final GC gc = e.gc;
				width = canvas.getSize().x;
				height = canvas.getSize().y;
				xoff = width/2;
				yoff = height/2;
				xfactor = (float) (width/200.0);
				yfactor = (float) (height/200.0);

				gc.drawLine(x(-100), y(0), x(100), y(0));
				gc.drawLine(x(0), y(-100), x(0), y(100));

				gc.drawOval(x(-100)-2, y(-100)-2, 4, 4);
				gc.drawOval(x(-75)-2, y(-25)-2, 4, 4);
				gc.drawOval(x(0)-2, y(0)-2, 4, 4);
				gc.drawOval(x(75)-2, y(25)-2, 4, 4);

				gc.drawLine(x(-100), y(-100), x(-75), y(-25));
				gc.drawLine(x(75), y(25), x(0), y(0));

				gc.drawOval(x(-50)-2, y(50)-2, 4, 4);
				gc.drawLine(x(-100), y(-100), x(-50), y(50));


				Path path=null;

				try {
					path = new Path(display);

					path.moveTo(x(-100), y(-100));
					path.cubicTo(x(-75), y(-25), x(0), y(0), x(75), y(25));

					path.moveTo(x(-100), y(-100));
					path.cubicTo(x(-50), y(50), x(0), y(0), x(75), y(25));

					gc.drawPath(path);
				} finally {
					if (path!= null) {
						path.dispose();
					}
				}
			}

			private int x(final float x) {
				return (int) (xoff + x*xfactor);
			}

			private int y(final float y) {
				return (int) (yoff - y*yfactor);
			}
		});

		shell.setLayout(new FillLayout());
		shell.setText("Spline Test");
		shell.setSize(800, 600);
		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}
}