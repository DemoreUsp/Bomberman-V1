
package Modelos.MapStuff;

public class Nuvem {
    private int x;
    private int y;
    private String imagePath;

    public Nuvem(int x, int y) {
        this.x = x;
        this.y = y;
        this.imagePath = "nuvem.png";
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getImagePath() {
        return imagePath;
    }
}
