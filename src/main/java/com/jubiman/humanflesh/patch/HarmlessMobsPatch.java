package com.jubiman.humanflesh.patch;

import com.jubiman.humanflesh.mob.HarmlessMobs;
import necesse.engine.modLoader.annotations.ModMethodPatch;
import necesse.entity.mobs.Mob;
import necesse.entity.mobs.MobBeforeHitCalculatedEvent;
import net.bytebuddy.asm.Advice;

@ModMethodPatch(target = Mob.class, name = "doBeforeHitCalculatedLogic", arguments = {MobBeforeHitCalculatedEvent.class})
public class HarmlessMobsPatch {
	@Advice.OnMethodEnter
	static void onEnter(@Advice.This Mob mob, @Advice.Argument(0) MobBeforeHitCalculatedEvent event) {
		if (event.attacker != null && event.attacker.getAttackOwner() instanceof HarmlessMobs.Harmless) {
			event.prevent();
			event.showDamageTip = false;
			event.playHitSound = false;
		}
	}
}
