package com.jubiman.humanflesh.patch;

import necesse.engine.modLoader.annotations.ModConstructorPatch;
import necesse.engine.network.NetworkClient;
import necesse.engine.registries.BuffRegistry;
import necesse.entity.mobs.PlayerMob;
import necesse.entity.mobs.buffs.ActiveBuff;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.asm.Advice.This;

@ModConstructorPatch(target = PlayerMob.class, arguments = {long.class, NetworkClient.class})
public class PlayerSanityPatch {
	@Advice.OnMethodExit
	static void onExit(@This PlayerMob player) {
		ActiveBuff ab = new ActiveBuff(BuffRegistry.getBuff("sanity"), player, 100000.0F, null);
		player.addBuff(ab, player.getLevel() != null && player.getLevel().isServerLevel());
	}
}
