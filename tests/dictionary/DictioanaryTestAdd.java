package dictionary;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class DictioanaryTestAdd {

	
	public DictioanaryTestAdd(String[] values) {
		this.values = values;
		dictionary = new Dictionary();
	}
	
	@Parameters
	public static List<Object[]> params() {
		return Arrays.asList(new Object[][] {
			{new String[] {
					"a", "1"
			}},
			{new String[] {
					"b", "2",
					"b", "3"
			}},
			{new String[] {
					"c", "1",
					"d", "2",
					"d", "3",
					"e", "4"
			}}
		});
	}
	
	private String[] values;
	
	private Dictionary dictionary;
	
	@Test
	public void testAdd() {
		for (int i = 0; i < values.length; i += 2) {
			dictionary.add(values[i], values[i + 1]);
		}
		
		for (int i = 0; i < values.length; i += 2) {
			assertTrue(dictionary.contains(values[i], values[i + 1]));
		}
	}
}
