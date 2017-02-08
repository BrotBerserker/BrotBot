package commands.games;

import java.util.ArrayList;
import java.util.List;

import commands.base.BasicCommand;
import exceptions.CommandExecutionException;
import listeners.base.BasicChatListener;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.exceptions.RateLimitedException;

/**
 * Asks if someone wants to play a certain game.
 *
 * @author Sigi
 *
 */
public class PlayCommand extends BasicCommand {

	private String game;
	private List<String> players;
	private Message message;
	private BasicChatListener listener;

	@Override
	public String getName() {
		return "play";
	}

	@Override
	public String getShortDescription() {
		return "Asks if someone wants to play a certain game.";
	}

	@Override
	public String execute(MessageReceivedEvent event, String... parameters) {
		game = parameters[0];
		players = new ArrayList<String>();
		players.add(event.getAuthor().getName());
		try {
			message = event.getChannel().sendMessage(getMessage()).block();
		} catch (RateLimitedException e) {
			e.printStackTrace();
			throw new CommandExecutionException(e);
		}
		if (listener == null) {
			listener = new PlusListener(event.getJDA());
			event.getJDA().addEventListener(listener);
		}
		return event.getAuthor().getName() + " wants to play " + parameters[0] + "! Type \"+\" if you wanna join!";
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
	public String getExampleUsage() {
		return "!play Rocket League";
	}

	@Override
	public String getCategory() {
		return "Games";
	}

	private String getMessage() {
		StringBuilder bob = new StringBuilder("**~" + game + " players: ~**");
		for (String player : players) {
			bob.append("\n:white_check_mark: " + player);
		}
		bob.append("\n");
		return bob.toString();
	}

	class PlusListener extends BasicChatListener {

		public PlusListener(JDA jda) {
			super(jda);
		}

		@Override
		protected void handlePublicMessage(MessageReceivedEvent event) throws Exception {
			if ("+".equals(event.getMessage().getRawContent()) && !players.contains(event.getAuthor().getName())) {
				players.add(event.getAuthor().getName());
				message.editMessage(getMessage()).queue();
			}
		}

	}

}
