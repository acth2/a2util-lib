package fr.acth2.a2util.arguments;

public class Arguments {
    private boolean debug;

    public Arguments() {
        this.debug = false;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public boolean isDebug() {
        return debug;
    }
}
