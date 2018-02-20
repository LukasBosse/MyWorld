package tools;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class TextureLoader {

    private final String PATH = "res";
    private HashMap<String, BufferedImage> textures = new HashMap<String, BufferedImage>();
    private final String[] texturePaths = {
            "background",
            "Grass",
            "player",
            "player_walkA",
            "player_walkB",
			"player_walkC",
			"player_walkD",
            "toolbox",
            "Grass_Icon",
            "Gold_Icon",
			"Tnt_Icon",
			"Redstone_Icon",
			"Coat_Icon",
			"Stone_Icon",
			"Leaves_Icon",
			"Wood_Icon",
			"Gold",
			"Tnt",
			"Redstone",
			"Coat",
			"Stone",
			"Leaves",
			"Wood",
			"explosion0",
			"explosion1",
			"explosion2",
			"explosion3",
			"explosion4",
			"explosion5",
			"explosion6",
			"explosion7",
			"explosion8",
			"explosion9",
			"explosion10",
			"explosion11"
    };

    public TextureLoader() {
        for(String s : texturePaths) {
            textures.put(s, loadTexture(PATH + "/" + s + ".png"));
        }
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

}
