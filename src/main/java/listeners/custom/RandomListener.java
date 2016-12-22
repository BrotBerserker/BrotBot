package listeners.custom;

import java.util.List;
import java.util.Random;

import listeners.base.BasicChatListener;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Emote;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.user.UserGameUpdateEvent;

/**
 * Sends random responses to group chat messages.
 *
 * @author Sigi
 *
 */
public class RandomListener extends BasicChatListener {

	/**
	 * @param jda
	 */
	public RandomListener(JDA jda) {
		super(jda);
	}

	@Override
	public void onUserGameUpdate(UserGameUpdateEvent event) {
		if (event.getUser().isBot()) {
			return;
		}

		Game game = event.getGuild().getMember(event.getUser()).getGame();
		if (game == null) {
			return;
		}

		event.getGuild().getTextChannels().get(0).sendMessage(event.getUser().getName() + " zockt jetzt " + game.getName() + "! Wer zockt mit?").queue();
	}

	@Override
	protected void handlePublicMessage(MessageReceivedEvent event) throws Exception {
		doRandomShit(event);
	}

	private void doRandomShit(MessageReceivedEvent event) {
		int x = new Random().nextInt(100);
		if (x < 5) {
			event.getTextChannel().sendMessage(event.getAuthor().getAsMention() + " kannst du bitte mal die Schnauze halten, danke.").queue();
		} else if (x < 20) {
			List<Emote> emotes = event.getGuild().getEmotes();
			int emote = new Random().nextInt(emotes.size());
			event.getMessage().addReaction(emotes.get(emote)).queue();
		}
	}

}
