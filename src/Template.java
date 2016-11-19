/**
 * Created by Kuryakov on 19-Nov-16.
 */
public class Template {
    private String name;
    private int elementSize;
    private int size;

    public String getName() {
        return name;
    }

    public int getElementSize() {
        return elementSize;
    }

    public int getSize() {
        return size;
    }

    public Template(String name, int elementSize, int size) {
        this.name = name;
        this.elementSize = elementSize;
        this.size = size;
    }
}
