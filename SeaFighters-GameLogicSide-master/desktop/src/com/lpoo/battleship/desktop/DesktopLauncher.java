package com.lpoo.battleship.desktop;

import battle.starter.Seafighter;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		//config.width = 1024;
		//config.height = 768;
		
		config.title = "FourPlacesShip";
		new LwjglApplication(new Seafighter(), config);
	}
}
