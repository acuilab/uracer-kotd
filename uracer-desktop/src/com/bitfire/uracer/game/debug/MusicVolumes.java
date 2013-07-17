
package com.bitfire.uracer.game.debug;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.Array;
import com.bitfire.uracer.game.debug.DebugHelper.RenderFlags;
import com.bitfire.uracer.game.debug.player.DebugMeter;
import com.bitfire.uracer.game.logic.gametasks.sounds.effects.PlayerTensiveMusic;
import com.bitfire.uracer.game.player.PlayerCar;
import com.bitfire.uracer.resources.Art;
import com.bitfire.uracer.utils.ColorUtils;
import com.bitfire.uracer.utils.SpriteBatchUtils;

public class MusicVolumes extends DebugRenderable {
	private Array<DebugMeter> meters = new Array<DebugMeter>();
	private PlayerTensiveMusic tensiveMusic;
	private Matrix4 idt = new Matrix4();

	public MusicVolumes (RenderFlags flag, PlayerTensiveMusic tensiveMusic) {
		super(flag);
		this.tensiveMusic = tensiveMusic;

		for (int i = 0; i < PlayerTensiveMusic.NumTracks; i++) {
			DebugMeter m = new DebugMeter(64, Art.DebugFontHeight);
			m.setLimits(0, 1);
			m.setShowLabel(false);
			meters.add(m);
		}
	}

	@Override
	public void dispose () {
		for (DebugMeter m : meters) {
			m.dispose();
		}
	}

	@Override
	public void player (PlayerCar player) {
		super.player(player);
		if (hasPlayer) {
			this.player = player;
		}
	}

	private boolean isActive () {
		return hasPlayer;
	}

	@Override
	public void tick () {
		if (isActive()) {

			// String dbg = "";
			for (int i = 0; i < tensiveMusic.getVolumes().length; i++) {
				float v = tensiveMusic.getVolumes()[i];
				meters.get(i).setValue(v);
				// dbg += "[" + ((i == tensiveMusic.getMusicIndex()) ? "*" : " ") + String.format("%02.1f", v) + "] ";
			}

			// Gdx.app.log("MusicVolumes", dbg);
		}
	}

	@Override
	public void renderBatch (SpriteBatch batch) {
		if (isActive() && meters.size > 0) {
			Matrix4 prev = batch.getTransformMatrix();
			batch.setTransformMatrix(idt);
			batch.enableBlending();

			float prevHeight = 0;
			int index = 0;
			int drawx = 300;
			int drawy = 0;

			int maxMusicIndex = tensiveMusic.getCurrentMusicIndexLimit();
			SpriteBatchUtils.drawString(batch, "music tracks max=" + maxMusicIndex, drawx, drawy);
			SpriteBatchUtils.drawString(batch, "==================", drawx, drawy + Art.DebugFontHeight);

			String text;
			for (DebugMeter m : meters) {
				int x = drawx, y = drawy + Art.DebugFontHeight * 2;

				// if (index > maxMusicIndex) continue;

				// offset by index
				y += index * (prevHeight + 1);

				// compute color
				float alpha = index > maxMusicIndex ? 0.5f : 1;
				Color c = ColorUtils.paletteRYG(1.5f - m.getValue() * 1.5f, alpha);

				{
					// render track number
					text = "T" + (index + 1);
					batch.setColor(1, 1, 1, alpha);
					SpriteBatchUtils.drawString(batch, text, x, y);
					batch.setColor(1, 1, 1, 1);

					// render meter after text
					int meter_x = x + (text.length() * Art.DebugFontWidth) + 2;
					m.color.set(c);
					m.setPosition(meter_x, y);
					m.render(batch);

					// render volume numerical value
					text = String.format("%.02f", m.getValue());
					batch.setColor(1, 1, 1, alpha);
					SpriteBatchUtils.drawString(batch, text, meter_x + m.getWidth() + 2, y);
					batch.setColor(1, 1, 1, 1);
				}

				index++;
				prevHeight = m.getHeight();
			}

			batch.setTransformMatrix(prev);
			batch.disableBlending();
		}
	}

	@Override
	public void reset () {
	}

}
