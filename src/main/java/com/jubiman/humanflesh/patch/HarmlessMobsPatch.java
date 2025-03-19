package com.jubiman.humanflesh.patch;

import com.jubiman.humanflesh.mob.HarmlessMobs;
import com.jubiman.humanflesh.sanity.SanityPlayer;
import com.jubiman.humanflesh.sanity.SanityPlayersHandler;
import necesse.engine.GameLog;
import necesse.engine.modLoader.annotations.ModMethodPatch;
import necesse.entity.mobs.Mob;
import necesse.entity.mobs.MobBeforeHitCalculatedEvent;
import necesse.entity.mobs.MobBeforeHitEvent;
import necesse.entity.mobs.PlayerMob;
import net.bytebuddy.asm.Advice;

@ModMethodPatch(target = Mob.class, name = "doBeforeHitLogic", arguments = {MobBeforeHitEvent.class})
public class HarmlessMobsPatch {
	@Advice.OnMethodEnter
	static void onEnter(@Advice.This Mob mob, @Advice.Argument(0) MobBeforeHitEvent event) {
		if (event.attacker != null && event.attacker.getAttackOwner() instanceof HarmlessMobs.Harmless) {
			// Calcualte the damage depending on sanity
			if (event.target instanceof PlayerMob) {
				SanityPlayer player = SanityPlayersHandler.getPlayer(((PlayerMob) event.target).getServerClient());
				float mod = (25 - player.getSanity());
				if (mod > 0) {
					event.damage = event.damage.setFinalMultiplier(1 + mod * 0.1f);
					return;
				}
				GameLog.debug.println("Sanity: " + player.getSanity() + " Damage: " + event.damage.getTotalDamage(event.target, event.attacker, event.attacker.getAttackOwner().getCritDamageModifier()));
			}
			event.prevent();
			event.showDamageTip = false;
			event.playHitSound = false;
		}
	}
}
