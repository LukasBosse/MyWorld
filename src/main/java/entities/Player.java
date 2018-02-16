package entities;

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

    public boolean left, right, destroy;
    public boolean click_Left, click_Right;

    private final int MOVE_SPEED = 1;
    private final int ALLOWED_DISTANCE = 2;

    int mouseX = 0;
    int mouseY = 0;

    private Toolbox toolbox;
    private List<Block> blockList;

    public Player(int x, int y, BufferedImage img, int DISPLAY_WIDTH, Toolbox toolbox, List<Block> blockList) {
        super(x, y, img);
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
        }
        if(right) {
            this.x += MOVE_SPEED;
            if((this.x + width) > DISPLAY_WIDTH) {
                right = false;
                int delta = (this.x + width) - DISPLAY_WIDTH;
                this.x -= delta;
            }
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
            //TODO: IMPLEMENT BUILDING FUNCTION
        }

    }

    public void keyTyped(KeyEvent e) { }

    public void keyPressed(KeyEvent key) {
        if((key.getKeyCode() == KeyEvent.VK_LEFT) || (key.getKeyCode() == KeyEvent.VK_A))
            left = true;
        if((key.getKeyCode() == KeyEvent.VK_RIGHT) || (key.getKeyCode() == KeyEvent.VK_D))
            right = true;
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
        if((key.getKeyCode() == KeyEvent.VK_RIGHT) || (key.getKeyCode() == KeyEvent.VK_D))
            right = false;
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
