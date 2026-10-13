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

package com.shatteredpixel.shatteredpixeldungeon.items.wands;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Cheats;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MagesStaff;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;

//cheat item: kills whatever it hits. Only obtainable from the cheat menu's item spawner.
public class WandOfInstantKill extends Wand {

	{
		image = ItemSpriteSheet.WAND_DISINTEGRATION;
	}

	private static final ItemSprite.Glowing RED = new ItemSprite.Glowing( 0xFF2222 );

	@Override
	public void onZap(Ballistica bolt) {

		Char ch = Actor.findChar( bolt.collisionPos );
		if (ch != null && ch != curUser) {
			Sample.INSTANCE.play( Assets.Sounds.HIT_MAGIC );
			ch.sprite.burst(0xFFFF2222, 10);
			Cheats.instantKill(ch, this);
		} else {
			Dungeon.level.pressCell(bolt.collisionPos);
		}
	}

	@Override
	public void onHit(MagesStaff staff, Char attacker, Char defender, int damage) {
		if (defender != null && defender != attacker) {
			Cheats.instantKill(defender, staff);
		}
	}

	@Override
	public int initialCharges() {
		return 10;
	}

	@Override
	public ItemSprite.Glowing glowing() {
		return RED;
	}
}
