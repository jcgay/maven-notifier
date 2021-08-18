package fr.jcgay.maven.notifier;

public class Mvnd {

    private Mvnd() {
        // Hide me, I'm famous! 😎
    }

    public static boolean isRunningWithMvnd() {
        return System.getProperties().keySet().stream().anyMatch(key -> key.toString().startsWith("mvnd."));
    }
}
