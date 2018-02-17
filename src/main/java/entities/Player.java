package entities;

import gui.Item;
import gui.Toolbox;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.List;

public class Player extends Entity implements KeyListener, MouseListener {

    private int DISPLAY_WIDTH;

    private enum DIRECTION {
    	LEFT,
		RIGHT
	};

    private DIRECTION direction = DIRECTION.RIGHT;
    public boolean left, right, destroy;
    public boolean click_Left, click_Right;
    private boolean alive = true;

    private final int MOVE_SPEED = 1;
    private final int ALLOWED_DISTANCE = 2;

    private BufferedImage[] images;
    private Block currentPosition;

    int mouseX = 0;
    int mouseY = 0;

    private Toolbox toolbox;
    private List<Block> blockList;
    private Item selectedItem;

    public Player(int x, int y, BufferedImage[] images, int DISPLAY_WIDTH, Toolbox toolbox, List<Block> blockList) {
        super(x, y, images[0]);
        this.images = images;
        this.DISPLAY_WIDTH = DISPLAY_WIDTH;
        this.toolbox = toolbox;
        this.blockList = blockList;
    }

    public void update() {
        if(left) {
            this.x -= MOVE_SPEED;
            if(x < 0) {
                left = false;
                int delta = 0 - x;
                this.x += delta;
            }
			for(BufferedImage bI : images) {
				img = bI;
			}
        }
        if(right) {
            this.x += MOVE_SPEED;
            for(BufferedImage bI : images) {
                img = bI;
            }
            if((this.x + width) > DISPLAY_WIDTH) {
                right = false;
                int delta = (this.x + width) - DISPLAY_WIDTH;
                this.x -= delta;
            }
        }
		currentPosition = getCurrentPosition();
        if(isFalling()) {
        	for(int i = 0; i < (getGroundDelta()); i++) {
				this.y += i;
			}
		}
		if(this.y > (DISPLAY_WIDTH + height)) {
        	alive = false;
		}
		if(!isAlive()) {
        	System.out.println("SORRY, YOU ARE DEAD!");
        	System.exit(0);
		}
        if(click_Right) {
            if(mouseX != 0 && mouseY != 0) {
                Iterator<Block> iter = blockList.iterator();
                while (iter.hasNext()) {
                    Block block = iter.next();
                    if (block.getX() / 80 == mouseX / 80 && block.getY() / 80 == (mouseY / 80)) {
                        if(((block.getX() / 80) - (x/80)) < ALLOWED_DISTANCE || ((block.getX() / 80) - (x/80)) < ALLOWED_DISTANCE)
                            iter.remove();
                    }
                }
            }
        }
        if(click_Left) {

            //Select item in toolbox
			if(toolbox.isVisible()) {
				for(Item i : toolbox.getItemList()) {
					if(i.getBounding().contains(mouseX, mouseY)) {
						selectedItem = i;
						break;
					}
				}
			}

			//Build block
			if(selectedItem != null) {
				Block block = null;
				if((mouseX > x && direction == DIRECTION.RIGHT) || (mouseX < x && direction == DIRECTION.LEFT)) {
					Block refBlock = null;
					Iterator<Block> iter = blockList.iterator();
					while (iter.hasNext()) {
						Block itBlock = iter.next();
						if(itBlock.findBlockAt(mouseX,mouseY)) {
							refBlock = itBlock;
						}
					}
					if(refBlock != null)
					block = new Block(refBlock.getX(), refBlock.getY() - refBlock.height, selectedItem.getOriginIMG());
				}
				if(block != null)
				blockList.add(block);
			}

        }

    }

    private Block getCurrentPosition() {
		Iterator<Block> iter = blockList.iterator();
		while (iter.hasNext()) {
			Block block = iter.next();
			if((block.getX()/80) == (x/80) && ((block.getY()/80) == (y/80)+1)) {
				return block;
			}
		}
		return null;
	}

    private int getGroundDelta() {
		if(currentPosition != null) {
			return currentPosition.y - y;
		}
		return 4;
	}

    private boolean isFalling() {
    	boolean falling = true;
		Iterator<Block> iter = blockList.iterator();
		while (iter.hasNext()) {
			Block block = iter.next();
			if((block.getX()/80) == (x/80) && ((block.getY()/80) == (y/80)+1)) {
				falling = false;
			}
		}
		return falling;
	}

	private boolean isAlive() {
    	return alive;
	}

    public void keyTyped(KeyEvent e) { }

    public void keyPressed(KeyEvent key) {
        if((key.getKeyCode() == KeyEvent.VK_LEFT) || (key.getKeyCode() == KeyEvent.VK_A))
            left = true;
			direction = DIRECTION.LEFT;
        if((key.getKeyCode() == KeyEvent.VK_RIGHT) || (key.getKeyCode() == KeyEvent.VK_D))
            right = true;
			direction = DIRECTION.RIGHT;
        if((key.getKeyCode() == KeyEvent.VK_SPACE))
            destroy = true;
        if((key.getKeyCode() == KeyEvent.VK_ESCAPE))
            System.exit(0);
        if((key.getKeyCode() == KeyEvent.VK_B))
            toolbox.setVisible();
    }

    public void keyReleased(KeyEvent key) {
        if((key.getKeyCode() == KeyEvent.VK_LEFT) || (key.getKeyCode() == KeyEvent.VK_A))
            left = false;
            img = images[0];
        if((key.getKeyCode() == KeyEvent.VK_RIGHT) || (key.getKeyCode() == KeyEvent.VK_D))
            right = false;
            img = images[0];
        if((key.getKeyCode() == KeyEvent.VK_SPACE))
            destroy = false;
    }

    public void mouseClicked(MouseEvent e) {
        mouseX=e.getX();
        mouseY=e.getY();
    }

    public void mousePressed(MouseEvent e) {
        mouseX=e.getX();
        mouseY=e.getY();
        if(SwingUtilities.isLeftMouseButton(e))
            click_Left = true;
        if(SwingUtilities.isRightMouseButton(e))
            click_Right = true;
    }

    public void mouseReleased(MouseEvent e) {
        if(SwingUtilities.isLeftMouseButton(e))
            click_Left = false;
        if(SwingUtilities.isRightMouseButton(e))
            click_Right = false;
    }

    public void mouseEntered(MouseEvent e) {

    }

    public void mouseExited(MouseEvent e) {

    }
}
