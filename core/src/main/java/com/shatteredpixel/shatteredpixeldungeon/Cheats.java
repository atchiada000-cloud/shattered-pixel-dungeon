/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2026 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.shatteredpixel.shatteredpixeldungeon;

import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Awareness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Charm;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Chill;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Corrosion;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Daze;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Dread;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Frost;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hunger;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MindVision;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Ooze;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Roots;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Terror;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.items.EquipableItem;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.KingsCrown;
import com.shatteredpixel.shatteredpixeldungeon.items.TengusMask;
import com.shatteredpixel.shatteredpixeldungeon.items.Waterskin;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.ClassArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.Artifact;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.Bag;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.MagicalHolster;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.PotionBandolier;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.ScrollHolder;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.VelvetPouch;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.Key;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.Potion;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfExperience;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.Ring;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.Scroll;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRemoveCurse;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MagesStaff;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.journal.Catalog;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.InterlevelScene;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndBag;
import com.watabou.noosa.Game;
import com.watabou.utils.Reflection;

import java.util.ArrayList;

//All cheat logic lives here. Toggles are saved in the game's settings, so they persist between runs.
public class Cheats {

	public static final int MAX_DEPTH = 26;

	public enum Toggle {
		GOD_MODE            ("god_mode",                 "God mode"),
		ONE_HIT_KILL        ("cheat_one_hit_kill",       "One-hit kills"),
		NO_HUNGER           ("cheat_no_hunger",          "No hunger"),
		DOUBLE_SPEED        ("cheat_double_speed",       "Double speed"),
		INVISIBLE           ("cheat_invisible",          "Permanent invisibility"),
		SEE_MONSTERS        ("cheat_see_monsters",       "Always see all monsters"),
		INFINITE_WANDS      ("cheat_infinite_wands",     "Unlimited wand charges"),
		UNBREAKABLE_THROWN  ("cheat_unbreakable_thrown", "Thrown weapons never break"),
		FREE_SHOP           ("cheat_free_shop",          "Free shop items"),
		INSTANT_BOMBS       ("cheat_instant_bombs",      "Bombs explode on impact"),
		DISABLE_TRAPS       ("cheat_disable_traps",      "Disable traps"),
		NO_SPAWNS           ("cheat_no_spawns",          "No new monster spawns");

		public final String key;
		public final String label;

		//settings reads are cached, as some of these are checked very frequently
		private Boolean cached = null;

		Toggle( String key, String label ){
			this.key = key;
			this.label = label;
		}

		public boolean on(){
			if (cached == null){
				cached = SPDSettings.getBoolean(key, false);
			}
			return cached;
		}

		public void set( boolean value ){
			cached = value;
			SPDSettings.put(key, value);
			if (value) markUsed();
		}
	}

	public static void markUsed(){
		if (Dungeon.hero != null){
			Statistics.cheatsUsed = true;
		}
	}

	//called at the start of each hero turn
	public static void tick( Hero hero ){
		for (Toggle t : Toggle.values()){
			if (t.on()){
				Statistics.cheatsUsed = true;
				break;
			}
		}
		if (Toggle.INVISIBLE.on()){
			Buff.prolong(hero, Invisibility.class, 2f);
		}
		if (Toggle.SEE_MONSTERS.on()){
			Buff.prolong(hero, MindVision.class, 2f);
		}
	}

	public static float attackDelay( float delay ){
		return Toggle.DOUBLE_SPEED.on() ? delay / 2f : delay;
	}

	//gives an item to the hero, dropping it at their feet if the backpack is full
	public static void give( Hero hero, Item item ){
		if (!item.collect()){
			Dungeon.level.drop(item, hero.pos).sprite.drop();
		}
	}

	//kills a character outright. Bosses take a fatal blow instead so their phase logic still runs.
	public static void instantKill( Char ch, Object src ){
		if (ch == null || ch == Dungeon.hero || !ch.isAlive()) return;
		markUsed();
		if (Char.hasProp(ch, Char.Property.BOSS) || Char.hasProp(ch, Char.Property.MINIBOSS)){
			ch.damage(ch.HP + ch.shielding() + 1, src);
		} else {
			ch.die(src);
		}
	}

