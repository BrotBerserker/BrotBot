package listeners.custom;

import java.io.IOException;
import java.net.URISyntaxException;

import net.dv8tion.jda.core.entities.Emote;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.react.GenericMessageReactionEvent;
import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.core.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.core.exceptions.RateLimitedException;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import properties.PropertiesHandler;
import properties.PropertiesManager;

/**
 * Listens for emote reactions and gives Karma points to message authors.
 *
 * @author Sigi
 *
 */
public class KarmaListener extends ListenerAdapter {

	@Override
	public void onMessageReactionAdd(MessageReactionAddEvent event) {
		if (!event.getReaction().getEmote().isEmote()) {
			return;
		}
		try {
			int karmaPoints = getKarmaPoints(event);
			giveKarmaPoints(event, karmaPoints);
		} catch (Exception e) {
			e.printStackTrace();
			event.getChannel().sendMessage(e.getMessage()).queue();
		}
	}

	@Override
	public void onMessageReactionRemove(MessageReactionRemoveEvent event) {
		if (!event.getReaction().getEmote().isEmote()) {
			return;
		}
		try {
			int karmaPoints = getKarmaPoints(event);
			giveKarmaPoints(event, -karmaPoints);
		} catch (Exception e) {
			e.printStackTrace();
			event.getChannel().sendMessage(e.getMessage()).queue();
		}
	}

	private void giveKarmaPoints(GenericMessageReactionEvent event, int karmaPoints) throws URISyntaxException, IOException, RateLimitedException {
		PropertiesHandler karma = PropertiesManager.getKarmaForGuild(getEmote(event).getGuild());
		String username = getAuthor(event).getName() + "#" + getAuthor(event).getDiscriminator();

		if (karma.contains(username)) {
			int oldKarma = Integer.parseInt(karma.get(username));
			karma.add(username, String.valueOf(oldKarma + karmaPoints));
		} else {
			karma.add(username, String.valueOf(karmaPoints));
		}
	}

	private int getKarmaPoints(GenericMessageReactionEvent event) throws URISyntaxException, IOException, RateLimitedException {
		User messageAuthor = getAuthor(event);
		User reactionAuthor = event.getUser();

		// You can't give yourself Karma
		if (reactionAuthor.equals(messageAuthor)) {
			return 0;
		}

		Emote emote = getEmote(event);
		PropertiesHandler karmaConfig = PropertiesManager.getKarmaConfigForGuild(emote.getGuild());

		// Emote is not a Karma emote -> give 0 Karma
		if (!karmaConfig.contains(emote.toString())) {
			return 0;
		}

		// Give Karma
		return Integer.parseInt(karmaConfig.get(emote.toString()));
	}

	private User getAuthor(GenericMessageReactionEvent event) throws RateLimitedException {
		return event.getChannel().getMessageById(event.getMessageId()).block().getAuthor();
	}

	private Emote getEmote(GenericMessageReactionEvent event) {
		return event.getReaction().getEmote().getEmote();
	}

}
