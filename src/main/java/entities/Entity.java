package entities;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Entity {

    public int x;
    public int y;
    public int width;
    public int height;
    public BufferedImage img;
    public Rectangle bounding;

    public Entity(int x, int y, BufferedImage img) {
        this.x = x;
        this.y = y;
        this.img = img;
        this.width = img.getWidth();
        this.height = img.getHeight();
        this.bounding = new Rectangle(x,y,width,height);
    }

    public void render(Graphics g) {
        g.drawImage(getImg(), getX(), getY(), null);
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public BufferedImage getImg() {
        return img;
    }

    public void setImg(BufferedImage img) {
        this.img = img;
    }

    public Rectangle getBounding() {
        return bounding;
    }

    public void setBounding(Rectangle bounding) {
        this.bounding = bounding;
    }
}