	// ***** Hero *****

	public static void heal( Hero hero ){
		markUsed();
		hero.HP = hero.HT;
		PotionOfHealing.cure(hero);
		Buff.detach(hero, Burning.class);
		Buff.detach(hero, Ooze.class);
		Buff.detach(hero, Corrosion.class);
		Buff.detach(hero, Chill.class);
		Buff.detach(hero, Frost.class);
		Buff.detach(hero, Paralysis.class);
		Buff.detach(hero, Roots.class);
		Buff.detach(hero, Charm.class);
		Buff.detach(hero, Terror.class);
		Buff.detach(hero, Dread.class);
		Buff.detach(hero, Daze.class);
		Buff.detach(hero, Vertigo.class);
		Buff.detach(hero, Blindness.class);
		GLog.p("Fully healed and cured.");
	}

	public static void feed( Hero hero ){
		markUsed();
		Hunger hunger = hero.buff(Hunger.class);
		if (hunger != null){
			hunger.satisfy(Hunger.STARVING);
		}
		GLog.p("You are completely full.");
	}

	public static void levelUp( Hero hero, boolean toMax ){
		if (hero.lvl >= Hero.MAX_LEVEL){
			GLog.w("Already at the max level.");
			return;
		}
		markUsed();
		do {
			hero.earnExp(hero.maxExp() - hero.exp, PotionOfExperience.class);
		} while (toMax && hero.lvl < Hero.MAX_LEVEL);
	}

	public static void addStrength( Hero hero ){
		markUsed();
		hero.STR++;
		GLog.p("Strength increased to " + hero.STR + ".");
	}

	public static void addTalentPoints( Hero hero ){
		markUsed();
		hero.cheatTalentPoints++;
		GLog.p("+1 talent point in every unlocked tier (" + hero.cheatTalentPoints + " bonus total).");
	}

	public static void giveSubclassItems( Hero hero ){
		markUsed();
		give(hero, new TengusMask());
		give(hero, new KingsCrown());
		GLog.p("Use Tengu's Mask to pick a subclass and the King's Crown to pick an armor ability.");
	}

	// ***** Items *****

	public static void clearInventory( Hero hero ){
		markUsed();

		EquipableItem[] equipped = new EquipableItem[]{
				hero.belongings.weapon, hero.belongings.secondWep, hero.belongings.armor,
				hero.belongings.artifact, hero.belongings.misc, hero.belongings.ring };
		for (EquipableItem item : equipped){
			if (item != null){
				item.cursed = false;
				item.doUnequip(hero, false, false);
			}
		}
		hero.belongings.weapon = null;
		hero.belongings.secondWep = null;
		hero.belongings.armor = null;
		hero.belongings.artifact = null;
		hero.belongings.misc = null;
		hero.belongings.ring = null;

		Bag backpack = hero.belongings.backpack;
		ArrayList<Item> contents = new ArrayList<>();
		for (Item item : backpack){
			contents.add(item);
		}
		//detach regular items first, then the bags that held them
		for (Item item : contents){
			if (!(item instanceof Bag)) item.detachAll(backpack);
		}
		for (Item item : contents){
			if (item instanceof Bag) item.detachAll(backpack);
		}
		backpack.clear();

		Dungeon.quickslot.reset();
		Item.updateQuickslot();
		GLog.p("Inventory cleared.");
	}

	public static void resetKit( Hero hero, HeroClass cls ){
		clearInventory(hero);
		cls.giveStartingKit(hero);
		Item.updateQuickslot();
		GLog.p("Inventory reset to the " + cls.title() + "'s starting kit.");
	}

