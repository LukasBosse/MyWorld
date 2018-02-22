import entities.Background;
import entities.Block;
import entities.Player;
import gui.Toolbox;
import tools.LevelLoader;
import tools.TextureLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.List;

public class Main extends JFrame implements Runnable {

    private final String TITLE = "MYWORLD";
    private final String VERSION = "0.0.1 ALPHA";

    private final int DISPLAY_WIDTH = 1600;
    private final int DISPLAY_HEIGHT = 900;

    private Thread thread;
    private boolean running;

    private TextureLoader textureLoader;
    private LevelLoader levelLoader;

    private Background background;
    private Player player;
    private List<Block> blockList;

    //GUI
    private Toolbox toolbox;

    public Main() {
        thread = new Thread(this);
        textureLoader = new TextureLoader();
        levelLoader = new LevelLoader(textureLoader, DISPLAY_HEIGHT);
        background = new Background(0,0, getTexture("background"));
        BufferedImage toolboxImg = getTexture("toolbox");
        toolbox = new Toolbox((DISPLAY_WIDTH/2) - (toolboxImg.getWidth()/2),DISPLAY_HEIGHT - (toolboxImg.getHeight()/2) - 35, toolboxImg, false,textureLoader);
        blockList = levelLoader.getLevel();
        player = new Player(100, 440, getPlayerAnimation(), DISPLAY_WIDTH, DISPLAY_HEIGHT, toolbox, blockList, generateExplosionAnimation());
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
        //Render player
        player.render(g);
        //Render toolbox
        if(toolbox.isVisible()) {
            toolbox.render(g);
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
            while (delta >= 1)//Make sure update is only happening 60 times a second
            {
            	//Update player
				player.update(delta);
				Iterator<Block> updateBlocks = blockList.iterator();
				while(updateBlocks.hasNext()) {
					Block block = updateBlocks.next();
					if(block.isExplosive() && block.isExplode()) {
						Iterator<Block> neighbourBlocks = blockList.iterator();
						while(neighbourBlocks.hasNext()) {
							Block neighbourBlock = neighbourBlocks.next();
							if(neighbourBlock.isExplosive()) {
								if(neighbourBlock.isNeighbour(block)) {
									neighbourBlock.explode();
								}
							}
						}
						if(block.update(delta) == block.getMaxImages()) {
							updateBlocks.remove();
						}
					}
				}
                delta--;
            }
            render();//displays to the screen unrestricted time
        }
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
