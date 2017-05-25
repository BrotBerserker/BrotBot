package commands.karma;

import commands.base.BasicCommand;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import properties.PropertiesHandler;
import properties.PropertiesManager;

/**
 * Displays your current Karma.
 *
 * @author Sigi
 *
 */
public class KarmaCommand extends BasicCommand {

	@Override
	public String getName() {
		return "karma";
	}

	@Override
	public String getShortDescription() {
		return "Displays your current Karma.";
	}

	@Override
	public String execute(MessageReceivedEvent event, String... parameters) throws Exception {
		PropertiesHandler karma = PropertiesManager.getKarmaForGuild(event.getGuild());
		String karmaString = karma.get(event.getAuthor().getName() + "#" + event.getAuthor().getDiscriminator());
		int userKarma = karmaString != null ? Integer.parseInt(karmaString) : 0;
		return event.getAuthor().getAsMention() + ", du hast sage und schreibe **" + userKarma + "** Karma!";

	}

	@Override
	public String getCategory() {
		return "Karma";
	}

}
