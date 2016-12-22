package commands.general;

import java.awt.Color;

import commands.base.BasicCommand;
import exceptions.CommandExecutionException;
import listeners.custom.CommandListener;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.exceptions.RateLimitedException;

/**
 * Sends the user a list of available commands.
 *
 * @author Sigi
 *
 */
public class HelpCommand extends BasicCommand {

	private CommandListener commandListener;

	/**
	 * @param commandListener
	 */
	public HelpCommand(CommandListener commandListener) {
		this.commandListener = commandListener;
	}

	@Override
	public String getName() {
		return "brothelp";
	}

	@Override
	public String getShortDescription() {
		return "Sends you a list of available commands.";
	}

	@Override
	public String execute(MessageReceivedEvent event, String... parameters) {
		// StringBuilder bob = new StringBuilder(
		// "Hi, faggot!\n\nHere is a list of available commands. Type `!command
		// -help` here or in a group chat to get detailed information on a
		// command." + "\n```Markdown\n");
		// for (int i = 0; i < commandListener.getCommands().size(); i++) {
		// bob.append(
		// "\n[" + (i + 1) + "]: !" +
		// commandListener.getCommands().get(i).getName() + "\n " +
		// commandListener.getCommands().get(i).getShortDescription() + "\n");
		// }
		// bob.append("```");

		EmbedBuilder builder = new EmbedBuilder();
		builder.setAuthor("BrotBot", null, "http://sim02.in.com/2db976709326c7c1e184783e2beefc86_m.jpg");
		builder.setColor(Color.RED);
		builder.setDescription("Available Commands");
		for (BasicCommand command : commandListener.getCommands()) {
			builder.addField("!" + command.getName(), command.getShortDescription(), true);
		}

		try {
			event.getAuthor().openPrivateChannel().block();
			// event.getAuthor().getPrivateChannel().sendMessage(bob.toString()).queue();
			event.getAuthor().getPrivateChannel().sendMessage(builder.build()).queue();
			return "Yo " + event.getAuthor().getAsMention() + ", ich hab dir die Liste geschickt.";
		} catch (RateLimitedException e) {
			throw new CommandExecutionException(e);
		}
	}

}
