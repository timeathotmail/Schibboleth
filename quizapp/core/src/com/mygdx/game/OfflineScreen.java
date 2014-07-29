package com.mygdx.game;



import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
/**
 * Beschreibt Prototyp Screen LoginSuccesfullyOffline
 * Used http://obviam.net/index.php/getting-started-in-android-game-
 * development-with-libgdx-create-a-working-prototype-in-a-day-tutorial-part-1/
 * 
 * @author halfelv
 *
 */
public class OfflineScreen implements Screen {

	private QuizGame game;
	private Stage stage;
	private Skin skin;

	public OfflineScreen(QuizGame game) {
		this.game = game;
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0.016f, 0.423f, 0.745f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		if(stage != null) {
			stage.draw();
		}
	}

	@Override
	public void resize(int width, int height) {
		game.resize(width, height);
		// TODO Auto-generated method stub
	}

	@Override
	public void show() {
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);
		skin = new Skin(Gdx.files.internal("uiskin.json"));
		
		// TODO
	}

	@Override
	public void hide() {
		Gdx.input.setInputProcessor(null);
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		stage.dispose();
		skin.dispose();
	}

}