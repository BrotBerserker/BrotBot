package properties;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;

import listeners.custom.BindingsListener;
import net.dv8tion.jda.core.entities.Guild;

/**
 * Contains chat bindings for a {@link Guild}. A chat binding is a key/value
 * pair and is used by {@link BindingsListener}. When this listener is
 * active and a user sends a string matching a binding's key, the bot will
 * respond with the binding's value. Example binding: Dies das = Ananas. User
 * writes "Dies das", bot responds with "Ananas."
 *
 * @author Sigi
 *
 */
public class PropertiesHandler {

	private Properties properties;
	private String fileName;
	private File file;

	/**
	 * @param fileName
	 * @throws URISyntaxException
	 * @throws IOException
	 */
	public PropertiesHandler(String fileName) throws URISyntaxException, IOException {
		this.fileName = fileName;
		initializeProperties();
	}

	private void initializeProperties() throws URISyntaxException, IOException {
		properties = new Properties();

		file = new File("src/main/resources/" + fileName + ".properties");

		if (!file.exists()) {
			file.getParentFile().mkdirs();
			file.createNewFile();
		} else {
			properties.load(new FileInputStream(file));
		}
	}

	/**
	 * Adds a chat binding to this guild.
	 *
	 * @param key
	 *            What the user writes
	 * @param value
	 *            What the bot will say in response
	 * @return True if an existing binding has been overwritten.
	 * @throws IOException
	 */
	public boolean add(String key, String value) throws IOException {
		Object old = properties.put(key, value);
		save();
		return old != null;
	}

	/**
	 * Checks if the guild contains a binding for a given command.
	 *
	 * @param key
	 *            The command
	 * @return True if a binding exists
	 */
	public boolean contains(String key) {
		return properties.containsKey(key);
	}

	/**
	 * Gets the binding for the given command.
	 *
	 * @param key
	 *            The command
	 * @return The resulting response
	 */
	public String get(String key) {
		return properties.getProperty(key);
	}

	/**
	 * Removes a binding.
	 *
	 * @param key
	 *            The bindings's key
	 * @return True if a binding has been removed
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public boolean remove(String key) throws FileNotFoundException, IOException {
		Object old = properties.remove(key);
		save();
		return old != null;
	}

	/**
	 * Gets a list of all binding commands for this server
	 *
	 * @return List of bindings
	 */
	public List<String> getKeys() {
		List<String> list = new ArrayList<String>();
		for (Object object : properties.keySet()) {
			list.add(object.toString());
		}
		return list;
	}

	/**
	 * Returns a sorted list of entries. Only use this if all values of the
	 * properties can be parsed to int values.
	 *
	 * @param asc
	 *            true for ascending, false for descending
	 * @return The sorted list
	 */
	public List<Entry<String, String>> getEntriesSortedByIntValues(boolean asc) {
		List<Entry<String, String>> list = new ArrayList<Entry<String, String>>();
		for (Entry<Object, Object> entry : properties.entrySet()) {
			list.add(new AbstractMap.SimpleEntry<String, String>(entry.getKey().toString(), entry.getValue().toString()));
		}
		Collections.sort(list, (Comparator<Entry<String, String>>) (Entry<String, String> e1, Entry<String, String> e2) -> {
			if (asc) {
				return Integer.parseInt(e1.getValue()) - Integer.parseInt(e2.getValue());
			} else {
				return Integer.parseInt(e2.getValue()) - Integer.parseInt(e1.getValue());
			}
		});
		return list;
	}

	private void save() throws IOException, FileNotFoundException {
		properties.store(new FileOutputStream(file), "Chat bindings for " + fileName);
	}

}
