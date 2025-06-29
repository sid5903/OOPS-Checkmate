package chess_game.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class InGameBottomMenu extends javax.swing.JPanel {

    public DefaultListModel killedPiecesListModel;
    public DefaultListModel<String> chatListModel;

    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JList<String> killedPiecesLIST;
    private javax.swing.JLabel playersColorLBL;
    private javax.swing.JLabel turnLBL;
    private javax.swing.JLabel playerNameLBL;
    private javax.swing.JLabel opponentNameLBL;
    
    // Chat components
    private javax.swing.JScrollPane chatScrollPane;
    private javax.swing.JList<String> chatLIST;
    private javax.swing.JTextField chatInputTXT;
    private javax.swing.JButton sendBTN;
    private javax.swing.JLabel chatLBL;
    
    // Quit game button
    private javax.swing.JButton quitGameBTN;
    
    // Save game button
    private javax.swing.JButton saveGameBTN;

    public InGameBottomMenu() {
        initComponents();
        killedPiecesListModel = new DefaultListModel();
        killedPiecesLIST.setModel(killedPiecesListModel);

        chatListModel = new DefaultListModel<>();
        chatLIST.setModel(chatListModel);
    }

    public JLabel getPlayersColorLBL() {
        return playersColorLBL;
    }

    public JList<String> getKilledPiecesLIST() {
        return killedPiecesLIST;
    }

    public JLabel getTurnLBL() {
        return turnLBL;
    }

    public JLabel getPlayerNameLBL() {
        return playerNameLBL;
    }

    public JLabel getOpponentNameLBL() {
        return opponentNameLBL;
    }

    public JTextField getChatInputTXT() {
        return chatInputTXT;
    }

    public JButton getSendBTN() {
        return sendBTN;
    }
    
    public JButton getQuitGameBTN() {
        return quitGameBTN;
    }

    public JButton getSaveGameBTN() {
        return saveGameBTN;
    }

    public void setPlayerName(String playerName) {
        playerNameLBL.setText("" + playerName);
    }

    public void setOpponentName(String opponentName) {
        opponentNameLBL.setText("" + opponentName);
    }

    public void addChatMessage(String message) {
        chatListModel.addElement(message);
        // Auto-scroll to bottom
        SwingUtilities.invokeLater(() -> {
            int lastIndex = chatLIST.getModel().getSize() - 1;
            if (lastIndex >= 0) {
                chatLIST.ensureIndexIsVisible(lastIndex);
            }
        });
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        this.setBackground(new Color(223, 238, 222)); // Light green background

        playersColorLBL = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        killedPiecesLIST = new javax.swing.JList<>();
        turnLBL = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        playerNameLBL = new javax.swing.JLabel();
        opponentNameLBL = new javax.swing.JLabel();

        JScrollPane rightKilledScrollPane = new javax.swing.JScrollPane();
        JList<String> rightKilledPiecesLIST = new javax.swing.JList<>();
        JLabel rightKilledLBL = new JLabel("Captured Pieces");

        chatScrollPane = new javax.swing.JScrollPane();
        chatLIST = new javax.swing.JList<>();
        chatInputTXT = new javax.swing.JTextField();
        sendBTN = new javax.swing.JButton();
        chatLBL = new javax.swing.JLabel();

        JPanel chatInputPanel = new JPanel();
        javax.swing.GroupLayout chatInputLayout = new javax.swing.GroupLayout(chatInputPanel);
        chatInputPanel.setLayout(chatInputLayout);

        quitGameBTN = new javax.swing.JButton();
        saveGameBTN = new javax.swing.JButton();

        Font buttonFont = new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 12);
        Color buttonColor = new java.awt.Color(15, 15, 15);

        // Left player name
        JPanel leftNamePanel = new JPanel(new GridBagLayout());
        leftNamePanel.setBackground(Color.WHITE);
        leftNamePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
        playerNameLBL.setText("Player 2");
        playerNameLBL.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14));
        playerNameLBL.setForeground(buttonColor);
        leftNamePanel.add(playerNameLBL);
        leftNamePanel.setPreferredSize(new Dimension(150, 30));

        // Right player name
        JPanel rightNamePanel = new JPanel(new GridBagLayout());
        rightNamePanel.setBackground(Color.WHITE);
        rightNamePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
        opponentNameLBL.setText("Player 1");
        opponentNameLBL.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14));
        opponentNameLBL.setForeground(buttonColor);
        rightNamePanel.add(opponentNameLBL);
        rightNamePanel.setPreferredSize(new Dimension(150, 30));

        // Left captured pieces panel
        jLabel2.setText("Captured Pieces");
        jLabel2.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14));
        jLabel2.setForeground(Color.BLACK);
        jLabel2.setOpaque(true);
        jLabel2.setBackground(Color.WHITE);
        killedPiecesLIST.setBackground(Color.WHITE);
        killedPiecesLIST.setForeground(Color.BLACK);
        jScrollPane2.setViewportView(killedPiecesLIST);
        jScrollPane2.setPreferredSize(new Dimension(150, 107));

        // Right captured pieces panel
        rightKilledLBL.setText("Captured Pieces");
        rightKilledLBL.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14));
        rightKilledLBL.setForeground(Color.WHITE);
        rightKilledLBL.setOpaque(true);
        rightKilledLBL.setBackground(Color.BLACK);
        rightKilledPiecesLIST.setBackground(Color.BLACK);
        rightKilledPiecesLIST.setForeground(Color.WHITE);
        rightKilledScrollPane.setViewportView(rightKilledPiecesLIST);
        rightKilledScrollPane.setBackground(Color.BLACK);
        rightKilledScrollPane.getViewport().setBackground(Color.BLACK); // ✅ critical for full black effect
        rightKilledScrollPane.setPreferredSize(new Dimension(150, 107));

        // Chat setup
        chatLBL.setText("CHAT BOX");
        chatLBL.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14));
        chatLBL.setForeground(Color.BLACK);
        chatScrollPane.setViewportView(chatLIST);
        chatScrollPane.setBorder(BorderFactory.createLineBorder(new Color(40, 167, 69), 2));
        chatScrollPane.setPreferredSize(new Dimension(300, 120));
        chatLIST.setBackground(new java.awt.Color(15, 15, 15));
        chatLIST.setForeground(Color.WHITE);
        chatLIST.setSelectionBackground(new Color(60, 60, 60));
        sendBTN.setText("Send");
        chatInputTXT.setToolTipText("Type your message here...");
        chatInputTXT.setPreferredSize(new Dimension(180, 30));
        sendBTN.setPreferredSize(new Dimension(64, 30));

        // Buttons
        quitGameBTN.setText("Quit Game");
        quitGameBTN.setBackground(Color.BLACK);
        quitGameBTN.setForeground(Color.RED);
        quitGameBTN.setFont(buttonFont);
        quitGameBTN.setOpaque(true);

        saveGameBTN.setText("Save Game");
        saveGameBTN.setBackground(Color.BLACK);
        saveGameBTN.setForeground(Color.DARK_GRAY);
        saveGameBTN.setFont(buttonFont);
        saveGameBTN.setOpaque(true);

        // Chat input layout
        chatInputLayout.setHorizontalGroup(
                chatInputLayout.createSequentialGroup()
                        .addComponent(chatInputTXT)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(sendBTN, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        chatInputLayout.setVerticalGroup(
                chatInputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(chatInputTXT, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(sendBTN, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        // Layouts
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(23)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(leftNamePanel, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel2)
                                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                                        .addComponent(chatLBL)
                                        .addComponent(chatScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(chatInputPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(saveGameBTN, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(100)
                                                .addComponent(quitGameBTN, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(rightNamePanel, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(rightKilledLBL)
                                        .addComponent(rightKilledScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(30))
        );

        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(chatLBL)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(leftNamePanel, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18)
                                                .addComponent(jLabel2)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(chatScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(chatInputPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(12)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(saveGameBTN, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(quitGameBTN, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(rightNamePanel, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18)
                                                .addComponent(rightKilledLBL)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(rightKilledScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(17))
        );
    }

}
