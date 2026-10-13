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

package com.shatteredpixel.shatteredpixeldungeon.windows;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Cheats;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.items.EnergyCrystal;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.Potion;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.Ring;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.Scroll;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.Runestone;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfInstantKill;
import com.shatteredpixel.shatteredpixeldungeon.journal.Catalog;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.OptionSlider;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.ScrollingListPane;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.Reflection;

import java.util.ArrayList;
import java.util.Collection;

//The in-game cheat menu. Every tab is a scrollable list; tap a row to use it.
public class WndCheats extends WndTabbed {

	private static final int WIDTH_P = 126;
	private static final int WIDTH_L = 200;

	private static final int ON_COLOR = 0x44FF44;
	private static final int OFF_COLOR = 0xFFFFFF;

	public static int last_index = 0;

	private ArrayList<ScrollingListPane> panes = new ArrayList<>();

	public WndCheats() {
		super();

		int width = PixelScene.landscape() ? WIDTH_L : WIDTH_P;
		int height = (int)Math.min(PixelScene.landscape() ? 140 : 190, PixelScene.uiCamera.height - 50);

		addTab(buildHeroTab(),    new ItemSprite(ItemSpriteSheet.ARMOR_HOLDER),  width, height);
		addTab(buildItemsTab(),   new ItemSprite(ItemSpriteSheet.BACKPACK),      width, height);
		addTab(buildSpawnTab(),   new ItemSprite(ItemSpriteSheet.SOMETHING),     width, height);
		addTab(buildWorldTab(),   new ItemSprite(ItemSpriteSheet.SCROLL_HOLDER), width, height);
		addTab(buildEnemyTab(),   new ItemSprite(ItemSpriteSheet.MOB_HOLDER),    width, height);

		resize(width, height);
		layoutTabs();
		select(last_index);
	}

	private void addTab( final ScrollingListPane pane, Image icon, int width, int height ){
		final int index = panes.size();
		panes.add(pane);
		add(pane);
		pane.setRect(0, 0, width, height);
		add(new IconTab(icon){
			@Override
			protected void select(boolean value) {
				super.select(value);
				pane.visible = pane.active = value;
				if (value) last_index = index;
			}
		});
	}

	private Hero hero(){
		return Dungeon.hero;
	}

	// ***** Tabs *****

	private ScrollingListPane buildHeroTab(){
		ScrollingListPane list = new ScrollingListPane();
		list.addTitle("Hero cheats");
		list.addItem(new ToggleRow(Cheats.Toggle.GOD_MODE));
		list.addItem(new ToggleRow(Cheats.Toggle.ONE_HIT_KILL));
		list.addItem(new ToggleRow(Cheats.Toggle.NO_HUNGER));
		list.addItem(new ToggleRow(Cheats.Toggle.DOUBLE_SPEED));
		list.addItem(new ToggleRow(Cheats.Toggle.INVISIBLE));
		list.addItem(new ActionRow(new ItemSprite(ItemSpriteSheet.POTION_HOLDER), "Full heal (and cure debuffs)", new Callback() {
			@Override
			public void call() { Cheats.heal(hero()); }
		}));
		list.addItem(new ActionRow(new ItemSprite(ItemSpriteSheet.RATION), "Feed to full", new Callback() {
			@Override
			public void call() { Cheats.feed(hero()); }
		}));
		list.addItem(new ActionRow(Icons.get(Icons.TALENT), "Level up +1", new Callback() {
			@Override
			public void call() { Cheats.levelUp(hero(), false); }
		}));
		list.addItem(new ActionRow(Icons.get(Icons.TALENT), "Level up to max (30)", new Callback() {
			@Override
			public void call() { Cheats.levelUp(hero(), true); }
		}));
		list.addItem(new ActionRow(new ItemSprite(ItemSpriteSheet.POTION_HOLDER), "+1 strength", new Callback() {
			@Override
			public void call() { Cheats.addStrength(hero()); }
		}));
		list.addItem(new ActionRow(Icons.get(Icons.TALENT), "+1 talent point (every tier)", new Callback() {
			@Override
			public void call() { Cheats.addTalentPoints(hero()); }
		}));
		list.addItem(new ActionRow(new ItemSprite(ItemSpriteSheet.MASK), "Pick subclass / armor ability", new Callback() {
			@Override
			public void call() { Cheats.giveSubclassItems(hero()); }
		}));
		return list;
	}

