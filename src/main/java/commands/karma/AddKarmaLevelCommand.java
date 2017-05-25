package commands.karma;

import commands.base.BasicCommand;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import properties.PropertiesHandler;
import properties.PropertiesManager;

/**
 * Adds a Karma level.
 *
 * @author Sigi
 *
 */
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
	public String execute(MessageReceivedEvent event, String... parameters) throws Exception {
		PropertiesHandler karmaLevels = PropertiesManager.getKarmaLevelsForGuild(event.getGuild());
		boolean updated = karmaLevels.add(parameters[0].trim(), parameters[1].trim());
		return "Karmalevel \"" + parameters[1].trim() + "\"" + (updated ? " updated!" : " created!");
	}

	@Override
	public boolean isExecutable(MessageReceivedEvent event, String... parameters) {
		try {
			Integer.parseInt(parameters[0].trim());
		} catch (NumberFormatException e) {
			return false;
		}

		if (event.getGuild().getRolesByName(parameters[1].trim(), true).size() != 1) {
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

	@Override
	public String getCategory() {
		return "Karma";
	}

}
