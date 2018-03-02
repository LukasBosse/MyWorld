import entities.*;
import gui.Highscores;
import gui.Menubox;
import gui.Toolbox;
import tools.LevelLoader;
import tools.TextureLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;

public class Main extends JFrame implements Runnable {

	//Final Settings
    private final String TITLE = "MYWORLD";
    private final String VERSION = "0.0.1 ALPHA";
    private final int DISPLAY_WIDTH = 1600;
    private final int DISPLAY_HEIGHT = 900;
    //OS
    private Thread thread;
    private boolean running;
    //Tools
    private TextureLoader textureLoader;
    private LevelLoader levelLoader;
    //Entities
    private Background background;
    private Player player;
    private List<Block> blockList;
    private List<Enemy> enemyList = new LinkedList<Enemy>();
    private List<Coin> coinList = new ArrayList<Coin>();
    //Timemanagement
	private long timeSinceLastEnemy = 0;
	private long timeSinceLastCoin = 0;
	private final int ENEMYSPAWNTIME = 10;
	private final int COINSPAWNTIME = 15;
	private int lastDirection = 1;
    //GUI
    private Toolbox toolbox;
    private Menubox menu;
    private Highscores highscores;

    public Main() {
        thread = new Thread(this);
        textureLoader = new TextureLoader("res");
        levelLoader = new LevelLoader(textureLoader, DISPLAY_HEIGHT);
        background = new Background(0,0, getTexture("background"));
        BufferedImage toolboxImg = getTexture("toolbox");
        BufferedImage menuImg = getTexture("menu");
        BufferedImage dirtIMG = getTexture("Dirt");
        toolbox = new Toolbox((DISPLAY_WIDTH/2) - (toolboxImg.getWidth()/2),DISPLAY_HEIGHT - (toolboxImg.getHeight()/2) - 35, toolboxImg, false,textureLoader);
		highscores = new Highscores(((DISPLAY_WIDTH/2)-(menuImg.getWidth()/2)), 200, menuImg, getTexture("btn_closeWindow"));
		menu = new Menubox(((DISPLAY_WIDTH/2)-(menuImg.getWidth()/2)), 200, menuImg, textureLoader, highscores);
        blockList = levelLoader.getLevel(0);
		player = new Player(100, 440, getPlayerAnimation(), DISPLAY_WIDTH, DISPLAY_HEIGHT, toolbox, menu, blockList, dirtIMG, generateExplosionAnimation(), levelLoader, 0, highscores, textureLoader);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addKeyListener(player);
        addMouseListener(player);
        setLayout(null);
       	setSize(DISPLAY_WIDTH, DISPLAY_HEIGHT);
        setResizable(false);
        setLocationRelativeTo(null);
        setTitle(TITLE + " - " + VERSION);
        setVisible(true);
        start();
    }

    public void render() {
        BufferStrategy bs = getBufferStrategy();
        if(bs == null) {
            createBufferStrategy(3);
            return;
        }
        Graphics g = bs.getDrawGraphics();
        //Render background
        background.render(g);
        //Render blocks
		Iterator<Block> bI = blockList.iterator();
		while (bI.hasNext()) {
			bI.next().render(g);
		}
		//Render coins
		Iterator<Coin> cI = coinList.iterator();
		while(cI.hasNext()) {
			cI.next().render(g);
		}
		//Render enemies
		Iterator<Enemy> eI = enemyList.iterator();
		while(eI.hasNext()) {
			eI.next().render(g);
		}
        //Render player
        player.render(g);
        //Render toolbox
        if(toolbox.isVisible()) {
            toolbox.render(g);
        }
		//Render menu
        if(menu.isVisible()) {
        	menu.render(g);
		}
		//Render highscores
		if(highscores.isVisible()) {
        	highscores.render(g);
		}
        bs.show();
    }

