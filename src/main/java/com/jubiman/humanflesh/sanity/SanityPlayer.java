package com.jubiman.humanflesh.sanity;

import necesse.engine.network.packet.PacketSpawnMob;
import necesse.engine.network.server.Server;
import necesse.engine.save.LoadData;
import necesse.engine.save.SaveComponent;
import necesse.engine.save.SaveData;
import necesse.engine.util.GameRandom;
import necesse.entity.mobs.Mob;
import necesse.entity.mobs.PlayerMob;
import necesse.entity.mobs.hostile.pirates.PirateMob;
import necesse.level.maps.Level;
import necesse.level.maps.biomes.MobChance;

import java.awt.*;

public class SanityPlayer {
	public int ticksSinceLastHallucination;
	public int nextHallucination;
	public int nextSanityIncrease;
	private byte sanity;
	private final long auth;

	public SanityPlayer(long auth) {
		this.auth = auth;
		ticksSinceLastHallucination = 0;
		nextHallucination = 3333;
		nextSanityIncrease = 1200;
		sanity = 100;
	}

	public void tick(Server server) {
		if (sanity < 33) {
			if (ticksSinceLastHallucination >= nextHallucination) {
				ticksSinceLastHallucination = 0;
				nextHallucination = GameRandom.globalRandom.getIntBetween(12000, 60000); // 60s -> 300s (assuming 20 TPS)
				for (int times = GameRandom.globalRandom.getIntBetween(1, 6 - sanity / 6); times > 0; --times) {
					PlayerMob player = server.getPlayerByAuth(auth);
					Point tile = player.getMapPos();
					tile.x += GameRandom.globalRandom.getIntBetween(-100, 100);
					tile.y += GameRandom.globalRandom.getIntBetween(-100, 100);
					Level level = player.getLevel();
					MobChance randomMob = SanityPlayers.spawnTable.getRandomMob(level, null, tile, GameRandom.globalRandom);
					if (randomMob != null) {
						Mob mob = randomMob.getMob(level, null, tile);
						mob.setLevel(level);
						level.entityManager.addMob(mob, tile.x, tile.y);
						if (level.isServerLevel())
							level.getServer().network.sendToClientsAt(new PacketSpawnMob(mob), level);
						if (mob instanceof PirateMob) break; // don't want more mobs after a boss lmao
					}
				}
			} else ++ticksSinceLastHallucination;
		}
		if (nextSanityIncrease == 0) {
			nextSanityIncrease = 1200; // TODO: add small randomness?
			++sanity;
			if (sanity > 100) sanity = 100;
		} else --nextSanityIncrease;
	}

	public void setSanity(int amount) {
		sanity = (byte) amount;
		if (sanity < 0) sanity = 0;
		else if (sanity > 100) sanity = 100;
	}

	public void addSanity(int amount) {
		sanity = (byte) Math.min(sanity + amount, 100);
	}

	public void removeSanity(int amount) {
		sanity = (byte) Math.max(sanity - amount, 0);
	}

	public byte getSanity() {
		return sanity;
	}

	public void addSaveData(SaveData save) {
		SaveData player = new SaveData(String.valueOf(auth));
		player.addByte("sanity", sanity);
		player.addInt("nextHallucination", nextHallucination);
		player.addInt("nextSanityIncrease", nextSanityIncrease);
		player.addInt("ticksSinceLastHallucination", ticksSinceLastHallucination);
		save.addSaveData(player);
	}

	public void load(LoadData data) {
		sanity = data.getByte("sanity");
		nextHallucination = data.getInt("nextHallucination");
		nextSanityIncrease = data.getInt("nextSanityIncrease");
		ticksSinceLastHallucination = data.getInt("ticksSinceLastHallucination");
	}
}
