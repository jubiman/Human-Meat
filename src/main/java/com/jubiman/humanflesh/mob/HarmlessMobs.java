package com.jubiman.humanflesh.mob;

import necesse.engine.util.GameRandom;
import necesse.inventory.lootTable.LootTable;

public class HarmlessMobs {
	public static class FrozenDwarfMob extends necesse.entity.mobs.hostile.FrozenDwarfMob implements Harmless {
		@Override
		public LootTable getLootTable() {
			return GameRandom.globalRandom.getIntBetween(0, 100) < 17 ? super.getLootTable() : new LootTable();
		}
	}

	public static class VoidApprentice extends necesse.entity.mobs.hostile.VoidApprentice implements Harmless {
		@Override
		public LootTable getLootTable() {
			return GameRandom.globalRandom.getIntBetween(0, 100) < 17 ? super.getLootTable() : new LootTable();
		}
	}

	public static class VampireMob extends necesse.entity.mobs.hostile.VampireMob implements Harmless {
		@Override
		public LootTable getLootTable() {
			return GameRandom.globalRandom.getIntBetween(0, 100) < 17 ? super.getLootTable() : new LootTable();
		}
	}

	public static class NinjaMob extends necesse.entity.mobs.hostile.NinjaMob implements Harmless {
		@Override
		public LootTable getLootTable() {
			return GameRandom.globalRandom.getIntBetween(0, 100) < 17 ? super.getLootTable() : new LootTable();
		}
	}

	public static class SwampCaveSpiderMob extends necesse.entity.mobs.hostile.SwampCaveSpiderMob implements Harmless {
		@Override
		public LootTable getLootTable() {
			return GameRandom.globalRandom.getIntBetween(0, 100) < 17 ? super.getLootTable() : new LootTable();
		}
	}

	public static class GiantCaveSpiderMob extends necesse.entity.mobs.hostile.GiantCaveSpiderMob implements Harmless {
		@Override
		public LootTable getLootTable() {
			return GameRandom.globalRandom.getIntBetween(0, 100) < 17 ? super.getLootTable() : new LootTable();
		}
	}

	public static class BlackCaveSpiderMob extends necesse.entity.mobs.hostile.BlackCaveSpiderMob implements Harmless {
		@Override
		public LootTable getLootTable() {
			return GameRandom.globalRandom.getIntBetween(0, 100) < 17 ? super.getLootTable() : new LootTable();
		}
	}

	public static class SandSpiritMob extends necesse.entity.mobs.hostile.SandSpiritMob implements Harmless {
		@Override
		public LootTable getLootTable() {
			return GameRandom.globalRandom.getIntBetween(0, 100) < 17 ? super.getLootTable() : new LootTable();
		}
	}

	public static class DeepCaveSpiritMob extends necesse.entity.mobs.hostile.DeepCaveSpiritMob implements Harmless {
		@Override
		public LootTable getLootTable() {
			return GameRandom.globalRandom.getIntBetween(0, 100) < 17 ? super.getLootTable() : new LootTable();
		}
	}

	public interface Harmless {}
}
