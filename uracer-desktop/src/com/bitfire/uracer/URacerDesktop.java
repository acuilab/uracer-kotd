package com.bitfire.uracer;

import org.lwjgl.opengl.Display;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.backends.openal.OpenALAudio;


//public class URacerDesktop
//{
//	public static void main (String[] argv) {
//		// (width!=height) && (width > height)
//
////		new JoglApplication(new URacer(), "uRacer: The King Of The Drift", 480, 320, true);
////		new JoglApplication(new URacer(), "uRacer: The King Of The Drift", 720, 480, true);
////		new JoglApplication(new URacer(), "uRacer: The King Of The Drift", 800, 480, true);
////		new JoglApplication(new URacer(), "uRacer: The King Of The Drift", 800, 800, true);
//		new JoglApplication(new URacer(), "uRacer: The King Of The Drift", 1280, 800, true);	// target
////		new JoglApplication(new URacer(), "uRacer: The King Of The Drift", 1280, 1024, true);
//
//		// higher resolutions than the target can't be supported without Track artifacts of
//		// some sort cropping out
////		new JoglApplication(new URacer(), "uRacer: The King Of The Drift", 1920, 1050, true);
//
//	}
//
//}

public class URacerDesktop
{
	public static void main( String[] argv )
	{
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "uRacer: The King Of The Drift";

		config.width = 1280; config.height = 752;
//		config.width = 1280; config.height = 720;
//		config.width = 800; config.height = 480;

		config.samples = 0;
		config.depth = 0;
		config.vSyncEnabled = true;
		config.useCPUSynch = false;
		config.useGL20 = true;
		config.fullscreen = false;

		URacer uracer = new URacer();
		LwjglApplication app = new LwjglApplication(uracer, config);

		URacerDesktopFinalizer finalizr = new URacerDesktopFinalizer( (OpenALAudio)app.getAudio() );
		uracer.setFinalizer( finalizr );

		int screenW = 1680; int screenH = 1050;
		Display.setLocation( 1920 + (screenW-config.width)/2, (screenH-config.height)/2 );
	}


}