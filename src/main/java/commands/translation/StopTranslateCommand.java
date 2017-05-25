package commands.translation;

import commands.base.BasicCommand;
import commands.translation.TranslateCommand.TranslateListener;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import properties.PropertiesHandler;
import properties.PropertiesManager;

/**
 * Stops translation of a user's messages.
 *
 * @author Sigi
 *
 */
public class StopTranslateCommand extends BasicCommand {

	@Override
	public String getName() {
		return "stoptranslate";
	}

	@Override
	public String getShortDescription() {
		return "Stops translation of a user's messages.";
	}

	@Override
	public String getExampleUsage() {
		return "!stoptranslate @BrotBerserker";
	}

	@Override
	public String execute(MessageReceivedEvent event, String... parameters) throws Exception {
		User user = event.getMessage().getMentionedUsers().get(0);
		for (Object listener : event.getJDA().getRegisteredListeners()) {
			if (listener instanceof TranslateListener) {
				if (((TranslateListener) listener).user.equals(user)) {
					event.getJDA().removeEventListener(listener);
				}
			}
		}
		PropertiesHandler translations = PropertiesManager.getTranslationsForGuild(event.getGuild());
		boolean removed = translations.remove(user.getName() + "#" + user.getDiscriminator());
		return removed ? "Stopped translation for " + user.getName() + "!" : "There was no translation to stop!";
	}

	@Override
	public int getRequiredParameterCount() {
		return 1;
	}

	@Override
	public boolean isExecutable(MessageReceivedEvent event, String... parameters) {
		if (event.getMessage().getMentionedUsers().size() != 1) {
			return false;
		}
		return true;
	}

	@Override
	public String getErrorMessage() {
		return "Please mention a user as 1. parameter!";
	}

	@Override
	public String getCategory() {
		return "Translation";
	}

}
