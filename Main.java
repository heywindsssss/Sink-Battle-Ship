import java.util.*;
// focus on what we have instead of procedure
// now focus on 2D DotCom game. -> Almost done just ensure that each dot com engages only unique cells

class DotCom {
	private Random random = new Random();
	private ArrayList<String> locationCells;
	private int noOfGuesses = 0;
	private ArrayList<Character> possibleCharacterLocations;
	private String name;
	private Boolean alive;

	public DotCom(String name) {
		this.random = new Random();
		this.locationCells = new ArrayList<String>();
		this.possibleCharacterLocations = new ArrayList<Character>();
		populateCharacterLocations();
		this.name = name;
	}

	private void populateCharacterLocations() {
		for (char c = 'A'; c <= 'G'; c++) {
			possibleCharacterLocations.add(c);
		}
	}

	public String getName() {
		return name;
	}

	public String chooseOrientation() {
		int randInt = random.nextInt(2);
		String orientation;
		if(randInt == 0) {
			orientation = "HORIZONTAL";
			return orientation;
		}
		else {
			orientation = "VERTICAL";
			return orientation;
		}
	}

	public void populateArray() {
		locationCells.clear();
		this.locationCells = new ArrayList<String>();
		String orientation = chooseOrientation();
		if (orientation.equals("HORIZONTAL")) {
			int randomNumber = random.nextInt(7);
			char startingChar = possibleCharacterLocations.get(randomNumber);
			int randomInteger = random.nextInt(5);
			locationCells.add("" + startingChar + (randomInteger));
			locationCells.add("" + startingChar + (randomInteger + 1));
			locationCells.add("" + startingChar + (randomInteger + 2));
		} else {
			int randomNumber = random.nextInt(5);
			char firstChar = possibleCharacterLocations.get(randomNumber);
			char secondChar = possibleCharacterLocations.get(randomNumber + 1);
			char thirdChar = possibleCharacterLocations.get(randomNumber + 2);
			int randomInteger = random.nextInt(7);
			locationCells.add("" + firstChar + randomInteger);
			locationCells.add("" + secondChar + randomInteger);
			locationCells.add("" + thirdChar + randomInteger);
		}
	}

	public ArrayList<String> getLocationCells() {
		return locationCells;
	}

	public String checkYourself(String userGuess) {
		String result = "MISS";
		noOfGuesses++;
		if(locationCells.contains(userGuess)) {
			locationCells.remove(userGuess);
			result = "HIT";
			if(locationCells.isEmpty()) {
				alive = false;
				result = "KILL";
			}
		}

		if(userGuess.equals("HELP")) {
			return userGuess;
		}

		return result;
	}

	public int getNoOfGuesses() {
		return noOfGuesses;
	}

	public boolean isAlive() {
		return alive;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("DotCom [");
		sb.append("Name: ").append(name);
		sb.append(", Locations: ").append(locationCells);
		sb.append("]");
		return sb.toString();
	}
}

class InputHelper {
	Scanner scanner = new Scanner(System.in);
	String userInput;

	public String getUserInput(String text) {
		boolean isValidInput = false;
		do {
			System.out.println(text);
			userInput = scanner.nextLine().toUpperCase();

			if (userInput.length() == 2) {
				char rowChar = userInput.charAt(0);
				char colChar = userInput.charAt(1);
				if (rowChar >= 'A' || rowChar <= 'G' && colChar >= '0' || colChar <= '6') {
					isValidInput = true;
				}
				else {
					System.out.println("Input must be in format A0 - G6.");
				}
			}
			else if(userInput.length() == 4) {
				isValidInput = true;
			}
			else {
				System.out.println("Input must be two characters (e.g., A3).");
			}
		} while (!isValidInput); // Keep looping until isValidInput is true
		return userInput;
	}
}

class DotComBust {
	private ArrayList<DotCom> arrayOfAllDotComs = new ArrayList<>();
	private InputHelper inputHelper = new InputHelper();
	private int totalGuesses = 0;

	public void setUpGame() {
		String[] names = {"Pet","AskMe","Go2"};
		for (String name : names) {
			DotCom dotCom = new DotCom(name);
			dotCom.populateArray();
			arrayOfAllDotComs.add(dotCom);
		}
		System.out.println("Your goal is to sink 3 dot coms: Go, Pet, AskMe.");
		System.out.println("Try to sink them in the fewest number of guesses.");
		System.out.println("Give up ??? Type \"HELP\" ");
	}

	public void startPlaying() {
		while (!arrayOfAllDotComs.isEmpty()) {
			String userGuess = inputHelper.getUserInput("Enter a guess: ");
			checkUserGuess(userGuess);
		}
		finishGame();
	}

	private ArrayList<DotCom> getArrayOfAllDotComArray() {
		return arrayOfAllDotComs;
	}

	public void printAns () {
		ArrayList<DotCom> dotComs = new ArrayList();
		dotComs =  getArrayOfAllDotComArray();
		for (DotCom dotCom : dotComs) {
			System.out.println(dotCom);
		}
	}

	private void checkUserGuess(String userGuess) {
		totalGuesses++;
		String result = "MISS";
		for(DotCom dotcom : arrayOfAllDotComs) {
			result = dotcom.checkYourself(userGuess);
			if(!result.equals("MISS")) {
				if (result.equals("KILL")) {
					System.out.println(dotcom.getName() + ".com is killed!");
					arrayOfAllDotComs.remove(dotcom);
				}
				else if (result.equals("HELP")) {
					printAns();
				}
				else {
					System.out.println("HIT");
				}
				break;
			}
		}

		if(result.equals("HELP")) {

		}

		if (result.equals("MISS")) {
			System.out.println("MISS");
		}
	}

	private void finishGame() {
		System.out.println("All dot coms are dead! You used " + totalGuesses + " guesses.");
		if (totalGuesses <= 18) {
			System.out.println("It only took you " + totalGuesses +  " guesses");
			System.out.println("You got out before your options sank");
		} else {
			System.out.println("Took you long enough. " + totalGuesses + " guesses");
			System.out.println("Fish are dancing with your options");
		}
	}
}

public class Main
{
	public static void main(String[] args) {
		DotComBust game = new DotComBust();
		game.setUpGame();
		game.startPlaying();
	}
}