package at.redeye.FrameWork.widgets.datetime;

import java.awt.Font;

public interface IDateTimeComponent {

	String getDate();

	void setDate(String dateStr);

	void setFont(Font font);

	void setName(String name);

	String getName();

}
