package entities;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Block extends Entity {

    private boolean explosive;
    private boolean explode = false;
    private boolean inExplosionRange = false;
    private int currentFrame = 0;
    private int positionX;
    private boolean isDirt;
    private BufferedImage dirtImg;
    private BufferedImage[] explosion;
    private List<Entity> victimList = new ArrayList<Entity>();
    private String imageName;
    private int health = 5;

	public Block(int x, int y, BufferedImage img, boolean explosive, BufferedImage[] explosion, boolean isDirt, BufferedImage dirt, String imgName) {
        super(x, y, img);
        this.positionX = x/width;
        this.explosive = explosive;
        this.isDirt = isDirt;
        this.dirtImg = dirt;
        this.imageName = imgName;
        if(explosive && explosion != null) {
        	this.explosion = explosion;
		}
    }

    @Override
	public void render(Graphics g) {
    	g.drawImage(getImg(), getX(), getY(), null);
    	if(explosion != null && explosive && explode) {
			g.drawImage(explosion[currentFrame], getX(), getY(), null);
			if(currentFrame >= explosion.length - 1) {
				killVictims();
			}
		}
	}

	private void killVictims() {
		for(Entity e : victimList) {
			e.die();
		}
	}

	public void addVictim(Entity e) {
		victimList.add(e);
	}

	public String getImageName() { return imageName; }

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

	public boolean isAlive() {
		if(health <= 0) return false;
		return true;
	}

	public boolean isOnAttack(Rectangle bounding) {
		if(this.bounding.intersects(bounding)) {
			health--;
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
        if((((getX()/width) - (x/width)) < allowedDistance) || (((getX()/width) - (getX() - x/width)) < allowedDistance)) {
            return true;
        }
        return false;
    }

    public void toggleDirt() {
		if(null != dirtImg) {
			img = dirtImg;
		}
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

	public boolean isInExplosionRange() {
		return inExplosionRange;
	}

	public void setInExplosionRange(boolean inExplosionRange) {
		this.inExplosionRange = inExplosionRange;
	}

	public boolean isDirt() {
		return isDirt;
	}

}
