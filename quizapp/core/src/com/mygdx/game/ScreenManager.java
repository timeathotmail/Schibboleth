/**
 * 
 */
package com.mygdx.game;

import javax.management.RuntimeErrorException;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.IntMap;

/**
 * Die Klasse dient zur Verwaltung von Screens
 * http://bioboblog.blogspot.de/2012/08/libgdx-screen-management.html
 * 
 * @author halfelv
 *
 */
public final class ScreenManager{

	/*a singletone*/
	private static ScreenManager INSTANCE;
	
	/*a game*/
	private QuizGame game;
	/*screens*/
	private IntMap<Screen> screens;
	
	/*constructor initializes the screens collection*/
	private ScreenManager()
	{
		screens = new IntMap<Screen>();
	}
	
	/* sets an Instance and returns it */
	public static ScreenManager getInstance(){
		if(INSTANCE == null){
			INSTANCE = new ScreenManager(); 
		}
		return INSTANCE;
	}
	
	/**
	 * Zeigt gewaehlten Screen aus dem Enum  mit Screens. Enum wird als Parameter erwartet.
	 * Gewaehlter Screen wird in Game Klasse als aktiv gesetzt. 
	 * Die gewaehlte Screen werden in dem IntMap fuer weitere Aufrufe gespeichert.
	 * Falls statt Enum {@code null} uebergeben wird, wird eine {@link IllegalArgumentException} ausgeloest.
	 * Falls Game Klasse noch nicht inizialisiert ist, wird eine {@link RuntimeException} ausgeloest
	 * @param select Enum with Screens 
	 */
	public void show(ScreenSelector select){
		if(select == null){
			throw new IllegalArgumentException("ScreenSelector is null");
		}
		if(game == null){
			throw new RuntimeException("Game may not be initialized?");
		}		
		if (!screens.containsKey(select.ordinal())) {
            screens.put(select.ordinal(), select.getScreenInstance());
        }
        game.setScreen(screens.get(select.ordinal()));
	}
	
	/* initializes a game */
	public void initialize(final QuizGame pGame){
		if(pGame == null){
			throw new RuntimeException("Game may not be initialized?");
		}
		this.game = pGame;
	}
	
	/**
	 * Gibt den Gameinstanz zurueck.
	 */
	public QuizGame getGame(){
		if(this.game == null){
			throw new RuntimeException("Game may not be initialized");
		}
		return game;
	}

	/**
	 * Raeumt den eingegebenen Screen auf
	 * @param screen
	 */
	public void dispose(ScreenSelector selector) {
        if (!screens.containsKey(selector.ordinal())) return;
        screens.remove(selector.ordinal()).dispose();
    }
	
	/**
	 * Raeumt alles auf
	 */
	 public void dispose() {
	        for (Screen screen : screens.values()) {
	            screen.dispose();
	        }
	        screens.clear();
	        INSTANCE = null;
	    }
}
