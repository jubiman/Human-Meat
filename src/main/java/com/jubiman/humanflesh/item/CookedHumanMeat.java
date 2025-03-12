package com.jubiman.humanflesh.item;

import com.jubiman.humanflesh.sanity.SanityPlayersHandler;
import necesse.engine.localization.Localization;
import necesse.engine.util.GameBlackboard;
import necesse.entity.mobs.HungerMob;
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
		this.debuff().setItemCategory("consumable", "food");
	}

	@Override
	public boolean consume(Level level, HungerMob hungerMob, InventoryItem item) {
		boolean consumed = super.consume(level, hungerMob, item);
		if (consumed)
			if (hungerMob instanceof PlayerMob && ((PlayerMob) hungerMob).isServerClient())
				SanityPlayersHandler.getPlayer(((PlayerMob) hungerMob).getServerClient()).removeSanity(10);
		return consumed;
	}

	@Override
	public ListGameTooltips getTooltips(InventoryItem item, PlayerMob perspective, GameBlackboard blackboard) {
		ListGameTooltips tooltips = super.getTooltips(item, perspective, blackboard);
		tooltips.remove(tooltips.size()-1);
		tooltips.add(GameColor.RED.getColorCode() + String.format(Localization.translate("buffmodifiers", "sanity"), -10));
		return tooltips;
	}
}
