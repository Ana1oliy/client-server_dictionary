package dictionary;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

public class Dictionary {
	
	public Dictionary () {
		storage = new ConcurrentHashMap<>();
	}

	private Map<String, Set<String>> storage;
	
	/**
	 * Adds single value to the dictionary if it is not already present for key.
	 * @param key is the key word to add new value.
	 * @param value value to add.
	 */
	public void add(String key, String value) {
		ensureEntry(key).add(value);
	}
	
	/**
	 * Sets collection of words to the dictionary. If word in the specified 
	 * collection is already present for key this word will be skipped.
	 * @param key is the key word to add values from the collection.
	 * @param vales collection to add.
	 */
	public void add(String key, Collection<String> vales) {
		ensureEntry(key).addAll(vales);
	}
	
	/**
	 * Returns the values set which is mapped for specified key.
	 * @param key key for which mapped set of words will be returned.
	 * @return Set of words for specified key.
	 */
	public Set<String> get(String key) {
		return new HashSet<>(storage.get(key));
	}
	
	public boolean delete(String key, String value) {
		if (storage.containsKey(key)) {
			return storage.get(key).remove(value);
		}
		
		return false;
	}
	
	public boolean delete(String key, Collection<String> values) {
		if (storage.containsKey(key)) {
			Set<String> storedValues = storage.get(key);
			return values.stream()
					.map(value -> storedValues.remove(value))
					.reduce(false, (total, current) -> total || current);
		}
		
		return false;
	}
	
	public boolean contains(String key, String value) {
		if (storage.containsKey(key)) {
			return storage.get(key).contains(value);
		} else {
			return false;
		}
	}
	
	private Set<String> ensureEntry(String key) {
		if (!storage.containsKey(key)) {
			storage.put(key, new ConcurrentSkipListSet<>());
		}
		
		return storage.get(key);
	}
}
