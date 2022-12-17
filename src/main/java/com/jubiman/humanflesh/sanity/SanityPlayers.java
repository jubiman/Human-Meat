package com.jubiman.humanflesh.sanity;

import com.jubiman.customplayerlib.CustomPlayerRegistry;
import com.jubiman.customplayerlib.CustomPlayers;
import com.jubiman.humanflesh.mob.HarmlessMobs;
import necesse.engine.network.server.ServerClient;
import necesse.entity.mobs.Mob;
import necesse.entity.mobs.hostile.pirates.PirateCaptainMob;
import necesse.level.maps.Level;
import necesse.level.maps.biomes.MobChance;
import necesse.level.maps.biomes.MobSpawnTable;

import java.awt.*;

public class SanityPlayers extends CustomPlayers<SanityPlayer> {
	public static final String name = "SANITYPLAYERS";
	public static final MobSpawnTable spawnTable = new MobSpawnTable();

	public SanityPlayers() {
		super(SanityPlayer.class, name);
	}

	public void init() {
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

	public static SanityPlayers getInstance() {
		return (SanityPlayers) CustomPlayerRegistry.get(name);
	}

	/**
	 * A null safe way to get a player from the map, adds player if they don't exist yet
	 * @param auth the authentication of the player's ServerClient
	 * @return the SanityPlayer object belonging to the player
	 */
	public static SanityPlayer getPlayer(long auth) {
		return getInstance().get(auth);
	}
}
