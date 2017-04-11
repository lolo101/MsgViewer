package at.redeye.FrameWork.widgets.datetime;

import java.awt.Font;

public interface IDateTimeComponent {

	public String getDate();

	public void setDate(String dateStr);

	public void setFont(Font font);

	public void setName(String name);

	public String getName();

}
