package chess_game.gui;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;

public class MainMenu extends javax.swing.JPanel {

    private javax.swing.JButton exitBTN;
    private javax.swing.JLabel infoLBL;
    private javax.swing.JButton playBTN;
    private javax.swing.JButton selectPlayerBTN; // New button
    private javax.swing.JButton loadGameBTN; // Load game button
    private javax.swing.JTextField playerNameTXT;
    private javax.swing.JLabel playerNameLBL;

    public MainMenu() {
        // Load background image
        try {
            backgroundImage = ImageIO.read(getClass().getClassLoader()
                    .getResourceAsStream("chess_game/Img/Main_menu_bgd_logo.png"));
        } catch (IOException | IllegalArgumentException e) {
            System.err.println("Failed to load background image: " + e.getMessage());
        }

        initComponents();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Draw background image scaled to panel size
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }

    public JButton getExitBTN() {
        return exitBTN;
    }

    public JLabel getInfoLBL() {
        return infoLBL;
    }

    public JButton getPlayBTN() {
        return playBTN;
    }

    public JButton getSelectPlayerBTN() {
        return selectPlayerBTN;
    }

    public JButton getLoadGameBTN() {
        return loadGameBTN;
    }

    public JTextField getPlayerNameTXT() {
        return playerNameTXT;
    }

    public String getPlayerName() {
        String name = playerNameTXT.getText().trim();
        return name.isEmpty() ? "Anonymous" : name;
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        playBTN = new javax.swing.JButton();
        selectPlayerBTN = new javax.swing.JButton();
        exitBTN = new javax.swing.JButton();
        infoLBL = new javax.swing.JLabel();
        playerNameLBL = new javax.swing.JLabel();
        playerNameTXT = new javax.swing.JTextField();
        loadGameBTN = new javax.swing.JButton();

        playBTN.setText("Quick Match");
        selectPlayerBTN.setText("Select Player");
        exitBTN.setText("Exit");
        loadGameBTN.setText("Load Game");

        infoLBL.setText("Matching");
        infoLBL.setVisible(false);
        playerNameLBL.setForeground(Color.WHITE);
        playerNameLBL.setText("Enter your name:");
        playerNameTXT.setText("Player");

        // Add Enter key support for name field
        playerNameTXT.addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    playBTN.doClick();
                }
            }
            
            @Override
            public void keyTyped(KeyEvent e) {}
            
            @Override
            public void keyReleased(KeyEvent e) {}
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(150, 150, 150)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(playerNameLBL)
                    .addComponent(playerNameTXT, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
                    .addComponent(playBTN, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(selectPlayerBTN, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(loadGameBTN, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(exitBTN, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(40, 40, 40)
                .addComponent(infoLBL)
                .addContainerGap(134, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(80, 80, 80)
                .addComponent(playerNameLBL)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(playerNameTXT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(playBTN)
                    .addComponent(infoLBL))
                .addGap(10, 10, 10)
                .addComponent(selectPlayerBTN)
                .addGap(10, 10, 10)
                .addComponent(loadGameBTN)
                .addGap(10, 10, 10)
                .addComponent(exitBTN)
                .addContainerGap(120, Short.MAX_VALUE))
        );
    }

    private void exitBTNActionPerformed(java.awt.event.ActionEvent evt) {
        System.exit(0);
    }
}
