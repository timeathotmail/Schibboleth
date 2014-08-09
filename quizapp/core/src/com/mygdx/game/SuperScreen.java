/**
 * 
 */
package com.mygdx.game;

import java.awt.Dialog;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
/**
 * Implements a Dialog method for all Screens
 * @author halfelv
 *
 */

public class SuperScreen implements Screen {
	
	/*an error button */
	private TextButton btnError;
	/*an error label */
	private Label lblError;
	/*saves a dialog*/
	private com.badlogic.gdx.scenes.scene2d.ui.Dialog dialog;
	
	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#render(float)
	 */
	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#resize(int, int)
	 */
	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}
	
	/**
	 * shows a dialog box with a error message
	 * 
	 */
	protected void errorDialog(String msg, Stage stage, Skin skin){
		
		lblError = new Label(msg, skin);
		lblError.setWrap(true);
		lblError.setFontScale(.8f);
		lblError.setAlignment(Align.center);
		
		btnError = new TextButton("Ok", skin);
		
		dialog = new com.badlogic.gdx.scenes.scene2d.ui.Dialog("Connection error", skin){
				    protected void result (Object object) {
				            ScreenManager.getInstance().show(ScreenSelector.LOGIN);
				    }
				};
		
				/*setting up a dialog window*/
		dialog.padTop(50).padBottom(50);
		dialog.getContentTable().add(lblError).width(850).row();
		dialog.getButtonTable().padTop(50);
		dialog.button(btnError);
		dialog.key(Keys.ENTER, true).key(Keys.ESCAPE, true);
		
		/*whatever */
		dialog.invalidateHierarchy();
		dialog.invalidate();
		dialog.layout();
		dialog.show(stage);
	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#show()
	 */
	@Override
	public void show() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#hide()
	 */
	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#pause()
	 */
	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#resume()
	 */
	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#dispose()
	 */
	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

}
