package listeners.custom;

import java.io.IOException;
import java.net.URISyntaxException;

import listeners.base.BasicChatListener;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import properties.PropertiesHandler;
import properties.PropertiesManager;

/**
 * Listens to chat bindings.
 *
 * @author Sigi
 *
 */
public class BindingsListener extends BasicChatListener {

	/**
	 * @param jda
	 */
	public BindingsListener(JDA jda) {
		super(jda);
	}

	@Override
	protected void handlePublicMessage(MessageReceivedEvent event) throws URISyntaxException, IOException {
		String content = event.getMessage().getContent();
		if (content.startsWith("!")) {
			return;
		}

		PropertiesHandler regexBindings = PropertiesManager.getRegexBindingsForGuild(event.getGuild());
		for (String key : regexBindings.getKeys()) {
			if (content.matches(key)) {
				send(event, regexBindings.get(key));
			}
		}

		PropertiesHandler chatBindings = PropertiesManager.getChatBindingsForGuild(event.getGuild());
		if (chatBindings.contains(content)) {
			send(event, chatBindings.get(content));
		}
	}

}
