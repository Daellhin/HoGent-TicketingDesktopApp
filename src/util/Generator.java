package util;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.Collection;
import java.util.Random;
import java.util.stream.Collectors;

public class Generator {

	private Random random;

	public Generator() {
		random = new Random();
	}

	// String values
	public String fullName() {
		return getRandomLineFromTheFile("files/names.txt");
	}

	public String firstName() {
		return getRandomLineFromTheFile("files/names.txt").split(" ")[0];
	}

	public String lastName() {
		return getRandomLineFromTheFile("files/names.txt").split(" ")[1];
	}

	public String telephone() {
		return getRandomLineFromTheFile("files/telephones.txt");
	}

	public String company() {
		return getRandomLineFromTheFile("files/companies.txt");
	}

	// randoms
	public <T> T randomElement(Collection<T> collection) {
		return collection.stream().skip((int) (collection.size() * random.nextDouble())).findFirst().orElseThrow();
	}

	public <T> T randomElement(Collection<T> collection, double bias, T biasedElement) {
		if (random.nextDouble() < bias) {
			return biasedElement;
		}
		return collection.stream().skip((int) (collection.size() * random.nextDouble())).findFirst().orElseThrow();
	}

	public <T> Collection<T> randomElements(Collection<T> collection) {
		return collection.stream().skip((int) (collection.size() * random.nextDouble())).collect(Collectors.toList());
	}

	public boolean random(double bound) {
		return random.nextDouble() < bound;
	}

	public int randomInt(int lowerBound, int upperBound) {
		return random.nextInt(upperBound - lowerBound) + lowerBound;
	}

	// private
	private String getRandomLineFromTheFile(String name) {
		try {
			File file = new File(this.getClass().getResource(name).toURI());
			final RandomAccessFile f = new RandomAccessFile(file, "r");
			final long randomLocation = (long) (Math.random() * f.length());
			f.seek(randomLocation);
			f.readLine();
			String randomLine = f.readLine();
			f.close();
			return randomLine;
		} catch (Exception e) {
			System.out.println("Error while reading file " + name);
			e.printStackTrace();
		}
		return "Error error";
	}

}
