package commands.general;

import java.awt.Color;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

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
	public String execute(MessageReceivedEvent event, String... parameters) throws Exception {
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

		// EmbedBuilder builder = createEmbedBuilder();
		// for (BasicCommand command : commandListener.getCommands()) {
		// builder.addField("!" + command.getName(),
		// command.getShortDescription(), false);
		// }
		Map<String, EmbedBuilder> map = new HashMap<String, EmbedBuilder>();
		for (BasicCommand command : commandListener.getCommands()) {
			String category = command.getCategory();
			if (!map.containsKey(category)) {
				map.put(category, createEmbedBuilder(category));
			}
			map.get(category).addField("!" + command.getName(), command.getShortDescription(), false);
		}

		List<String> sortedKeys = Arrays.asList(map.keySet().toArray(new String[] {}));
		Collections.sort(sortedKeys);

		for (String key : sortedKeys) {
			try {
				event.getAuthor().openPrivateChannel().block();
				event.getAuthor().getPrivateChannel().sendMessage(map.get(key).build()).queue();
			} catch (RateLimitedException e) {
				throw new CommandExecutionException(e);
			}
		}

		return "Yo " + event.getAuthor().getAsMention() + ", ich hab dir die Liste geschickt.";
	}

	private EmbedBuilder createEmbedBuilder(String category) {
		EmbedBuilder builder = new EmbedBuilder();
		builder.setAuthor(category, null, "http://sim02.in.com/2db976709326c7c1e184783e2beefc86_m.jpg");
		Random random = new Random();
		builder.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
		return builder;
	}

	@Override
	public boolean isPrivateExecutionAllowed() {
		return true;
	}

	@Override
	public String executePrivate(MessageReceivedEvent event, String... parameters) throws Exception {
		execute(event, parameters);
		return " ";
	}

}
