package window.tree;

public class SpanString {
    private final String str;
    final int span;

    public SpanString(String str, int span) {
        this.str = str;
        this.span = span;
    }

    @Override
    public String toString() {
        return str;
    }
}
