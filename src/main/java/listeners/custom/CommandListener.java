package listeners.custom;

import java.util.ArrayList;
import java.util.List;

import commands.base.BasicCommand;
import exceptions.CommandExecutionException;
import listeners.base.BasicChatListener;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 * Listens to and executes commands.
 *
 * @author Sigi
 *
 */
public class CommandListener extends BasicChatListener {

	private List<BasicCommand> commands = new ArrayList<BasicCommand>();

	/**
	 * @param jda
	 */
	public CommandListener(JDA jda) {
		super(jda);
	}

	/**
	 * @return the commands
	 */
	public List<BasicCommand> getCommands() {
		return commands;
	}

	@Override
	protected void handlePrivateMessage(MessageReceivedEvent event) throws Exception {
		String message = event.getMessage().getRawContent();
		for (BasicCommand command : commands) {
			if (message.equals("!" + command.getName() + " -help")) {
				sendPrivate(event, command.getHelpMessage());
				return;
			}
			if ((message + " ").startsWith("!" + command.getName() + " ")) {
				if (command.isPrivateExecutionAllowed()) {
					sendPrivate(event, handleCommand(event, command));
				} else {
					throw new CommandExecutionException("This command cannot be used in a private chat!");
				}
			}
		}
	}

	@Override
	protected void handlePublicMessage(MessageReceivedEvent event) {
		String message = event.getMessage().getRawContent();
		for (BasicCommand command : commands) {
			if (message.equals("!" + command.getName() + " -help")) {
				send(event, command.getHelpMessage());
				return;
			}
			if ((message + " ").startsWith("!" + command.getName() + " ")) {
				if (!userHasRequiredRole(event, command)) {
					throw new CommandExecutionException("Sorry, you are not Brot enough! Required role for this command: `" + command.getRequiredRole() + "`");
				}
				send(event, handleCommand(event, command));
				return;
			}
		}
	}

	private boolean userHasRequiredRole(MessageReceivedEvent event, BasicCommand command) {
		if (command.getRequiredRole() == null || command.getRequiredRole().isEmpty()) {
			return true;
		}
		for (Role role : event.getGuild().getMember(event.getAuthor()).getRoles()) {
			if (role.getName().equalsIgnoreCase(command.getRequiredRole())) {
				return true;
			}
		}
		return false;
	}

	private String handleCommand(MessageReceivedEvent event, BasicCommand command) {
		if (command.getRequiredParameterCount() == 0) {
			return handleCommandWithoutParams(event, command);
		} else {
			return handleCommandWithParams(event, command);
		}
	}

	private String handleCommandWithoutParams(MessageReceivedEvent event, BasicCommand command) {
		if (command.isExecutable(event)) {
			return executeCommand(event, command);
		} else {
			throw new CommandExecutionException(command.getErrorMessage());
		}
	}

	private String handleCommandWithParams(MessageReceivedEvent event, BasicCommand command) {
		String message = event.getMessage().getRawContent();
		if (!message.startsWith("!" + command.getName() + " ")) {
			throw new CommandExecutionException("No parameters were given! Try `!" + command.getName() + " -help`");
		}

		String[] parameters = message.replaceFirst("!" + command.getName() + " ", "").split(command.getParameterDelimiter() + "");
		if (parameters.length < command.getRequiredParameterCount()) {
			throw new CommandExecutionException("Not enough parameters were given! Try `!" + command.getName() + " -help`");
		}

		if (command.isExecutable(event, parameters)) {
			return executeCommand(event, command, parameters);
		} else {
			throw new CommandExecutionException(command.getErrorMessage());
		}
	}

	private String executeCommand(MessageReceivedEvent event, BasicCommand command, String... parameters) {
		if (event.getChannelType() == ChannelType.PRIVATE) {
			return command.executePrivate(event, parameters);
		}
		return command.execute(event, parameters);
	}

}
