package gui;

import java.awt.Component;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class PlayerResource {

	private static Map<String, Image> images;
	
	public static void load(Component c) {
		images = new HashMap<String, Image>();
		try {
			images.put("play", loadImage("res/play.png"));
			images.put("play-rollover", loadImage("res/play-rollover.png"));
			images.put("play-selected", loadImage("res/play-selected.png"));
			images.put("pause", loadImage("res/pause.png"));
			images.put("pause-rollover", loadImage("res/pause-rollover.png"));
			images.put("pause-selected", loadImage("res/pause-selected.png"));
			images.put("fast-forward", loadImage("res/fast-forward.png"));
			images.put("fast-forward-rollover", loadImage("res/fast-forward-rollover.png"));
			images.put("fast-forward-selected", loadImage("res/fast-forward-selected.png"));
			images.put("fast-back", loadImage("res/fast-back.png"));
			images.put("fast-back-rollover", loadImage("res/fast-back-rollover.png"));
			images.put("fast-back-selected", loadImage("res/fast-back-selected.png"));
			images.put("forward", loadImage("res/forward.png"));
			images.put("forward-rollover", loadImage("res/forward-rollover.png"));
			images.put("forward-selected", loadImage("res/forward-selected.png"));
			images.put("rewind", loadImage("res/rewind.png"));
			images.put("rewind-rollover", loadImage("res/rewind-rollover.png"));
			images.put("rewind-selected", loadImage("res/rewind-selected.png"));
			images.put("folder", loadImage("res/folder.png"));
			images.put("folder-rollover", loadImage("res/folder-rollover.png"));
			images.put("folder-selected", loadImage("res/folder-selected.png"));
			images.put("left-arrow", loadImage("res/left-arrow.png"));
			images.put("left-arrow-rollover", loadImage("res/left-arrow-rollover.png"));
			images.put("left-arrow-selected", loadImage("res/left-arrow-selected.png"));
		} catch (IOException e) {
			JOptionPane.showMessageDialog(
					c, 
					"Player image not found in res/\n" 
					+ e.getMessage());
			System.exit(0);
		}
		System.out.println("Loaded player icons");
	}
	
	private static Image loadImage(String path) throws IOException {
		return ImageIO.read(new File(path)).getScaledInstance(50, 50, Image.SCALE_SMOOTH);
	}
	
	public static Image getImage(String getter) {
		return images.get(getter);
	}
	
	public static ImageIcon getIcon(String getter) {
		return new ImageIcon(images.get(getter));
	}
}
