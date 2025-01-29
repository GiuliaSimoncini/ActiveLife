package project;

import project.operations.ToDoVisitor;

public class GamingSession extends ToDo {
    private final int duration;
    private Boolean multiplayer;
    private String platform;

    public GamingSession(String title, int duration) {
        super(title);
        if (duration <= 0)
            throw new IllegalArgumentException("Duration can not be a negative number or 0");
        this.duration = duration;
    }

    public Boolean getMultiplayer() {
        return this.multiplayer;
    }

    public String getPlatform() {
        return this.platform;
    }

    public GamingSession multiplayer(Boolean multiplayer) {
        if (multiplayer == null)
            throw new IllegalArgumentException("The multiplayer value can not be null");
        this.multiplayer = multiplayer;
        return this;
    }

    public GamingSession platform(String platform) {
        if (platform == null)
            throw new IllegalArgumentException("The name of a platform can not be null");
        if (platform.isEmpty())
            throw new IllegalArgumentException("The name of a platform can not be empty");
        this.platform = platform;
        return this;
    }

    @Override
    public int getDuration() {
        return duration;
    }

    @Override
    public int getPriority() {
        return 0;
    }

    @Override
    public void accept(ToDoVisitor visitor) {
        visitor.visitGamingSession(this);
    }
}

