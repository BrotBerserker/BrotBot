package commands.karma;

import commands.base.BasicCommand;
import exceptions.CommandExecutionException;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import properties.PropertiesHandler;
import properties.PropertiesManager;

public class AddKarmaLevelCommand extends BasicCommand {

	@Override
	public String getName() {
		return "addkarmalevel";
	}

	@Override
	public String getShortDescription() {
		return "Adds a Karma level.";
	}

	@Override
	public String getExampleUsage() {
		return "!addkarmalevel 100 = Nice Guy";
	}

	@Override
	public String execute(MessageReceivedEvent event, String... parameters) {
		try {
			PropertiesHandler karmaLevels = PropertiesManager.getKarmaLevelsForGuild(event.getGuild());
			boolean updated = karmaLevels.add(parameters[0], parameters[1]);
			return "Karmalevel \"" + parameters[1] + "\"" + (updated ? "updated!" : "created!");
		} catch (Exception e) {
			throw new CommandExecutionException(e);
		}
	}

	@Override
	public boolean isExecutable(MessageReceivedEvent event, String... parameters) {
		try {
			Integer.parseInt(parameters[0]);
		} catch (NumberFormatException e) {
			return false;
		}

		if (event.getGuild().getRolesByName(parameters[1], true).size() != 1) {
			return false;
		}

		return true;
	}

	@Override
	public String getErrorMessage() {
		return "Please specify a whole number as first and an existing role as second parameter!";
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
