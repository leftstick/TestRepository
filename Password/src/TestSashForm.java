import java.text.Collator;
import java.util.Arrays;
import java.util.Locale;

public class TestSashForm {
	private static final Collator myCollator = Collator
			.getInstance(Locale.CHINA);

	public static void main(String[] args) {
		String[] ss = new String[] { "sdf", "ÌÚÑ¶QQ", "ÌÔ±¦Íø", };
		Arrays.sort(ss, myCollator);
		for (String s : ss) {
			System.out.println(s);
		}
	}
}