package com.jubiman.humanflesh.patch;

import com.jubiman.humanflesh.mob.HarmlessMobs;
import necesse.engine.modLoader.annotations.ModMethodPatch;
import necesse.entity.mobs.Attacker;
import necesse.entity.mobs.Mob;
import necesse.entity.mobs.MobHitEvent;
import net.bytebuddy.asm.Advice;

@ModMethodPatch(target = Mob.class, name = "isHit", arguments = {MobHitEvent.class, Attacker.class})
public class HarmlessMobsPatch {
	@Advice.OnMethodEnter
	static void onEnter(@Advice.This Mob mob, @Advice.Argument(0) MobHitEvent event, @Advice.Argument(1) Attacker attacker) {
		if (attacker != null && attacker.getAttackOwner() instanceof HarmlessMobs.Harmless) {
			event.prevent();
			event.showDamageTip = false;
			event.playHitSound = false;
		}
	}
}
