public class FloydCell {
    public int intermediateVertex = 0;
    public double cost;

    @Override
    public String toString () {
        String costString = String.format("%1.2f", cost);
        if (cost == Double.POSITIVE_INFINITY) {
            costString = "–";
        }
        return String.format("%s/%d", costString, intermediateVertex);
    }
}
