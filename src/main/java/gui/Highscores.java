package gui;

import entities.Entity;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class Highscores extends Entity {

	private List<String> scores;
	private boolean visible;
	private BufferedImage btn_close;
	private Rectangle btnBounding;

	public Highscores(int x, int y, BufferedImage img, BufferedImage btn_close) {
		super(x, y, img);
		this.btn_close = btn_close;
		this.btnBounding = new Rectangle(x + getWidth() - btn_close.getWidth(), y, btn_close.getWidth(), btn_close.getHeight());
	}

	public void updateScores(List<String> scores) {
		this.scores = scores;
	}

	public boolean isVisible() {
		return visible;
	}

	public List<String> readHighscores() {
		BufferedReader br = null;
		List<String> highscores = new ArrayList<String>();
		try {
			br = new BufferedReader(new FileReader("res/Stats/Highscores.txt"));
			String line;
			while ((line = br.readLine()) != null) {
				highscores.add(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return highscores;
	}

	public void setVisible() {
		this.visible = !this.visible;
		if(this.visible) {
			updateScores(readHighscores());
		}
	}

	public void isClosing(int x, int y) {
		if(btnBounding.contains(x,y)) {
			setVisible();
		}
	}

	@Override
	public void render(Graphics g) {
		g.drawImage(img, x, y, null);
		g.drawImage(btn_close, (int) btnBounding.getX(), (int) btnBounding.getY(), null);
		if(scores != null) {
			for (int i = 0; i < scores.size(); i++) {
				g.setColor(Color.WHITE);
				if(i == 0) {
					g.drawString(scores.get(i), x + 10, y + 20 + (20 * i));
				} else {
					g.drawString(scores.get(i), x + 10, y + 20 + (30 * i));
				}
			}
		}
	}

}
