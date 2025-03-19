package com.jubiman.humanflesh.patch;

import necesse.engine.modLoader.annotations.ModMethodPatch;
import necesse.entity.mobs.hostile.pirates.PirateMob;
import necesse.inventory.lootTable.LootTable;
import necesse.inventory.lootTable.lootItem.LootItem;
import net.bytebuddy.asm.Advice;

@ModMethodPatch(target = PirateMob.class, name = "getLootTable", arguments = {})
public class PirateMobLootPatch {
	@Advice.OnMethodExit
	static void onExit(@Advice.This PirateMob mob, @Advice.Return LootTable returnVal) {
		returnVal.items.add(LootItem.between("humanmeat", 1, 3));
	}
}
