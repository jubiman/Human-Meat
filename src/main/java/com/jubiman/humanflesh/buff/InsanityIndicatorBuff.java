package com.jubiman.humanflesh.buff;

import necesse.engine.localization.Localization;
import necesse.engine.util.GameBlackboard;
import necesse.entity.mobs.buffs.ActiveBuff;
import necesse.entity.mobs.buffs.BuffEventSubscriber;
import necesse.entity.mobs.buffs.staticBuffs.Buff;
import necesse.gfx.gameTooltips.ListGameTooltips;

public class InsanityIndicatorBuff extends Buff {
	@Override
	public void init(ActiveBuff activeBuff, BuffEventSubscriber buffEventSubscriber) {
		this.canCancel = false;
		this.overrideSync = true;
	}

	@Override
	public ListGameTooltips getTooltip(ActiveBuff ab, GameBlackboard blackboard) {
		ListGameTooltips tooltips = super.getTooltip(ab, blackboard);
		tooltips.add(Localization.translate("buff", "insanityindicatorbufftooltip"));
		return tooltips;
	}
}
