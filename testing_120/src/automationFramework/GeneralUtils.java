package automationFramework;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Date;

public class GeneralUtils {
	public static DateTimeFormatter formatter = new DateTimeFormatterBuilder()
			.appendPattern("dd").appendLiteral("/")
			.appendPattern("MM").appendLiteral("/")
			.appendPattern("yyyy").appendLiteral(" ")
			.appendPattern("HH").appendLiteral(":")
			.appendPattern("mm").appendLiteral(":")
			.appendPattern("ss").appendLiteral(":")
			.appendPattern("SSS  - ")
			.toFormatter();
	
	public static Date date = new Date();
	public static SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-YYYY_HH-mm"); //   dd/MM/yyyy HH:mm:ss
}
