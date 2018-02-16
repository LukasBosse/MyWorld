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
            "Gold_Icon"
    };

    private List<BufferedImage> imgList = new ArrayList<BufferedImage>();

    public Toolbox(int x, int y, BufferedImage img, boolean visible, TextureLoader textureLoader) {

        super(x, y, img);
        this.visible = visible;

        for(String s : textureList) {
            imgList.add(textureLoader.getTexture(s));
        }

    }

    @Override
    public void render(Graphics g) {
        g.drawImage(img, x, y, null);
        for(int i = 0; i < imgList.size(); i++) {
            BufferedImage buffImg = imgList.get(i);
            if(i == 0) {
                g.drawImage(buffImg, x + 3, y + 2, null);
            } else {
                g.drawImage(buffImg, x + 3 + (i * buffImg.getWidth() + 3), y + 2, null);
            }
        }
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible() {
        this.visible = !visible;
    }

}
