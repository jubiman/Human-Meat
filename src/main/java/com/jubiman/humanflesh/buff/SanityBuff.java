package com.jubiman.humanflesh.buff;

import com.jubiman.humanflesh.mob.HarmlessMobs;
import necesse.engine.localization.Localization;
import necesse.engine.network.gameNetworkData.GNDItemMap;
import necesse.engine.network.packet.PacketSpawnMob;
import necesse.engine.network.server.ServerClient;
import necesse.engine.util.GameRandom;
import necesse.entity.mobs.Mob;
import necesse.entity.mobs.buffs.ActiveBuff;
import necesse.entity.mobs.buffs.staticBuffs.Buff;
import necesse.entity.mobs.hostile.pirates.PirateCaptainMob;
import necesse.entity.mobs.hostile.pirates.PirateMob;
import necesse.gfx.gameTooltips.ListGameTooltips;
import necesse.level.maps.Level;
import necesse.level.maps.biomes.MobChance;
import necesse.level.maps.biomes.MobSpawnTable;

import java.awt.*;
import java.util.HashMap;

public class SanityBuff extends Buff {
	private int ticksSinceLastHallucination = 0;
	private int nextHallucination = 3333; // TODO: add save info?
	private int nextSanityIncrease = 1000;
	private final MobSpawnTable spawnTable = new MobSpawnTable();
	public static final HashMap<Integer, Integer> userMap = new HashMap<>(); // used to keep sanity data after death
	// TODO: somehow save this after restart?

	public SanityBuff() {
		this.canCancel = false;
		this.isVisible = false;
		this.isPassive = true;
	}

	@Override
	public void init(ActiveBuff activeBuff) {
		GNDItemMap gndItemMap = activeBuff.getGndData();
		if (!gndItemMap.hasKey("sanity")) {
			gndItemMap.setInt("sanity", 100);
		}
		// Create spawn table with harmless mobs
		// TODO: possibly reduce/remove drops (might be OP for farming end-game mob drops as an early game player, since mobs deal 0 damage)
		spawnTable.add(generate(10, HarmlessMobs.DeepCaveSpiritMob.class)
		).add(generate(15, HarmlessMobs.SandSpiritMob.class)
		).add(generate(10, HarmlessMobs.BlackCaveSpiderMob.class)
		).add(generate(15, HarmlessMobs.GiantCaveSpiderMob.class)
		).add(generate(7, HarmlessMobs.SwampCaveSpiderMob.class)
		).add(generate(20, HarmlessMobs.NinjaMob.class)
		).add(generate(20, HarmlessMobs.VampireMob.class)
		).add(generate(7, HarmlessMobs.VoidApprentice.class)
		).add(generate(1, PirateCaptainMob.class)
		).add(generate(20, HarmlessMobs.FrozenDwarfMob.class)
		);
		userMap.put(activeBuff.owner.getID(), 100);
	}

	@Override
	public void serverTick(ActiveBuff buff) {
		super.serverTick(buff);
		int sanity = buff.getGndData().getInt("sanity");
		if (!userMap.containsKey(buff.owner.getID())) // weird edge case
			userMap.put(buff.owner.getID(), sanity);
		else userMap.replace(buff.owner.getID(), sanity);

		if (sanity < 0)
			buff.getGndData().setInt("sanity", sanity = 0);
		if (sanity < 33) {
			this.isVisible = true;
			// TODO: fix mobs
			if (ticksSinceLastHallucination >= nextHallucination) {
				ticksSinceLastHallucination = 0;
				nextHallucination = GameRandom.globalRandom.getIntBetween(1200, 6000); // 60s -> 300s (assuming 20 TPS)
				for (int times = GameRandom.globalRandom.getIntBetween(1, 6 - sanity / 6); times > 0; --times) {
					Point tile = buff.owner.getMapPos();
					tile.x += GameRandom.globalRandom.getIntBetween(-100, 100);
					tile.y += GameRandom.globalRandom.getIntBetween(-100, 100);
					MobChance randomMob = spawnTable.getRandomMob(buff.owner.getLevel(), null, tile, GameRandom.globalRandom);
					if (randomMob != null) {
						Mob mob = randomMob.getMob(buff.owner.getLevel(), null, tile);
						mob.setLevel(buff.owner.getLevel());
						buff.owner.getLevel().entityManager.addMob(mob, tile.x, tile.y);
						if (mob.getLevel().isServerLevel())
							buff.owner.getLevel().getServer().network.sendToClientsAt(new PacketSpawnMob(mob), mob.getLevel());
						if (mob instanceof PirateMob) return; // don't want more mobs after a boss lmao
					}
				}
			} else ++ticksSinceLastHallucination;
		} else this.isVisible = false;
		if (nextSanityIncrease == 0) {
			nextSanityIncrease = 1200;
			buff.getGndData().setInt("sanity", ++sanity);
		} else --nextSanityIncrease;
	}

	@Override
	public ListGameTooltips getTooltip(ActiveBuff ab) {
		ListGameTooltips tooltips = new ListGameTooltips();
		tooltips.add(Localization.translate("buff", "sanity"));
		return tooltips;
	}

	private MobChance generate(int tickets, Class<? extends Mob> mob) {
		return new MobChance(tickets) {
			@Override
			public boolean canSpawn(Level level, ServerClient serverClient, Point point) {
				return true;
			}

			@Override
			public Mob getMob(Level level, ServerClient serverClient, Point point) {
				try {
					return mob.newInstance();
				} catch (InstantiationException | IllegalAccessException e) { // shouldn't happen
					throw new RuntimeException(e);
				}
			}
		};
	}
}