	private ScrollingListPane buildItemsTab(){
		ScrollingListPane list = new ScrollingListPane();
		list.addTitle("Item cheats");
		list.addItem(new ToggleRow(Cheats.Toggle.INFINITE_WANDS));
		list.addItem(new ToggleRow(Cheats.Toggle.UNBREAKABLE_THROWN));
		list.addItem(new ToggleRow(Cheats.Toggle.FREE_SHOP));
		list.addItem(new ToggleRow(Cheats.Toggle.INSTANT_BOMBS));
		list.addItem(new ActionRow(new ItemSprite(ItemSpriteSheet.BACKPACK), "Clear inventory", new Callback() {
			@Override
			public void call() {
				GameScene.show(new WndOptions(Icons.get(Icons.WARNING), "Clear inventory",
						"This removes everything you are carrying and wearing. Gold is kept.",
						"Clear it", "Cancel"){
					@Override
					protected void onSelect(int index) {
						if (index == 0) Cheats.clearInventory(hero());
					}
				});
			}
		}));
		list.addItem(new ActionRow(new ItemSprite(ItemSpriteSheet.BACKPACK), "Reset to a class's starting kit", new Callback() {
			@Override
			public void call() {
				final HeroClass[] classes = HeroClass.values();
				String[] names = new String[classes.length];
				for (int i = 0; i < classes.length; i++){
					names[i] = Messages.titleCase(classes[i].title());
				}
				GameScene.show(new WndOptions(Icons.get(Icons.WARNING), "Reset inventory",
						"Your inventory is cleared and replaced with the chosen class's starting items. Your class does not change.",
						names){
					@Override
					protected void onSelect(int index) {
						Cheats.resetKit(hero(), classes[index]);
					}
				});
			}
		}));
		list.addItem(new ActionRow(new ItemSprite(ItemSpriteSheet.WATERSKIN), "Fill waterskin", new Callback() {
			@Override
			public void call() { Cheats.fillWaterskin(hero()); }
		}));
		list.addItem(new ActionRow(new ItemSprite(ItemSpriteSheet.WAND_HOLDER), "Charge all wands", new Callback() {
			@Override
			public void call() { Cheats.chargeWands(hero()); }
		}));
		list.addItem(new ActionRow(new ItemSprite(ItemSpriteSheet.ARTIFACT_HOLDER), "Charge artifacts & abilities", new Callback() {
			@Override
			public void call() { Cheats.chargeArtifacts(hero()); }
		}));
		list.addItem(new ActionRow(new ItemSprite(ItemSpriteSheet.SCROLL_HOLDER), "Identify everything carried", new Callback() {
			@Override
			public void call() { Cheats.identifyAll(hero()); }
		}));
		list.addItem(new ActionRow(new ItemSprite(ItemSpriteSheet.RING_HOLDER), "Reveal all potion/scroll/ring types", new Callback() {
			@Override
			public void call() { Cheats.revealItemTypes(); }
		}));
		list.addItem(new ActionRow(new ItemSprite(ItemSpriteSheet.SCROLL_HOLDER), "Remove all curses", new Callback() {
			@Override
			public void call() { Cheats.uncurseAll(hero()); }
		}));
		list.addItem(new ActionRow(new ItemSprite(ItemSpriteSheet.WEAPON_HOLDER), "Upgrade an item (+1)", new Callback() {
			@Override
			public void call() {
				hide();
				Cheats.upgradeItem();
			}
		}));
		list.addItem(new ActionRow(new ItemSprite(ItemSpriteSheet.GOLD), "+1,000 gold", new Callback() {
			@Override
			public void call() { Cheats.addGold(1000); }
		}));
		list.addItem(new ActionRow(new ItemSprite(ItemSpriteSheet.GOLD), "+10,000 gold", new Callback() {
			@Override
			public void call() { Cheats.addGold(10000); }
		}));
		list.addItem(new ActionRow(new ItemSprite(ItemSpriteSheet.ENERGY), "+100 alchemy energy", new Callback() {
			@Override
			public void call() { Cheats.addEnergy(100); }
		}));
		list.addItem(new ActionRow(new ItemSprite(ItemSpriteSheet.HOLDER), "Give all bags", new Callback() {
			@Override
			public void call() { Cheats.giveAllBags(hero()); }
		}));
		return list;
	}

