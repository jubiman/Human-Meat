package com.jubiman.humanflesh.patch;

import com.jubiman.humanflesh.sanity.SanityPlayers;
import necesse.engine.modLoader.annotations.ModMethodPatch;
import necesse.engine.network.server.ServerClient;
import necesse.engine.save.SaveData;
import net.bytebuddy.asm.Advice;

@ModMethodPatch(target = ServerClient.class, name = "getSave", arguments = {})
public class SavePatch {
	@Advice.OnMethodExit
	static void onExit(@Advice.Return(readOnly = false) SaveData save) {
		SanityPlayers.save(save);
	}
}
