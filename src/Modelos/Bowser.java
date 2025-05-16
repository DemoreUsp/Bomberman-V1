package Modelos;

public class Bowser extends Personagem {
    private boolean bRight;
    int iContador;

    public Bowser(String sNomeImagePNG) {
        super(sNomeImagePNG);
        bRight = true;
        iContador = 0;
    }

    public void autoDesenho() {
        if (iContador == 5) {
            iContador = 0;
            if (bRight) {
                this.setPosicao(pPosicao.getLinha(), pPosicao.getColuna() + 1);
            } else {
                this.setPosicao(pPosicao.getLinha(), pPosicao.getColuna() - 1);
            }

            bRight = !bRight;
        }
        super.autoDesenho();
        iContador++;
    }
}
