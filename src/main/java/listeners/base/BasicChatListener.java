package listeners.base;

import exceptions.CommandExecutionException;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

/**
 * Basic listener that contains method for reacting to private and group chat
 * messages. messages.
 *
 * @author Sigi
 *
 */
public abstract class BasicChatListener extends ListenerAdapter {

	protected JDA jda;

	/**
	 * @param jda
	 */
	public BasicChatListener(JDA jda) {
		this.jda = jda;
	}

	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		if (event.getAuthor().getId().equals(jda.getSelfUser().getId())) {
			return;
		}
		try {
			if (event.isFromType(ChannelType.PRIVATE)) {
				handlePrivateMessage(event);
			} else {
				handlePublicMessage(event);
			}
		} catch (CommandExecutionException e) {
			System.out.println(e.getMessage());
			send(event, ":no_entry: Error: " + e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			send(event, ":no_entry: Error: " + e.getMessage());
		}
	}

	protected void handlePublicMessage(MessageReceivedEvent event) throws Exception {
		// empty per default
	}

	protected void handlePrivateMessage(MessageReceivedEvent event) throws Exception {
		// empty per default
	}

	protected void send(MessageReceivedEvent event, String message) {
		event.getChannel().sendMessage(message).queue();
	}

	protected void sendPrivate(MessageReceivedEvent event, String message) {
		event.getAuthor().openPrivateChannel();
		event.getAuthor().getPrivateChannel().sendMessage(message).queue();
	}

}
