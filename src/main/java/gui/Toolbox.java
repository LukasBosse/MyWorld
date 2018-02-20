package gui;

import entities.Entity;
import tools.TextureLoader;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Toolbox extends Entity {

    private boolean visible;
    private String[] textureList = {
            "Grass_Icon",
            "Gold_Icon",
			"Tnt_Icon",
			"Redstone_Icon",
			"Leaves_Icon",
			"Coat_Icon",
			"Wood_Icon",
			"Stone_Icon"
    };

    private List<Item> itemList = new ArrayList<Item>();

    public Toolbox(int x, int y, BufferedImage img, boolean visible, TextureLoader textureLoader) {

        super(x, y, img);
        this.visible = visible;

        for(int i = 0; i < textureList.length; i++) {
        	Item item;
        	BufferedImage buffImg = textureLoader.getTexture(textureList[i]);
        	BufferedImage originImg = textureLoader.getTexture(textureList[i].split("_")[0]);
        	boolean isExplosiv = textureList[i].equals("Tnt_Icon");

			if(i == 0) {
				item = new Item(x + 3, y + 2, buffImg, originImg, isExplosiv);
			} else {
				item = new Item(x + (i * buffImg.getWidth() + 3), y + 2,buffImg,originImg, isExplosiv);
			}
			itemList.add(item);
        }

    }

    @Override
    public void render(Graphics g) {
        g.drawImage(img, x, y, null);
        for(Item i : itemList) {
        	i.render(g);
		}
    }

    public List<Item> getItemList() {
    	return itemList;
	}

    public boolean isVisible() {
        return visible;
    }

    public void setVisible() {
        this.visible = !visible;
    }

}
