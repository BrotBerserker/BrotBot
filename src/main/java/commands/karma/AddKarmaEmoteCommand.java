package commands.karma;

import commands.base.BasicCommand;
import exceptions.CommandExecutionException;
import net.dv8tion.jda.core.entities.Emote;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import properties.PropertiesHandler;
import properties.PropertiesManager;

/**
 * Makes a reaction emote give a message's author Karma points.
 *
 * @author Sigi
 *
 */
public class AddKarmaEmoteCommand extends BasicCommand {

	@Override
	public String getName() {
		return "addkarmaemote";
	}

	@Override
	public String getShortDescription() {
		return "Makes a reaction emote give the author Karma points.";
	}

	@Override
	public String getLongDescription() {
		return "Currently only works with custom (server specific) emojis.";
	}

	@Override
	public String execute(MessageReceivedEvent event, String... parameters) {
		try {
			Emote emote = getEmote(event, parameters[0]);
			PropertiesHandler karmaConfig = PropertiesManager.getKarmaConfigForGuild(event.getGuild());
			karmaConfig.add(emote.toString(), parameters[1].trim());
			return "Reaction " + emote.getAsMention() + " now gives " + parameters[1].trim() + " Karma!";
		} catch (Exception e) {
			throw new CommandExecutionException(e);
		}
	}

	@Override
	public int getRequiredParameterCount() {
		return 2;
	}

	@Override
	public boolean isExecutable(MessageReceivedEvent event, String... parameters) {
		try {
			Integer.parseInt(parameters[1].trim());
		} catch (Exception e) {
			return false;
		}

		Emote emote = getEmote(event, parameters[0]);
		if (emote == null) {
			return false;
		}

		return true;
	}

	private Emote getEmote(MessageReceivedEvent event, String str) {
		String emoteID = str.substring(str.lastIndexOf(":") + 1, str.length() - 1);
		Emote emote = event.getGuild().getEmoteById(emoteID);
		return emote;
	}

	@Override
	public String getErrorMessage() {
		return "Please specify a custom emote as first and a whole number as second parameter!";
	}

	@Override
	public String getRequiredRole() {
		return "BrotCommander";
	}

	@Override
	public String getExampleUsage() {
		return "!addkarmaemote :pogchamp: 5";
	}

}
