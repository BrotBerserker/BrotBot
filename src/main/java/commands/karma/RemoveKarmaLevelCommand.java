package commands.karma;

import commands.base.BasicCommand;
import exceptions.CommandExecutionException;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import properties.PropertiesHandler;
import properties.PropertiesManager;

/**
 * Removes a Karma level.
 *
 * @author Sigi
 *
 */
public class RemoveKarmaLevelCommand extends BasicCommand {

	@Override
	public String getName() {
		return "removekarmalevel";
	}

	@Override
	public String getShortDescription() {
		return "Removes a Karma level.";
	}

	@Override
	public String getExampleUsage() {
		return "!removekarmalevel 100";
	}

	@Override
	public String execute(MessageReceivedEvent event, String... parameters) {
		try {
			PropertiesHandler karmaLevels = PropertiesManager.getKarmaLevelsForGuild(event.getGuild());
			boolean removed = karmaLevels.remove(parameters[0]);
			return removed ? "Removed Karma level!" : "There was nothing to remove!";
		} catch (Exception e) {
			throw new CommandExecutionException(e);
		}
	}

	@Override
	public int getRequiredParameterCount() {
		return 1;
	}

	@Override
	public boolean isExecutable(MessageReceivedEvent event, String... parameters) {
		try {
			Integer.parseInt(parameters[0]);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

	@Override
	public String getErrorMessage() {
		return "Please provide a whole number as parameter!";
	}

	@Override
	public String getRequiredRole() {
		return "BrotCommander";
	}

	@Override
	public String getCategory() {
		return "Karma";
	}
}
