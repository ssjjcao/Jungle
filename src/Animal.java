import java.io.FileNotFoundException;

/**
 * author: Caojiajun.
 * Used to define the property of animal and the general functions of animal.
 */
public class Animal {
    private boolean camp;
    private int power;
    private int positionX;
    private int positionY;
    private AnimalType animalType;

    Animal(boolean camp, int power, int positionX, int positionY, AnimalType animalType) {
        this.camp = camp;
        this.power = power;
        this.positionX = positionX;
        this.positionY = positionY;
        this.animalType = animalType;
    }

    /**
     * Used to check whether the animal is at the edge of the board.
     * return false: the animal will be out of boundary.
     * return true: the animal will not be out of boundary.
     */
    protected boolean[] checkBoundary() {
        boolean[] result = new boolean[4];

        if (this.getPositionX() <= 0)
            result[0] = false;
        else result[0] = true;

        if (this.getPositionY() >= 8)
            result[1] = false;
        else result[1] = true;

        if (this.getPositionX() >= 6)
            result[2] = false;
        else result[2] = true;

        if (this.getPositionY() <= 0)
            result[3] = false;
        else result[3] = true;

        return result;
    }

    /**
     * Used to set both the animals' property after fighting.
     * the dead animal's power will be set as -1, just the same as the LAND;
     * the alive animal's x-coordinate and y-coordinate will be set the same as the the dead animal's.
     */
    public void eat(Animal enemy) {
        this.setPositionX(enemy.getPositionX());
        this.setPositionY(enemy.getPositionY());
        enemy.setPower(-1);
    }

    /**
     * Used to judge whether the animal can attack the enemy.
     * the argument:(Animal enemy) is the enemy;
     * return false: the animal cannot attack the enemy;
     * return true: the animal can eat the enemy.
     */
    protected boolean canAttack(Animal enemy) throws FileNotFoundException {
        if (enemy.getPower() == -1)
            return true;
        else if (this.getCamp() == enemy.getCamp())
            return false;
        else return comparePower(enemy);
    }

    /**
     * Used to judge whether the animal is stronger than the enemy.
     * the argument:(Animal enemy) is the enemy;
     * return false: the animal is weaker than the enemy;
     * return true: the animal is stronger than the enemy.
     */
    private boolean comparePower(Animal enemy) throws FileNotFoundException {
        Board board = Board.getBoard();
        if (enemy.isTrapped())
            return true;
        else if (this.getAnimalType() == AnimalType.MOUSE && enemy.getAnimalType() == AnimalType.ELEPHANT
                && board.getTileArray()[this.getPositionX()][this.getPositionY()].getTileType() != Tile.TileType.RIVER)
            return true;
        else if (this.getAnimalType() == AnimalType.ELEPHANT && enemy.getAnimalType() == AnimalType.MOUSE)
            return false;
        else return this.getPower() >= enemy.getPower();
    }

    /**
     * Used to judge whether the animal is trapped in the enemy's trap.
     * return false: the animal is free;
     * return true: the animal is trapped.
     */
    private boolean isTrapped() throws FileNotFoundException {
        Board board = Board.getBoard();
        if (this.getCamp() && board.getTileArray()[this.getPositionX()][this.getPositionY()].getTileType() == Tile.TileType.TRAP_RIGHT)
            return true;
        else if (!this.getCamp() && board.getTileArray()[this.getPositionX()][this.getPositionY()].getTileType() == Tile.TileType.TRAP_LEFT)
            return true;
        else return false;
    }

    public boolean getCamp() {
        return camp;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public int getPositionX() {
        return positionX;
    }

    public void setPositionX(int positionX) {
        this.positionX = positionX;
    }

    public int getPositionY() {
        return positionY;
    }

    public void setPositionY(int positionY) {
        this.positionY = positionY;
    }

    public AnimalType getAnimalType() {
        return animalType;
    }

    public void setAnimalType(AnimalType animalType) {
        this.animalType = animalType;
    }

    public enum AnimalType {
        MOUSE, CAT, DOG, WOLF, LEOPARD, TIGER, LION, ELEPHANT, NONE
    }
}
