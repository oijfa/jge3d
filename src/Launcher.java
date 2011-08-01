import launcher.JarClassLoader;

public class Launcher {
    public static void main(String[] args) {
        JarClassLoader jcl = new JarClassLoader();
        try {
            jcl.invokeMain("test.Main", args);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}