package entities;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Block extends Entity {

    public Block(int x, int y, BufferedImage img) {
        super(x, y, img);
    }

    public boolean findBlockAt(int x, int y) {
        if(new Rectangle(getX(), getY() - getHeight(), width, height).contains(x,y)) {
            return true;
        }
        return false;
    }

}
