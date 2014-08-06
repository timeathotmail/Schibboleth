package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldListener;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils.Text;

/**
 * @author halfelv
 *
 */
public class RegisterScreen implements Screen {

	private QuizGame game;
	private Stage stage;
	private Skin skin;
	
	private SpriteBatch batch;
	private OrthographicCamera camera;
	
	private Texture button;
	private TextureRegion buttonRegion;
	private Texture field;
	private TextureRegion fieldRegion;
	
	/**speichert Spiellogo */
	private Texture logo;
	private TextureRegion logoRegion;
	

	private Table table;
	private TextButton btnSubmit;
	//Unterschrifte fuer Buttons und Textfelder fuer Eingaben
	private Label lblUsername, lblPassword, lblConfirm, lblError;
	private TextField txtUsername, txtPassword, txtConfirm;

	public RegisterScreen(QuizGame pGame)
	{
		if(pGame == null){
			throw new IllegalArgumentException("Parameter is null");
		}
		this.game = pGame;
	}
	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#render(float)
	 */
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0.459f, 0.702f, 0.459f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		if(stage != null) {
			stage.draw();
		}
		
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		
		batch.end();
		

	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#resize(int, int)
	 */
	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
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
		camera = new OrthographicCamera();
		
		button = new Texture(Gdx.files.internal("button.png"));
		//buttonRegion = new TextureRegion(button, );
		field = new Texture(Gdx.files.internal("field.png"));
		
		logo = new Texture(Gdx.files.internal("logo.png"));
		logoRegion = new TextureRegion(logo, 0, 0, 463, 163);
		
		
		

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
		stage.dispose();
		skin.dispose();
		button.dispose();
		field.dispose();
	}

}
