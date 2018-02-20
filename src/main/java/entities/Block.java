package entities;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Block extends Entity {

    private boolean explosive;
    private boolean explode = false;
    private BufferedImage[] explosion;

    public Block(int x, int y, BufferedImage img, boolean explosive, BufferedImage[] explosion) {
        super(x, y, img);
        this.explosive = explosive;
        if(explosive && explosion != null) {
        	this.explosion = explosion;
		}
    }

    @Override
	public void render(Graphics g) {
    	g.drawImage(getImg(), getX(), getY(), null);
    	if(explosion != null && explosive && explode) {
			for(BufferedImage img : explosion) {
				g.drawImage(img, getX(), getY(), null);
			}
		}
	}

    public void explode() {
    	explode = true;
	}

    public boolean findBlockAt(int x, int y) {
        if(new Rectangle(getX(), getY() - getHeight(), width, height).contains(x,y)) {
            return true;
        }
        return false;
    }

    public boolean findBlock(int x, int y) {
        if(new Rectangle(getX(),getY(),width,height).contains(x,y)) return true;
        return false;
    }

    public boolean isinDistance(int x, int y, int allowedDistance) {
       // (block.getX() / 80) - (x/80)) < ALLOWED_DISTANCE || ((block.getX() / 80) - (x/80)) < ALLOWED_DISTANCE
        if((((getX()/width) - (x/width)) < allowedDistance) || (((getX()/width) - (getX() - x/width)) < allowedDistance)) {
            return true;
        }
        return false;
    }

    public boolean isExplosive() {
        return explosive;
    }

}
