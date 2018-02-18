package dictionary.commands;

import java.io.Serializable;
import java.util.Collection;

import dictionary.Dictionary;

public class DeleteCommand implements Serializable, DictionaryCommand {
	
	private static final long serialVersionUID = 1L;

	public DeleteCommand(String key, Collection<String> words) {
		this.key = key;
		this.words = words;
	}
	
	private String key;
	
	private Collection<String> words;

	@Override
	public String run(Dictionary dictionary) {
		if (dictionary.delete(key, words)) {
			return "<значения слова успешно удалены>";
		} else {
			return "<слово/значение отсутвует в словаре>";
		}
	}
}
