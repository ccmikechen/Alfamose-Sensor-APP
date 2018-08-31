package test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Test {

	public static void main(String[] args) throws FileNotFoundException {
		Scanner scanner = new Scanner(new File("G:\\mask.txt"));
		for (int i = 0; i < 90; i++) {
			String line = scanner.nextLine();
			System.out.print("{");
			for (int j = 0; j < 30; j++) {
				if (j > 0) {
					System.out.print(",");
				}
				System.out.print(line.charAt(j));
			}
			System.out.println("},");
		}
	}
	
}
