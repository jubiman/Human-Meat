package com.jubiman.humanflesh.patch;

import com.jubiman.humanflesh.buff.SanityBuff;
import necesse.engine.modLoader.annotations.ModMethodPatch;
import necesse.engine.registries.BuffRegistry;
import necesse.entity.mobs.PlayerMob;
import necesse.entity.mobs.buffs.ActiveBuff;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.asm.Advice.This;

@ModMethodPatch(target = PlayerMob.class, name = "restore", arguments = {})
public class PlayerSanityRestorePatch {
	@Advice.OnMethodExit
	static void onExit(@This PlayerMob player) {
		ActiveBuff ab = new ActiveBuff(BuffRegistry.getBuff("sanity"), player, 100000.0F, null);
		ab.getGndData().setInt("sanity", SanityBuff.userMap.get(player.getID()));
		player.addBuff(ab, player.getLevel() != null && player.getLevel().isServerLevel());
	}
}
