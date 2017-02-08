package commands.bindings;

import commands.base.BasicCommand;
import exceptions.CommandExecutionException;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import properties.PropertiesHandler;
import properties.PropertiesManager;

/**
 * Adds a new chat binding. !bind key=value
 *
 * @author Sigi
 *
 */
public class BindCommand extends BasicCommand {

	@Override
	public String getName() {
		return "bind";
	}

	@Override
	public String getShortDescription() {
		return "Adds a new chat binding.";
	}

	@Override
	public String execute(MessageReceivedEvent event, String... parameters) {
		try {
			PropertiesHandler bindings = PropertiesManager.getChatBindingsForGuild(event.getGuild());
			boolean updated = bindings.add(parameters[0].trim(), parameters[1].trim());
			return "Binding for \"" + parameters[0].trim() + "\" " + (updated ? "updated!" : "created!");
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

	@Override
	public String getLongDescription() {
		return "A chat binding is a key/value pair. When a user sends a message containing the key, the bot will respond with the corresponding value. "
				+ "\n- Works with links to images etc.\n- Chat bindings are stored per server.\n- If a binding with the given key already exists, it will be overwritten.";
	}

	@Override
	public String getExampleUsage() {
		return "!bind rip = lel rekt in ripperonis m8 \n-> When a user sends \"rip\", the bot will respond with \"lel rekt in ripperonis m8\"";
	}

	@Override
	public String getCategory() {
		return "Bindings";
	}

}
