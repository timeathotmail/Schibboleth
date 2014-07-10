package com.mygdx.game;

import java.util.Collection;
import java.util.List;

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
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Scaling;

import common.entities.*;

import com.mygdx.net.Client;

/**
 * Game class.
 * 
 * @author Tim Wiechers
 */
public class QuizGame extends ApplicationAdapter implements Game {
	/**
	 * Client used for server communication.
	 */
	private final Client client = new Client(this);
	/**
	 * 
	 */
	private SpriteBatch batch;
	/**
	 * 
	 */
    private Skin skin;
    /**
     * 
     */
    private Stage stage;

    /* (non-Javadoc)
     * @see com.badlogic.gdx.ApplicationAdapter#create()
     */
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
        
        client.login("username", "password", 0);
    }

    /* (non-Javadoc)
     * @see com.badlogic.gdx.ApplicationAdapter#dispose()
     */
    @Override
    public void dispose() {
        batch.dispose();
    }

    /* (non-Javadoc)
     * @see com.badlogic.gdx.ApplicationAdapter#render()
     */
    @Override
    public void render() {        
    	Gdx.gl.glClearColor(0.75f, 0.75f, 0.75f, 1);
    	Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    	batch.begin();
    	stage.draw();
    	batch.end();
    }

    /* (non-Javadoc)
     * @see com.badlogic.gdx.ApplicationAdapter#resize(int, int)
     */
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

    /* (non-Javadoc)
     * @see com.badlogic.gdx.ApplicationAdapter#pause()
     */
    @Override
    public void pause() {
    }

    /* (non-Javadoc)
     * @see com.badlogic.gdx.ApplicationAdapter#resume()
     */
    @Override
    public void resume() {
    }
    

    // =====================================================================
	// Game implementation
    // =====================================================================

	/* (non-Javadoc)
	 * @see com.mygdx.game.IGame#autoLogin()
	 */
	@Override
	public void autoLogin() {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.mygdx.game.IGame#login(java.lang.String, java.lang.String)
	 */
	@Override
	public void login(String username, String password) {
		// TODO Auto-generated method stub	
	}

	/* (non-Javadoc)
	 * @see com.mygdx.game.IGame#logout()
	 */
	@Override
	public void logout() {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.mygdx.game.IGame#changeUserData(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void changeUserData(String username, String pw1, String pw2) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.mygdx.game.IGame#playOffline()
	 */
	@Override
	public void playOffline() {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.mygdx.game.IGame#searchMatch()
	 */
	@Override
	public void searchMatch() {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.mygdx.game.IGame#cancelSearch()
	 */
	@Override
	public void cancelSearch() {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.mygdx.game.IGame#displayUserList()
	 */
	@Override
	public void displayUserList() {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.mygdx.game.IGame#sendChallenge(common.entities.User)
	 */
	@Override
	public void sendChallenge(User user) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.mygdx.game.IGame#displayChallenges()
	 */
	@Override
	public void displayChallenges() {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.mygdx.game.IGame#acceptChallenge(common.entities.User)
	 */
	@Override
	public void acceptChallenge(User user) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.mygdx.game.IGame#denyChallenge(common.entities.User)
	 */
	@Override
	public void denyChallenge(User user) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.mygdx.game.IGame#displayRankings(int, int)
	 */
	@Override
	public void displayRankings(int offset, int length) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.mygdx.game.IGame#displaySettings()
	 */
	@Override
	public void displaySettings() {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.mygdx.game.IGame#displayStatistics()
	 */
	@Override
	public void displayStatistics() {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.mygdx.game.IGame#displayQuestion()
	 */
	@Override
	public void displayQuestion() {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.mygdx.game.IGame#submitAnswer(int, boolean)
	 */
	@Override
	public void submitAnswer(int answer, boolean correct) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.mygdx.game.IGame#leaveMatch()
	 */
	@Override
	public void leaveMatch() {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.mygdx.game.IGame#endMatch()
	 */
	@Override
	public void endMatch() {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.mygdx.game.IGame#onNoConnection()
	 */
	@Override
	public void onNoConnection() {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.mygdx.game.IGame#onLogin(boolean, java.util.Collection)
	 */
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

	/* (non-Javadoc)
	 * @see com.mygdx.game.IGame#onConnectionLost()
	 */
	@Override
	public void onConnectionLost() {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.mygdx.game.IGame#onUserListChanged(boolean, common.entities.User)
	 */
	@Override
	public void onUserListChanged(boolean connected, User user) {
		if(connected) {
			// TODO add user to list
		} else {
			// TODO remove user from list
		}
	}

	/* (non-Javadoc)
	 * @see com.mygdx.game.IGame#onMatchStarted(common.entities.User, java.util.List)
	 */
	@Override
	public void onMatchStarted(User user, List<Integer> questionIds) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.mygdx.game.IGame#onChallengeDenied(common.entities.User)
	 */
	@Override
	public void onChallengeDenied(User user) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.mygdx.game.IGame#onChallengeReceived(common.entities.User)
	 */
	@Override
	public void onChallengeReceived(User user) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.mygdx.game.IGame#onOpponentLeft()
	 */
	@Override
	public void onOpponentLeft() {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.mygdx.game.IGame#onOpponentAnswered(int)
	 */
	@Override
	public void onOpponentAnswered(int opponentAnswer) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.mygdx.game.IGame#onError(java.lang.String)
	 */
	@Override
	public void onError(String message) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.mygdx.game.IGame#onSearchCancelled()
	 */
	@Override
	public void onSearchCancelled() {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.mygdx.game.IGame#onRankingsReceived(java.util.List)
	 */
	@Override
	public void onRankingsReceived(List<User> users) {
		// TODO Auto-generated method stub
		
	}
}
