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

public class HumanMeat extends FoodConsumableItem {
	public HumanMeat() {
		super(500, Rarity.EPIC, Settler.FOOD_SIMPLE, 30, 0);
		this.debuff().addGlobalIngredient("anyrawmeat").setItemCategory("consumable", "rawfood");
	}

	@Override
	public boolean consume(Level level, PlayerMob player, InventoryItem item) {
		boolean consumed = super.consume(level, player, item);
		if (consumed) {
			GNDItemMap gnd = player.buffManager.getBuff("sanity").getGndData();
			if (!gnd.hasKey("sanity"))
				gnd.setInt("sanity", 80);
			else gnd.setInt("sanity", gnd.getInt("sanity") - 20);
		}
		return consumed;
	}

	@Override
	public ListGameTooltips getTooltips(InventoryItem item, PlayerMob perspective) {
		ListGameTooltips tooltips = super.getTooltips(item, perspective);
		tooltips.remove(tooltips.size()-1);
		tooltips.add(GameColor.RED.getColorCode() + String.format(Localization.translate("buffmodifiers", "sanity"), -20));
		return tooltips;
	}
}
