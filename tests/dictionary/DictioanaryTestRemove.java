package dictionary;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class DictioanaryTestRemove {

	
	public DictioanaryTestRemove(Object[] values) {
		this.values = values;
		dictionary = new Dictionary();
		dictionary.add("a", "1");
		dictionary.add("b", Arrays.asList(new String[] {"1", "2", "3"}));
		dictionary.add("c", Arrays.asList(new String[] {"3", "4", "5"}));
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
	
	private Object[] values;
	
	private Dictionary dictionary;
	
	@Test
	public void testRemove() {
//		for (int i = 0; i < values.length; i += 2) {
//			dictionary.add(values[i], values[i + 1]);
//		}
//		
//		for (int i = 0; i < values.length; i += 2) {
//			assertTrue(dictionary.contains(values[i], values[i + 1]));
//		}
	}
}
