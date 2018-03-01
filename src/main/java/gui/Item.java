package gui;

import entities.Entity;

import java.awt.image.BufferedImage;

public class Item extends Entity {

	private BufferedImage originIMG;
	private String imgName;
	private boolean explosive;
	private boolean dirt;

	public Item(int x, int y, BufferedImage img, BufferedImage orgImg, boolean explosive, boolean dirt, String imgName) {
		super(x, y, img);
		originIMG = orgImg;
		this.imgName = imgName;
		this.explosive = explosive;
		this.dirt = dirt;
	}

	public String getImgName() { return imgName; }

	public boolean isDirt() { return dirt; }

	public BufferedImage getOriginIMG() {
		return originIMG;
	}

	public boolean isExplosive() {
		return explosive;
	}

}