    public void run() {
        long lastTime = System.nanoTime();
        final double ns = 1000000000.0 / 60.0;//60 times per second
        double delta = 0;
        requestFocus();
        while(running) {
            long now = System.nanoTime();
            delta = delta + ((now-lastTime) / ns);
            lastTime = now;
			//Spawn enemys
			checkEnemySpawnTime(now);
			//Spawn coins
			checkCoinSpawnTime(now);
            while (delta >= 1 && player.isAlive())//Make sure update is only happening 60 times a second
            {
            	//Update player
				player.update(coinList, enemyList);
				Iterator<Block> updateBlocks = blockList.iterator();
				while(updateBlocks.hasNext()) {
					Block block = updateBlocks.next();
					checkAttack(block);
					if(!block.isAlive()) updateBlocks.remove();
					if(block.isExplosive() && block.isExplode()) {
						for(Block neighbourBlock : blockList) {
							if(neighbourBlock.isNeighbour(block)) {
								if(neighbourBlock.isExplosive()) {
									neighbourBlock.explode();
								} else {
									neighbourBlock.setInExplosionRange(true);
								}
							}
						}
						if(block.update(delta) == block.getMaxImages()) {
							updateBlocks.remove();
						}
					}
					if(block.isInExplosionRange()) {
						updateBlocks.remove();
					}
				}
				//Update enemies
				Iterator<Enemy> eI = enemyList.iterator();
				while(eI.hasNext()) {
					Enemy e = eI.next();
					if(!e.isAlive()) eI.remove();
					e.inNearOfExplosion(blockList);
					if(e.getX() > player.getX()) e.update(-1, blockList, DISPLAY_HEIGHT);
					if(e.getX() < player.getX()) e.update(1, blockList, DISPLAY_HEIGHT);
					if(e.isAttackedOnHead(player.getBounding())) eI.remove();
				}
				//Update coins
				Iterator<Coin> cI = coinList.iterator();
				while(cI.hasNext()) {
					cI.next().update(blockList);
				}
                delta--;
            }
            render();//displays to the screen unrestricted time
        }
    }

    private void checkAttack(Block block) {
    	Iterator<Enemy> eI = enemyList.iterator();
    	while(eI.hasNext()) {
    		Enemy e = eI.next();
    		if(block.isOnAttack(e.getBounding())) {
				e.jumpBack();
			}
		}
	}

    private void checkCoinSpawnTime(long now) {
		if((now-timeSinceLastCoin)/1000000000 >= COINSPAWNTIME) {
			coinList.add(new Coin(generateRandomXCoords(),0, getTexture("coin"), blockList, DISPLAY_HEIGHT, getCoinAnimation()));
			timeSinceLastCoin = now;
		}
	}

	private void checkEnemySpawnTime(long now) {
		if((now-timeSinceLastEnemy)/1000000000 >= ENEMYSPAWNTIME) {
			enemyList.add(new Enemy(generateEnemyXCoords(), 200, getEnemyAnimation(),blockList));
			timeSinceLastEnemy = now;
		}
	}

	private int generateEnemyXCoords() {
		lastDirection = lastDirection * -1;
		if(lastDirection == 1) {
			return DISPLAY_WIDTH + 50;
		} else {
			return -50;
		}
	}

	private int generateRandomXCoords() {
		Random rand = new Random();
		int random = rand.nextInt(DISPLAY_WIDTH);
		if(!player.getBounding().contains(random, player.getY())) {
			return random;
		}
		return generateRandomXCoords();
    }

    private BufferedImage[] getCoinAnimation() {
    	return textureLoader.getCoinAnimation();
	}

	private BufferedImage[] getEnemyAnimation() {
    	return textureLoader.getEnemyAnimation();
	}

	private BufferedImage[] generateExplosionAnimation() {
		return textureLoader.getExplosionAnimation();
	}

	private BufferedImage[] getPlayerAnimation() {
		return textureLoader.getPlayerAnimation();
	}

	private BufferedImage getTexture(String key) {
		return textureLoader.getTexture(key);
	}

    private synchronized void start() {
        running = true;
        thread.start();
    }

    public synchronized void stop() {
        running = false;
        try {
            thread.join();
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Main();
    }

}
