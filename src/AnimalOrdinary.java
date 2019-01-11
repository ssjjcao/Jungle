import java.io.FileNotFoundException;

public class AnimalOrdinary extends Animal {

    public AnimalOrdinary(boolean camp, int power, int positionX, int positionY, AnimalType animalType) {
        super(camp, power, positionX, positionY, animalType);
    }

    public boolean[] judge() throws FileNotFoundException {
        boolean[] result = this.checkBoundary();

        if (result[0]) {
            result[0] = generalJudge(this.getPositionX() - 1, this.getPositionY());
        }
        if (result[1]) {
            result[1] = generalJudge(this.getPositionX(), this.getPositionY() + 1);
        }
        if (result[2]) {
            result[2] = generalJudge(this.getPositionX() + 1, this.getPositionY());
        }
        if (result[3]) {
            result[3] = generalJudge(this.getPositionX(), this.getPositionY() - 1);
        }
        return result;
    }

    private boolean generalJudge(int willX, int willY) throws FileNotFoundException {
        Board board = Board.getBoard();
        if (this.getCamp() && board.getTileArray()[willX][willY].getTileType() == Tile.TileType.values()[3])
            return false;
        else if (!this.getCamp() && board.getTileArray()[willX][willY].getTileType() == Tile.TileType.values()[5])
            return false;
        else if (board.getTileArray()[willX][willY].getTileType() == Tile.TileType.RIVER)
            return false;
        else return canAttack(board.getAnimal(willX, willY));
    }
}

