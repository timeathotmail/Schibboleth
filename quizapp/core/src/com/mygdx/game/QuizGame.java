package com.mygdx.game;

import java.util.Collection;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.Viewport;

import common.entities.*;

import com.mygdx.net.Client;
import com.sun.prism.image.ViewPort;

/**
 * Game class.
 * 
 * @author Tim Wiechers
 */
public class QuizGame extends ApplicationAdapter implements IGame {
	/**
	 * Client used for server communication.
	 */
	private final Client client = new Client(this);
	private SpriteBatch batch;
    private Skin skin;
    private Stage stage;

    @Override
    public void create() {        
        batch = new SpriteBatch();
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        stage = new Stage();

        Window window = new Window("Dialogtitel", skin);
        Label label = new Label("Etwas Text hier um dem User zu sagen, was Sache ist", skin);
        final TextButton button = new TextButton("Drück mich", skin, "default");
        
        button.addListener(new ClickListener(){
            @Override 
            public void clicked(InputEvent event, float x, float y){
                button.setText("Du hast den Button gedrückt");
            }
        });
        
    	window.getButtonTable().add(new TextButton("X", skin)).height(window.getPadTop());
    	window.setPosition(0, 0);
    	window.setCenterPosition(Gdx.graphics.getWidth() /2, Gdx.graphics.getHeight() /2);
    	window.row();
    	window.add(label);
    	window.row();
    	window.add(button);
		window.pack();
        
		stage.addActor(window);
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void dispose() {
        batch.dispose();
    }

    @Override
    public void render() {        
    	Gdx.gl.glClearColor(0.75f, 0.75f, 0.75f, 1);
    	Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    	batch.begin();
    	stage.draw();
    	batch.end();
    }

    @Override
    public void resize(int width, int height) {
        Vector2 size = Scaling.fit.apply(480, 800, width, height);
        int viewportX = (int)(width - size.x) / 2;
        int viewportY = (int)(height - size.y) / 2;
        int viewportWidth = (int)size.x;
        int viewportHeight = (int)size.y;
        Gdx.gl.glViewport(viewportX, viewportY, viewportWidth, viewportHeight);
        stage.getViewport().update(480, 800);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

	
	// === IGame implementation ===

	@Override
	public void onNoConnection() {
		Gdx.app.log("Game", "no connection");
		// TODO display error
	}

	@Override
	public void onLogin(boolean success, Collection<User> users) {
		Gdx.app.log("Game", "login = " + success);
		if (success) {
			// TODO add users to list
			// TODO enter menu
		} else {
			// TODO display error
		}
	}

	@Override
	public void onConnectionLost() {
		Gdx.app.log("Game", "connection lost");
		// TODO display error
	}

	@Override
	public void onUserListChanged(boolean add, User user) {
		if(add) {
			// TODO add user to list
		} else {
			// TODO remove user from list
		}
	}

	@Override
	public void autoLogin() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void login(String username, String password) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void changeUserData(String username, String pw1, String pw2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void playOffline() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void searchMatch() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void cancelSearch() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void displayUserList() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendChallenge(User user) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void displayChallenges() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void acceptChallenge(User user) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void denyChallenge(User user) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void displayRankings(int offset, int length) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void displaySettings() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void displayStatistics() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void displayQuestion() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void submitAnswer(int answer, boolean correct) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void leaveMatch() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void endMatch() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMatchFound(User user) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onChallengeAccepted(User user) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onChallengeDenied(User user) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onChallengeReceived(User user) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onOpponentLeft() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onOpponentAnswered(int opponentAnswer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void logout() {
		// TODO Auto-generated method stub
		
	}
}
