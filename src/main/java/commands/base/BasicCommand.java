package commands.base;

import org.apache.commons.lang3.StringUtils;

import commands.general.HelpCommand;
import exceptions.CommandExecutionException;
import listeners.functional.CommandListener;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 * Abstract base class for commands. Extend this class and add an instance of
 * your new class to the {@link CommandListener} to create a new command for the
 * BrotBot.
 *
 * @author Sigi
 *
 */
public abstract class BasicCommand {

	/**
	 * Returns the command's name without prefix. Example: if you return
	 * "fancycommand" and the currently configured prefix is "!", the bot will
	 * execute your command when a user types "!fancycommand".
	 *
	 * @return The command's name
	 */
	public abstract String getName();

	/**
	 * Returns a short (1 sentence) description of the command. Used by the
	 * {@link HelpCommand} for displaying a list of available commands.
	 *
	 * @return Short description
	 */
	public abstract String getShortDescription();

	/**
	 * Executes the command and returns a String to display in Discord as
	 * response. This method is only called if
	 * {@link #isExecutable(MessageReceivedEvent, String...)} previously
	 * returned <code>true</code>.
	 *
	 * @param event
	 *            The {@link MessageReceivedEvent} that contains the command
	 * @param parameters
	 *            String Array containing the command's parameters. You can
	 *            assume that its size equals the result of
	 *            {@link #getRequiredParameterCount()}.
	 * @return A message that will be send to the text chat in which the command
	 *         has been called
	 * @throws Exception
	 *             Might throw any kind of exception while executing the command
	 */
	public abstract String execute(MessageReceivedEvent event, String... parameters) throws Exception;

	/**
	 * Similar to {@link #execute(MessageReceivedEvent, String...)}, but is
	 * called when the command is used in a private chat between a user and the
	 * bot. Currently not supported.
	 *
	 * @param event
	 *            The {@link MessageReceivedEvent} that contains the command
	 * @param parameters
	 *            String Array containing the command's parameters. You can
	 *            assume that its size equals the result of
	 *            {@link #getRequiredParameterCount()}.
	 * @return A message that will be send to the private chat in which the
	 *         command has been called
	 * @throws Exception
	 *             Might throw any kind of exception while executing the command
	 */
	public String executePrivate(MessageReceivedEvent event, String... parameters) throws Exception {
		throw new CommandExecutionException("This command's functionality is not yet implemented for private channels, but will be soon!");
	}

	/**
	 * Returns how many parameters the command requires.
	 *
	 * @return Number of required parameters
	 */
	public int getRequiredParameterCount() {
		return 0;
	}

	/**
	 * If the command's parameters should not be delimited by spaces, you can
	 * override this method.
	 *
	 * @return The char that parameters are delimited by, for example '='
	 */
	public char getParameterDelimiter() {
		return ' ';
	}

	/**
	 * Checks if the command can be executed using the given
	 * {@link MessageReceivedEvent} and parameter set. If a user attempts to use
	 * a command, this method is called first. If it returns true,
	 * {@link #execute(MessageReceivedEvent, String...)} (or
	 * {@link #executePrivate(MessageReceivedEvent, String...)}) will be called.
	 * If it returns false, {@link #getErrorMessage()} will be called and the
	 * result will be send to the user.
	 *
	 * @param event
	 *            The {@link MessageReceivedEvent} that contains the command
	 * @param parameters
	 *            String Array containing the command's parameters. You can
	 *            assume that its size equals the result of
	 *            {@link #getRequiredParameterCount()}.
	 * @return true if the command can be executed.
	 */
	public boolean isExecutable(MessageReceivedEvent event, String... parameters) {
		return true;
	}

	/**
	 * Determines whether this command can be executed in a private chat between
	 * a user and the bot. If an attempt to use a command in a private chat is
	 * made, this method as well as
	 * {@link BasicCommand#isExecutable(MessageReceivedEvent, String...)
	 * isExecutable(...)} have to return <code>true</code> before
	 * {@link BasicCommand#executePrivate(MessageReceivedEvent, String...)
	 * executePrivate(...)} will be called. . <br>
	 *
	 * @return <code>true</code> if this command can be used in a private chat.
	 *         Default value: <code>false</code>
	 */
	public boolean isPrivateExecutionAllowed() {
		return false;
	}

	/**
	 * Message that will be sent to a user if
	 * {@link #isExecutable(MessageReceivedEvent, String...)} returns false.
	 *
	 * @return The error message
	 */
	public String getErrorMessage() {
		return "Command " + getName() + " could not be executed due to reasons!";
	}

	/**
	 * Returns the name of a {@link Role} that is required to execute this
	 * command.
	 *
	 * @return The role's name
	 */
	public String getRequiredRole() {
		return "";
	}

	/**
	 * Returns a longer description of the command, used when the command is
	 * called using the "-help" option.
	 *
	 * @return The description
	 */
	public String getLongDescription() {
		return "(no detailed description available)";
	}

	/**
	 * Returns an example of how the command can be used. Will be displayed when
	 * the command is called using the "-help" option.
	 *
	 * @return The example
	 */
	public String getExampleUsage() {
		StringBuilder bob = new StringBuilder("!" + getName());
		for (int i = 0; i < getRequiredParameterCount(); i++) {
			if (i == 0) {
				bob.append(" [parameter0]");
			} else {
				bob.append(getParameterDelimiter() + "[parameter" + i + "]");
			}
		}
		return bob.toString();
	}

	/**
	 * Returns the command's category. At the moment it is only used by
	 * {@link HelpCommand}. Default value is <code>"General"</code>.
	 *
	 * @return The category
	 */
	public String getCategory() {
		return "General";
	}

	/**
	 * Returns the message that will be displayed when the command is called
	 * using the "-help" option. Usually you don't have to override this,
	 * instead you can override {@link #getLongDescription()} and
	 * {@link #getExampleUsage()}.
	 *
	 * @return The help message
	 */
	public String getHelpMessage() {
		String s = "```Markdown";
		s += "\nCommand Info: !" + getName() + "\n" + StringUtils.repeat("=", 15 + getName().length());
		s += "\n* " + getShortDescription();
		s += "\n* <Parameters: " + getRequiredParameterCount() + ">";
		s += "\n* <Private: " + isPrivateExecutionAllowed() + ">";
		s += "\n* <Role: " + getRequiredRole() + ">";
		s += "\n*";
		s += "\n* " + getLongDescription();
		s += "\n*\n[Example]: " + getExampleUsage();
		s += "```";
		return s;
	}

	@Override
	public String toString() {
		return "BasicCommand [getName()=" + getName() + ", getHelpMessage()=" + getHelpMessage() + ", getRequiredParameterCount()=" + getRequiredParameterCount()
				+ ", getParameterDelimiter()=" + getParameterDelimiter() + ", getErrorMessage()=" + getErrorMessage() + ", getRequiredRole()=" + getRequiredRole() + "]";
	}

}
