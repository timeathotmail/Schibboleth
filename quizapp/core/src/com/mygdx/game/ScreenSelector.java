/**
 * 
 */
package com.mygdx.game;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

/**
 * Switches the screens in game
 * http://bioboblog.blogspot.de/2012/08/libgdx-screen-management.html
 * 
 * @author halfelv
 *
 */
public enum ScreenSelector {

	MAIN_MENU {
        @Override
        protected com.badlogic.gdx.Screen getScreenInstance() {
            return new MainScreen();
        }
    },
 
    LOGIN {
        @Override
        protected com.badlogic.gdx.Screen getScreenInstance() {
             return new LoginScreen();
        }
    }, 
    
    GAME {
        @Override
        protected com.badlogic.gdx.Screen getScreenInstance() {
             return new GameScreen();
        }
    },
    
    USERLIST {
        @Override
        protected com.badlogic.gdx.Screen getScreenInstance() {
             return new UserListScreen();
        }
    },
 
    OPTIONS {
        @Override
        protected com.badlogic.gdx.Screen getScreenInstance() {
             return new OptionsScreen();
        }
    };  
 
    protected abstract Screen getScreenInstance();
}
