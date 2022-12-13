package com.jubiman.humanflesh.item;

import com.jubiman.humanflesh.sanity.SanityPlayers;
import necesse.engine.localization.Localization;
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
		if (consumed)
			if (player.isServerClient())
				SanityPlayers.get(player.getServerClient().authentication).removeSanity(10);
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