	private ScrollingListPane buildSpawnTab(){
		ScrollingListPane list = new ScrollingListPane();
		list.addTitle("Spawn items");

		ArrayList<Class<?>> cheatItems = new ArrayList<>();
		cheatItems.add(WandOfInstantKill.class);
		list.addItem(new CategoryRow(new ItemSprite(ItemSpriteSheet.WAND_DISINTEGRATION), "Cheat items", cheatItems));

		ArrayList<Catalog> catalogs = new ArrayList<>();
		catalogs.addAll(Catalog.equipmentCatalogs);
		catalogs.addAll(Catalog.consumableCatalogs);
		for (Catalog catalog : catalogs){
			//enchantments and glyphs aren't items
			if (catalog == Catalog.ENCHANTMENTS || catalog == Catalog.GLYPHS) continue;
			list.addItem(new CategoryRow(categoryIcon(catalog),
					Messages.titleCase(catalog.title()), catalog.items()));
		}
		return list;
	}

	private ScrollingListPane buildWorldTab(){
		ScrollingListPane list = new ScrollingListPane();
		list.addTitle("World cheats");
		list.addItem(new ToggleRow(Cheats.Toggle.SEE_MONSTERS));
		list.addItem(new ToggleRow(Cheats.Toggle.DISABLE_TRAPS));
		list.addItem(new ActionRow(new ItemSprite(ItemSpriteSheet.SCROLL_HOLDER), "Reveal the whole level", new Callback() {
			@Override
			public void call() { Cheats.revealLevel(hero()); }
		}));
		list.addItem(new ActionRow(Icons.get(Icons.TARGET), "Teleport to a tapped tile", new Callback() {
			@Override
			public void call() {
				hide();
				Cheats.teleport(hero());
			}
		}));
		list.addItem(new ActionRow(Icons.get(Icons.STAIRS), "Go down one floor", new Callback() {
			@Override
			public void call() {
				hide();
				Cheats.travelTo(Dungeon.depth + 1);
			}
		}));
		list.addItem(new ActionRow(Icons.get(Icons.STAIRS), "Go up one floor", new Callback() {
			@Override
			public void call() {
				hide();
				Cheats.travelTo(Dungeon.depth - 1);
			}
		}));
		list.addItem(new ActionRow(Icons.get(Icons.STAIRS), "Jump to any floor (1-26)", new Callback() {
			@Override
			public void call() { GameScene.show(new WndCheatFloor(WndCheats.this)); }
		}));
		return list;
	}

	private ScrollingListPane buildEnemyTab(){
		ScrollingListPane list = new ScrollingListPane();
		list.addTitle("Enemy cheats");
		list.addItem(new ToggleRow(Cheats.Toggle.NO_SPAWNS));
		list.addItem(new ActionRow(new ItemSprite(ItemSpriteSheet.MOB_HOLDER), "Kill all enemies on this floor", new Callback() {
			@Override
			public void call() { Cheats.killAllEnemies(hero()); }
		}));
		list.addItem(new ActionRow(new ItemSprite(ItemSpriteSheet.MOB_HOLDER), "Freeze all enemies (20 turns)", new Callback() {
			@Override
			public void call() { Cheats.freezeAllEnemies(); }
		}));
		return list;
	}

