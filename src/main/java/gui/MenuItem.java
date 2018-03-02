package gui;

import entities.Entity;

import java.awt.image.BufferedImage;

public class MenuItem extends Entity {

	private int index;
	private Highscores highscores;
	private Menubox menu;

	public MenuItem(int x, int y, BufferedImage img, int index, Highscores highscores, Menubox menu) {
		super(x, y, img);
		this.index = index;
		this.highscores = highscores;
		this.menu = menu;
	}

	public void trigger() {
		if(index == 0) {
			System.exit(0);
		} else if(index == 1) {
			highscores.setVisible();
			menu.setVisible();
		} else if(index == 2) {
			System.exit(0);
		}
	}

}
