package oev.model;

public enum SumAlgo {

    KEEP_BRIGHTEST("keep brighter pixel"), SUM_RGB("sum up colors");

    private final String title;

    SumAlgo(String title){
        this.title = title;
    }

    @Override
    public String toString() {
        return title;
    }
}
