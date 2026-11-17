package modelo;
public class InfiniteSnakeModel extends SnakeModel {
    public InfiniteSnakeModel(int numCasillas) {
        super(numCasillas);
    }

    @Override
    public void move() {
        for (int i = getBodyParts(); i > 0; i--) {
            getX()[i] = getX()[i - 1];
            getY()[i] = getY()[i - 1];
        }
        switch (getDirection()) {
            case 'U' -> getY()[0] -= 1;
            case 'D' -> getY()[0] += 1;
            case 'L' -> getX()[0] -= 1;
            case 'R' -> getX()[0] += 1;
        }
        // Teletransporte por los bordes
        int cols = getNumCols();
        int rows = getNumRows();
        if (getX()[0] < 0) getX()[0] = cols - 1;
        if (getX()[0] >= cols) getX()[0] = 0;
        if (getY()[0] < 0) getY()[0] = rows - 1;
        if (getY()[0] >= rows) getY()[0] = 0;

        
        if (getBodyParts() < getX().length && getPendingGrowth() > 0) {
            setBodyParts(getBodyParts() + 1);
            setPendingGrowth(getPendingGrowth() - 1);
        }
    }
}