package entities;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

public class Coin extends Entity {

	private int DISPLAY_HEIGHT;
	private int currentFrame = 0;
	private boolean dying = false;
	private BufferedImage[] animation;

	public Coin(int x, int y, BufferedImage img, List<Block> blockList, int DISPLAY_HEIGHT, BufferedImage[] animation) {
		super(x, y, img);
		this.DISPLAY_HEIGHT = DISPLAY_HEIGHT;
		this.animation = animation;
		adjustY(this, blockList, DISPLAY_HEIGHT);
	}

	@Override
	public void render(Graphics g) {
		if(dying) {
			g.drawImage(animation[currentFrame], x,y, null);
		} else {
			g.drawImage(img, x, y, null);
		}
	}

	public boolean isDying() {
		return dying;
	}

	public void setDying() {
		this.dying = true;
	}

	public int getAnimationSize() {
		return animation.length;
	}

	public int update() {
		currentFrame++;
		return currentFrame;
	}

	public void update(List<Block> blockList) {
		adjustY(this, blockList, DISPLAY_HEIGHT);
		bounding.x = x;
		bounding.y = y;
	}

}
