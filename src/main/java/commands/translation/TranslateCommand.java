package commands.translation;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequest;

import commands.base.BasicCommand;
import listeners.base.BasicChatListener;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import properties.PropertiesHandler;
import properties.PropertiesManager;

/**
 * Translates everything a user writes to another language.
 *
 * @author Sigi
 *
 */
public class TranslateCommand extends BasicCommand {

	@Override
	public String getName() {
		return "translate";
	}

	@Override
	public String getShortDescription() {
		return "Translates everything a user writes to another language.";
	}

	@Override
	public String getExampleUsage() {
		return "!translate @BrotBerserker de en";
	}

	@Override
	public String execute(MessageReceivedEvent event, String... parameters) throws Exception {
		User user = event.getMessage().getMentionedUsers().get(0);
		String username = user.getName() + "#" + user.getDiscriminator();

		PropertiesHandler translations = PropertiesManager.getTranslationsForGuild(event.getGuild());
		String newTranslation = parameters[1] + "-" + parameters[2];
		if (translations.contains(username)) {
			if (translations.get(username).contains(newTranslation)) {
				return user.getName() + " already speaks \"" + parameters[2] + "\"!";
			}
			translations.add(username, translations.get(username) + "," + newTranslation);
		} else {
			translations.add(username, newTranslation);
		}
		event.getJDA().addEventListener(new TranslateListener(event.getJDA(), user, parameters[1], parameters[2]));
		return user.getName() + " now speaks \"" + parameters[2] + "\"!";
	}

	@Override
	public int getRequiredParameterCount() {
		return 3;
	}

	@Override
	public boolean isExecutable(MessageReceivedEvent event, String... parameters) {
		if (event.getMessage().getMentionedUsers().size() != 1) {
			return false;
		}
		try {
			if (!new LanguagesCommand().execute(event, parameters).contains(parameters[1] + "-" + parameters[2])) {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	@Override
	public String getErrorMessage() {
		return "Please mention a user as 1. parameter and provide valid languages as 2. and 3. parameters! Use `!languages` to get a list of available languages!";
	}

	@Override
	public String getCategory() {
		return "Translation";
	}

	class TranslateListener extends BasicChatListener {

		User user;
		String languageFrom;
		String languageTo;

		public TranslateListener(JDA jda, User user, String languageFrom, String languageTo) {
			super(jda);
			this.user = user;
			this.languageFrom = languageFrom;
			this.languageTo = languageTo;
		}

		@Override
		protected void handlePublicMessage(MessageReceivedEvent event) throws Exception {
			if (event.getMessage().getRawContent().startsWith("!")) {
				return;
			}
			if (!event.getAuthor().equals(user)) {
				return;
			}
			HttpRequest request = Unirest
					.get("https://translate.yandex.net/api/v1.5/tr.json/translate?key=trnsl.1.1.20161222T161519Z.60aad109e3ac4625.b0d7b8119cfd48f7591d2a35fc21687cd9c7995c")
					.queryString("text", event.getMessage().getContent()).queryString("lang", languageFrom + "-" + languageTo);
			try {
				String result = (String) request.asJson().getBody().getObject().getJSONArray("text").get(0);
				event.getChannel().sendMessage("[" + languageTo + "] " + result).queue();
			} catch (UnirestException e) {
				e.printStackTrace();
			}
		}

	}

}
