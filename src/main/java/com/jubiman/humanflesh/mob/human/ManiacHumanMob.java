package com.jubiman.humanflesh.mob.human;

import necesse.engine.network.server.Server;
import necesse.engine.network.server.ServerClient;
import necesse.engine.registries.MobRegistry;
import necesse.engine.tickManager.TickManager;
import necesse.engine.util.GameLootUtils;
import necesse.engine.util.GameRandom;
import necesse.entity.mobs.HumanTextureFull;
import necesse.entity.mobs.MobDrawable;
import necesse.entity.mobs.PlayerMob;
import necesse.entity.mobs.friendly.human.humanShop.HumanShop;
import necesse.entity.mobs.friendly.human.humanShop.HunterHumanMob;
import necesse.gfx.camera.GameCamera;
import necesse.gfx.drawOptions.DrawOptions;
import necesse.gfx.drawOptions.human.HumanDrawOptions;
import necesse.gfx.drawables.OrderableDrawables;
import necesse.gfx.gameTexture.GameTexture;
import necesse.inventory.InventoryItem;
import necesse.inventory.lootTable.lootItem.CountOfTicketLootItems;
import necesse.inventory.lootTable.lootItem.LootItem;
import necesse.level.maps.Level;
import necesse.level.maps.levelData.villageShops.ShopItem;
import necesse.level.maps.levelData.villageShops.VillageShopsData;
import necesse.level.maps.light.GameLight;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;

public class ManiacHumanMob extends HumanShop {
	public static HumanTextureFull texture;

	public ManiacHumanMob() {
		super(666, 666, "maniac");
	}

	@Override
	public HumanGender getHumanGender() {
		return HumanGender.MALE;
	}

	@Override
	public ArrayList<ShopItem> getShopItems(VillageShopsData data, ServerClient client) {
		if (isTravelingHuman())
			return null;
		ArrayList<ShopItem> out = new ArrayList<>();
		GameRandom random = new GameRandom(getShopSeed() + 5L);
		out.add(ShopItem.item("humanmeat", getRandomHappinessPrice(random, 10, 20, 2)));
		ShopItem.addStockedItem(out, data, "cookedhumanmeat", getRandomHappinessPrice(random, -24, -6, 3));
		return out;
	}

	@Override
	public List<InventoryItem> getRecruitItems(ServerClient client) {
		if (this.isSettler()) {
			return null;
		} else {
			GameRandom random = new GameRandom(this.getSettlerSeed() * 83L);
			if (this.isTravelingHuman()) {
				return Collections.singletonList(new InventoryItem("coin", random.getIntBetween(100, 1000)));
			} else {
				ArrayList<InventoryItem> out = new ArrayList<>();
				out.add(new InventoryItem("humanmeat", random.getIntBetween(3, 50)));
				out.add(new InventoryItem("coin", random.getIntBetween(100, 1000)));
				return out;
			}
		}
	}

	@Override
	public DrawOptions getUserDrawOptions(Level level, int x, int y, TickManager tickManager, GameCamera camera, PlayerMob perspective, Consumer<HumanDrawOptions> humanDrawOptionsModifier) {
		GameLight light = level.getLightLevel(x / 32, y / 32);
		int drawX = camera.getDrawX(x) - 22 - 10;
		int drawY = camera.getDrawY(y) - 44 - 7;
		Point sprite = this.getAnimSprite(x, y, this.dir);
		HumanDrawOptions humanOptions = (new HumanDrawOptions(texture)).helmet(this.getDisplayArmor(0, "farmerhat")).chestplate(this.getDisplayArmor(1, "farmershirt")).boots(this.getDisplayArmor(2, "farmershoes")).sprite(sprite).dir(this.dir).light(light);
		humanDrawOptionsModifier.accept(humanOptions);
		DrawOptions drawOptions = humanOptions.pos(drawX, drawY);
		DrawOptions markerOptions = this.getMarkerDrawOptions(x, y, light, camera, 0, -45, perspective);
		return () -> {
			drawOptions.draw();
			markerOptions.draw();
		};
	}

	@Override
	public void addDrawables(List<MobDrawable> list, OrderableDrawables tileList, OrderableDrawables topList, Level level, int x, int y, TickManager tickManager, GameCamera camera, PlayerMob perspective) {
		super.addDrawables(list, tileList, topList, level, x, y, tickManager, camera, perspective);
		if (this.objectUser == null || this.objectUser.object.drawsUsers()) {
			if (this.isVisible()) {
				GameLight light = level.getLightLevel(x / 32, y / 32);
				int drawX = camera.getDrawX(x) - 22 - 10;
				int drawY = camera.getDrawY(y) - 44 - 7;
				Point sprite = this.getAnimSprite(x, y, this.dir);
				drawY += this.getBobbing(x, y);
				drawY += this.getLevel().getTile(x / 32, y / 32).getMobSinkingAmount(this);
				HumanDrawOptions humanDrawOptions = (new HumanDrawOptions(texture)).helmet(this.getDisplayArmor(0, "farmerhat")).chestplate(this.getDisplayArmor(1, "farmershirt")).boots(this.getDisplayArmor(2, "farmershoes")).sprite(sprite).dir(this.dir).holdItem(new InventoryItem("farmerpitchfork")).light(light);
				humanDrawOptions = this.setCustomItemAttackOptions(humanDrawOptions);
				final DrawOptions drawOptions = humanDrawOptions.pos(drawX, drawY);
				final DrawOptions markerOptions = this.getMarkerDrawOptions(x, y, light, camera, 0, -45, perspective);
				list.add(new MobDrawable() {
					public void draw(TickManager tickManager) {
						drawOptions.draw();
						markerOptions.draw();
					}
				});
				this.addShadowDrawables(tileList, x, y, light, camera);
			}
		}
	}
}
