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
            "toolbox",
            "Grass_Icon",
            "Gold_Icon"
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
