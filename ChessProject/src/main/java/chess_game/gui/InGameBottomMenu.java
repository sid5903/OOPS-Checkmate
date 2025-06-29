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
        playerNameLBL.setText("You: " + playerName);
    }

    public void setOpponentName(String opponentName) {
        opponentNameLBL.setText("Opponent: " + opponentName);
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
        playersColorLBL = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        killedPiecesLIST = new javax.swing.JList<>();
        turnLBL = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        playerNameLBL = new javax.swing.JLabel();
        opponentNameLBL = new javax.swing.JLabel();
        
        // Chat components
        chatScrollPane = new javax.swing.JScrollPane();
        chatLIST = new javax.swing.JList<>();
        chatInputTXT = new javax.swing.JTextField();
        sendBTN = new javax.swing.JButton();
        chatLBL = new javax.swing.JLabel();
        
        // Quit game button
        quitGameBTN = new javax.swing.JButton();
        
        // Save game button
        saveGameBTN = new javax.swing.JButton();

        playersColorLBL.setText("You're playing: ");
        jScrollPane2.setViewportView(killedPiecesLIST);

        turnLBL.setBackground(new java.awt.Color(13, 197, 197));
        turnLBL.setFont(new java.awt.Font("Segoe UI", 0, 24));
        turnLBL.setForeground(new java.awt.Color(153, 255, 153));
        turnLBL.setText("YOUR TURN!");

        jLabel2.setBackground(new java.awt.Color(0, 153, 102));
        jLabel2.setForeground(new java.awt.Color(0, 255, 153));
        jLabel2.setText("Killed Pieces");

        playerNameLBL.setText("You: Player");
        opponentNameLBL.setText("Opponent: Waiting...");

        // Chat setup
        chatScrollPane.setViewportView(chatLIST);
        chatLBL.setText("Chat");
        chatLBL.setForeground(new java.awt.Color(0, 255, 153));
        sendBTN.setText("Send");
        chatInputTXT.setToolTipText("Type your message here...");
        
        // Quit game button setup
        quitGameBTN.setText("Quit Game");
        quitGameBTN.setBackground(new java.awt.Color(220, 53, 69)); // Red background
        quitGameBTN.setForeground(new java.awt.Color(255, 255, 255)); // White text
        quitGameBTN.setFont(new java.awt.Font("Segoe UI", 1, 12)); // Bold font
        
        // Save game button setup
        saveGameBTN.setText("Save Game");
        saveGameBTN.setBackground(new java.awt.Color(40, 167, 69)); // Green background
        saveGameBTN.setForeground(new java.awt.Color(255, 255, 255)); // White text
        saveGameBTN.setFont(new java.awt.Font("Segoe UI", 1, 12)); // Bold font
        
        // Style chat list
        chatLIST.setBackground(new java.awt.Color(40, 40, 40));
        chatLIST.setForeground(new java.awt.Color(255, 255, 255));
        chatLIST.setSelectionBackground(new java.awt.Color(60, 60, 60));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(playersColorLBL, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(playerNameLBL, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(opponentNameLBL, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(chatLBL)
                    .addComponent(chatScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(chatInputTXT, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(sendBTN, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(turnLBL)
                    .addComponent(saveGameBTN, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(quitGameBTN, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(50, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(turnLBL)
                    .addComponent(playersColorLBL, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(chatLBL))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(playerNameLBL)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(opponentNameLBL)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 11, Short.MAX_VALUE)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(chatScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(chatInputTXT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(sendBTN))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(saveGameBTN, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(quitGameBTN, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(17, 17, 17))
        );
    }
}
