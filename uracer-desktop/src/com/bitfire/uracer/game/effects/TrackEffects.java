package com.bitfire.uracer.game.effects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.bitfire.uracer.game.GameEvents;
import com.bitfire.uracer.game.effects.TrackEffect.Type;
import com.bitfire.uracer.game.events.GameLogicEvent;
import com.bitfire.uracer.game.events.GameRendererEvent;
import com.bitfire.uracer.game.player.Car;
import com.bitfire.uracer.task.Task;

public class TrackEffects extends Task {
	private TrackEffect[] effects = new TrackEffect[ Type.values().length ];

	private final GameLogicEvent.Listener gameLogicEvent = new GameLogicEvent.Listener() {
		@Override
		public void gameLogicEvent( com.bitfire.uracer.game.events.GameLogicEvent.Type type ) {
			switch( type ) {
			case onReset:
			case onRestart:
				reset();
				break;
			}
		}
	};

	private final GameRendererEvent.Listener gameRendererEvent = new GameRendererEvent.Listener() {
		@Override
		public void gameRendererEvent( GameRendererEvent.Type type ) {
			SpriteBatch batch = GameEvents.gameRenderer.batch;
			for( TrackEffect effect : effects ) {
				if( effect != null ) {
					effect.render( batch );
				}
			}
		}
	};

	public TrackEffects( Car car ) {
		GameEvents.gameLogic.addListener( gameLogicEvent );
		GameEvents.gameRenderer.addListener( gameRendererEvent, GameRendererEvent.Type.BatchBeforeMeshes, GameRendererEvent.Order.MINUS_4 );

		// TODO, custom render event
		// for CarSkidMarks GameRenderer.event.addListener( gameRendererEvent, GameRendererEvent.Type.BatchBeforeMeshes,
		// GameRendererEvent.Order.Order_Minus_4 );
		// for SmokeTrails GameRenderer.event.addListener( gameRendererEvent, GameRendererEvent.Type.BatchBeforeMeshes,
		// GameRendererEvent.Order.Order_Minus_3 );

		effects[Type.CarSkidMarks.ordinal()] = new CarSkidMarks( car );
		// effects[Type.SmokeTrails.ordinal()] = new SmokeTrails( car );
	}

	public TrackEffect get( Type what ) {
		return effects[what.ordinal()];
	}

	/** life */

	@Override
	public void onTick() {
		for( TrackEffect effect : effects ) {
			if( effect != null ) {
				effect.onTick();
			}
		}
	}

	public void reset() {
		for( TrackEffect effect : effects ) {
			if( effect != null ) {
				effect.reset();
			}
		}
	}

	@Override
	public void dispose() {
		for( TrackEffect effect : effects ) {
			if( effect != null ) {
				effect.dispose();
			}
		}

		effects = null;
	}

	public int getParticleCount( Type what ) {
		TrackEffect effect = effects[what.ordinal()];
		if( effect == null ) {
			return 0;
		}

		return effect.getParticleCount();
	}
}
