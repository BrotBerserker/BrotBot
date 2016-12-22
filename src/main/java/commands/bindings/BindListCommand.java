package commands.bindings;

import commands.base.BasicCommand;
import exceptions.CommandExecutionException;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import properties.PropertiesHandler;
import properties.PropertiesManager;

/**
 * Shows a server's chat bindings.
 *
 * @author Sigi
 *
 */
public class BindListCommand extends BasicCommand {

	@Override
	public String getName() {
		return "bindlist";
	}

	@Override
	public String getShortDescription() {
		return "Shows a server's chat bindings.";
	}

	@Override
	public String execute(MessageReceivedEvent event, String... parameters) {
		try {

			StringBuilder bob = new StringBuilder("**~ Bindings for " + event.getGuild().getName() + ": ~**");
			PropertiesHandler bindings = PropertiesManager.getChatBindingsForGuild(event.getGuild());
			for (String binding : bindings.getKeys()) {
				bob.append("\n🗒  " + binding);
			}
			PropertiesHandler regexBindings = PropertiesManager.getRegexBindingsForGuild(event.getGuild());
			bob.append("\n\n**~ Regex bindings: ~**");
			for (String regexBinding : regexBindings.getKeys()) {
				bob.append("\n⚙ " + regexBinding);
			}
			return bob.toString();
		} catch (Exception e) {
			throw new CommandExecutionException(e);
		}
	}

}
