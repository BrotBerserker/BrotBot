package listeners.functional;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;

import net.dv8tion.jda.core.entities.Emote;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;
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

		int oldKarma = 0;
		if (karma.contains(username)) {
			oldKarma = Integer.parseInt(karma.get(username));
		}

		int newKarma = oldKarma + karmaPoints;

		karma.add(username, String.valueOf(newKarma));

		updateKarmaLevel(event, newKarma);
	}

	private void updateKarmaLevel(GenericMessageReactionEvent event, int newKarma) throws URISyntaxException, IOException, RateLimitedException {
		Guild guild = getEmote(event).getGuild();
		PropertiesHandler karmaLevels = PropertiesManager.getKarmaLevelsForGuild(guild);
		Member author = guild.getMember(getAuthor(event));

		Role oldRole = null;
		Role newRole = null;

		// int oldPoints = Integer.MIN_VALUE;
		int maxPoints = Integer.MIN_VALUE;

		// For each Karma level (e.g. 100 points, 200 points, 300 points, ...)
		for (String level : karmaLevels.getKeys()) {

			// Get the appropriate role
			String roleName = karmaLevels.get(level);
			Role role = getRole(roleName, guild);

			// Set old role
			if (oldRole == null && author.getRoles().contains(role)) {
				oldRole = role;
				// oldRole = new RoleImpl(role.getId(), guild);
			}

			// Get the points required for that role
			int levelPoints = Integer.parseInt(level);

			// Set max points
			if (newKarma >= levelPoints && levelPoints > maxPoints) {
				maxPoints = levelPoints;
				newRole = role;
				// newRole = new RoleImpl(role.getId(), guild);
			}
		}
		if (newRole != null && !newRole.equals(oldRole)) {
			guild.getController().modifyMemberRoles(author, Arrays.asList(newRole), Arrays.asList(oldRole)).block();

			event.getChannel()
					.sendMessage("Listen up everyone! " + author.getEffectiveName() + " reached `" + maxPoints + "` Karma and is now considered a **" + newRole.getName() + "**!")
					.queue();
		}
	}

	private Role getRole(String roleName, Guild guild) {
		for (Role role : guild.getRoles()) {
			if (role.getName().equalsIgnoreCase(roleName)) {
				return role;
			}
		}
		return null;
	}

	private int getKarmaPoints(GenericMessageReactionEvent event) throws URISyntaxException, IOException, RateLimitedException {
		User messageAuthor = getAuthor(event);
		User reactionAuthor = event.getUser();

		// You can't give yourself Karma
		if (reactionAuthor.equals(messageAuthor)) {
			return 0;
		}

		Emote emote = getEmote(event);
		PropertiesHandler karmaEmotes = PropertiesManager.getKarmaEmotesForGuild(emote.getGuild());

		// Emote is not a Karma emote -> give 0 Karma
		if (!karmaEmotes.contains(emote.toString())) {
			return 0;
		}

		// Give Karma
		return Integer.parseInt(karmaEmotes.get(emote.toString()));
	}

	private User getAuthor(GenericMessageReactionEvent event) throws RateLimitedException {
		return event.getChannel().getMessageById(event.getMessageId()).block().getAuthor();
	}

	private Emote getEmote(GenericMessageReactionEvent event) {
		return event.getReaction().getEmote().getEmote();
	}

}
