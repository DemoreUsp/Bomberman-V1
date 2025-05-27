/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelos.MapStuff;

/**
 *
 * @author leodemore
 */
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
