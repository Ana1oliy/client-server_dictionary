package dictionary.commands;

import java.io.Serializable;

import dictionary.Dictionary;

public class DeleteCommand implements Serializable, DictionaryCommand {
	
	private static final long serialVersionUID = 1L;

	public DeleteCommand(String key, String word) {
		this.key = key;
		this.word = word;
	}
	
	private String key;
	
	private String word;

	@Override
	public String run(Dictionary dictionary) {
		if (dictionary.delete(key, word)) {
			return "<значения слова успешно удалены>";
		} else {
			return "<слово/значение отсутвует в словаре>";
		}
	}
}
