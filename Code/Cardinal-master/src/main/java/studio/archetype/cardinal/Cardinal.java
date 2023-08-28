package studio.archetype.cardinal;

public class Cardinal extends Entrypoint {

    public static Cardinal INSTANCE;

    public static void main(String[] args) {
        INSTANCE = new Cardinal();
    }

    @Override
    protected void onInitialize() {
    }
}
