package com.jubiman.humanflesh.sanity;

import com.jubiman.humanflesh.mob.HarmlessMobs;
import necesse.engine.network.server.ServerClient;
import necesse.engine.save.LoadData;
import necesse.engine.save.SaveData;
import necesse.entity.mobs.Mob;
import necesse.entity.mobs.hostile.pirates.PirateCaptainMob;
import necesse.level.maps.Level;
import necesse.level.maps.biomes.MobChance;
import necesse.level.maps.biomes.MobSpawnTable;

import java.awt.*;
import java.util.Collection;
import java.util.HashMap;

public class SanityPlayers {
	private static final HashMap<Long, SanityPlayer> userMap = new HashMap<>();
	public static final MobSpawnTable spawnTable = new MobSpawnTable();

	public static void init() {
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
	}

	private static MobChance generate(int tickets, Class<? extends Mob> mob) {
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

	/**
	 * A null safe way to get a player from the map, adds player if they don't exist yet
	 * @param auth the authentication of the player's ServerClient
	 * @return the SanityPlayer object belonging to the player
	 */
	public static SanityPlayer get(long auth) {
		if (!userMap.containsKey(auth))
			userMap.put(auth, new SanityPlayer(auth));
		return userMap.get(auth);
	}

	/**
	 * Iterate through the values
	 * @return a collection of all values (all SanityPlayers)
	 */
	public static Collection<SanityPlayer> valueIterator() {
		return userMap.values();
	}

	public static void save(SaveData saveData) {
		SaveData save = new SaveData("SANITYPLAYERS");
		for (SanityPlayer player : valueIterator())
			player.addSaveData(save);

		saveData.addSaveData(save);
	}

	public static void load(LoadData loadData) {
		for (LoadData data : loadData.getLoadData())
			get(Long.parseLong(data.getName())).load(data);
	}

	public static void stop() {
		userMap.clear(); // avoid overwriting other worlds
	}
}
