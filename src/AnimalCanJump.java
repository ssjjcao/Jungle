import java.io.FileNotFoundException;

public class AnimalCanJump extends Animal {

    public AnimalCanJump(boolean camp, int power, int positionX, int positionY, AnimalType animalType) {
        super(camp, power, positionX, positionY, animalType);
    }

    public boolean[] judge() throws FileNotFoundException {
        boolean[] result = this.checkBoundary();

        if (result[0]) {
            result[0] = generalJudge(this.getPositionX(), this.getPositionY(), this.getPositionX() - 1, this.getPositionY());
        }
        if (result[1]) {
            result[1] = generalJudge(this.getPositionX(), this.getPositionY(), this.getPositionX(), this.getPositionY() + 1);
        }
        if (result[2]) {
            result[2] = generalJudge(this.getPositionX(), this.getPositionY(), this.getPositionX() + 1, this.getPositionY());
        }
        if (result[3]) {
            result[3] = generalJudge(this.getPositionX(), this.getPositionY(), this.getPositionX(), this.getPositionY() - 1);
        }
        return result;
    }

    private boolean generalJudge(int nowX, int nowY, int willX, int willY) throws FileNotFoundException {
        Board board = Board.getBoard();

        if (this.getCamp() && board.getTileArray()[willX][willY].getTileType() == Tile.TileType.values()[3])
            return false;
        else if (!this.getCamp() && board.getTileArray()[willX][willY].getTileType() == Tile.TileType.values()[5])
            return false;
        else if (board.getTileArray()[willX][willY].getTileType() == Tile.TileType.RIVER) {
            if (willX - nowX == -1) {
                if ((board.getAnimal(willX, willY).getPower() != -1 && board.getAnimal(willX, willY).getCamp() != this.getCamp()) ||
                        (board.getAnimal(willX - 1, willY).getPower() != -1 && board.getAnimal(willX - 1, willY).getCamp() != this.getCamp()))
                    return false;
                else return canAttack(board.getAnimal(willX - 2, willY));
            } else if (willX - nowX == 1) {
                if ((board.getAnimal(willX, willY).getPower() != -1 && board.getAnimal(willX, willY).getCamp() != this.getCamp()) ||
                        (board.getAnimal(willX + 1, willY).getPower() != -1 && board.getAnimal(willX + 1, willY).getCamp() != this.getCamp()))
                    return false;
                else return canAttack(board.getAnimal(willX + 2, willY));
            } else if (willY - nowY == -1) {
                if ((board.getAnimal(willX, willY).getPower() != -1 && board.getAnimal(willX, willY).getCamp() != this.getCamp()) ||
                        (board.getAnimal(willX, willY - 1).getPower() != -1 && board.getAnimal(willX, willY - 1).getCamp() != this.getCamp()) ||
                        (board.getAnimal(willX, willY - 2).getPower() != -1 && board.getAnimal(willX, willY - 2).getCamp() != this.getCamp()))
                    return false;
                else return canAttack(board.getAnimal(willX, willY - 3));
            } else {
                if ((board.getAnimal(willX, willY).getPower() != -1 && board.getAnimal(willX, willY).getCamp() != this.getCamp()) ||
                        (board.getAnimal(willX, willY + 1).getPower() != -1 && board.getAnimal(willX, willY + 1).getCamp() != this.getCamp()) ||
                        (board.getAnimal(willX, willY + 2).getPower() != -1 && board.getAnimal(willX, willY + 2).getCamp() != this.getCamp()))
                    return false;
                else return canAttack(board.getAnimal(willX, willY + 3));
            }
        } else return canAttack(board.getAnimal(willX, willY));
    }
}
