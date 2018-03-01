package entities;

import gui.Item;
import gui.MenuItem;
import gui.Menubox;
import gui.Toolbox;
import tools.LevelLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Iterator;
import java.util.List;

public class Player extends Entity implements KeyListener, MouseListener {

	private int DISPLAY_WIDTH;
	private int DISPLAY_HEIGHT;

	private enum DIRECTION {
		LEFT,
		RIGHT
	}

	private DIRECTION direction = DIRECTION.RIGHT;
	public boolean left, right, jump;
	public boolean click_Left, click_Right;

	private final int MOVE_SPEED = 2;
	private final int ALLOWED_DISTANCE = 2;

	private final float GRAVITY = 0.5f;
	private float jumpPower = 0;

	private BufferedImage[] images;
	private int currentFrame = 0;
	private int currentLevel;

	private int mouseX = 0;
	private int mouseY = 0;

	//Stats
	private String name = "John Doe";
	private int gold = 0;
	private int health = 5;

	private LevelLoader levelLoader;
	private Toolbox toolbox;
	private Menubox menubox;
	private List<Block> blockList;
	private BufferedImage[] explosionAnimation;
	private BufferedImage dirtIMG;
	private Item selectedItem;

	public Player(int x, int y, BufferedImage[] images, int DISPLAY_WIDTH, int DISPLAY_HEIGHT, Toolbox toolbox, Menubox menubox, List<Block> blockList, BufferedImage dirtIMG,
			BufferedImage[] explosionAnimation, LevelLoader levelLoader, int currentLevel) {
		super(x, y, images[0]);
		this.images = images;
		this.currentLevel = currentLevel;
		this.DISPLAY_WIDTH = DISPLAY_WIDTH;
		this.DISPLAY_HEIGHT = DISPLAY_HEIGHT;
		this.toolbox = toolbox;
		this.menubox = menubox;
		this.blockList = blockList;
		this.levelLoader = levelLoader;
		this.y = getGround(this.x, blockList);
		this.dirtIMG = dirtIMG;
		this.explosionAnimation = explosionAnimation;
	}

	@Override
	public void render(Graphics g) {
		g.drawImage(images[currentFrame], x, y, null);
		g.drawString("Leben: " + health + " | Münzen: " + gold, 10, 50);
	}

	public void update(List<Coin> coinList, List<Enemy> enemyList) {

		//WALKING
		if (left) {
			currentFrame++;
			if (currentFrame >= images.length) currentFrame = 5;
			this.x -= MOVE_SPEED;
			collisionDetection(0, coinList, enemyList);
		}

		if(right) {
			currentFrame++;
			if (currentFrame >= 5) currentFrame = 1;
			this.x += MOVE_SPEED;
			collisionDetection(1, coinList, enemyList);
		}

		if(!left && !right){
			if(direction == DIRECTION.LEFT) {
				currentFrame = 5;
			} else if(direction == DIRECTION.RIGHT) {
				currentFrame = 0;
			}
		}

		if(jump && (getGround(this.x, blockList)-this.y) < 50) {
			jumpPower = 12;
		}

		if(jumpPower > 0) {
			this.y -= jumpPower;
			jumpPower -= GRAVITY;
		} else {
			adjustY(this, blockList, DISPLAY_HEIGHT);
		}

		if (!isAlive()) {
			playDeathAnimation();
		}
		if (click_Right) {
			destroyBlock();
		}
		if (click_Left) {
			//Select item in toolbox
			selectItem();
			//Select menu item
			selectMenuItem();
			//Select and build block
			buildBlock();
		}

		this.bounding.x = x;
		this.bounding.y = y;

	}

	private void playDeathAnimation() {
		writeHighscore();
	}

	private void writeHighscore() {
		String fileName = "res/Stats/Highscores.txt";
		PrintWriter printWriter = null;
		File file = new File(fileName);
		try {
			if (!file.exists()) file.createNewFile();
			printWriter = new PrintWriter(new FileOutputStream(fileName, true));
			if(isFileEmpty(fileName)) {
				printWriter.write(getName() + "|" + getCoins());
			} else {
				printWriter.write(System.getProperty("line.separator") + getName() + "|" + getCoins());
			}
		} catch (IOException ioex) {
			ioex.printStackTrace();
		} finally {
			if (printWriter != null) {
				printWriter.flush();
				printWriter.close();
			}
		}
	}

	private boolean isFileEmpty(String filePath) {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(filePath));
			if (br.readLine() == null) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	private void destroyBlock() {
		if (mouseX != 0 && mouseY != 0) {
			Iterator<Block> iter = blockList.iterator();
			while (iter.hasNext()) {
				Block block = iter.next();
				if (block.findBlock(mouseX, mouseY) && block.isinDistance(x, y, ALLOWED_DISTANCE)) {
					if (block.isExplosive()) {
						block.explode();
					} else {
						iter.remove();
					}
					break;
				}
			}
		}
	}

	private void selectMenuItem() {
		if(menubox.isVisible()) {
			for(MenuItem menuItem : menubox.getMenuItemList()) {
				if(menuItem.getBounding().contains(mouseX, mouseY)) {
					menuItem.trigger();
					break;
				}
			}
		}
	}

	private void selectItem() {
		if (toolbox.isVisible()) {
			for (Item i : toolbox.getItemList()) {
				if (i.getBounding().contains(mouseX, mouseY)) {
					selectedItem = i;
					break;
				}
			}
		}
	}

