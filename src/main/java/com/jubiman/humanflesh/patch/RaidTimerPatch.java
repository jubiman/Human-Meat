package com.jubiman.humanflesh.patch;

import necesse.engine.GameRaidFrequency;
import necesse.engine.modLoader.annotations.ModMethodPatch;
import necesse.level.maps.levelData.settlementData.SettlementLevelData;
import net.bytebuddy.asm.Advice;


@ModMethodPatch(target = SettlementLevelData.class, name = "resetNextRaidTimer", arguments = {boolean.class, boolean.class})
public class RaidTimerPatch {
	@Advice.OnMethodExit
	static void onExit(@Advice.This SettlementLevelData data, @Advice.Argument(1) boolean onlyIfShorter, @Advice.FieldValue(value = "nextRaid", readOnly = false) long nextRaid) {
		if (!onlyIfShorter && data.getLevel().getWorldSettings().raidFrequency == GameRaidFrequency.valueOf("OFTEN"))
			nextRaid /= 10;
	}
}
