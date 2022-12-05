package com.jubiman.humanflesh.command;

import necesse.engine.commands.CmdParameter;
import necesse.engine.commands.CommandLog;
import necesse.engine.commands.ModularChatCommand;
import necesse.engine.commands.PermissionLevel;
import necesse.engine.commands.parameterHandlers.IntParameterHandler;
import necesse.engine.commands.parameterHandlers.StringParameterHandler;
import necesse.engine.network.client.Client;
import necesse.engine.network.server.Server;
import necesse.engine.network.server.ServerClient;

public class SanityCommand extends ModularChatCommand {
	public SanityCommand() {
		super("sanity", "Changing your sanity value", PermissionLevel.ADMIN, true, new CmdParameter("string", new StringParameterHandler()), new CmdParameter("int", new IntParameterHandler(), true));
	}

	@Override
	public void runModular(Client client, Server server, ServerClient serverClient, Object[] args, String[] errors, CommandLog commandLog) {
		int i = serverClient.playerMob.buffManager.getBuff("sanity").getGndData().getInt("sanity");
		switch ((String) args[0]) {
			case "set":
				serverClient.playerMob.buffManager.getBuff("sanity").getGndData().setInt("sanity", (int) args[1]);
				break;
			case "add":
				serverClient.playerMob.buffManager.getBuff("sanity").getGndData().setInt("sanity", (int) args[1] + i);
				break;
			case "remove":
				serverClient.playerMob.buffManager.getBuff("sanity").getGndData().setInt("sanity", i - (int) args[1]);
				break;
			case "get":
				commandLog.add("Sanity: " + i);
				return;
		}
		commandLog.add("Sanity set to " + serverClient.playerMob.buffManager.getBuff("sanity").getGndData().getInt("sanity"));
	}
}
