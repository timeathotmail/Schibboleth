package com.mygdx.game;

import java.util.Collection;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.fasterxml.jackson.core.util.TextBuffer;
import com.mygdx.net.Client;

import common.entities.User;
import common.net.SocketWriteException;

public class MainScreen extends SuperScreen implements Screen {

	private Stage stage;
	private Skin skin;
	
	private SpriteBatch batch;
	private Texture logo;
	private TextureRegion logoRegion;
	private OrthographicCamera camera;
		
	private Table table;
	private TextButton btnPlay, btnOptions, btnLogout;
	private ButtonGroup btnGroup;
	private TextButton btnNewMatch, btnSelectChallenge, btnBack;
	private Label lblWelcome;
	private Label lblError;

	MainScreen() {
		// TODO Auto-generated constructor stub
	}

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
		
		
		/*Gdx.app.postRunnable(new Runnable() {
	         @Override
	         public void run() {*/
	     		
	    		
	    		/*buttons */
	    		btnPlay = new TextButton("Play", skin);
	    		btnOptions = new TextButton("Options", skin);
	    		btnLogout = new TextButton("Logout", skin);
	    		btnNewMatch = new TextButton("New Match", skin);
	    		btnSelectChallenge = new TextButton("Select challenge", skin);
	    		btnBack = new TextButton("Back", skin);
	    		
	    		/*buttongroup*/
	    		btnGroup = new ButtonGroup(btnPlay, btnBack);
	    		btnGroup.setMaxCheckCount(1);
	    		btnGroup.setMinCheckCount(1);
	    		btnGroup.setUncheckLast(true);
	    		
	    		//oder back
	    		btnBack.setChecked(true);
	    		//hier ebenso
	    		btnBack.addListener(new EventListener() {
	    			@Override
	    			public boolean handle(Event event) {
	    				if (event instanceof ChangeEvent) {
	    					switchView(btnBack.isChecked());
	    				}

	    				return true;
	    			}
	    		});
	    		
	    		
	    		
	    		/*shown text */
	    		lblWelcome = new Label("Welcome by the QuizzApp!", skin);
	    		lblError = new Label("", skin);
	    		
	    		/*button listeners */
	    		btnNewMatch.addListener(new ClickListener() {
	    			public void clicked(InputEvent event, float x, float y) {
	    				ScreenManager.getInstance().show(ScreenSelector.USERLIST);
	    				}
	    		});
	    		
	    		btnOptions.addListener(new ClickListener() {
	    			public void clicked(InputEvent event, float x, float y) {
	    				ScreenManager.getInstance().show(ScreenSelector.OPTIONS);
	    				}
	    		});
	    		
	    		btnLogout.addListener(new ClickListener() {
	    			public void clicked(InputEvent event, float x, float y) {
	    				ScreenManager.getInstance().getGame().logout();
	    				ScreenManager.getInstance().show(ScreenSelector.LOGIN);
	    				}
	    		});
	    		
	    		btnSelectChallenge.addListener(new ClickListener() {
	    			public void clicked(InputEvent event, float x, float y) {
	    				ScreenManager.getInstance().show(ScreenSelector.CHALLENGES);
	    				}
	    		});
	    		
	    		table = new Table();
	    		switchView(true);
	         }
	    //  });

	//}
	
	/**
	 * Displays an error message
	 * @param msg
	 */
	public void showErrorMsg(String msg) {
		lblError.setText(msg);
	}
	
	/**
	 * Die Methode schaltet zwischen  play-options-logout und new match-select challenge - back
	 * @param play	falls der Knopf gedrueckt, wird anderer View gezeigt 
	 */
	private void switchView(boolean play){
		table.clear();
		
		table.add(lblWelcome).pad(10);
		table.row();
		
		if(play){			
			table.add(btnPlay).width(200).height(50).align(Align.left).padBottom(5);
			table.row();
			table.add(btnOptions).width(200).height(50).align(Align.left).padBottom(5);
			table.row();
			table.add(btnLogout).width(200).height(50).align(Align.left).padBottom(5);
			
		} else{
			table.add(btnNewMatch).width(200).height(50).align(Align.left).padBottom(5);
			table.row();
			table.add(btnSelectChallenge).width(200).height(50).align(Align.left).padBottom(5);
			table.row();
			table.add(btnBack).width(200).height(50).align(Align.left).padBottom(5);
		}
		
		table.row();
			table.add(lblError).pad(2);
			
		table.setPosition(Gdx.graphics.getWidth() / 2,
				Gdx.graphics.getHeight() / 2);

		stage.addActor(table);
		
	}
	

	@Override
	public void resize(int width, int height) {
		//stage.getViewport().update(width, height, true);
		// TODO Auto-generated method stub
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