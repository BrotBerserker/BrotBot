package commands.bindings;

import commands.base.BasicCommand;
import exceptions.CommandExecutionException;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import properties.PropertiesHandler;
import properties.PropertiesManager;

/**
 * Removes a regex binding.
 *
 * @author Sigi
 *
 */
public class UnbindRegexCommand extends BasicCommand {

	@Override
	public String getName() {
		return "unbindregex";
	}

	@Override
	public String getShortDescription() {
		return "Removes a regex binding.";
	}

	@Override
	public String execute(MessageReceivedEvent event, String... parameters) {
		try {
			PropertiesHandler bindings = PropertiesManager.getRegexBindingsForGuild(event.getGuild());
			boolean removed = bindings.remove(parameters[0].trim());
			return removed ? "Removed regex binding for \"" + parameters[0].trim() + "\"!" : "There was no binding to remove!";
		} catch (Exception e) {
			throw new CommandExecutionException(e);
		}
	}

	@Override
	public int getRequiredParameterCount() {
		return 1;
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