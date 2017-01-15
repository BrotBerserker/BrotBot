package commands.translation;

import org.json.JSONArray;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequest;

import commands.base.BasicCommand;
import exceptions.CommandExecutionException;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 * Displays a list of languages that are available for translation with
 * !translate.
 *
 * @author Sigi
 *
 */
public class LanguagesCommand extends BasicCommand {

	@Override
	public String getName() {
		return "languages";
	}

	@Override
	public String getShortDescription() {
		return "Displays a list of languages that are available for translation with !translate.";
	}

	@Override
	public String execute(MessageReceivedEvent event, String... parameters) {
		HttpRequest request = Unirest
				.get("https://translate.yandex.net/api/v1.5/tr.json/getLangs?key=trnsl.1.1.20161222T161519Z.60aad109e3ac4625.b0d7b8119cfd48f7591d2a35fc21687cd9c7995c");
		try {
			StringBuilder bob = new StringBuilder("Available languages: ");
			JSONArray array = request.asJson().getBody().getObject().getJSONArray("dirs");
			for (Object language : array) {
				bob.append("\n- " + language.toString());
			}
			return bob.toString();
		} catch (UnirestException e) {
			e.printStackTrace();
			throw new CommandExecutionException(e);
		}
	}

}
