package commands.karma;

import java.util.List;
import java.util.Map.Entry;

import commands.base.BasicCommand;
import exceptions.CommandExecutionException;
import net.dv8tion.jda.core.entities.Emote;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import properties.PropertiesHandler;
import properties.PropertiesManager;

/**
 * Displays a list of emotes that give Karma points.
 *
 * @author Sigi
 *
 */
public class KarmaEmotesCommand extends BasicCommand {

	@Override
	public String getName() {
		return "karmaemotes";
	}

	@Override
	public String getShortDescription() {
		return "Displays a list of emotes that give Karma points.";
	}

	@Override
	public String execute(MessageReceivedEvent event, String... parameters) {
		try {
			PropertiesHandler karmaEmotes = PropertiesManager.getKarmaEmotesForGuild(event.getGuild());
			List<Entry<String, String>> entries = karmaEmotes.getEntriesSortedByIntValues(false);
			StringBuilder bob = new StringBuilder("**~ Karma Emotes for " + event.getGuild().getName() + ": ~**");
			for (Entry<String, String> entry : entries) {
				Emote emote = event.getGuild().getEmoteById(entry.getKey().substring(entry.getKey().indexOf("(") + 1, entry.getKey().indexOf(")")));
				bob.append("\n" + emote.getAsMention() + ": " + entry.getValue());
			}
			return bob.toString();
		} catch (Exception e) {
			throw new CommandExecutionException(e);
		}
	}

}
