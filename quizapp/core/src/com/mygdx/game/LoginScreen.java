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

public class LoginScreen implements Screen {

	private final QuizGame game;
	private Stage stage;
	private Skin skin;
	private SpriteBatch batch;
	private Texture logo;
	private TextureRegion logoRegion;
	private OrthographicCamera camera;

	private Table table;
	private TextButton btnToggleLogin, btnToggleRegister, btnPlayOffline;
	private ButtonGroup btnGroup;
	private Label lblUsername, lblPassword, lblConfirmation, lblError;
	private TextField txtUsername, txtPassword, txtConfirmation;

	public LoginScreen(QuizGame game) {
		this.game = game;
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0.459f, 0.702f, 0.459f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.draw();
		
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		batch.draw(logoRegion, 0, 637, 463/(163.0f/480.0f),	480);//or 800?
		batch.end();
	}

	@Override
	public void show() {
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);
		skin = new Skin(Gdx.files.internal("uiskin.json"));
		
		logo = new Texture(Gdx.files.internal("logo.png"));
		logoRegion = new TextureRegion(logo, 0, 637, 463, 163);
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
						game.login(txtUsername.getText(), txtPassword.getText());
					} else {
						game.register(txtUsername.getText(),
								txtPassword.getText(),
								txtConfirmation.getText());
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
		btnPlayOffline = new TextButton("Play offline", skin);

		btnToggleLogin.addListener(new EventListener() {
			@Override
			public boolean handle(Event event) {
				if (event instanceof ChangeEvent) {
					switchView(btnToggleLogin.isChecked());
				}

				return true;
			}
		});

		btnPlayOffline.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				game.playOffline();
			}
		});

		switchView(true);
	}

	private void switchView(boolean login) {
		table.clear();

		if (!login) {
			table.add(btnToggleLogin).align(Align.left);
			table.row();
		} else {
			table.add(btnToggleRegister).align(Align.left);
			table.row();
		}
		table.add(btnPlayOffline).align(Align.left);
		table.row();
		table.add(lblUsername).pad(2);
		table.row();
		table.add(txtUsername).pad(2);
		table.row().pad(10);
		table.add(lblPassword).pad(2);
		table.row();
		table.add(txtPassword).pad(2);

		if (!login) {
			table.row();
			table.add(lblConfirmation).pad(2);
			table.row();
			table.add(txtConfirmation).pad(2);
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

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
		// TODO Auto-generated method stub
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
		batch.dispose();
		logo.dispose();
		stage.dispose();
		skin.dispose();
	}
}