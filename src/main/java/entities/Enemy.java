package entities;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

public class Enemy extends Entity {

	private final int SPEED = 2;
	private int direction = -1;
	private int currentFrame = 0;
	private BufferedImage[] images;
	private List<Block> blockList;

	public Enemy(int x, int y, BufferedImage[] images, List<Block> blockList) {
		super(x, y, images[0]);
		this.images = images;
		this.blockList = blockList;
	}

	public void update(int dir, List<Block> blockList, int DISPLAY_HEIGHT) {
		if(!collisionDetection()) {
			this.x += (dir * SPEED);
			this.direction = dir;
			currentFrame++;
			if(dir > 0) {
				if (currentFrame >= 2) currentFrame = 0;
			} else {
				if (currentFrame >= images.length) currentFrame = 3;
			}
		}
		adjustY(this, blockList, DISPLAY_HEIGHT);
		bounding.x = x;
		bounding.y = y;
	}

	private boolean collisionDetection() {
		//Check collision
		int yScala = y / 80;
		for (Block block : blockList) {
			if ((block.getY() / 80) == yScala) {
				if (block.getBounding().intersects(getBounding())) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean isAttackedOnHead(Rectangle bounding) {
		return new Rectangle(getX(), getY(), getWidth(), 1).intersects(bounding);
	}

	public void jumpBack() {
		this.x += (-1*direction) * 5;
	}

	@Override
	public void render(Graphics g) {
		g.drawImage(images[currentFrame], x, y, null);
	}

}
