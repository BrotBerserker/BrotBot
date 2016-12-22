package commands.bindings;

import commands.base.BasicCommand;
import exceptions.CommandExecutionException;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import properties.PropertiesHandler;
import properties.PropertiesManager;

/**
 * Removes a chat binding. !unbind key
 *
 * @author Sigi
 *
 */
public class UnbindCommand extends BasicCommand {

	@Override
	public String getName() {
		return "unbind";
	}

	@Override
	public String getShortDescription() {
		return "Removes a chat binding.";
	}

	@Override
	public String execute(MessageReceivedEvent event, String... parameters) {
		try {
			PropertiesHandler bindings = PropertiesManager.getChatBindingsForGuild(event.getGuild());
			boolean removed = bindings.remove(parameters[0].trim());
			return removed ? "Removed binding for \"" + parameters[0].trim() + "\"!" : "There was no binding to remove!";
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
