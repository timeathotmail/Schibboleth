/**
 * 
 */
package com.mygdx.game;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.net.Client;

import common.entities.Match;

/**
 * Displays actual matches
 * @author halfelv
 *
 */
public class SelectMatchScreen extends SuperScreen implements Screen {
	private Stage stage;
	private Skin skin;
	
	private SpriteBatch batch;
	private Texture logo;
	private TextureRegion logoRegion;
	private OrthographicCamera camera;
		
	private Table table;
	private Label lblMatches, lblError;
	private TextButton btnBack;
	private List list;
	private ScrollPane scroll;
	
	SelectMatchScreen()
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
		
		//set and show logo
		logo = new Texture(Gdx.files.internal("logo.png"));
		logoRegion = new TextureRegion(logo, 0, 0, 463, 163);
		
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);
		
		//adding a list and a scroll pane
		list = new List(skin);
		scroll = new ScrollPane(list, skin);
		list.setItems(castMatches(QuizGame.getInstance().displayMatches()));
		
		list.addListener(new ClickListener(){
			public void clicked(InputEvent e, float x, float y){
				QuizGame.getInstance().onMatchStarted((Match) list.getSelected());
			}
		});
		
		
		lblMatches = new Label("Active challenges", skin);
		lblError = new Label("", skin);
		
		btnBack = new TextButton("Cancel", skin);
		
		btnBack.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				ScreenManager.getInstance().show(ScreenSelector.MAIN_MENU);
				}
		});
		
		table = new Table();
		setTable();
		
		table.setPosition(Gdx.graphics.getWidth() / 2,
				Gdx.graphics.getHeight() / 2);

		stage.addActor(table);

	}
	
	private void setTable(){
		table.add(lblMatches).pad(10);
		table.row();
		table.add(btnBack).width(150).height(30).align(Align.right).padBottom(5);
		table.row();
		table.add(list);
		
		
		//TODO add and display challenges
	}
	
	private Match[] castMatches(java.util.List<Match> list2){
		int n = list2.size();
		Match[] tmpMatches = new Match[n];
		int i = 0;
		for(Match match: list2){
			/*name of challenge wish I to have (Yoda) */
			tmpMatches[i] = list2.get(i);			
			i++;
		}
		return tmpMatches;
	}

	/**
	 * Displays an error message
	 * @param msg
	 */
	public void showErrorMsg(String msg) {
		lblError.setText(msg);
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
