package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldListener;

public class LoginScreen implements Screen {

	QuizGame game; 
	
	public LoginScreen(QuizGame game) {
		this.game = game;
	}

	Table table;
	Stage stage;

	Skin menuSkin;

	Label lblUsername, lblPassword;
	TextField txtUsername, txtPassword;

	@Override
	public void render(float delta) {
		stage.act(Gdx.graphics.getDeltaTime());
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

		menuSkin = new Skin(Gdx.files.internal("uiskin.json"));

		lblUsername = new Label("Username:", menuSkin);
		lblPassword = new Label("Password:", menuSkin);

		txtUsername = new TextField("", menuSkin);
		txtPassword = new TextField("", menuSkin);
		txtPassword.setPasswordMode(true);
		txtPassword.setPasswordCharacter('*');
		txtUsername.setMessageText("test");

		txtUsername.setTextFieldListener(new TextFieldListener() {
			@Override
			public void keyTyped(TextField textField, char key) {
				// TODO
			}
		});

		txtPassword.setTextFieldListener(new TextFieldListener() {

			@Override
			public void keyTyped(TextField textField, char key) {

				// TODO
			}
		});

		table.add(lblUsername).pad(2);
		table.row();
		table.add(txtUsername).pad(2);
		table.row().pad(10);
		table.add(lblPassword).pad(2);
		table.row();
		table.add(txtPassword).pad(2);
		table.setCenterPosition(Gdx.graphics.getWidth() / 2,
				Gdx.graphics.getHeight() / 2);
		stage.addActor(table);
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
		menuSkin.dispose();
	}

}