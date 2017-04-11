package at.redeye.FrameWork.widgets.datetime.impl;

import javax.swing.JTextField;

import at.redeye.FrameWork.widgets.datetime.IDateTimeComponent;

public class CommonDateTimeComponent extends JTextField implements
		IDateTimeComponent {

	@Override
	public String getDate() {
		return getText();
	}

	@Override
	public void setDate(String dateStr) {
		setText(dateStr);
	}

}
