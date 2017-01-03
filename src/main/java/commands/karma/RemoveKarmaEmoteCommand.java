/**
 *
 */
package commands.karma;

import commands.base.BasicCommand;
import exceptions.CommandExecutionException;
import net.dv8tion.jda.core.entities.Emote;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import properties.PropertiesHandler;
import properties.PropertiesManager;

/**
 * Removes an emote from the list of Karma emotes.
 *
 * @author Sigi
 *
 */
public class RemoveKarmaEmoteCommand extends BasicCommand {

	@Override
	public String getName() {
		return "removekarmaemote";
	}

	@Override
	public String getShortDescription() {
		return "Removes an emote from the list of Karma emotes.";
	}

	@Override
	public String getExampleUsage() {
		return "!removekarmaemote :pogchamp:";
	}

	@Override
	public String execute(MessageReceivedEvent event, String... parameters) {
		try {
			Emote emote = getEmote(event, parameters[0]);
			PropertiesHandler karmaEmotes = PropertiesManager.getKarmaEmotesForGuild(event.getGuild());
			boolean removed = karmaEmotes.remove(emote.toString());
			return removed ? "Removed Karma emote!" : "There was nothing to remove!";
		} catch (Exception e) {
			throw new CommandExecutionException(e);
		}
	}

	@Override
	public boolean isExecutable(MessageReceivedEvent event, String... parameters) {
		return getEmote(event, parameters[0]) != null;
	}

	@Override
	public String getErrorMessage() {
		return "Please specify a custom emote as parameter!";
	}

	private Emote getEmote(MessageReceivedEvent event, String str) {
		String emoteID = str.substring(str.lastIndexOf(":") + 1, str.length() - 1);
		Emote emote = event.getGuild().getEmoteById(emoteID);
		return emote;
	}

	@Override
	public int getRequiredParameterCount() {
		return 1;
	}

	@Override
	public String getRequiredRole() {
		return "BrotCommander";
	}

}
