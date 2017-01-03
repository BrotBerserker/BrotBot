package commands.karma;

import java.util.List;
import java.util.Map.Entry;

import commands.base.BasicCommand;
import exceptions.CommandExecutionException;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import properties.PropertiesHandler;
import properties.PropertiesManager;

public class KarmaLevelsCommand extends BasicCommand {

	@Override
	public String getName() {
		return "karmalevels";
	}

	@Override
	public String getShortDescription() {
		return "Displays the list of Karma levels.";
	}

	@Override
	public String execute(MessageReceivedEvent event, String... parameters) {
		try {
			PropertiesHandler karmaLevels = PropertiesManager.getKarmaLevelsForGuild(event.getGuild());
			List<Entry<String, String>> entries = karmaLevels.getEntriesSortedByIntKeys(false);
			StringBuilder bob = new StringBuilder("**~ Karma Levels for " + event.getGuild().getName() + ": ~**");
			for (Entry<String, String> entry : entries) {
				bob.append("\n" + entry.getKey() + ": " + entry.getValue());
			}
			return bob.toString();
		} catch (Exception e) {
			throw new CommandExecutionException(e);
		}
	}

}
