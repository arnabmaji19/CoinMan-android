package io.github.arnabmaji19.coinman;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.List;

public class CoinMan extends ApplicationAdapter {

	private SpriteBatch batch;
	private Texture backgroundTexture;

	private int screenHeight;
	private int screenWidth;

	private Man man;
	private CoinMaker coinMaker;
	private BombMaker bombMaker;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
        // get screen screenHeight and screenWidth
        screenHeight = Gdx.graphics.getHeight();
        screenWidth = Gdx.graphics.getWidth();
		backgroundTexture = new Texture("bg.png"); // create background image to display on screen

		man = new Man(screenHeight, screenWidth); // create man object
		coinMaker = new CoinMaker(screenHeight, screenWidth); // create CoinMaker for creating coins
		bombMaker = new BombMaker(screenHeight, screenWidth); // create BombMake for creating bombs
	}

	@Override
	public void render () {
		batch.begin();

		batch.draw(backgroundTexture, 0, 0, screenWidth, screenHeight); // draw background image to the screen

		coinMaker.createObjectInInterval(); // create coins in a certain interval
		bombMaker.createObjectInInterval(); // create bombs in a certain interval

		drawMovingObjects(coinMaker); // draw coins on screen
		drawMovingObjects(bombMaker); // draw bombs on screen


		man.animateCharacter(); // change man character in every interval

		if (Gdx.input.justTouched()) man.jump(); // move man upwards upon touch

		man.move(); // let the man jump or fall off the gravity

		//draw man on the screen
		batch.draw(man.getTexture(), man.getXPosition(), man.getYPosition());

		// look for object collision
        checkForCollision(coinMaker); // look for collision with coin
        checkForCollision(bombMaker); // look for collision with bomb

		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}

	private void drawMovingObjects(MovingObjectMaker objectMaker){
		// move all existing objects along negative x axis
		for (MovingObject object : objectMaker.getMovingObjects()){
			batch.draw(object.getTexture(), object.getXPosition(), object.getYPosition()); //display object texture
			object.moveAlongXAxis(); // move coin position for next iteration
			objectMaker.removeLaterIfNecessary(object); // mark coin to be removed later
		}
		objectMaker.removeUnnecessaryObjects();
	}

	private void checkForCollision(MovingObjectMaker movingObjectMaker){
	    // create rectangle for man's current position
        Rectangle manRectangle = new Rectangle(
                man.getXPosition(),
                man.getYPosition(),
                man.getManWidth(),
                man.getManHeight());

        // look for collision
		List<MovingObject> movingObjectList = movingObjectMaker.getMovingObjects();
        for (MovingObject object : movingObjectList){
            // create rectangle for current object
            Rectangle movingObjectRectangle = new Rectangle(
                    object.getXPosition(),
                    object.getYPosition(),
                    object.getObjectWidth(),
                    object.getObjectWidth()
            );

            // if current object collides with man
            if (Intersector.overlaps(manRectangle, movingObjectRectangle)){
            	//TODO: determine collision type
            	movingObjectList.remove(object); // remove collided object from screen
            }
        }

    }

}
