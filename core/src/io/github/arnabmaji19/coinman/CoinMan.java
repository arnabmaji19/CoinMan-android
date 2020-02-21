package io.github.arnabmaji19.coinman;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class CoinMan extends ApplicationAdapter {

	private static final int MAX_MAN_STATE_CHANGE_DELAY = 8;

	private SpriteBatch batch;
	private Texture backgroundTexture;
	private Texture[] man;

	private int screenHeight;
	private int screenWidth;
	private int manHeight;
	private int manWidth;

	private int manState = 0;
	private int manStateChangeDelay = 0;
	private float velocity = 0f;
	private float gravity = 0.2f;
	private float manYPosition;

	private CoinMaker coinMaker;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
        // get screen screenHeight and screenWidth
        screenHeight = Gdx.graphics.getHeight();
        screenWidth = Gdx.graphics.getWidth();
		backgroundTexture = new Texture("bg.png"); // create background image to display on screen
        man = new Texture[4]; // create all graphics assets for the man
        // set all types of man textures
        man[0] = new Texture("frame-1.png");
        man[1] = new Texture("frame-2.png");
        man[2] = new Texture("frame-3.png");
        man[3] = new Texture("frame-4.png");


        // get screenHeight and screenWidth of man texture
        manHeight = man[0].getHeight();
        manWidth = man[0].getWidth();

        manYPosition = screenHeight / 2; // initial position for man in air
		coinMaker = new CoinMaker(screenHeight, screenWidth);
	}

	@Override
	public void render () {
		batch.begin();

		batch.draw(backgroundTexture, 0, 0, screenWidth, screenHeight); // draw background image to the screen

		// create coin in a delay
		if (coinMaker.getCount() < coinMaker.getCREATION_DELAY()) coinMaker.increaseCount();
		else {
			coinMaker.createObject(); // create new coin in each interval
			coinMaker.resetCount(); // reset interval count
		}

		// move all existing coins along negative x axis
		for (MovingObject coin : coinMaker.getMovingObjects()){
			batch.draw(Coin.getCoinTexture(), coin.getXPosition(), coin.getYPosition()); //display coin texture
			coin.moveAlongXAxis(); // move coin position for next iteration
			coinMaker.removeLaterIfNecessary(coin); // mark coin to be removed later
		}
		coinMaker.removeUnnecessaryObjects(); // remove all unnecessary coins

		// set frequency for changing man state
		if (manStateChangeDelay < MAX_MAN_STATE_CHANGE_DELAY){
			manStateChangeDelay++;
		} else {
			manStateChangeDelay = 0;
			// toggle between various man states
			if (manState < man.length-1){
				manState++;
			} else {
				manState = 0;
			}
		}

		if (Gdx.input.justTouched()) velocity -= 10; // move man upwards upon touch

		// define velocity for falling down
		velocity += gravity;
		manYPosition -= velocity;

		if (manYPosition <= 0) {
			manYPosition = 0; // prevent man from going out of the screen
		}
		//draw man in the middle of the screen
		batch.draw(man[manState], (screenWidth - manWidth)/2, manYPosition);

		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}

}