	public static void fillWaterskin( Hero hero ){
		markUsed();
		Waterskin skin = hero.belongings.getItem(Waterskin.class);
		if (skin == null){
			skin = new Waterskin();
			give(hero, skin);
		}
		skin.fill();
		GLog.p("Waterskin filled.");
	}

	public static void chargeWands( Hero hero ){
		markUsed();
		for (Wand wand : hero.belongings.getAllItems(Wand.class)){
			wand.curCharges = Math.max(wand.curCharges, wand.maxCharges);
		}
		for (MagesStaff staff : hero.belongings.getAllItems(MagesStaff.class)){
			staff.gainCharge(100);
		}
		Item.updateQuickslot();
		GLog.p("All wands fully charged.");
	}

	public static void chargeArtifacts( Hero hero ){
		markUsed();
		for (Artifact artifact : hero.belongings.getAllItems(Artifact.class)){
			artifact.charge(hero, 1000f);
		}
		for (ClassArmor armor : hero.belongings.getAllItems(ClassArmor.class)){
			armor.charge = 100f;
		}
		MeleeWeapon.Charger weaponCharger = hero.buff(MeleeWeapon.Charger.class);
		if (weaponCharger != null){
			weaponCharger.charges = weaponCharger.chargeCap();
		}
		Item.updateQuickslot();
		GLog.p("Artifacts, class armor and weapon abilities charged.");
	}

	public static void identifyAll( Hero hero ){
		markUsed();
		ArrayList<Item> items = new ArrayList<>();
		for (Item item : hero.belongings){
			items.add(item);
		}
		for (Item item : items){
			item.identify();
		}
		Item.updateQuickslot();
		GLog.p("Everything you are carrying is identified.");
	}

	public static void revealItemTypes(){
		markUsed();
		Catalog[] catalogs = new Catalog[]{ Catalog.POTIONS, Catalog.SCROLLS, Catalog.RINGS,
				Catalog.EXOTIC_POTIONS, Catalog.EXOTIC_SCROLLS };
		for (Catalog catalog : catalogs){
			for (Class<?> cls : catalog.items()){
				Object item = Reflection.newInstance(cls);
				if (item instanceof Potion){
					((Potion) item).setKnown();
				} else if (item instanceof Scroll){
					((Scroll) item).setKnown();
				} else if (item instanceof Ring){
					((Ring) item).setKnown();
				}
			}
		}
		Item.updateQuickslot();
		GLog.p("All potion, scroll and ring types revealed.");
	}

	public static void uncurseAll( Hero hero ){
		markUsed();
		ArrayList<Item> items = new ArrayList<>();
		for (Item item : hero.belongings){
			items.add(item);
		}
		ScrollOfRemoveCurse.uncurse(hero, items.toArray(new Item[0]));
		Item.updateQuickslot();
		GLog.p("All curses removed.");
	}

	public static void upgradeItem(){
		GameScene.selectItem(new WndBag.ItemSelector() {
			@Override
			public String textPrompt() {
				return "Upgrade which item?";
			}

			@Override
			public boolean itemSelectable(Item item) {
				return item.isUpgradable();
			}

			@Override
			public void onSelect(Item item) {
				if (item != null){
					markUsed();
					item.upgrade();
					Item.updateQuickslot();
					GLog.p("Upgraded " + item.name() + ".");
				}
			}
		});
	}

	public static void addGold( int amount ){
		markUsed();
		Dungeon.gold += amount;
		GLog.p("+" + amount + " gold.");
	}

	public static void addEnergy( int amount ){
		markUsed();
		Dungeon.energy += amount;
		GLog.p("+" + amount + " alchemy energy.");
	}

	public static void giveAllBags( Hero hero ){
		markUsed();
		if (hero.belongings.getItem(VelvetPouch.class) == null)     give(hero, new VelvetPouch());
		if (hero.belongings.getItem(PotionBandolier.class) == null) give(hero, new PotionBandolier());
		if (hero.belongings.getItem(ScrollHolder.class) == null)    give(hero, new ScrollHolder());
		if (hero.belongings.getItem(MagicalHolster.class) == null)  give(hero, new MagicalHolster());
		GLog.p("You have every bag.");
	}

