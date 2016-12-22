package main;

import java.util.List;

import commands.base.BasicCommand;
import commands.bindings.BindCommand;
import commands.bindings.BindListCommand;
import commands.bindings.BindRegexCommand;
import commands.bindings.UnbindCommand;
import commands.bindings.UnbindRegexCommand;
import commands.general.HelpCommand;
import commands.karma.AddKarmaCommand;
import commands.karma.KarmaCommand;
import commands.karma.KarmaListCommand;
import commands.karma.KarmaStatsCommand;
import commands.karma.RemoveKarmaCommand;
import listeners.custom.BindingsListener;
import listeners.custom.CommandListener;
import listeners.custom.DalerListener;
import listeners.custom.KarmaListener;
import listeners.custom.RandomListener;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game.GameType;
import net.dv8tion.jda.core.entities.impl.GameImpl;

/**
 * @author Sigi
 *
 */
public class Main {

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		try {
			JDA jda = new JDABuilder(AccountType.BOT).setToken("").buildBlocking();
			jda.getPresence().setGame(new GameImpl("!brothelp", "asd", GameType.DEFAULT));

			CommandListener commandListener = new CommandListener(jda);
			addCommands(commandListener);
			jda.addEventListener(commandListener);
			jda.addEventListener(new BindingsListener(jda));
			jda.addEventListener(new DalerListener(jda));
			jda.addEventListener(new KarmaListener());
			jda.addEventListener(new RandomListener(jda));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void addCommands(CommandListener commandListener) {
		List<BasicCommand> commands = commandListener.getCommands();
		commands.add(new HelpCommand(commandListener));
		// commands.add(new SetCommand());
		commands.add(new BindCommand());
		commands.add(new BindRegexCommand());
		commands.add(new UnbindCommand());
		commands.add(new UnbindRegexCommand());
		commands.add(new BindListCommand());
		commands.add(new AddKarmaCommand());
		commands.add(new KarmaCommand());
		commands.add(new KarmaStatsCommand());
		commands.add(new KarmaListCommand());
		commands.add(new RemoveKarmaCommand());
	}

}
