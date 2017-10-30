public class FloydCell {
    public int intermediateVertex;
    public double cost;

    public FloydCell (Double initialCost) {
        this.cost = initialCost;
        this.intermediateVertex = 0;
    }

    @Override
    public String toString () {
        String costString = String.format("%1.2f", cost);
        if (cost == Double.POSITIVE_INFINITY) {
            costString = "–";
        }
        return String.format("%s/%d", costString, intermediateVertex);
    }
}