	//creates and gives a spawned item from the cheat menu
	public static void spawn( Hero hero, Class<?> cls, int quantity, int level ){
		Item item = (Item) Reflection.newInstance(cls);
		if (item == null) return;
		markUsed();

		if (item.stackable){
			item.quantity(quantity);
		}
		if (level > 0 && item.isUpgradable()){
			item.upgrade(level);
		}
		if (item instanceof Wand){
			((Wand) item).curCharges = ((Wand) item).maxCharges;
		}
		if (item instanceof Key){
			((Key) item).depth = Dungeon.depth;
		}
		item.identify();

		String name = item.name();
		if (!item.doPickUp(hero, hero.pos)){
			Dungeon.level.drop(item, hero.pos).sprite.drop();
			GLog.w("Your pack is full, so the " + name + " was dropped at your feet.");
		} else {
			GLog.p("Spawned " + (item.stackable && quantity > 1 ? quantity + "x " : "") + name + ".");
		}
	}

	// ***** World *****

	public static void revealLevel( Hero hero ){
		markUsed();
		Level level = Dungeon.level;
		for (int i = 0; i < level.length(); i++){
			if (level.discoverable[i]){
				level.mapped[i] = true;
				if ((Terrain.flags[level.map[i]] & Terrain.SECRET) != 0){
					level.discover(i);
				}
			}
		}
		Buff.prolong(hero, Awareness.class, 30f);
		Buff.prolong(hero, MindVision.class, 30f);
		GameScene.updateMap();
		Dungeon.observe();
		GameScene.updateFog();
		GLog.p("The whole level is revealed, including secrets, traps, items and monsters.");
	}

	public static void teleport( final Hero hero ){
		GameScene.selectCell(new CellSelector.Listener() {
			@Override
			public void onSelect(Integer cell) {
				if (cell == null) return;
				Level level = Dungeon.level;
				if (cell < 0 || cell >= level.length()
						|| !(level.passable[cell] || level.avoid[cell])
						|| (Actor.findChar(cell) != null && Actor.findChar(cell) != hero)){
					GLog.w("You can't teleport there.");
					return;
				}
				markUsed();
				ScrollOfTeleportation.appear(hero, cell);
				Buff.detach(hero, Roots.class);
				level.occupyCell(hero);
				Dungeon.observe();
				GameScene.updateFog();
			}

			@Override
			public String prompt() {
				return "Tap a tile to teleport to";
			}
		});
	}

	public static void travelTo( int depth ){
		depth = Math.max(1, Math.min(MAX_DEPTH, depth));
		if (depth == Dungeon.depth && Dungeon.branch == 0){
			GLog.w("You are already on floor " + depth + ".");
			return;
		}
		markUsed();
		Level.beforeTransition();
		InterlevelScene.mode = InterlevelScene.Mode.RETURN;
		InterlevelScene.returnDepth = depth;
		InterlevelScene.returnBranch = 0;
		InterlevelScene.returnPos = -1;
		Game.switchScene(InterlevelScene.class);
	}

	// ***** Enemies *****

	public static void killAllEnemies( Hero hero ){
		int killed = 0;
		for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])){
			if (mob.alignment == Char.Alignment.ENEMY && mob.isAlive()){
				instantKill(mob, hero);
				killed++;
			}
		}
		markUsed();
		GLog.p("Killed " + killed + " enem" + (killed == 1 ? "y" : "ies") + ".");
	}

	public static void freezeAllEnemies(){
		int frozen = 0;
		for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])){
			if (mob.alignment == Char.Alignment.ENEMY && mob.isAlive()){
				Buff.prolong(mob, Paralysis.class, 20f);
				frozen++;
			}
		}
		markUsed();
		GLog.p("Froze " + frozen + " enem" + (frozen == 1 ? "y" : "ies") + " for 20 turns.");
	}
}
