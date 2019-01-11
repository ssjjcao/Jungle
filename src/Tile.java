public class Tile {
    private final int positionX;
    private final int positionY;
    private final TileType tileType;

    public Tile(int x, int y, TileType tileType) {
        this.positionX = x;
        this.positionY = y;
        this.tileType = tileType;
    }

    public enum TileType {
        LAND, RIVER, TRAP_LEFT, HOME_LEFT, TRAP_RIGHT, HOME_RIGHT
    }

    public TileType getTileType() {
        return tileType;
    }
}

