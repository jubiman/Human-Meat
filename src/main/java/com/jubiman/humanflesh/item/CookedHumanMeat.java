package com.jubiman.humanflesh.item;

import necesse.engine.localization.Localization;
import necesse.engine.network.gameNetworkData.GNDItemMap;
import necesse.entity.mobs.PlayerMob;
import necesse.gfx.GameColor;
import necesse.gfx.gameTooltips.ListGameTooltips;
import necesse.inventory.InventoryItem;
import necesse.inventory.item.placeableItem.consumableItem.food.FoodConsumableItem;
import necesse.level.maps.Level;
import necesse.level.maps.levelData.settlementData.settler.Settler;

public class CookedHumanMeat extends FoodConsumableItem {
	public CookedHumanMeat() {
		super(50, Rarity.LEGENDARY, Settler.FOOD_GOURMET, 66, 0);
		this.debuff();
	}

	@Override
	public boolean consume(Level level, PlayerMob player, InventoryItem item) {
		boolean consumed = super.consume(level, player, item);
		if (consumed) {
			GNDItemMap gnd = player.buffManager.getBuff("sanity").getGndData();
			if (!gnd.hasKey("sanity"))
				gnd.setInt("sanity", 90);
			else gnd.setInt("sanity", gnd.getInt("sanity") - 10);
		}
		return consumed;
	}

	@Override
	public ListGameTooltips getTooltips(InventoryItem item, PlayerMob perspective) {
		ListGameTooltips tooltips = super.getTooltips(item, perspective);
		tooltips.remove(tooltips.size()-1);
		tooltips.add(GameColor.RED.getColorCode() + String.format(Localization.translate("buffmodifiers", "sanity"), -10));
		return tooltips;
	}
}
