package com.jubiman.humanflesh.sanity;

import com.jubiman.customdatalib.api.Savable;
import com.jubiman.customdatalib.player.CustomPlayer;
import necesse.engine.GameLog;
import necesse.engine.network.packet.PacketMobBuff;
import necesse.engine.network.packet.PacketSpawnMob;
import necesse.engine.network.server.Server;
import necesse.engine.save.LoadData;
import necesse.engine.save.SaveData;
import necesse.engine.util.GameRandom;
import necesse.entity.mobs.Mob;
import necesse.entity.mobs.PlayerMob;
import necesse.entity.mobs.buffs.ActiveBuff;
import necesse.entity.mobs.hostile.pirates.PirateMob;
import necesse.level.maps.Level;
import necesse.level.maps.biomes.MobChance;

import java.awt.*;

public class SanityPlayer extends CustomPlayer implements Savable {
	public long nextHallucination;
	public short nextSanityIncrease;
	private byte sanity;

	public SanityPlayer(long auth) {
		super(auth);
		nextHallucination = 3333;
		nextSanityIncrease = 1200;
		sanity = 100;
	}

	@Override
	public void serverTick(Server server) {
		PlayerMob player = server.getPlayerByAuth(auth);
		if (sanity < 33) {
			int tps = server.tickManager().getTPS();
			if (!player.buffManager.hasBuff("insanityindicatorbuff"))
				player.addBuff(new ActiveBuff("insanityindicatorbuff", player, ((33 - sanity) * 1200 - 1200 + nextSanityIncrease) / tps, null), true);
			else {
				player.buffManager.getBuff("insanityindicatorbuff").setDurationLeftSeconds(((33 - sanity) * 1200 - 1200 + nextSanityIncrease) / (float) tps);
				server.network.sendToAllClients(new PacketMobBuff(player.getUniqueID(), player.buffManager.getBuff("insanityindicatorbuff")));
			}

			if (nextHallucination <= 0) {
				nextHallucination = GameRandom.globalRandom.getIntBetween(
						60 * tps,
						300 * tps
				); // 60s -> 300s
                GameLog.debug.println("Hallucination time: " + nextHallucination + " ticks");
                int times = GameRandom.globalRandom.getIntBetween(1, (8 - sanity / 4) + 1);
                GameLog.debug.println("Hallucination times: " + times);
				for (; times > 0; --times) {
					Point tile = player.getMapPos();
					tile.x += GameRandom.globalRandom.getIntBetween(-300, 300);
					tile.y += GameRandom.globalRandom.getIntBetween(-300, 300);
					Level level = player.getLevel();
					MobChance randomMob = SanityPlayersHandler.spawnTable.getRandomMob(level, player.getServerClient(), tile, GameRandom.globalRandom);
					if (randomMob != null) {
						Mob mob = randomMob.getMob(level, player.getServerClient(), tile);
						mob.setLevel(level);
						level.entityManager.addMob(mob, tile.x, tile.y);
						level.getServer().network.sendToClientsWithEntity(new PacketSpawnMob(mob), mob);
						if (mob instanceof PirateMob) break; // don't want more mobs after a boss lmao
					}
				}
			} else --nextHallucination;
		} else {
			if (player.buffManager.hasBuff("insanityindicatorbuff")) {
				player.buffManager.removeBuff("insanityindicatorbuff", true);
			}
		}
		if (nextSanityIncrease <= 0) {
			nextSanityIncrease = 1200;
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

	@Override
	public void addSaveData(SaveData save) {
		save.addByte("sanity", sanity);
		save.addLong("nextHallucination", nextHallucination);
		save.addInt("nextSanityIncrease", nextSanityIncrease);
	}

	@Override
	public void loadEnter(LoadData data) {
	}

	@Override
	public void loadExit(LoadData data) {
		sanity = data.getByte("sanity");
		nextHallucination = data.getLong("nextHallucination");
		nextSanityIncrease = data.getShort("nextSanityIncrease"); // shouldn't crash existing saves
	}
}
