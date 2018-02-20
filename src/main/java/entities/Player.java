package entities;

import gui.Item;
import gui.Toolbox;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.List;

public class Player extends Entity implements KeyListener, MouseListener {

    private int DISPLAY_WIDTH;
	private int DISPLAY_HEIGHT;

    private enum DIRECTION {
    	LEFT,
		RIGHT
	};

    private DIRECTION direction = DIRECTION.RIGHT;
    public boolean left, right, jump;
    public boolean click_Left, click_Right;
    private boolean alive = true;

    private final int MOVE_SPEED = 2;
    private final int ALLOWED_DISTANCE = 2;

    private final float GRAVITY = 0.05f;
	private float jumpPower = 0;

	private BufferedImage[] images;
    private Block currentPosition;

    private int mouseX = 0;
    private int mouseY = 0;

    private int coat = 10;
    private int tnt = 10;
    private int gold = 10;
    private int grass = 10;
    private int leaves = 10;
    private int redstone = 10;
    private int stone = 10;

    private Toolbox toolbox;
    private List<Block> blockList;
    private BufferedImage[] explosionAnimation;
    private Item selectedItem;

    public Player(int x, int y, BufferedImage[] images, int DISPLAY_WIDTH, int DISPLAY_HEIGHT, Toolbox toolbox, List<Block> blockList, BufferedImage[] explosionAnimation) {
        super(x, y, images[0]);
        this.images = images;
        this.DISPLAY_WIDTH = DISPLAY_WIDTH;
        this.DISPLAY_HEIGHT = DISPLAY_HEIGHT;
        this.toolbox = toolbox;
        this.blockList = blockList;
		currentPosition = getCurrentPosition();
		this.y = getGround();
		this.explosionAnimation = explosionAnimation;
    }

    @Override
	public void render(Graphics g) {
    	g.drawImage(img, x, y, null);
		g.drawString("Stone: " + stone + " | Coat: " + coat + " | TNT: " + tnt + " | Gold: " + gold + " | Grass: " + grass + " | Redstone: " + redstone + " | Leaves: " + leaves, 10,50);
    }

    public void update() {

    	//WALKING
    	if(left) {
            this.x -= MOVE_SPEED;
            collisionDetection(0);
        }

        if(right) {
            this.x += MOVE_SPEED;
            collisionDetection(1);
        }

        if(left || right) {
    		for(BufferedImage bI : images) {
				img = bI;
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
        	System.out.println("- SORRY, YOU ARE DEAD! -");
        	System.exit(0);
		}
        if(click_Right) {
            if(mouseX != 0 && mouseY != 0) {
                Iterator<Block> iter = blockList.iterator();
                while (iter.hasNext()) {
                    Block block = iter.next();
                    if (block.findBlock(mouseX, mouseY) && block.isinDistance(x,y, ALLOWED_DISTANCE)) {
                    	iter.remove();
                    	break;
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
				if((mouseX > x && direction == DIRECTION.RIGHT) || (mouseX < x && direction == DIRECTION.LEFT)) {
					Block refBlock = null;
					Iterator<Block> iter = blockList.iterator();
					while (iter.hasNext()) {
						Block itBlock = iter.next();
						if(itBlock.findBlockAt(mouseX,mouseY)) {
							refBlock = itBlock;
						}
					}
					if(refBlock != null && !alreadyPlaced(refBlock.getX(), refBlock.getY() - refBlock.height)) {
						blockList.add(new Block(refBlock.getX(), refBlock.getY() - refBlock.height, selectedItem.getOriginIMG(), selectedItem.isExplosive(),explosionAnimation));
						System.out.println(blockList.size());
					}
				}

			}

        }

        this.bounding.x = x;
        this.bounding.y = y;

    }

    private boolean alreadyPlaced(int x, int y) {
    	Iterator<Block> iB = blockList.iterator();
    	while(iB.hasNext()) {
    		if(iB.next().findBlock(x,y)) {
    			return true;
			}
		}
		return false;
	}

    private void collisionDetection(int index) {

    	//Check if ran out of map
    	if(index == 0) {
			if(this.x < 0) {
				left = false;
				int delta = 0 - this.x;
				this.x += delta;
			}
		} else {
			if((this.x + width) > DISPLAY_WIDTH) {
				right = false;
				int delta = (this.x + width) - DISPLAY_WIDTH;
				this.x -= delta;
			}
		}

		//Check collision
		int yScala = y/80;
    	for(Block block : blockList) {
    		if((block.getY()/80) == yScala) {
    			if(block.getBounding().intersects(getBounding())) {
    				right = false;
    				left = false;
    				this.x = block.getX() - getWidth();
				}
			}
		}

	}

    private Block getCurrentPosition() {
		Iterator<Block> iter = blockList.iterator();
		while (iter.hasNext()) {
			Block block = iter.next();
			int displayHeight = (DISPLAY_HEIGHT / 80);
			for(int i = 0; i < displayHeight; i++) {
				if ((block.getX() / 80) == (x / 80) && ((block.getY() / 80) == (y / 80) + i)) {
					return block;
				}
			}
		}
		return null;
	}

	private int getGround() {
    	if(currentPosition != null) {
    		return currentPosition.y - currentPosition.getHeight();
		}
		return DISPLAY_HEIGHT-80;
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
        if((key.getKeyCode() == KeyEvent.VK_ESCAPE))
            System.exit(0);
        if((key.getKeyCode() == KeyEvent.VK_B))
            toolbox.setVisible();
        if((key.getKeyCode() == KeyEvent.VK_SPACE))
    		jump = true;
    }

    public void keyReleased(KeyEvent key) {
        if((key.getKeyCode() == KeyEvent.VK_LEFT) || (key.getKeyCode() == KeyEvent.VK_A))
            left = false;
            img = images[0];
        if((key.getKeyCode() == KeyEvent.VK_RIGHT) || (key.getKeyCode() == KeyEvent.VK_D))
            right = false;
            img = images[0];
        if((key.getKeyCode() == KeyEvent.VK_SPACE))
			jump = false;
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

	public int getCoat() {
		return coat;
	}
	public int getGold() {
		return gold;
	}
	public int getGrass() {
		return grass;
	}
	public int getLeaves() {
		return leaves;
	}
	public int getRedstone() {
		return redstone;
	}
	public int getStone() {
		return stone;
	}
	public int getTnt() {
		return tnt;
	}

    public void mouseEntered(MouseEvent e) {

    }

    public void mouseExited(MouseEvent e) {

    }
}
