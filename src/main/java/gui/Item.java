package gui;

import entities.Entity;

import java.awt.image.BufferedImage;

public class Item extends Entity {

	private BufferedImage originIMG;

	public Item(int x, int y, BufferedImage img, BufferedImage orgImg) {
		super(x, y, img);
		originIMG = orgImg;
	}

	public BufferedImage getOriginIMG() {
		return originIMG;
	}
}
