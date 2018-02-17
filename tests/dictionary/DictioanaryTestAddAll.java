package dictionary;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class DictioanaryTestAddAll {

	
	public DictioanaryTestAddAll(Object[] values) {
		this.values = values;
		dictionary = new Dictionary();
	}
	
	@Parameters
	public static List<Object[]> params() {
		return Arrays.asList(new Object[][] {
			{new Object[] {
					"a", Arrays.asList(new String[] {"1", "2", "3"})
			}},
			{new Object[] {
					"b", Arrays.asList(new String[] {"1", "2", "3"}),
					"b", Arrays.asList(new String[] {"3", "4", "5"})
			}},
			{new Object[] {
					"c", Arrays.asList(new String[] {"1"}),
					"d", Arrays.asList(new String[] {"2", "3", "4"}),
					"d", Arrays.asList(new String[] {"3", "5", "6"}),
					"e", Arrays.asList(new String[] {"7", "8", "9"})
			}}
		});
	}
	
	private Object[] values;
	
	private Dictionary dictionary;
	
	@SuppressWarnings("unchecked")
	@Test
	public void testAdd() {
		for (int i = 0; i < values.length; i += 2) {
			dictionary.add((String) values[i], (Collection<String>) values[i + 1]);
		}
		
		for (int i = 0; i < values.length; i += 2) {
			String key = (String) values[i];
			Collection<String> words = (Collection<String>) values[i + 1];
			words.forEach(word -> assertTrue(dictionary.contains(key, word)));
		}
	}
}
