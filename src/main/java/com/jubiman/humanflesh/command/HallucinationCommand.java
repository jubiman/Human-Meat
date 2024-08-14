package com.jubiman.humanflesh.command;

import com.jubiman.humanflesh.sanity.SanityPlayer;
import com.jubiman.humanflesh.sanity.SanityPlayersHandler;
import necesse.engine.commands.CmdParameter;
import necesse.engine.commands.CommandLog;
import necesse.engine.commands.ModularChatCommand;
import necesse.engine.commands.PermissionLevel;
import necesse.engine.commands.parameterHandlers.IntParameterHandler;
import necesse.engine.commands.parameterHandlers.StringParameterHandler;
import necesse.engine.network.client.Client;
import necesse.engine.network.server.Server;
import necesse.engine.network.server.ServerClient;

public class HallucinationCommand extends ModularChatCommand {
	public HallucinationCommand() {
		super("hallucination", "Changing your hallucination value", PermissionLevel.ADMIN, true, new CmdParameter("string", new StringParameterHandler()), new CmdParameter("int", new IntParameterHandler(), true));
	}

	@Override
	public void runModular(Client client, Server server, ServerClient serverClient, Object[] args, String[] errors, CommandLog commandLog) {
		SanityPlayer player = SanityPlayersHandler.getPlayer(serverClient);
		switch ((String) args[0]) {
			case "set":
				player.nextHallucination = server.tickManager().getTotalTicks() + (int) args[1];
				break;
			case "add":
				player.nextHallucination += (int) args[1];
				break;
			case "remove":
				player.nextHallucination -= (int) args[1];
				break;
			case "get":
				commandLog.add("Hallucination: " + player.nextHallucination + ". Which is in "
						+ player.nextHallucination + " ticks.");
				return;
			case "force":
				player.nextHallucination = 0;
				commandLog.add("Forcing hallucination in 1 tick...");
				return;
		}
		commandLog.add("Hallucination set to " + player.nextHallucination);
	}
}
