package listeners.random;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import listeners.base.BasicChatListener;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 * Random sends Daler Mehndi quotes to text channels.
 *
 * @author Sigi
 *
 */
public class DalerListener extends BasicChatListener {

	/**
	 * @param jda
	 */
	public DalerListener(JDA jda) {
		super(jda);
	}

	@Override
	protected void handlePublicMessage(final MessageReceivedEvent event) throws Exception {
		if (new Random().nextInt(100) < 5) {
			final int sleep = new Random().nextInt(240000) + 60000;
			System.out.println("Random Daler initiated! (" + sleep + " ms)");
			new Thread() {
				@Override
				public void run() {
					try {
						sleep(sleep);
						event.getChannel().sendTyping().queue();
						sleep(3000);
						event.getChannel().sendMessage(getRandomMessage()).queue();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}.start();
		}
	}

	private String getRandomMessage() {
		List<String> list = new ArrayList<String>();
		list.add(":notes: Tunak tunak tun, tunak tunak tun, tunak tunak tun dadada! :notes:");
		list.add("Dööööööööönnneeeeeeer!");
		list.add(":notes: Dalail lam wutschde dunne molita Sohle Müll im Pokal! :notes:");
		return list.get(new Random().nextInt(list.size()));
	}
}
