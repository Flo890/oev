package oev.model;

public enum Mode {

  SUMMIMAGE("Lightpainting Image","sumImage"), SUMVIDEO("Lightpainting Video","sumVideo"), TRAILVIDEO("Lighttrailing Video","trailVideo");

    private final String displayName;

  private final String actionCommand;

    Mode(String displayName, String actionCommand) {
        this.displayName = displayName;
        this.actionCommand = actionCommand;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getActionCommand() {
        return actionCommand;
    }
}
