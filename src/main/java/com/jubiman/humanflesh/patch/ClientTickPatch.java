package com.jubiman.humanflesh.patch;

import com.jubiman.humanflesh.sanity.SanityPlayers;
import necesse.engine.modLoader.annotations.ModMethodPatch;
import necesse.engine.network.client.Client;
import necesse.engine.network.client.ClientClient;
import net.bytebuddy.asm.Advice;

@ModMethodPatch(target = Client.class, name = "tick", arguments = {})
public class ClientTickPatch {
	@Advice.OnMethodExit
	static void onExit(@Advice.This Client client) {
		ClientClient clientClient = client.getClient();
		if (clientClient != null)
			SanityPlayers.getPlayer(clientClient.authentication).clientTick(client);
	}
}
