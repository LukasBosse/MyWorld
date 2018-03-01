package entities;

import java.awt.image.BufferedImage;
import java.util.List;

public class Coin extends Entity {

	private int DISPLAY_HEIGHT;

	public Coin(int x, int y, BufferedImage img, List<Block> blockList, int DISPLAY_HEIGHT) {
		super(x, y, img);
		this.DISPLAY_HEIGHT = DISPLAY_HEIGHT;
		adjustY(this, blockList, DISPLAY_HEIGHT);
	}

	public void update(List<Block> blockList) {
		adjustY(this, blockList, DISPLAY_HEIGHT);
		bounding.x = x;
		bounding.y = y;
	}

}
