package dictionary.commands;

import java.io.Serializable;
import java.util.Set;

import dictionary.Dictionary;

public class GetCommand implements Serializable, DictionaryCommand {

	private static final long serialVersionUID = 1L;

	public GetCommand(String key) {
		this.key = key;
	}
	
	private String key;

	@Override
	public String run(Dictionary dictionary) {
		Set<String> words = dictionary.get(key);
		if (words.isEmpty()) {
			return "<слово отсутвует в словаре>";
		} else {
			return String.join("\n", words);
		}
	}
}
