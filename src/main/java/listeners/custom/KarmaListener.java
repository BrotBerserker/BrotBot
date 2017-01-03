package listeners.custom;

import java.io.IOException;
import java.net.URISyntaxException;

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
		if(karma.contains(username)){
			oldKarma=Integer.parseInt(karma.get(username));
		}

		int newKarma = oldKarma+karmaPoints;

		karma.add(username, String.valueOf(newKarma));

		updateKarmaLevel(event, newKarma);
	}

	private void updateKarmaLevel(GenericMessageReactionEvent event, int newKarma) throws URISyntaxException, IOException, RateLimitedException {
		Guild guild = getEmote(event).getGuild();
		PropertiesHandler karmaLevels = PropertiesManager.getKarmaLevelsForGuild(guild);
		Member author = guild.getMember(getAuthor(event));

		// For each Karma level (e.g. 100 points, 200 points, 300 points, ...)
		for (String level : karmaLevels.getKeys()) {

			// Get the appropriate role
			String roleName = karmaLevels.get(level);
			Role role = getRole(roleName, guild);

			// Get the points required for that role
			int levelPoints = Integer.parseInt(level);

			// If the user reached the points and doesn't already have the role -> assign it
			if(newKarma >= levelPoints && !author.getRoles().contains(role)){
				guild.getController().addRolesToMember(author, role).queue();
				event.getChannel().sendMessage("Yay! "+author.getEffectiveName()+" reached "+levelPoints+ " Karma and is now considered a "+role.getName()+"!").queue();
			}
			// Remove the role if the user has lost too many karma points
			else if(newKarma < levelPoints && author.getRoles().contains(role)){
				guild.getController().removeRolesFromMember(author, role).queue();
				event.getChannel().sendMessage("Oh noes! "+author.getEffectiveName()+" doesn't have enough Karma to be a "+role.getName()+" anymore!").queue();
			}
		}

	}

	private Role getRole(String roleName, Guild guild){
		for (Role role : guild.getRoles()) {
			if(role.getName().equalsIgnoreCase(roleName)){
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
