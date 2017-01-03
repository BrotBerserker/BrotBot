package properties;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import net.dv8tion.jda.core.entities.Guild;

/**
 * Manages {@link PropertiesHandler} objects in a map.
 *
 * @author Sigi
 *
 */
public class PropertiesManager {

	private static Map<String, PropertiesHandler> handlerMap = new HashMap<String, PropertiesHandler>();

	/**
	 * Gets the {@link PropertiesHandler} for the given file name. Only creates
	 * a new object if this is the first time the properties are accessed.
	 * Returns a previously created object otherwise.
	 *
	 * @param fileName
	 *
	 * @return The {@link PropertiesHandler}
	 * @throws URISyntaxException
	 * @throws IOException
	 */
	public static PropertiesHandler getPropertiesHandler(String fileName) throws URISyntaxException, IOException {
		if (handlerMap.containsKey(fileName)) {
			return handlerMap.get(fileName);
		}
		PropertiesHandler handler = new PropertiesHandler(fileName);
		handlerMap.put(fileName, handler);
		return handler;
	}

	/**
	 * Returns a {@link PropertiesHandler} for handling a {@link Guild}'s
	 * chatbindings.
	 *
	 * @param guild
	 *            The {@link Guild}
	 * @return The {@link PropertiesHandler}
	 * @throws URISyntaxException
	 * @throws IOException
	 */
	public static PropertiesHandler getChatBindingsForGuild(Guild guild) throws URISyntaxException, IOException {
		return getPropertiesHandler("servers/" + uniqueGuildName(guild) + "/ChatBindings");
	}

	/**
	 * Returns a {@link PropertiesHandler} for handling a {@link Guild}'s regex
	 * bindings.
	 *
	 * @param guild
	 *            The {@link Guild}
	 * @return The {@link PropertiesHandler}
	 * @throws URISyntaxException
	 * @throws IOException
	 */
	public static PropertiesHandler getRegexBindingsForGuild(Guild guild) throws URISyntaxException, IOException {
		return getPropertiesHandler("servers/" + uniqueGuildName(guild) + "/RegexBindings");
	}

	/**
	 * Returns a {@link PropertiesHandler} for handling a {@link Guild}'s Karma
	 * config.
	 *
	 * @param guild
	 *            The {@link Guild}
	 * @return The {@link PropertiesHandler}
	 * @throws URISyntaxException
	 * @throws IOException
	 */
	public static PropertiesHandler getKarmaConfigForGuild(Guild guild) throws URISyntaxException, IOException {
		return getPropertiesHandler("servers/" + uniqueGuildName(guild) + "/KarmaConfig");
	}

	/**
	 * Returns a {@link PropertiesHandler} for handling a {@link Guild}'s Karma
	 * points.
	 *
	 * @param guild
	 *            The {@link Guild}
	 * @return The {@link PropertiesHandler}
	 * @throws URISyntaxException
	 * @throws IOException
	 */
	public static PropertiesHandler getKarmaForGuild(Guild guild) throws URISyntaxException, IOException {
		return getPropertiesHandler("servers/" + uniqueGuildName(guild) + "/Karma");
	}

	/**
	 * Returns a {@link PropertiesHandler} for handling a {@link Guild}'s Karma
	 * levels.
	 *
	 * @param guild
	 *            The {@link Guild}
	 * @return The {@link PropertiesHandler}
	 * @throws URISyntaxException
	 * @throws IOException
	 */
	public static PropertiesHandler getKarmaLevelsForGuild(Guild guild) throws URISyntaxException, IOException {
		return getPropertiesHandler("servers/" + uniqueGuildName(guild) + "/KarmaLevels");
	}

	private static String uniqueGuildName(Guild guild) {
		return guild.getName() + " - " + guild.getId();
	}

}
