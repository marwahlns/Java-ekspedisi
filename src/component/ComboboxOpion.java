package component;

public class ComboboxOpion {
    private String value;
    private String display;

    public ComboboxOpion(String value, String display) {
        this.value = value;
        this.display = display;
    }

    public String getValue() {
        return value;
    }

    public String getDisplay() {
        return display;
    }

    @Override
    public String toString() {
        return display;
    }
}
