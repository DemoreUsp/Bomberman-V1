package Controler;

import java.awt.*;
import javax.swing.*;

// Classe que printa o menu inicial, antes do loop do jogo
public class MenuInicial {
    private JFrame frame;
    private final JButton btnNewGame;
    private final JButton btnContinue;
    private final JButton btnSobre;
    private boolean iniciarJogo = false;
    private boolean carregarJogo = false;

    public MenuInicial() {
        frame = new JFrame("Mario - Menu Inicial");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        
        JPanel panel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                try {
                    Image bgImage = Toolkit.getDefaultToolkit().getImage("imgs/ceu.png");
                    g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
                } catch (Exception e) {
                    g.setColor(Color.BLUE);
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };
        panel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 50, 10, 50);

        btnNewGame = createStyledButton("New Game");
        btnContinue = createStyledButton("Continue");
        btnSobre = createStyledButton("Sobre");

        btnNewGame.addActionListener(e -> {
            iniciarJogo = true;
            carregarJogo = false;
            frame.dispose();
        });

        btnContinue.addActionListener(e -> {
            iniciarJogo = true;
            carregarJogo = true;
            frame.dispose();
        });

        btnSobre.addActionListener(e -> {
            JOptionPane.showMessageDialog(frame, 
                "Desenvolvido por:\nArthur Araujo (Nusp: 14651458)\nLeo Demore (Nusp: 15674786)\nJulio Fuganti (Nusp: 15638792)", 
                "Informações dos Desenvolvedores", 
                JOptionPane.INFORMATION_MESSAGE);
        });

        panel.add(btnNewGame, gbc);
        panel.add(btnContinue, gbc);
        panel.add(btnSobre, gbc);

        frame.add(panel);
        frame.setVisible(true);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 24));
        button.setBackground(new Color(70, 130, 180));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        button.setPreferredSize(new Dimension(250, 60));
        return button;
    }

    public boolean deveIniciarJogo() {
        return iniciarJogo;
    }

    public boolean deveCarregarJogo() {
        return carregarJogo;
    }
}