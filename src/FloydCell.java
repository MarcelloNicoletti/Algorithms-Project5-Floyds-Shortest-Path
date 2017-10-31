public class FloydCell {
    public int next;
    public double cost;

    public FloydCell (Double initialCost) {
        this.cost = initialCost;
        this.next = 0;
    }

    @Override
    public String toString () {
        String costString = String.format("%1.2f", cost);
        if (cost == Double.POSITIVE_INFINITY) {
            costString = "–";
        }
        return String.format("%s/%d", costString, next);
    }
}
