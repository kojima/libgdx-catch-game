package com.hide.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

// [Player] http://opengameart.org/content/bevouliin-free-orange-bubble-land-monster-sprite-sheets
// [Enemy] http://opengameart.org/content/red-horn-land-monster
// [Background] http://opengameart.org/content/bevouliin-free-game-background-for-game-developers
// [BGM] http://freesound.org/people/LittleRobotSoundFactory/sounds/323958/
// [Lose sound] http://freesound.org/people/LittleRobotSoundFactory/sounds/270328/
public class CatchGame extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	Texture enemyImg;
	Texture background;

	private OrthographicCamera camera;

	private Rectangle player;
	private Rectangle enemy;

	private Sound loseSound;
	private Music bgm;

	private Boolean gamOver = false;

	private Vector2 enemyVelocity;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("player.png");
		enemyImg = new Texture("enemy.png");
		background = new Texture("full-background.png");


		player = new Rectangle(0, 0, 128, 95);
		enemy = new Rectangle(400, 300, 116, 128);

		enemyVelocity = new Vector2(-160, -160);

		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);

		loseSound = Gdx.audio.newSound(Gdx.files.internal("lose.wav"));
		bgm = Gdx.audio.newMusic(Gdx.files.internal("bgm.wav"));

		bgm.setLooping(true);
		bgm.play();
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.update();
		batch.setProjectionMatrix(camera.combined);

		batch.begin();
		batch.draw(background, 0, 0);
		batch.draw(img, player.x, player.y);
		batch.draw(enemyImg, enemy.x, enemy.y);
		batch.end();

		if (gamOver) return;

		if (Math.abs(Gdx.input.getAccelerometerY()) > 0.2) {
			player.x += 200 * Gdx.input.getAccelerometerY() * Gdx.graphics.getDeltaTime();
		}
		if (Math.abs(Gdx.input.getAccelerometerX()) > 0.2) {
			player.y -= 200 * Gdx.input.getAccelerometerX() * Gdx.graphics.getDeltaTime();
		}

		if (player.x < 0) {
			player.x = 0;
		} else if (player.x + player.width > 800) {
			player.x = 800 - player.width;
		}
		if (player.y < 0) {
			player.y = 0;
		} else if (player.y + player.height > 480) {
			player.y = 480 - player.height;
		}

		enemy.x += enemyVelocity.x * Gdx.graphics.getDeltaTime();
		enemy.y += enemyVelocity.y * Gdx.graphics.getDeltaTime();

		if (enemy.x < 0 || enemy.x + enemy.width > 800) {
			enemyVelocity.x *= -1;
		}
		if (enemy.y < 0 || enemy.y + enemy.height > 480) {
			enemyVelocity.y *= -1;
		}

		if (player.overlaps(enemy)) {
			gamOver = true;
			loseSound.play();
			bgm.stop();
		}
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
		enemyImg.dispose();
		background.dispose();
		loseSound.dispose();
		bgm.dispose();
	}
}
