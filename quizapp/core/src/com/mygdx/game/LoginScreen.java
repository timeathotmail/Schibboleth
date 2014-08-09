package com.mygdx.game;

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
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldListener;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.net.Client;

import common.net.SocketWriteException;

public class LoginScreen extends SuperScreen implements Screen {

	//private final QuizGame game;
	private Stage stage;
	private Skin skin;
	private SpriteBatch batch;
	private Texture logo;
	private TextureRegion logoRegion;
	private OrthographicCamera camera;

	private Table table;
	private TextButton btnToggleLogin, btnToggleRegister, btnSubmitLogin, btnSubmitRegister;
	private ButtonGroup btnGroup;
	private Label lblUsername, lblPassword, lblConfirmation, lblError;
	private TextField txtUsername, txtPassword, txtConfirmation;	
	
	/** Gruppe fuer No Connection found disclaimer*/
	private Texture disclaimer;
	private Texture btnDisclaimerTexture;
	private TextButton btnDisclaimer;

	private TextButton btnOptions;
	
	
	
	LoginScreen(QuizGame game) {
	//	this.game = game;
	}
	
	LoginScreen()
	{
		
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
		
		
		table = new Table();

		lblUsername = new Label("Username:", skin);
		lblPassword = new Label("Password:", skin);
		lblConfirmation = new Label("Confirm:", skin);
		lblError = new Label("", skin);

		txtUsername = new TextField("", skin);
		txtPassword = new TextField("", skin);
		txtPassword.setPasswordMode(true);
		txtPassword.setPasswordCharacter('*');
		txtConfirmation = new TextField("", skin);
		txtConfirmation.setPasswordMode(true);
		txtConfirmation.setPasswordCharacter('*');
		
		
		TextFieldListener enterListener = new TextFieldListener() {
			@Override
			public void keyTyped(TextField textField, char key) {
				if (key == '\r' || key == '\n') {
					if (btnToggleLogin.isChecked()) {
						login(txtUsername.getText(), txtPassword.getText());
					} else {
						register(txtUsername.getText(),
											txtPassword.getText(), txtConfirmation.getText());
						/*game.register(txtUsername.getText(),
								txtPassword.getText(),
								txtConfirmation.getText());*/
					}
				}
			}
		};

		txtUsername.setTextFieldListener(enterListener);
		txtPassword.setTextFieldListener(enterListener);
		txtConfirmation.setTextFieldListener(enterListener);

		btnToggleLogin = new TextButton("Login", skin);
		btnToggleRegister = new TextButton("Register", skin);
		btnGroup = new ButtonGroup(btnToggleLogin, btnToggleRegister);
		btnGroup.setMaxCheckCount(1);
		btnGroup.setMinCheckCount(1);
		btnGroup.setUncheckLast(true);
		btnToggleLogin.setChecked(true);
		btnSubmitLogin = new TextButton("Submit", skin);
		btnSubmitRegister = new TextButton("Submit", skin);
		
		btnOptions = new TextButton("Options", skin);

		btnToggleLogin.addListener(new EventListener() {
			@Override
			public boolean handle(Event event) {
				if (event instanceof ChangeEvent) {
					switchView(btnToggleLogin.isChecked());
				}

				return true;
			}
		});

		btnSubmitLogin.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				login(txtUsername.getText(), txtPassword.getText());
				ScreenManager.getInstance().show(ScreenSelector.MAIN_MENU);
			}
		});
		
		btnSubmitRegister.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				register(txtUsername.getText(), txtPassword.getText(), txtConfirmation.getText());
				ScreenManager.getInstance().show(ScreenSelector.MAIN_MENU);
			}
		});
		
		btnOptions.addListener(new ClickListener(){
			public void clicked(InputEvent event, float x, float y){
				ScreenManager.getInstance().show(ScreenSelector.OPTIONS);
			}
		});

		switchView(true);
	}

	private void switchView(boolean login) {
		table.clear();	
	
		table.add(lblUsername).pad(2);
		table.row();
		table.add(txtUsername).pad(2);
		table.row().pad(10);
		table.add(lblPassword).pad(2);
		table.row();
		table.add(txtPassword).pad(2);
		table.row();
		
		if(!login){
						
			table.add(lblConfirmation).pad(2);
			table.row();
			table.add(txtConfirmation).pad(2);
			table.row();
			table.add(btnSubmitRegister).width(150).height(30).align(Align.left).padBottom(5);
			table.row();			
			table.add(btnToggleLogin).width(150).height(30).align(Align.left);
		}else{
			table.add(btnSubmitLogin).width(150).height(30).align(Align.left).padBottom(5);
			table.row();
			table.add(btnToggleRegister).width(150).height(30).align(Align.left);			
		}		

		table.row();
		table.add(lblError);

		table.setPosition(Gdx.graphics.getWidth() / 2,
				Gdx.graphics.getHeight() / 2);

		stage.addActor(table);
	}	

	public void showErrorMsg(String msg) {
		lblError.setText(msg);
	}
	
	/**
	 * Login with provided username and password.
	 * Save credentials for auto-login.
	 * Enter main menu.
	 * @param username username
	 * @param password password
	 */
	private void login(String username, String password) {
		if (username.isEmpty()) {
			showErrorMsg("enter a username");			
		} else if (password.isEmpty()) {
			showErrorMsg("enter a password");			
		} else {
			try {
				Client.getInstance().login(username, password);
			} catch (SocketWriteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void register(String username, String password, String confirmation) {		
		if(!password.equals(confirmation)){
			showErrorMsg("passwords do not match");
		}
		if(checkUsername(username) && checkPassword(password)){
			try {
				Client.getInstance().register(username, password);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (SocketWriteException e) {
				e.printStackTrace();
			}
			
		}
	}
	
	
	
	/**
	 * Methode wird in register() aufgerufen. 
	 * Ueberprueft, ob Username den Einschraenkungen uebereinstimmt
	 * 
	 * @param username
	 * @return {@code true} falls die Bedienungen erfuellt sind
	 */
	private boolean checkUsername(String username){
		//is null or ""
		if(username == null || username.isEmpty()){
			showErrorMsg("Username is empty");			
			return false;
		}
		//check length, min. 6 chars
		if(username.length() < 6){
			showErrorMsg("Username is too short");
			return false;
		}
		//is illegal, f.example swearword
		/*int i = 0;
		while(i < bannedExpressions.size()){
			if(username.contains(bannedExpressions.get(i))){
				loginScreen.showErrorMsg("Username contains banned expressions");
				return false;
			}
			i++;
		}*/
		//is not a numeric or letter
		if(!username.matches("[A-Za-z_0-9]*")){
			showErrorMsg("Username contains illegal characters");
			return false;
		}
		return true;
		
	}
	
	/**
	 * Methode wird in register() aufgerufen.
	 * Ueberprueft, ob Passwort den Einschraenkungen uebereinstimmt
	 * 
	 * @param password
	 * 
	 *  @return {@code true} falls die Bedienungen erfuellt sind
	 */
	private boolean checkPassword(String password){
		// empty
		if(password.isEmpty() || password == ""){
			showErrorMsg("Password is empty");
			return false;
		}
		//contains less then 6 chars
		if(password.length() < 6){
			showErrorMsg("Passwort is too short");
			return false;
		}
		// not matches regular expression a-z A-Z _ and 0-9
		if(!password.matches("[A-Za-z0-9]*")){
			showErrorMsg("Password contains illegal characters");
			return false;
		}
		//contains less then two digits and less then one upper case or contains special chars
		if(!(password.matches(".*[a-z].*") || password.matches(".*[A-Z].*") || password.matches(".*\\d.*\\d.*"))){
			showErrorMsg("Password contains illegal characters");
			return false;
		}
		return true;
	}
	
	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
		// TODO Auto-generated method stub
	}

	/**
	 * a reason why optionsScreen cant be called?
	 */
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
		batch.dispose();
		logo.dispose();
		stage.dispose();
		skin.dispose();
	}
}