package com.jubiman.humanflesh.mob.settler;

import com.jubiman.humanflesh.mob.human.ManiacHumanMob;
import necesse.engine.network.server.Server;
import necesse.engine.network.server.ServerClient;
import necesse.engine.registries.MobRegistry;
import necesse.engine.util.TicketSystemList;
import necesse.entity.mobs.friendly.human.HumanMob;
import necesse.entity.mobs.friendly.human.humanShop.FarmerHumanMob;
import necesse.entity.mobs.friendly.human.humanShop.HunterHumanMob;
import necesse.level.maps.Level;
import necesse.level.maps.levelData.settlementData.SettlementLevelData;
import necesse.level.maps.levelData.settlementData.settler.Settler;

import java.awt.*;
import java.util.function.Supplier;

public class ManiacSettler extends Settler {
	public ManiacSettler() {
		super("maniachuman");
	}

	public void addNewRecruitSettler(SettlementLevelData data, boolean isRandomEvent, TicketSystemList<Supplier<HumanMob>> ticketSystem) {
		ticketSystem.addObject(66, getNewRecruitMob(data));
	}

	@Override
	public void spawnAtClient(Server server, ServerClient client, Level level) {
		ManiacHumanMob mob = (ManiacHumanMob) MobRegistry.getMob(this.mobType, level);
		Point spawnPos = client.playerMob.getMapPos();
		if (spawnPos != null)
			level.entityManager.addMob(mob, (float) spawnPos.x, (float) spawnPos.y);
	}
}
