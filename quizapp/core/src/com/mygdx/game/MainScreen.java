package com.mygdx.game;

import java.util.Collection;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import common.entities.User;

public class MainScreen implements Screen {

	private QuizGame game;
	private Stage stage;
	private Skin skin;
	private SpriteBatch batch;
	private Texture logo;
	private TextureRegion logoRegion;
	private OrthographicCamera camera;
	
	private final Collection<User> users;

	public MainScreen(QuizGame game, Collection<User> users) {
		this.game = game;
		this.users = users;
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0.459f, 0.702f, 0.459f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		if(stage != null) {
			stage.draw();
		}
		//display logo 
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		batch.draw(logoRegion, 0, 300, 463/(163.0f/280.0f), 163);
		batch.end();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
	}

	@Override
	public void show() {
		batch = new SpriteBatch();
		Gdx.app.postRunnable(new Runnable() {
	         @Override
	         public void run() {
	     		stage = new Stage();
	    		Gdx.input.setInputProcessor(stage);
	    		skin = new Skin(Gdx.files.internal("uiskin.json"));
	    		
	    		logo = new Texture(Gdx.files.internal("logo.png"));
	    		logoRegion = new TextureRegion(logo, 0, 637, 463, 163);
	    		camera = new OrthographicCamera();
	    		camera.setToOrtho(false, 800, 480);
	         }
	      });
		
		// TODO
	}

	@Override
	public void hide() {
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
		logo.dispose();
		batch.dispose();
	}

}