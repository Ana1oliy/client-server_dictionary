package dictionary.commands;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;

import dictionary.Dictionary;

public class AddCommand implements Serializable, DictionaryCommand {

	private static final long serialVersionUID = 1L;

	public AddCommand(String key, Collection<String> words) {
		this.key = key;
		this.words = (String[]) words.toArray();
	}
	
	private String key;
	
	private String[] words;

	@Override
	public String run(Dictionary dictionary) {
		dictionary.add(key, Arrays.asList(words));
		return "<значения слова успешно добавлены>";
	}
}
