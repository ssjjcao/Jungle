import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * author: Caojiajun.
 * Used to provide a logical board for GUI.
 */
public class Board {
    private Tile[][] tileArray = new Tile[7][9];
    private Animal[][] animalArray = new Animal[7][9];
    private static Board board = null;

    private Board(File tile, File animals) throws FileNotFoundException {
        try {
            initTile(tile);
            initAnimals(animals);
        } catch (FileNotFoundException exception) {
            System.out.println("Cannot Find Files!");
        }
    }

    public static Board getBoard() throws FileNotFoundException {
        if (board == null) board = new Board(new File("data/tile.txt"), new File("data/animals.txt"));
        return board;
    }

    private void initTile(File tile) throws FileNotFoundException {
        Scanner scanner = new Scanner(tile);
        for (int r = 0; r < 7; r++) {
            String row = scanner.nextLine();
            for (int c = 0; c < 9; c++) {
                tileArray[r][c] = new Tile(r, c, Tile.TileType.values()[row.charAt(c) - '0']);
            }
        }
    }

    private void initAnimals(File animals) throws FileNotFoundException {
        Scanner scanner = new Scanner(animals);
        for (int r = 0; r < 7; r++) {
            String row = scanner.nextLine();
            for (int c = 0; c < 9; c++) {
                if (row.charAt(c) >= 65 && row.charAt(c) <= 72)
                    animalArray[r][c] = new Animal(true, row.charAt(c) - 64, r, c, Animal.AnimalType.values()[row.charAt(c) - 65]);
                else if (row.charAt(c) >= 97 && row.charAt(c) <= 104)
                    animalArray[r][c] = new Animal(false, row.charAt(c) - 96, r, c, Animal.AnimalType.values()[row.charAt(c) - 97]);
                else animalArray[r][c] = new Animal(true, -1, r, c, Animal.AnimalType.NONE);
            }
        }
    }

    public boolean isWin(Animal animal) throws FileNotFoundException {
        if (isInEnemyHome(animal))
            return true;
        else if (isAceTheEnemy(animal))
            return true;
        else return isAliveEnemyCannotMove(animal);
    }

    public boolean isInEnemyHome(Animal animal) {
        if (animal.getCamp() && board.getTileArray()[animal.getPositionX()][animal.getPositionY()].getTileType() == Tile.TileType.HOME_RIGHT)
            return true;
        else if (!animal.getCamp() && board.getTileArray()[animal.getPositionX()][animal.getPositionY()].getTileType() == Tile.TileType.HOME_LEFT)
            return true;
        else return false;
    }

    public boolean isAceTheEnemy(Animal animal) throws FileNotFoundException {
        if (animal.getCamp())
            return countRight() == 0;
        else return countLeft() == 0;
    }

    public boolean isAliveEnemyCannotMove(Animal animal) throws FileNotFoundException {
        int countAliveEnemy;
        if (animal.getCamp())
            countAliveEnemy = countRight();
        else countAliveEnemy = countLeft();

        int countCannotMove = 0;
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 9; j++) {
                if (board.getAnimal(i, j).getCamp() != animal.getCamp() && board.getAnimal(i, j).getPower() != -1) {
                    if (board.getAnimal(i, j).getAnimalType() == Animal.AnimalType.MOUSE) {
                        AnimalCanSwim animalCanSwim = new AnimalCanSwim(board.getAnimal(i, j).getCamp(),
                                board.getAnimal(i, j).getPower(), i, j, board.getAnimal(i, j).getAnimalType());
                        if (!animalCanSwim.judge()[0] && !animalCanSwim.judge()[1] && !animalCanSwim.judge()[2] && !animalCanSwim.judge()[3])
                            countCannotMove++;
                    } else if (board.getAnimal(i, j).getAnimalType() == Animal.AnimalType.TIGER ||
                            board.getAnimal(i, j).getAnimalType() == Animal.AnimalType.LION) {
                        AnimalCanJump animalCanJump = new AnimalCanJump(board.getAnimal(i, j).getCamp(),
                                board.getAnimal(i, j).getPower(), i, j, board.getAnimal(i, j).getAnimalType());
                        if (!animalCanJump.judge()[0] && !animalCanJump.judge()[1] && !animalCanJump.judge()[2] && !animalCanJump.judge()[3])
                            countCannotMove++;
                    } else {
                        AnimalOrdinary animalOrdinary = new AnimalOrdinary(board.getAnimal(i, j).getCamp(),
                                board.getAnimal(i, j).getPower(), i, j, board.getAnimal(i, j).getAnimalType());
                        if (!animalOrdinary.judge()[0] && !animalOrdinary.judge()[1] && !animalOrdinary.judge()[2] && !animalOrdinary.judge()[3])
                            countCannotMove++;
                    }
                }
            }
        }
        return countAliveEnemy == countCannotMove;
    }

    public int countLeft() throws FileNotFoundException {
        int countLeftAlive = 8;

        Scanner scanner = new Scanner(new File("data/animals.txt"));
        for (int r = 0; r < 7; r++) {
            String row = scanner.nextLine();
            for (int c = 0; c < 9; c++) {
                if (row.charAt(c) >= 65 && row.charAt(c) <= 72 && board.getAnimalArray()[r][c].getPower() == -1)
                    countLeftAlive--;
            }
        }
        return countLeftAlive;
    }

    public int countRight() throws FileNotFoundException {
        int countRightAlive = 8;

        Scanner scanner = new Scanner(new File("data/animals.txt"));
        for (int r = 0; r < 7; r++) {
            String row = scanner.nextLine();
            for (int c = 0; c < 9; c++)
                if (row.charAt(c) >= 97 && row.charAt(c) <= 104 && board.getAnimalArray()[r][c].getPower() == -1)
                    countRightAlive--;
        }
        return countRightAlive;
    }

    public static void setBoard(Board board) {
        Board.board = board;
    }

    public Tile[][] getTileArray() {
        return tileArray;
    }

    public Animal getAnimal(int r, int c) throws FileNotFoundException {
        Board board = Board.getBoard();
        Animal[][] index = board.getAnimalArray();
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 9; j++) {
                if (index[i][j].getPower() != -1 && index[i][j].getPositionX() == r && index[i][j].getPositionY() == c)
                    return index[i][j];
            }
        }
        return new Animal(true, -1, r, c, Animal.AnimalType.NONE);
    }

    public Animal[][] getAnimalArray() {
        return animalArray;
    }
}

