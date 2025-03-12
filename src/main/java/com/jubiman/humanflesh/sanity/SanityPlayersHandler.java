package com.jubiman.humanflesh.sanity;

import com.jubiman.customdatalib.player.CustomPlayerRegistry;
import com.jubiman.customdatalib.player.CustomPlayersHandler;
import com.jubiman.humanflesh.mob.HarmlessMobs;
import necesse.engine.GameLog;
import necesse.engine.network.client.ClientClient;
import necesse.engine.network.server.ServerClient;
import necesse.entity.mobs.Mob;
import necesse.entity.mobs.PlayerMob;
import necesse.entity.mobs.hostile.pirates.PirateCaptainMob;
import necesse.level.maps.Level;
import necesse.level.maps.biomes.MobChance;
import necesse.level.maps.biomes.MobSpawnTable;

import java.awt.*;
import java.lang.reflect.InvocationTargetException;

public class SanityPlayersHandler extends CustomPlayersHandler<SanityPlayer> {
	public static final String name = "SANITYPLAYERS";
	public static final MobSpawnTable spawnTable = new MobSpawnTable();

	public SanityPlayersHandler() {
		super(SanityPlayer.class, name);
		init();
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
			public boolean canSpawn(Level level, ServerClient serverClient, Point point, String purpose) {
				return purpose.equals("hallucination");
			}

			@Override
			public Mob getMob(Level level, ServerClient serverClient, Point point) {
				try {
					return mob.getDeclaredConstructor().newInstance();
				} catch (InstantiationException | IllegalAccessException e) { // shouldn't happen
					throw new RuntimeException(e);
				} catch (InvocationTargetException | NoSuchMethodException e) {
					throw new RuntimeException(e);
				}
			}
		};
	}

	public static SanityPlayersHandler getInstance() {
		return (SanityPlayersHandler) CustomPlayerRegistry.INSTANCE.get(name);
	}

	/**
	 * A null safe way to get a player from the map, adds player if they don't exist yet
	 * @param auth the authentication of the player's ServerClient
	 * @return the SanityPlayer object belonging to the player
	 */
	public static SanityPlayer getPlayer(long auth) {
		return getInstance().get(auth);
	}

	/**
	 * Gets the player from the ServerClient or the ClientClient.
	 * @param serverClient The ServerClient to get the SanityPlayer from.
	 * @return The SanityPlayer.
	 */
	public static SanityPlayer getPlayer(ServerClient serverClient) {
		return getPlayer(serverClient.authentication);
	}

	/**
	 * Gets the player from the ServerClient or the ClientClient.
	 * @param clientClient The ClientClient to get the SanityPlayer from.
	 * @return The SanityPlayer.
	 */
	public static SanityPlayer getPlayer(ClientClient clientClient) {
		return getPlayer(clientClient.authentication);
	}

	/**
	 * Gets the player from the PlayerMob.
	 * @param playerMob The PlayerMob to get the SanityPlayer from.
	 * @return The SanityPlayer.
	 */
	public static SanityPlayer getPlayer(PlayerMob playerMob) {
		if (playerMob.isClientClient())
			return getPlayer(playerMob.getClientClient());
		else
			return getPlayer(playerMob.getServerClient());
	}
}
