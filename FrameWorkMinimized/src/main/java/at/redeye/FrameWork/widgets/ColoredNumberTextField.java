package at.redeye.FrameWork.widgets;

import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JTextField;

public class ColoredNumberTextField extends JTextField {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1224028010442945788L;

	public ColoredNumberTextField() {
		super();

		this.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent e) {
				

			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyTyped(KeyEvent e) {
				String tester;
				char c = e.getKeyChar();
				if (c == ',') {
					e.setKeyChar('.');
				}
				tester = String.valueOf(c);
				if (c == KeyEvent.VK_ENTER) {
					tester = ((JTextField) e.getComponent()).getText();
					evaluateFieldAttributes(tester);
				}
				if (c == KeyEvent.VK_BACK_SPACE) {
					
					JTextField myfield = ((JTextField) e.getComponent());
					if (myfield.getText().isEmpty()) 
						myfield.setForeground(Color.BLACK);
				}
				tester = String.valueOf(c);
				if (!tester.matches("[0-9,.-]+")) {
					e.consume();
				}

			}
		});

		this.addFocusListener(new FocusListener() {

			@Override
			public void focusGained(FocusEvent e) {
				
			}

			@Override
			public void focusLost(FocusEvent e) {
				String text = ((JTextField) e.getComponent()).getText();
				setText(text);

			}

		});

		// TODO Auto-generated constructor stub
	}

	@Override
	public void setText(String t) {
		if (evaluateFieldAttributes(t))
			super.setText(t);
	}

	protected boolean evaluateFieldAttributes(String toValidate) {

		double d = 0.0;
		try {
			d = Double.parseDouble(toValidate);
		} catch (NumberFormatException ne) {
			return false;
		}
		if (d > 0) {
			this.setForeground(Color.GREEN);
		} else if (d < 0) {
			this.setForeground(Color.RED);
		}
		return true;
	}

}
