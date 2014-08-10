/**
 * 
 */
package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.mygdx.net.Client;

/**
 * shows active match
 * @author halfelv
 *
 */
public class GameScreen extends SuperScreen implements Screen {
	
	private Stage stage;
	private Skin skin;
	
	private SpriteBatch batch;
	private OrthographicCamera camera;
	
	private Table table;
	private Label lblQuestion, lblAnswer1, lblAnswer2, lblAnswer3, lblAnswer4, lblError, lblTimer, lblStatistics;
	private TextButton btnBack, btnAnswer1, btnAnswer2, btnAnswer3, btnAnswer4;
	private ButtonGroup btnGroup;
	
	/*timer*/
	private boolean timerIsOn = false;

	GameScreen()
	{
		
	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#render(float)
	 */
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0.459f, 0.702f, 0.459f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.draw();

		if(!timerIsOn){
			timerIsOn = true;
			Timer.schedule(new Task(){
				@Override
				public void run(){
					//TODO show new question, mark old question as failed
					ScreenManager.getInstance().show(ScreenSelector.GAME);
				}
			}, 60); //60 s to answer a question
		}else if(Gdx.input.isTouched()){
			Timer.instance().clear();
			//TODO show new question, mark old question as passed or failed
		}
		
		batch.begin();
		
		//TODO draw game question field and buttons, also time and statistics
		batch.end();


	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#resize(int, int)
	 */
	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

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
		
		//TODO set dialog with challenge
		
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);
		batch.setProjectionMatrix(camera.combined);
		
		lblError = new Label("", skin);
		lblQuestion = new Label("" , skin); //TODO Frage holen
		
		btnAnswer1 = new TextButton( "", skin);
		btnAnswer2 = new TextButton( "", skin);
		btnAnswer3 = new TextButton( "", skin);
		btnAnswer4 = new TextButton( "", skin);
		
		btnAnswer1.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				//TODO Antwort holen von der fragen von dem aktuellen Match
				}
		});
		btnAnswer2.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				
				}
		});
		btnAnswer3.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				
				}
		});
		btnAnswer4.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				
				
				}
		});
		
		btnBack = new TextButton("Cancel", skin);
		
		btnBack.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				//cancelSearch();
				ScreenManager.getInstance().show(ScreenSelector.CHALLENGES);
				}
		});
		
		table = new Table();
		setTable(true);
		
		table.setPosition(Gdx.graphics.getWidth() / 2,
				Gdx.graphics.getHeight() / 2);

		stage.addActor(table);



	}

	
	private void setTable(boolean timeout){
		if(timeout){
			table.clear();
			table.add();
			
			table.add();
		}
	}
	
	/**
	 * Display the active user's stats.
	 */
	private void displayStatistics(){
		
	}
	
	/**
	 * Display question and answers.
	 */
	private void displayQuestion(){
		
	}
	
	/**
	 * Choosing an answer for a question in-game. 
	 * Send the choice to the server.
	 * Highlight the chosen answer.
	 * @param answer index of the answer
	 */
	private void submitAnswer(int answer, boolean correct){
		
	}
	
	/**
	 * Leave the current match.
	 * Inform the server.
	 * Enter the main menu.
	 */
	private void leaveMatch(){
		
	}
	
	/**
	 * Confirm the result and enter the main menu.
	 */
	private void endMatch(){
		
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
		batch.dispose();
	

	}

}
