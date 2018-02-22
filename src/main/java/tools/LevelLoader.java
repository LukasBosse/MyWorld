package tools;

import entities.Block;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class LevelLoader {

    private final String PATH = "res";

    private List<Block> blockList = new ArrayList<Block>();
    private TextureLoader textureLoader;

    private int DISPLAY_HEIGHT = 0;

    public LevelLoader(TextureLoader textureLoader, int DISPLAY_HEIGHT) {
        this.textureLoader = textureLoader;
        this.DISPLAY_HEIGHT = DISPLAY_HEIGHT;
    }

    public List<Block> getLevel() {

        BufferedReader br = null;
		List<String> tmp = new ArrayList<String>();

        try {
            br = new BufferedReader(new FileReader(PATH + "/" + "map.txt"));
            String line;
            int lineCounter = 0;
            while ((line = br.readLine()) != null) {
				tmp.add(line);
            }
            for(int i = tmp.size()-1; i>=0; i--) {
				generateBlock(tmp.get(i), lineCounter);
				lineCounter++;
			}
        } catch (Exception e) {
            e.printStackTrace();
        }

        return blockList;

    }

    private void generateBlock(String line, int lineCounter) {
        for(int i = 0; i < line.length(); i++) {

            BufferedImage img;

            switch(line.charAt(i)) {
                case '0': {
                    img = textureLoader.getTexture("Grass");
                    break;
                }
                case '1': {
                	img = textureLoader.getTexture("Coat");
                    break;
                }
                default: {
                    continue;
                }
            }
            if(i == 0) {
                blockList.add(new Block(i, calcYPos(lineCounter, img.getHeight()), img, false, null));
            } else {
                Block lastItem = blockList.get(blockList.size() - 1);
                blockList.add(new Block(lastItem.x + lastItem.img.getWidth(), calcYPos(lineCounter, img.getHeight()), img, false, null));
            }
        }
    }

    private int calcYPos(int lineCounter, int blockHeight) {
        return (DISPLAY_HEIGHT - blockHeight) - (lineCounter * 80);
    }

}
