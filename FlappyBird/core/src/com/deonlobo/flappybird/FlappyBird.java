package com.deonlobo.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import org.w3c.dom.css.Rect;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class FlappyBird extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	//ShapeRenderer shapeRenderer;
    Texture gameover;
    Texture playagaintext;

    int width;
    int height;

	Texture[] birds;
	float birdY;
	float velocity = 0;
	Circle birdCircle;
	int score = 0;
	int scoringTube =0 ;
	BitmapFont font ;

	static int gameState = 0;
	float gravity = 2;

	Texture topTube;
	Texture bottomTube;
	float gap ;
	float maxTubeOffset;
	Random random;
	float tubeVelocity = 4;

	int numberOFTubes = 4;
	float tubeX[] = new float[numberOFTubes];
	float tubeOffset[] = new float[numberOFTubes];
	float distanceBetweenTubes;
	Rectangle[] topTubeRectangles;
	Rectangle[] bottomTubeRectangles;

	String[] bg;
	static Boolean notSlept = true;


	@Override
	public void create () {
		random = new Random();

		width = Gdx.graphics.getWidth();
		height =  Gdx.graphics.getHeight();

		bg = new String[6];
		bg[0]= "bg1.jpg";
		bg[1]= "bg2.png";
		bg[2]= "bg3.jpg";
		bg[3]= "bg4.jpg";
		bg[4]= "bg5.jpeg";
		bg[5]= "bg6.jpg";
		batch = new SpriteBatch();
		background = new Texture(bg[random.nextInt(6)]);
		gameover = new Texture("gameover.png");
		playagaintext = new Texture(("playagaintext.png"));

		//shapeRenderer = new ShapeRenderer();
		birdCircle = new Circle();
		font = new BitmapFont();
		font.setColor(Color.WHITE);

		birds = new Texture[2];
		birds[0] = new Texture("bird33.png");
		birds[1] = new Texture("bird44.png");

		topTube = new Texture("toptube.png");
		bottomTube = new Texture("bottomtube.png");

		gap = birds[0].getHeight()*1.5f;
		maxTubeOffset = Gdx.graphics.getHeight()/2 - gap/2 -100;

		distanceBetweenTubes = Gdx.graphics.getWidth() * 3 /2 ;
		topTubeRectangles = new Rectangle[numberOFTubes];
		bottomTubeRectangles = new Rectangle[numberOFTubes];

		startGame();

	}

	public void startGame(){

		birdY = Gdx.graphics.getHeight()/2-birds[0].getHeight()/2;

		for(int i=0;i<numberOFTubes ;i++){

			tubeOffset[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - gap - 200);

			tubeX[i] = Gdx.graphics.getWidth()/2 - topTube.getWidth()/2 + i * distanceBetweenTubes + (Gdx.graphics.getWidth());

			topTubeRectangles[i] = new Rectangle();
			bottomTubeRectangles[i] = new Rectangle();

			font.getData().setScale(6);
			font.setColor(Color.WHITE);

		}

	}

	@Override
	public void render () {

		batch.begin();
		batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		if(gameState == 1) {

			if(tubeX[scoringTube] < Gdx.graphics.getWidth()/2){

				score++;

				if(score % 10 == 0 && score > 0){

					background = new Texture(bg[random.nextInt(6)]);

					tubeVelocity += 1;

					gap = gap - 1;

				}

				if(scoringTube < numberOFTubes-1){

					scoringTube++;

				}else{
					scoringTube=0;
				}

			}

			if(Gdx.input.justTouched()){

				velocity = -20;

			}
			for(int i=0;i<numberOFTubes ;i++) {

				if(tubeX[i] < -topTube.getWidth()){

					tubeX[i] += numberOFTubes * distanceBetweenTubes;
					tubeOffset[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - gap - 200);

				}else {

					tubeX[i] -= tubeVelocity;

				}

				batch.draw(topTube, tubeX[i], Gdx.graphics.getHeight() / 2 + gap + tubeOffset[i]);
				batch.draw(bottomTube, tubeX[i], Gdx.graphics.getHeight() / 2 - gap - bottomTube.getHeight() + tubeOffset[i]);

				topTubeRectangles[i] = new Rectangle(tubeX[i], Gdx.graphics.getHeight() / 2 + gap + tubeOffset[i] ,topTube.getWidth() , topTube.getHeight());
				bottomTubeRectangles[i] = new Rectangle(tubeX[i], Gdx.graphics.getHeight() / 2 - gap - bottomTube.getHeight()+ tubeOffset[i] ,bottomTube.getWidth() , topTube.getHeight());

			}

			if(birdY > 0) {
				velocity += gravity;
				birdY -= velocity;
			}else{

			    gameState = 3;
            }
			font.draw(batch,String.valueOf(score),100,200);

        }else if (gameState == 0){

			font.draw(batch,"Start game",width/2-200,200);

			batch.draw(birds[random.nextInt(2)], 0 , height /5,700,560);
			batch.draw(birds[random.nextInt(2)], Gdx.graphics.getWidth() -600 , height /10,600,480);
			batch.draw(birds[random.nextInt(2)], width / 2 +100 , birdY-100,300,250);
			batch.draw(birds[random.nextInt(2)], Gdx.graphics.getWidth() -700 , height /2,700,560);
			batch.draw(birds[random.nextInt(2)], width / 5 , height /2,500,400);
			batch.draw(birds[random.nextInt(2)], width / 9 , Gdx.graphics.getHeight()-(height /3),600,480);
			batch.draw(birds[random.nextInt(2)], width / 10 , birdY-20,300,250);


			if(Gdx.input.justTouched()){

				gameState = 1;

			}

		}else if(gameState == 2){

            batch.draw(gameover , Gdx.graphics.getWidth()/2 - gameover.getWidth()/2 ,Gdx.graphics.getHeight()/2 - gameover.getHeight()/2 );
			font.getData().setScale(8);
			font.setColor(Color.FIREBRICK);
			font.draw(batch,"Score : "+String.valueOf(score),Gdx.graphics.getWidth()/2-220,200);

			if(random.nextInt(4) == 0) {
				batch.draw(playagaintext, Gdx.graphics.getWidth() / 2 - playagaintext.getWidth() / 2, (float) (Gdx.graphics.getHeight() / (1.3 + random.nextFloat() * 0.05)));
			}else{
				batch.draw(playagaintext, Gdx.graphics.getWidth() / 2 - playagaintext.getWidth() / 2, (float) (Gdx.graphics.getHeight() / 1.3));
			}
            if (Gdx.input.isTouched()) {

				score = 0;
				scoringTube = 0;
				velocity = 0;
				tubeVelocity = 4;
				gap = birds[0].getHeight() * 1.5f;
				notSlept = true;
				background = new Texture(bg[random.nextInt(6)]);

				startGame();
				gameState = 1;

			}


        }else if(gameState == 3){

			if(notSlept){

				pause(1500);

				birdY = Gdx.graphics.getHeight()/6;

				for(int i=0;i<numberOFTubes ;i++){

					tubeOffset[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - gap - 200);

					tubeX[i] = Gdx.graphics.getWidth()/2 - topTube.getWidth()/2 + i * distanceBetweenTubes + (Gdx.graphics.getWidth());

					topTubeRectangles[i] = new Rectangle();
					bottomTubeRectangles[i] = new Rectangle();

				}

				gameState = 2;

			}

		}


		batch.draw(birds[random.nextInt(2)], Gdx.graphics.getWidth() / 2 - birds[random.nextInt(2)].getWidth() / 2, birdY);


		birdCircle.set(Gdx.graphics.getWidth() /2 , birdY + birds[0].getHeight()/2, (float) (birds[0].getWidth()/3));

		//shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		//shapeRenderer.setColor(Color.RED);
		//shapeRenderer.circle(birdCircle.x , birdCircle.y , birdCircle.radius );

		for(int i=0;i<numberOFTubes ;i++) {

			//shapeRenderer.rect(tubeX[i], Gdx.graphics.getHeight() / 2 + gap + tubeOffset[i] ,topTube.getWidth() , topTube.getHeight());
			//shapeRenderer.rect(tubeX[i], Gdx.graphics.getHeight() / 2 - gap - bottomTube.getHeight()+ tubeOffset[i] ,bottomTube.getWidth() , topTube.getHeight());

			if(Intersector.overlaps(birdCircle,topTubeRectangles[i]) || Intersector.overlaps(birdCircle,bottomTubeRectangles[i])){

                    gameState = 3;
			}

		}
		//shapeRenderer.end();
		batch.end();

	}
    public static void pause(long timeInMilliSeconds) {

        long timestamp = System.currentTimeMillis();


        do {

        } while (System.currentTimeMillis() < timestamp + timeInMilliSeconds);

    }
	
	@Override
	public void dispose () {
		batch.dispose();
		background.dispose();
	}
}
