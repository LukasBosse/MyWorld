package tools;

import entities.Block;

import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class LevelLoader {

    private final String PATH = "res/Maps";

    private List<Block> blockList = new ArrayList<Block>();
    private TextureLoader textureLoader;

    private int DISPLAY_HEIGHT;

    public LevelLoader(TextureLoader textureLoader, int DISPLAY_HEIGHT) {
        this.textureLoader = textureLoader;
        this.DISPLAY_HEIGHT = DISPLAY_HEIGHT;
    }

    public List<Block> getLevel(int level) {

        BufferedReader br = null;
		List<String> tmp = new ArrayList<String>();

        try {
            br = new BufferedReader(new FileReader(PATH + "/" + "map"+ level + ".txt"));
            String line;
            int lineCounter = 0;
            while ((line = br.readLine()) != null) {
				tmp.add(line.replace(",","").replace(";",""));
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

    public void saveToFile(int level, List<Block> blockList) {
    	int oldY = 0;
		int x = 0;
    	Collections.sort(blockList, new Comparator<Block>() {
			public int compare(Block o1, Block o2) {
				Integer y1 = o1.getY();
				return y1.compareTo(o2.getY());
			}
		});

		PrintWriter out = null;

		try {

			FileWriter fw = new FileWriter(PATH + "/" + "map" + level + ".txt", true);
			BufferedWriter bw = new BufferedWriter(fw);
			out = new PrintWriter(bw);

			for(Block block : blockList) {
				int y = decalcYPos(block.getY(), block.getHeight());
				int textureType = textureLoader.getTextureID(block.getImageName());
				if(y != oldY) {
					oldY = y;
					x = 0;
					out.print(";\n" + textureType);
				} else {
					out.print("," + textureType);
					x++;
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			out.close();
		}
	}

    private void generateBlock(String line, int lineCounter) {
        for(int i = 0; i < line.length(); i++) {
            char mapItem = line.charAt(i);
            String imgName = textureLoader.getTexture(mapItem);
			BufferedImage img = textureLoader.getTexture(imgName);

            if(i == 0) {
                blockList.add(new Block(i, calcYPos(lineCounter, img.getHeight()), img, false, null, mapItem=='0',textureLoader.getTexture("Dirt"),imgName));
            } else {
                Block lastItem = blockList.get(blockList.size() - 1);
                blockList.add(new Block(lastItem.x + lastItem.img.getWidth(), calcYPos(lineCounter, img.getHeight()), img, false, null, mapItem=='0', textureLoader.getTexture("Dirt"),imgName));
            }
        }
    }

    private int decalcYPos(int y, int blockHeight) {
    	return -(y/blockHeight) + (DISPLAY_HEIGHT-blockHeight);
	}

    private int calcYPos(int lineCounter, int blockHeight) {
        return (DISPLAY_HEIGHT - blockHeight) - (lineCounter * blockHeight);
    }

}