	private void buildBlock() {
		if (selectedItem != null) {
			if ((mouseX > x && direction == DIRECTION.RIGHT) || (mouseX < x && direction == DIRECTION.LEFT)) {
				Block refBlock = null;
				Iterator<Block> iter = blockList.iterator();
				while (iter.hasNext()) {
					Block itBlock = iter.next();
					if (itBlock.findBlockAt(mouseX, mouseY)) {
						refBlock = itBlock;
					}
				}
				if (refBlock != null && !alreadyPlaced(refBlock.getX(), refBlock.getY() - refBlock.height)) {
					blockList.add(new Block(refBlock.getX(), refBlock.getY() - refBlock.height, selectedItem.getOriginIMG(),
							selectedItem.isExplosive(), explosionAnimation, selectedItem.isDirt(), dirtIMG, selectedItem.getImgName()));
					if(selectedItem.isDirt() && refBlock.isDirt()) {
						refBlock.toggleDirt();
					}
				}
			}

		}
	}

	private boolean alreadyPlaced(int x, int y) {
		Iterator<Block> iB = blockList.iterator();
		while (iB.hasNext()) {
			if (iB.next().findBlock(x, y)) {
				return true;
			}
		}
		return false;
	}

	private void collisionDetection(int index, List<Coin> coinList, List<Enemy> enemyList) {

		//Check for map boundings
		checkMapBounding(index);

		//Check for collision with coins
		Iterator<Coin> cI = coinList.iterator();
		while(cI.hasNext()) {
			if(cI.next().getBounding().intersects(this.getBounding())) {
				cI.remove();
				gold++;
			}
		}

		//Check for collision with enemies
		Iterator<Enemy> eI = enemyList.iterator();
		while(eI.hasNext()) {
			if(eI.next().getBounding().intersects(this.getBounding())) {
				health--;
				checkHealth();
				eI.remove();
			}
		}

		//Check collision
		int yScala = y / 80;
		for (Block block : blockList) {
			if ((block.getY() / 80) == yScala) {
				if (block.getBounding().intersects(getBounding())) {
					if(right) {
						this.x = block.getX() - getWidth();
					} else if(left) {
						this.x = block.getX() + block.getWidth();
					}
					right = false;
					left = false;
				}
			}
		}

	}

	private void checkHealth() {
		if(health <= 0) {
			setAlive(false);
			playDeathAnimation();
		}
	}

	private void checkMapBounding(int index) {
		//Check if ran out of map
		if (index == 0) {
			if (this.x < 0) {
				if(((currentLevel-1) >= 0) && !levelLoader.getLevel(currentLevel-1).isEmpty()) {
					//levelLoader.saveToFile(currentLevel, blockList);
					currentLevel--;
					blockList.clear();
					blockList = levelLoader.getLevel(currentLevel);
					this.x = DISPLAY_WIDTH - getWidth();
				} else {
					left = false;
					int delta = 0 - this.x;
					this.x += delta;
				}
			}
		} else {
			if ((this.x + width) > DISPLAY_WIDTH) {
				if(!levelLoader.getLevel(currentLevel+1).isEmpty()) {
					//levelLoader.saveToFile(currentLevel, blockList);
					currentLevel++;
					blockList.clear();
					blockList = levelLoader.getLevel(currentLevel);
					this.x = 0;
				} else {
					right = false;
					int delta = (this.x + width) - DISPLAY_WIDTH;
					this.x -= delta;
				}
			}
		}
	}

	public String getName() { return name; }

	public int getCoins() { return gold; }

	public void keyTyped(KeyEvent e) { /*UNUSED*/ }

	public void keyPressed(KeyEvent key) {
		if ((key.getKeyCode() == KeyEvent.VK_LEFT) || (key.getKeyCode() == KeyEvent.VK_A)) {
			left = true;
			direction = DIRECTION.LEFT;
		}
		if ((key.getKeyCode() == KeyEvent.VK_RIGHT) || (key.getKeyCode() == KeyEvent.VK_D)) {
			right = true;
			direction = DIRECTION.RIGHT;
		}
		if ((key.getKeyCode() == KeyEvent.VK_ESCAPE))
			menubox.setVisible();
		if ((key.getKeyCode() == KeyEvent.VK_B))
			toolbox.setVisible();
		if ((key.getKeyCode() == KeyEvent.VK_SPACE))
			jump = true;
	}

	public void keyReleased(KeyEvent key) {
		if ((key.getKeyCode() == KeyEvent.VK_LEFT) || (key.getKeyCode() == KeyEvent.VK_A)) {
			left = false;
			img = images[0];
		}
		if ((key.getKeyCode() == KeyEvent.VK_RIGHT) || (key.getKeyCode() == KeyEvent.VK_D)) {
			right = false;
			img = images[0];
		}
		if ((key.getKeyCode() == KeyEvent.VK_SPACE))
			jump = false;
	}

	public void mouseClicked(MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();
	}

	public void mousePressed(MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();
		if (SwingUtilities.isLeftMouseButton(e))
			click_Left = true;
		if (SwingUtilities.isRightMouseButton(e))
			click_Right = true;
	}

	public void mouseReleased(MouseEvent e) {
		if (SwingUtilities.isLeftMouseButton(e))
			click_Left = false;
		if (SwingUtilities.isRightMouseButton(e))
			click_Right = false;
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

}