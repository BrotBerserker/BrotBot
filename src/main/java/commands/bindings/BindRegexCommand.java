package commands.bindings;

import commands.base.BasicCommand;
import exceptions.CommandExecutionException;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import properties.PropertiesHandler;
import properties.PropertiesManager;

/**
 * Adds a new regex binding. !bindregex regex=value
 *
 * @author Sigi
 *
 */
public class BindRegexCommand extends BasicCommand {

	@Override
	public String getName() {
		return "bindregex";
	}

	@Override
	public String getShortDescription() {
		return "Adds a new regex chat binding.";
	}

	@Override
	public String getLongDescription() {
		return "Similar to !bind, but the first parameter can be a regular expression. "
				+ "If a message that fits the given regular expression is sent, the Bot will respond with the text specified as the second parameter.";
	}

	@Override
	public String getExampleUsage() {
		return "!bindregex .*(deutschland|Deutschland).* = That's a Nudelholz!";
	}

	@Override
	public String execute(MessageReceivedEvent event, String... parameters) {
		try {
			PropertiesHandler bindings = PropertiesManager.getRegexBindingsForGuild(event.getGuild());
			boolean updated = bindings.add(parameters[0].trim(), parameters[1].trim());
			return "Regex binding for \"" + parameters[0].trim() + "\" " + (updated ? "updated!" : "created!");
		} catch (Exception e) {
			throw new CommandExecutionException(e);
		}
	}

	@Override
	public int getRequiredParameterCount() {
		return 2;
	}

	@Override
	public char getParameterDelimiter() {
		return '=';
	}

	@Override
	public String getRequiredRole() {
		return "BrotCommander";
	}

}
