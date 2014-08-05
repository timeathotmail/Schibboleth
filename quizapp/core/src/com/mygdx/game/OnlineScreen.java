package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

/**Beschreibt Prototyp Screen LoginSuccessfullOnline
 * 
 * @author halfelv
 *
 */
public class OnlineScreen implements Screen {

	private QuizGame game;
	private Stage stage;
	private Skin skin;

	//was macht table?
	private Table table;

	private TextButton btnTogglePlay, btnToggleOptions, btnToggleSignOut;
	private ButtonGroup btnGroup;

	public OnlineScreen(QuizGame pGame)
	{
		if(pGame == null){
			throw new IllegalArgumentException("Parameter is null");
		}
		this.game = pGame;
	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#render(float)
	 */
	//wozu hier parameter wenn nie benutzt?
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0.459f, 0.702f, 0.459f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		if(stage != null) {
			stage.draw();
		}

	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#resize(int, int)
	 */
	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);

	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#show()
	 */
	@Override
	public void show() {
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);
		skin = new Skin(Gdx.files.internal("uiskin.json"));
		table = new Table();

		btnTogglePlay = new TextButton("Play", skin);
		btnToggleOptions = new TextButton("Options", skin);
		btnToggleSignOut = new TextButton("Sign out", skin);

		//brauche ich ButtonGroup ueberhaupt?
		btnGroup = new ButtonGroup(btnTogglePlay, btnToggleOptions, btnToggleSignOut);
		btnGroup.setMaxCheckCount(1);
		btnGroup.setMinCheckCount(1);
		btnGroup.setUncheckLast(true);

		btnToggleSignOut.setChecked(true);

		//KA ob es jetzt hier richtig ist
		btnTogglePlay.addListener(new EventListener() {
			@Override
			public boolean handle(Event event) {
				if (event instanceof ChangeEvent) {
					switchView(btnTogglePlay.isChecked());
				}

				return true;
			}
		});

		btnToggleOptions.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				game.displaySettings();
			}
		});

		btnToggleSignOut.addListener(new EventListener() {

			@Override
			public boolean handle(Event event) {
				if(event instanceof ChangeEvent){
					switchView(btnToggleSignOut.isChecked());
				}
				return true;
			}
		});

		switchView(true);

	}

	protected void switchView(boolean checked) {
		//TODO

	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#hide()
	 */
	@Override
	public void hide() {
		Gdx.input.setInputProcessor(null);
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
