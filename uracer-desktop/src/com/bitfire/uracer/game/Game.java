package com.bitfire.uracer.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.bitfire.uracer.Config;
import com.bitfire.uracer.Director;
import com.bitfire.uracer.URacer;
import com.bitfire.uracer.debug.Debug;
import com.bitfire.uracer.entities.EntityManager;
import com.bitfire.uracer.entities.vehicles.Car;
import com.bitfire.uracer.game.logic.GameLogic;
import com.bitfire.uracer.hud.Hud;
import com.bitfire.uracer.postprocessing.PostProcessor;
import com.bitfire.uracer.tiled.Level;

public class Game
{
	private Level level = null;
	private Car player = null;
	private Hud hud = null;

	// config
	public final GameplaySettings gameSettings;

	// logic
	private GameLogic logic = null;

	public Game( GameDifficulty difficulty )
	{
		gameSettings = GameplaySettings.create( difficulty );
		Director.create( Gdx.graphics.getWidth(), Gdx.graphics.getHeight() );

		level = Director.loadLevel( "level1", gameSettings );
		player = level.getPlayer();

		Hud.init();

		logic = new GameLogic( this );
		hud = new Hud( logic );
	}

	public void dispose()
	{
		Director.dispose();
	}

	public void tick()
	{
		logic.tick();
		level.tick();
		hud.tick();
		Debug.update();
	}

	public void render()
	{
		GL20 gl = Gdx.graphics.getGL20();
		EntityManager.raiseOnBeforeRender( URacer.getTemporalAliasing() );

		// follow the car
		if( player != null )
		{
			// we'll do here since we could have the interpolated position
			Director.setPositionPx( player.state().position, false );
		}

		if( Config.EnablePostProcessingFx )
		{
			PostProcessor.begin();
			level.render();
			PostProcessor.end();
		} else
		{
			gl.glViewport( 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight() );
			level.render();
		}

		hud.render();

		//
		// debug
		//

		if( Config.isDesktop ) Debug.renderB2dWorld( Director.getMatViewProjMt() );

		Debug.begin();
		EntityManager.raiseOnDebug();
		Debug.renderVersionInfo();
		Debug.renderGraphicalStats( Gdx.graphics.getWidth() - Debug.getStatsWidth(),
				Gdx.graphics.getHeight() - Debug.getStatsHeight() - Debug.fontHeight );
		if( Config.isDesktop ) Debug.renderMemoryUsage();
		// Debug.drawString( "EMgr::maxSpritesInBatch = " +
		// EntityManager.maxSpritesInBatch(), 0, 6 );
		// Debug.drawString( "EMgr::renderCalls = " +
		// EntityManager.renderCalls(), 0, 12 );
		Debug.end();

	}

	public Level getLevel()
	{
		return level;
	}

	public void restart()
	{
		Hud.clearMessages();
		level.restart();
		logic.restart();
	}

	public void reset()
	{
		Hud.clearMessages();
		level.restart();
		logic.reset();
	}
}