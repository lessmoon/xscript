package vm;

/**
 * Position
 */
public class Position {
    private final int frame;
    private final int offset;

    public Position(int frame, int offset) {
        this.frame = frame;
        this.offset = offset;
    }

    public int getFrame() {
        return frame;
    }

    public int getOffset() {
        return offset;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Position other = (Position) obj;
        return frame == other.frame && offset == other.offset;
    }
}