	private static Image categoryIcon( Catalog catalog ){
		switch (catalog){
			case MELEE_WEAPONS:     return new ItemSprite(ItemSpriteSheet.WEAPON_HOLDER);
			case ARMOR:             return new ItemSprite(ItemSpriteSheet.ARMOR_HOLDER);
			case THROWN_WEAPONS:    return new ItemSprite(ItemSpriteSheet.MISSILE_HOLDER);
			case WANDS:             return new ItemSprite(ItemSpriteSheet.WAND_HOLDER);
			case RINGS:             return new ItemSprite(ItemSpriteSheet.RING_HOLDER);
			case ARTIFACTS:         return new ItemSprite(ItemSpriteSheet.ARTIFACT_HOLDER);
			case TRINKETS:          return new ItemSprite(ItemSpriteSheet.TRINKET_HOLDER);
			case POTIONS:           return new ItemSprite(ItemSpriteSheet.POTION_HOLDER);
			case EXOTIC_POTIONS:    return new ItemSprite(ItemSpriteSheet.POTION_HOLDER);
			case SCROLLS:           return new ItemSprite(ItemSpriteSheet.SCROLL_HOLDER);
			case EXOTIC_SCROLLS:    return new ItemSprite(ItemSpriteSheet.SCROLL_HOLDER);
			case SEEDS:             return new ItemSprite(ItemSpriteSheet.SEED_HOLDER);
			case STONES:            return new ItemSprite(ItemSpriteSheet.STONE_HOLDER);
			case FOOD:              return new ItemSprite(ItemSpriteSheet.FOOD_HOLDER);
			case BOMBS:             return new ItemSprite(ItemSpriteSheet.BOMB);
			case TIPPED_DARTS:      return new ItemSprite(ItemSpriteSheet.MISSILE_HOLDER);
			case BREWS_ELIXIRS:     return new ItemSprite(ItemSpriteSheet.ELIXIR_HOLDER);
			case SPELLS:            return new ItemSprite(ItemSpriteSheet.SPELL_HOLDER);
			default:                return new ItemSprite(ItemSpriteSheet.SOMETHING);
		}
	}

	//builds a display copy of an item that shows its true identity, not this run's random appearance
	private static Item displayItem( Class<?> cls ){
		Object obj = Reflection.newInstance(cls);
		if (!(obj instanceof Item)) return null;
		Item item = (Item) obj;
		if (item instanceof Potion){
			((Potion) item).anonymize();
		} else if (item instanceof Scroll){
			((Scroll) item).anonymize();
		} else if (item instanceof Ring){
			((Ring) item).anonymize();
		} else if (item instanceof Runestone){
			((Runestone) item).anonymize();
		}
		return item;
	}

	// ***** Rows *****

	//a tappable row that runs an action
	private static class ActionRow extends ScrollingListPane.ListItem {

		private final Callback action;

		public ActionRow( Image icon, String text, Callback action ){
			super(icon, null, text);
			this.action = action;
		}

		@Override
		public boolean onClick(float x, float y) {
			if (inside(x, y)){
				Sample.INSTANCE.play(Assets.Sounds.CLICK);
				action.call();
				return true;
			}
			return false;
		}
	}

	//a row that switches a cheat on or off, shown with a checkbox
	private static class ToggleRow extends ScrollingListPane.ListItem {

		private final Cheats.Toggle toggle;

		public ToggleRow( Cheats.Toggle toggle ){
			super(Icons.get(Icons.UNCHECKED), null, toggle.label);
			this.toggle = toggle;
			refresh();
		}

		private void refresh(){
			icon.copy(Icons.get(toggle.on() ? Icons.CHECKED : Icons.UNCHECKED));
			label.text(toggle.label + (toggle.on() ? ": ON" : ": OFF"));
			label.hardlight(toggle.on() ? ON_COLOR : OFF_COLOR);
			if (width > 0) layout();
		}

		@Override
		public boolean onClick(float x, float y) {
			if (inside(x, y)){
				Sample.INSTANCE.play(Assets.Sounds.CLICK);
				toggle.set(!toggle.on());
				refresh();
				return true;
			}
			return false;
		}
	}

	//a row that opens the list of items in one category
	private static class CategoryRow extends ScrollingListPane.ListItem {

		private final String title;
		private final Collection<Class<?>> classes;

		public CategoryRow( Image icon, String title, Collection<Class<?>> classes ){
			super(icon, null, title + " (" + classes.size() + ")");
			this.title = title;
			this.classes = classes;
		}

		@Override
		public boolean onClick(float x, float y) {
			if (inside(x, y)){
				Sample.INSTANCE.play(Assets.Sounds.CLICK);
				GameScene.show(new WndCheatSpawnList(title, classes));
				return true;
			}
			return false;
		}
	}

	// ***** Popup windows *****

