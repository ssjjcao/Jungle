import java.io.FileNotFoundException;

public class History {
    private static History history;
    private Node originalNode;

    private History() {
        originalNode = new Node(null, null);
    }

    public static History getHistory() {
        if (history == null) history = new History();
        return history;
    }

    private class HistoryRecord {
        int AnimalFromX;
        int AnimalFromY;
        int AnimalToX;
        int AnimalToY;
        int AnimalToPower;
        boolean AnimalToCamp;
        Animal.AnimalType AnimalToType;

        HistoryRecord(Animal from, Animal to) {
            this.AnimalFromX = from.getPositionX();
            this.AnimalFromY = from.getPositionY();
            this.AnimalToX = to.getPositionX();
            this.AnimalToY = to.getPositionY();
            this.AnimalToPower = to.getPower();
            this.AnimalToCamp = to.getCamp();
            this.AnimalToType = to.getAnimalType();
        }
    }

    private class Node {
        HistoryRecord historyRecord;
        Node before;
        Node after;

        Node(HistoryRecord historyRecord, Node before) {
            this.historyRecord = historyRecord;
            this.before = before;
        }
    }

    public void addRecord(Animal from, Animal to) {
        HistoryRecord record = new HistoryRecord(from, to);
        Node newNode = new Node(record, originalNode);
        originalNode.after = newNode;
        originalNode = newNode;
    }

    public void undo() throws FileNotFoundException {
        if (originalNode == null || originalNode.historyRecord == null) {

        } else {
            Board board = Board.getBoard();
            HistoryRecord record = originalNode.historyRecord;
            board.getAnimal(record.AnimalToX, record.AnimalToY).setPositionX(record.AnimalFromX);
            board.getAnimal(record.AnimalToX, record.AnimalToY).setPositionY(record.AnimalFromY);

            if (record.AnimalToPower != -1) {
                for (int i = 0; i < 7; i++) {
                    for (int j = 0; j < 9; j++) {
                        if (board.getAnimalArray()[i][j].getCamp() == record.AnimalToCamp &&
                                board.getAnimalArray()[i][j].getAnimalType() == record.AnimalToType)
                            board.getAnimalArray()[i][j].setPower(record.AnimalToPower);
                    }
                }
            }
            originalNode = originalNode.before;
        }
    }

    public void restart() {
        history = null;
    }
}

