package main;

import javax.swing.JFrame;

import gui.MainFrame;

public class Main {

	public static void main(String[] args) {
		MainFrame mainFrame = new MainFrame("Alfamose Sensor Tech");
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setVisible(true);
	}
	
}
