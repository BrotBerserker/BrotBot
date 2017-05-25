package commands.karma;

import java.util.List;
import java.util.Map.Entry;

import commands.base.BasicCommand;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import properties.PropertiesHandler;
import properties.PropertiesManager;

/**
 * Shows the server's Karma Leaderboard.
 *
 * @author Sigi
 *
 */
public class KarmaStatsCommand extends BasicCommand {

	@Override
	public String getName() {
		return "karmastats";
	}

	@Override
	public String getShortDescription() {
		return "Shows the server's Karma Leaderboard.";
	}

	@Override
	public String getLongDescription() {
		return "Users and their Karma points are displayed in a descending order. "
				+ "Only those users who already gained some Karma points or who used !karma at least once are mentioned in the leaderboad.";
	}

	@Override
	public String execute(MessageReceivedEvent event, String... parameters) throws Exception {
		PropertiesHandler karma = PropertiesManager.getKarmaForGuild(event.getGuild());
		List<Entry<String, String>> entries = karma.getEntriesSortedByIntValues(false);
		StringBuilder bob = new StringBuilder("```Markdown\n# Karma Stats for " + event.getGuild().getName() + ": #");
		int i = 1;
		for (Entry<String, String> entry : entries) {
			bob.append("\n" + (i++) + ". " + entry.getKey() + ": " + entry.getValue());
		}
		bob.append("```");
		return bob.toString();
	}

	@Override
	public String getCategory() {
		return "Karma";
	}
}
