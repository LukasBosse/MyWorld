package gui;

import entities.Entity;

import java.awt.image.BufferedImage;

public class MenuItem extends Entity {

	private int index;

	public MenuItem(int x, int y, BufferedImage img, int index) {
		super(x, y, img);
		this.index = index;
	}

	public void trigger() {
		if(index == 0) {
			System.exit(0);
		} else if(index == 1) {
			System.exit(0);
		} else if(index == 2) {
			System.exit(0);
		}
	}

}
