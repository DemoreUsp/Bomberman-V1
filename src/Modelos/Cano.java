/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelos;

import Auxiliar.Consts;
import java.awt.Rectangle;

/**
 *
 * @author leodemore
 */
public class Cano extends Personagem {
    
    public Cano(String sNomeImagePNG) {
        super(sNomeImagePNG);
        this.bTransponivel = false;
        bMortal = false;
    }
    
}
