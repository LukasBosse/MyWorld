package entities;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Block extends Entity {

    private boolean explosive;
    private boolean explode = false;
    private BufferedImage[] explosion;
    private int currentFrame = 0;
    private int positionX;
    private int positionY;

    public Block(int x, int y, BufferedImage img, boolean explosive, BufferedImage[] explosion) {
        super(x, y, img);
        this.positionX = x/width;
        this.positionY = y/height;
        this.explosive = explosive;
        if(explosive && explosion != null) {
        	this.explosion = explosion;
		}
    }

    @Override
	public void render(Graphics g) {
    	g.drawImage(getImg(), getX(), getY(), null);
    	if(explosion != null && explosive && explode) {
			g.drawImage(explosion[currentFrame], getX(), getY(), null);
		}
	}

	public int getMaxImages() {
    	return explosion.length;
	}

	public int update(double delta) {
		currentFrame++;
		return currentFrame;
	}

	public boolean isNeighbour(Block block) {
    	if(((block.getX()/block.getWidth())+1 == getX()/getWidth()) || ((block.getX()/block.getWidth())-1 == getX()/getWidth()) || ((block.getX()/block.getWidth()) == getX()/getWidth())) {
			if((block.getY()/block.getHeight()+1) == (getY()/getHeight()) || (block.getY()/block.getHeight())-1 == (getY()/getHeight()) || (block.getY()/block.getHeight()) == getY()/getHeight()) {
				return true;
			}
		}
		return false;
	}

	public boolean isBelow(int x, int y) {
		if((this.y/height) - (y/height) < 2) {
			return true;
		}
		return false;
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

    public boolean isExplode() {
    	return explode;
	}

    public boolean isExplosive() {
        return explosive;
    }

	public int getPositionX() {
    	return positionX;
    }

    public int getPositionY() {
    	return positionY;
	}

}
