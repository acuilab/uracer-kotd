package com.bitfire.uracer.screen.transitions;

import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.TimeUtils;
import com.bitfire.uracer.postprocessing.FullscreenQuad;
import com.bitfire.uracer.utils.ShaderLoader;

public class Fader extends ScreenTransition {
	FrameBuffer from, to;
	long duration, elapsed, last;
	float factor;
	FullscreenQuad quad;
	ShaderProgram fade;

	public Fader( long durationMs ) {
		duration = durationMs;
		quad = new FullscreenQuad();
		fade = ShaderLoader.fromFile( "fade", "fade" );
		rebind();
	}

	private void rebind() {
		fade.begin();
		fade.setUniformi( "u_texture0", 0 );
		fade.setUniformi( "u_texture1", 1 );
		fade.end();
	}

	@Override
	public void init( FrameBuffer curr, FrameBuffer next ) {
		from = curr;
		to = next;
		factor = 0;

		elapsed = 0;
		last = 0;
	}

	@Override
	public void dispose() {
		quad.dispose();
		fade.dispose();
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
		rebind();
		last = 0;
	}

	@Override
	public void update() {
		long now = TimeUtils.millis();

		if( last == 0 ) {
			last = now;
			return;
		}

		long incr = now - last;

		elapsed += incr;
		last = now;

		if( elapsed < 0 ) {
			return;
		}

		if( elapsed > duration ) {
			elapsed = duration;
		}

		factor = (float)elapsed / (float)duration;
	}

	@Override
	public void render() {
		from.getColorBufferTexture().bind( 0 );
		to.getColorBufferTexture().bind( 1 );

		fade.begin();
		fade.setUniformf( "Ratio", factor );
		quad.render( fade );
		fade.end();
	}

	@Override
	public boolean hasFinished() {
		return elapsed >= duration;
	}
}
