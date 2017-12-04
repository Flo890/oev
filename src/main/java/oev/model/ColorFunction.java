package oev.model;

public enum ColorFunction {

    PHYSICAL_BRIGHTNESS(0,"physical"), HUMAN_PERCEIVED_BRIGHTNESS(1,"human perception (recommended)"), LIGHTED_BRIGHTNESS(2,"luminance");

    private final int index;
    private final String displayName;

    ColorFunction(int index, String displayName) {
        this.index = index;
        this.displayName = displayName;
    }

    public static ColorFunction getFromIndex(int index){
      for(ColorFunction cf : values()){
          if(index == cf.index){
              return cf;
          }
      }
      throw new IllegalArgumentException("no color function with index "+index+" found");
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getIndex() {
        return index;
    }

    @Override
    public String toString() {
        return getDisplayName();
    }
}
