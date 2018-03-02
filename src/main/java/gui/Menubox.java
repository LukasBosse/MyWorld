package gui;

import entities.Entity;
import tools.TextureLoader;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Menubox extends Entity {

	private TextureLoader textureLoader;
	private boolean visible = false;
	private List<MenuItem> menuItemList = new ArrayList<MenuItem>();
	private String[] textureList = {
			"btn_settings",
			"btn_statics",
			"btn_close"
	};

	public Menubox(int x, int y, BufferedImage img, TextureLoader textureLoader, Highscores highscores) {

		super(x, y, img);
		this.textureLoader = textureLoader;

		for(int i = 0; i < textureList.length; i++) {

			MenuItem item = null;
			BufferedImage itemImage = textureLoader.getTexture(textureList[i]);

			if(i == 0) {
				item = new MenuItem(x+3, y+3, itemImage,i, highscores,this);
			} else if(i == 1) {
				item = new MenuItem(x+3, y + (itemImage.getHeight()*i), itemImage,i,highscores,this);
			} else if(i == 2) {
				item = new MenuItem(x+3, y - 1 + (itemImage.getHeight()*i), itemImage,i, highscores, this);
			}
			menuItemList.add(item);
		}
	}

	@Override
	public void render(Graphics g) {
		g.drawImage(img, x, y, null);
		for(MenuItem menuItem: menuItemList) {
			menuItem.render(g);
		}
	}

	public List<MenuItem> getMenuItemList() {
		return menuItemList;
	}

	public void setVisible() {
		this.visible = !this.visible;
	}

	public boolean isVisible() {
		return this.visible;
	}

}
