package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
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

public class LoginScreen implements Screen {

	QuizGame game;

	public LoginScreen(QuizGame game) {
		this.game = game;
	}

	Table table;
	Stage stage;
	TextButton toggleLogin, toggleRegister;
	ButtonGroup bgroup;

	Skin skin;

	Label lblUsername, lblPassword, lblConfirmation, lblError;
	TextField txtUsername, txtPassword, txtConfirmation;

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0.016f, 0.423f, 0.745f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
	}

	@Override
	public void show() {

		stage = new Stage();
		Gdx.input.setInputProcessor(stage);

		table = new Table();

		skin = new Skin(Gdx.files.internal("uiskin.json"));

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
					if (toggleLogin.isChecked()) {
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

		toggleLogin = new TextButton("Login", skin);
		toggleRegister = new TextButton("Register", skin);
		bgroup = new ButtonGroup(toggleLogin, toggleRegister);
		bgroup.setMaxCheckCount(1);
		bgroup.setMinCheckCount(1);
		bgroup.setUncheckLast(true);
		toggleLogin.setChecked(true);

		toggleLogin.addListener(new EventListener() {
			@Override
			public boolean handle(Event event) {
				if (event instanceof ChangeEvent) {
					switchView(toggleLogin.isChecked());
				}

				return true;
			}
		});

		switchView(true);
	}

	private void switchView(boolean login) {
		table.clear();
		table.add(toggleLogin).align(Align.left);
		table.row();
		table.add(toggleRegister).align(Align.left);
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

		table.setCenterPosition(Gdx.graphics.getWidth() / 2,
				Gdx.graphics.getHeight() / 2);

		stage.addActor(table);
	}

	public void showErrorMsg(String msg) {
		lblError.setText(msg);
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
	}

}