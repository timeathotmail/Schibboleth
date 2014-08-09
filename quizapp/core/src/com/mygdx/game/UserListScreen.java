/**
 * 
 */
package com.mygdx.game;

import java.util.Collection;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.net.Client;
import common.entities.User;

/**
 * Implements a Screen that shows a user online list.
 * You can choose user by tipping him in the list 
 * @author halfelv
 *
 */
public class UserListScreen extends SuperScreen implements Screen {
	private Stage stage;
	private Skin skin;
	
	private SpriteBatch batch;
	private Texture logo;
	private TextureRegion logoRegion;
	private OrthographicCamera camera;
		
	private Table table;
	private Label lblUsers, lblError;
	private TextButton btnBack;

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#render(float)
	 */
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0.459f, 0.702f, 0.459f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.draw();
		
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		batch.draw(logoRegion, 0, 320, 463/(163.0f/290.0f), 163);
		batch.end();

	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#resize(int, int)
	 */
	@Override
	public void resize(int width, int height) {
		

	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#show()
	 */
	@Override
	public void show() {
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);
		skin = new Skin(Gdx.files.internal("uiskin.json"));
		batch = new SpriteBatch();
		
		//set and show logo
		logo = new Texture(Gdx.files.internal("logo.png"));
		logoRegion = new TextureRegion(logo, 0, 0, 463, 163);
		
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);
		
		lblUsers = new Label("Users online", skin);
		lblError = new Label("", skin);
		
		btnBack = new TextButton("Cancel", skin);
		
		btnBack.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				ScreenManager.getInstance().show(ScreenSelector.MAIN_MENU);
				}
		});
		
		//TODO add dialog
		
		table = new Table();
		setTable();
		
		table.setPosition(Gdx.graphics.getWidth() / 2,
				Gdx.graphics.getHeight() / 2);

		stage.addActor(table);
		//TODO add scroll pane
		//TODO add users
		//TODO dialog mit accept/deny challenge. if accept setScreen(Game)
		//TODO GameScreen

	}
	
	private void setTable(){
		table.add(lblUsers).pad(10);
		table.row();
		table.add(btnBack).width(150).height(30).align(Align.right).padBottom(5);
		table.row();
		
		//TODO add and display users
		//TODO scroll pane , accept deny challenge
	}

	/**
	 * Displays an error message
	 * @param msg
	 */
	public void showErrorMsg(String msg) {
		lblError.setText(msg);
	}
	
	private Collection<User> dispayUsers(){
		
		//return Client.getInstance().getUsersOnline();
		return null;
	}
	/**
	 * Display the user list.
	 */
	private void displayUserList(){
		//Client.getInstance().
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
		stage.dispose();
		skin.dispose();
		logo.dispose();
		batch.dispose();
	}

}
