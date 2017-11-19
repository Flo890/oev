package oev.ioservices;

public class JobMetaData {

  private int width;
  private int height;
  private int amountFrames;

  public JobMetaData(int width, int height, int amountFrames) {
    this.width = width;
    this.height = height;
    this.amountFrames = amountFrames;
  }

  public int getWidth() {
    return width;
  }

  public int getHeight() {
    return height;
  }

  public int getAmountFrames() {
    return amountFrames;
  }
}
