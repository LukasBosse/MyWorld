package entities;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Entity {

    public int x;
    public int y;
    public int width;
    public int height;
    public BufferedImage img;
    public Rectangle bounding;
    private boolean alive = true;

    public Entity(int x, int y, BufferedImage img) {
        this.x = x;
        this.y = y;
        this.img = img;
        this.width = img.getWidth();
        this.height = img.getHeight();
        this.bounding = new Rectangle(x,y,width,height);
    }

    public int getGround(int x, List<Block> blockList) {
        Iterator<Block> bI = blockList.iterator();
        List<Block> helperList = new LinkedList<Block>();
        while (bI.hasNext()) {
            Block b = bI.next();
            if(b.getPositionX() == x/b.getWidth()) {
                helperList.add(b);
            }
        }
        if(!helperList.isEmpty()) {
            Block help = helperList.get(helperList.size() - 1);
            return help.getY() - help.getHeight();
        }
        return 0;
    }

    public void adjustY(Entity e, List<Block> blockList, int DISPLAY_HEIGHT) {
        int yNew = getGround(e.getX(), blockList);
        int groundDelta = yNew - e.getY();
        if (groundDelta > 0) {
            for (int i = 0; i < 5; i++) {
                e.setY(e.getY() + i);
            }
        } else if (groundDelta == 0) {
            e.setY(yNew);
        } else if (yNew == 0) {
            for (int i = 0; i < 5; i++) {
                e.setY(e.getY() + i);
            }
            if (y > DISPLAY_HEIGHT - getHeight()) {
                e.setAlive(false);
            }
        }
    }

    public void render(Graphics g) {
        g.drawImage(getImg(), getX(), getY(), null);
    }

    public void setAlive(boolean alive) { this.alive = alive; }

    public boolean isAlive() { return alive; }

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
