package commands.general;

import commands.base.BasicCommand;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 * Displays information about this bot.
 *
 * @author Sigi
 *
 */
public class InfoCommand extends BasicCommand {

	@Override
	public String getName() {
		return "info";
	}

	@Override
	public String getShortDescription() {
		return "Displays information about this bot.";
	}

	@Override
	public String execute(MessageReceivedEvent event, String... parameters) {
		String s = "```apache\n# About BrotBot\n" + "Author: BrotBerserker\n" + "Language: Java 1.8\n" + "Framework: JDA (Java Discord API)\n"
				+ "GitHub: https://github.com/BrotBerserker/BrotBot\n" + "Trello: https://trello.com/b/MsDs9PZg/brotbot\n" + "```";
		return s;
	}

	@Override
	public String executePrivate(MessageReceivedEvent event, String... parameters) {
		return execute(event, parameters);
	}

	@Override
	public boolean isPrivateExecutionAllowed() {
		return true;
	}

}
