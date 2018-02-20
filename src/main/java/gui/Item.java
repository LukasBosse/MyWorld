package gui;

import entities.Entity;

import java.awt.image.BufferedImage;

public class Item extends Entity {

	private BufferedImage originIMG;
	private boolean explosive;

	public Item(int x, int y, BufferedImage img, BufferedImage orgImg, boolean explosive) {
		super(x, y, img);
		originIMG = orgImg;
		this.explosive = explosive;
	}

	public BufferedImage getOriginIMG() {
		return originIMG;
	}

	public boolean isExplosive() {
		return explosive;
	}

}
