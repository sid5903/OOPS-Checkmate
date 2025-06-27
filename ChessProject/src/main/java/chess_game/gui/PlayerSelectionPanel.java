package chess_game.gui;

import Messages.PlayerInfo;
import Messages.PlayRequest;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 *
 * @author Enes Kızılcın <nazifenes.kizilcin@stu.fsm.edu.tr>
 */
public class PlayerSelectionPanel extends javax.swing.JPanel {

    private javax.swing.JLabel titleLBL;
    private javax.swing.JScrollPane playersScrollPane;
    private javax.swing.JList<String> playersLIST;
    private javax.swing.JButton sendRequestBTN;
    private javax.swing.JButton refreshBTN;
    private javax.swing.JButton backBTN;
    private javax.swing.JLabel statusLBL;
    
    public DefaultListModel<String> playersListModel;
    private ArrayList<PlayerInfo> availablePlayers;
    
    public PlayerSelectionPanel() {
        initComponents();
        playersListModel = new DefaultListModel<>();
        playersLIST.setModel(playersListModel);
        availablePlayers = new ArrayList<>();
    }

    public JList<String> getPlayersLIST() {
        return playersLIST;
    }

    public JButton getSendRequestBTN() {
        return sendRequestBTN;
    }

    public JButton getRefreshBTN() {
        return refreshBTN;
    }

    public JButton getBackBTN() {
        return backBTN;
    }

    public JLabel getStatusLBL() {
        return statusLBL;
    }

    public void updatePlayersList(ArrayList<PlayerInfo> players) {
        this.availablePlayers = players;
        playersListModel.clear();
        for (PlayerInfo player : players) {
            if (player.isAvailable) {
                playersListModel.addElement(player.playerName);
            }
        }
    }

    public PlayerInfo getSelectedPlayer() {
        String selectedName = playersLIST.getSelectedValue();
        if (selectedName != null) {
            for (PlayerInfo player : availablePlayers) {
                if (player.playerName.equals(selectedName)) {
                    return player;
                }
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        titleLBL = new javax.swing.JLabel();
        playersScrollPane = new javax.swing.JScrollPane();
        playersLIST = new javax.swing.JList<>();
        sendRequestBTN = new javax.swing.JButton();
        refreshBTN = new javax.swing.JButton();
        backBTN = new javax.swing.JButton();
        statusLBL = new javax.swing.JLabel();

        titleLBL.setFont(new java.awt.Font("Segoe UI", 1, 18));
        titleLBL.setText("Select Player to Challenge");
        titleLBL.setHorizontalAlignment(SwingConstants.CENTER);

        playersScrollPane.setViewportView(playersLIST);
        playersLIST.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        sendRequestBTN.setText("Send Play Request");
        refreshBTN.setText("Refresh List");
        backBTN.setText("Back to Menu");
        
        statusLBL.setText("Select a player and click 'Send Play Request'");
        statusLBL.setForeground(new java.awt.Color(0, 150, 0));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(titleLBL, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
                    .addComponent(playersScrollPane)
                    .addComponent(statusLBL, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(sendRequestBTN, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(20, 20, 20)
                        .addComponent(refreshBTN, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(20, 20, 20)
                        .addComponent(backBTN, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(50, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(titleLBL)
                .addGap(20, 20, 20)
                .addComponent(playersScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(sendRequestBTN)
                    .addComponent(refreshBTN)
                    .addComponent(backBTN))
                .addGap(20, 20, 20)
                .addComponent(statusLBL)
                .addContainerGap(50, Short.MAX_VALUE))
        );
    }
}
