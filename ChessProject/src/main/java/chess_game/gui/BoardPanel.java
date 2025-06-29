/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chess_game.gui;

import ClientSide.Client;
import chess_game.Boards.Board;
import chess_game.Pieces.Coordinate;
import chess_game.Pieces.Team;
import java.awt.Graphics;
import javax.swing.JPanel;
import chess_game.Resources.BOARD_Configurations;
import chess_game.Resources.PIECE_Configurations;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.MouseListener;
import java.util.List;

/**
 *
 * @author Enes Kızılcın <nazifenes.kizilcin@stu.fsm.edu.tr>
 */

// This class is the visual version of the Board object.
public class BoardPanel extends JPanel {

    private TilePanel boardTiles[][];
    private Client client;
    private boolean isBoardRotated;

    public BoardPanel(Board chessBoard, Client client) {
        super(new GridLayout(BOARD_Configurations.ROW_COUNT, BOARD_Configurations.ROW_TILE_COUNT));
        this.client = client;
        this.isBoardRotated = (client.getTeam() == Team.BLACK);
        this.boardTiles = new TilePanel[BOARD_Configurations.ROW_COUNT][BOARD_Configurations.ROW_TILE_COUNT];
        
        for (int i = 0; i < BOARD_Configurations.ROW_COUNT; i++) {
            for (int j = 0; j < BOARD_Configurations.ROW_TILE_COUNT; j++) {
                Coordinate displayCoord = getDisplayCoordinate(i, j);
                TilePanel tilePanel = new TilePanel(this, displayCoord, chessBoard, client);
                this.boardTiles[i][j] = tilePanel;
                add(tilePanel);
            }
        }
    }

    /**
     * Converts display coordinates to actual board coordinates based on player's team
     * @param row The display row (0-7)
     * @param col The display column (0-7)
     * @return The actual board coordinate
     */
    private Coordinate getDisplayCoordinate(int row, int col) {
        if (isBoardRotated) {
            // For black player, rotate the board 180 degrees
            // This means (0,0) becomes (7,7), (0,1) becomes (7,6), etc.
            return new Coordinate(7 - col, 7 - row);
        } else {
            // For white player, keep the original orientation
            return new Coordinate(col, row);
        }
    }

    /**
     * Converts actual board coordinates to display coordinates based on player's team
     * @param coord The actual board coordinate
     * @return The display coordinate
     */
    public Coordinate getDisplayCoordinate(Coordinate coord) {
        if (isBoardRotated) {
            // For black player, rotate the board 180 degrees
            return new Coordinate(7 - coord.getX(), 7 - coord.getY());
        } else {
            // For white player, keep the original orientation
            return coord;
        }
    }

    public TilePanel[][] getBoardTiles() {
        return boardTiles;
    }

    public void setBoardTiles(TilePanel[][] boardTiles) {
        this.boardTiles = boardTiles;
    }

    public void updateBoardGUI(Board board) {
        for (int i = 0; i < BOARD_Configurations.ROW_COUNT; i++) {
            for (int j = 0; j < BOARD_Configurations.ROW_TILE_COUNT; j++) {
                boardTiles[i][j].assignTileColor(board);
                boardTiles[i][j].assignTilePieceIcon(board);
            }
        }
    }

    public boolean isBoardRotated() {
        return isBoardRotated;
    }
}
