package com.jubiman.humanflesh.patch;

import necesse.engine.modLoader.annotations.ModMethodPatch;
import necesse.entity.mobs.friendly.human.HumanMob;
import necesse.inventory.lootTable.LootTable;
import necesse.inventory.lootTable.lootItem.LootItem;
import net.bytebuddy.asm.Advice;

@ModMethodPatch(target = HumanMob.class, name = "getLootTable", arguments = {})
public class HumanMobLootPatch {
	@Advice.OnMethodExit
	static void onExit(@Advice.This HumanMob mob, @Advice.Return LootTable returnVal) {
		returnVal.items.add(LootItem.between("humanmeat", 1, 10));
	}
}