	//lists every item in a category by its true name and picture
	public static class WndCheatSpawnList extends Window {

		public WndCheatSpawnList( String title, Collection<Class<?>> classes ){
			super();

			int width = PixelScene.landscape() ? WIDTH_L : WIDTH_P;
			int height = (int)Math.min(PixelScene.landscape() ? 160 : 210, PixelScene.uiCamera.height - 30);

			ScrollingListPane list = new ScrollingListPane();
			add(list);
			list.addTitle(title + " - tap to spawn");

			for (final Class<?> cls : classes){
				final Item item = displayItem(cls);
				if (item == null) continue;
				list.addItem(new ScrollingListPane.ListItem(new ItemSprite(item), null, Messages.titleCase(item.name())){
					@Override
					public boolean onClick(float x, float y) {
						if (inside(x, y)){
							Sample.INSTANCE.play(Assets.Sounds.CLICK);
							GameScene.show(new WndCheatSpawnItem(cls, item));
							return true;
						}
						return false;
					}
				});
			}

			resize(width, height);
			list.setRect(0, 0, width, height);
		}
	}

	//lets you pick an amount and upgrade level, then spawns the item
	public static class WndCheatSpawnItem extends Window {

		private static final int WIDTH = 120;
		private static final int BTN_HEIGHT = 18;
		private static final int GAP = 2;

		private int level = 0;

		public WndCheatSpawnItem( final Class<?> cls, Item displayItem ){
			super();

			IconTitle title = new IconTitle(new ItemSprite(displayItem), Messages.titleCase(displayItem.name()));
			title.setRect(0, 0, WIDTH, 0);
			add(title);

			float pos = title.bottom() + GAP;

			if (displayItem.isUpgradable()){
				OptionSlider levelSlider = new OptionSlider("Upgrade level", "+0", "+10", 0, 10) {
					@Override
					protected void onChange() {
						level = getSelectedValue();
					}
				};
				levelSlider.setRect(0, pos, WIDTH, 21);
				levelSlider.setSelectedValue(0);
				add(levelSlider);
				pos = levelSlider.bottom() + GAP;
			}

			int[] amounts;
			if (displayItem instanceof Gold){
				amounts = new int[]{100, 1000, 5000, 10000};
			} else if (displayItem instanceof EnergyCrystal){
				amounts = new int[]{10, 50, 100, 500};
			} else if (displayItem.stackable){
				amounts = new int[]{1, 5, 10, 20};
			} else {
				amounts = new int[]{1};
			}

			float btnWidth = (WIDTH - GAP * (amounts.length - 1)) / (float) amounts.length;
			for (int i = 0; i < amounts.length; i++){
				final int amount = amounts[i];
				RedButton btn = new RedButton(amounts.length == 1 ? "Spawn" : "x" + amount){
					@Override
					protected void onClick() {
						hide();
						Cheats.spawn(Dungeon.hero, cls, amount, level);
					}
				};
				btn.setRect(i * (btnWidth + GAP), pos, btnWidth, BTN_HEIGHT);
				add(btn);
			}
			pos += BTN_HEIGHT;

			resize(WIDTH, (int)pos);
		}
	}

	//picks a floor to jump to
	public static class WndCheatFloor extends Window {

		private static final int WIDTH = 120;

		private int depth;

		public WndCheatFloor( final Window parent ){
			super();

			depth = Math.max(1, Math.min(Cheats.MAX_DEPTH, Dungeon.depth));

			final RedButton go = new RedButton("Go to floor " + depth){
				@Override
				protected void onClick() {
					hide();
					parent.hide();
					Cheats.travelTo(depth);
				}
			};

			OptionSlider slider = new OptionSlider("Floor", "1", Integer.toString(Cheats.MAX_DEPTH), 1, Cheats.MAX_DEPTH) {
				@Override
				protected void onChange() {
					depth = getSelectedValue();
					go.text("Go to floor " + depth);
				}
			};
			slider.setRect(0, 0, WIDTH, 21);
			slider.setSelectedValue(depth);
			add(slider);

			go.setRect(0, slider.bottom() + 2, WIDTH, 18);
			add(go);

			resize(WIDTH, (int)go.bottom());
		}
	}
}
