package tools;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class TextureLoader {

    private HashMap<String, BufferedImage> textures = new HashMap<String, BufferedImage>();

    public TextureLoader(String path) {
    	initTextures(path);
    }

    private void initTextures(String path) {
		File folder = new File(path);
		File[] listOfFiles = folder.listFiles();
		for (File file : listOfFiles) {
			if (file.isFile()) {
				textures.put(file.getName().replace(".png","").replace(".PNG",""), loadTexture(file.getAbsolutePath()));
			} else if(file.isDirectory()) {
				initTextures(file.getAbsolutePath());
			}
		}
	}

	public int getTextureID(String imgName) {
		if(imgName.equals("Grass")) return 0;
		if(imgName.equals("Dirt")) return 1;
		if(imgName.equals("Coat")) return 2;
		if(imgName.equals("Tnt")) return 3;
		if(imgName.equals("Gold")) return 4;
		if(imgName.equals("Leaves")) return 5;
		if(imgName.equals("Redstone")) return 6;
		if(imgName.equals("Stone")) return 7;
		if(imgName.equals("Wood")) return 8;
		return 0;
	}

	public String getTexture(char i) {
		if(i == '0') return "Grass";
		if(i == '1') return "Dirt";
		if(i == '2') return "Coat";
		if(i == '3') return "Tnt";
		if(i == '4') return "Gold";
		if(i == '5') return "Leaves";
		if(i == '6') return "Redstone";
		if(i == '7') return "Stone";
		if(i == '8') return "Wood";
		return "Grass";
	}

    public BufferedImage getTexture(String key) {
        return textures.get(key);
    }

    private BufferedImage loadTexture(String path) {
        try {
            return ImageIO.read(new File(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public BufferedImage[] getPlayerAnimation() {
		return new BufferedImage[] {
				getTexture("player_0"),
				getTexture("player_1"),
				getTexture("player_2"),
				getTexture("player_3"),
				getTexture("player_4"),
				getTexture("player_5"),
				getTexture("player_6"),
				getTexture("player_7"),
				getTexture("player_8"),
				getTexture("player_9")
		};
	}

    public BufferedImage[] getExplosionAnimation() {
		return new BufferedImage[] {
				getTexture("explosion0"),
				getTexture("explosion1"),
				getTexture("explosion2"),
				getTexture("explosion3"),
				getTexture("explosion4"),
				getTexture("explosion5"),
				getTexture("explosion6"),
				getTexture("explosion7"),
				getTexture("explosion8"),
				getTexture("explosion9"),
				getTexture("explosion10"),
				getTexture("explosion11")
		};
	}

	public BufferedImage[] getEnemyAnimation() {
		return new BufferedImage[] {
				getTexture("enemy_0"),
				getTexture("enemy_1"),
				getTexture("enemy_2"),
				getTexture("enemy_3"),
				getTexture("enemy_4"),
				getTexture("enemy_5")
		};
	}
}
