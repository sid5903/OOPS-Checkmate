package chess_game.gui;

import ClientSide.Client;
import Messages.Message;
import Messages.MovementMessage;
import chess_game.Boards.Board;
import chess_game.Boards.Tile;
import chess_game.Pieces.Coordinate;
import chess_game.Move.Move;
import chess_game.Pieces.PieceTypes;
import chess_game.Pieces.Team;
import java.awt.GridBagLayout;
import javax.swing.JPanel;
import chess_game.Resources.BOARD_Configurations;
import chess_game.Resources.GUI_Configurations;
import chess_game.Utilities.BoardUtilities;
import chess_game.Utilities.MoveUtilities;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class TilePanel extends JPanel {

    private Coordinate coordinate;
    private JLabel pieceIcon;

    public TilePanel(BoardPanel boardPanel, Coordinate coord, Board chessBoard, Client client) {
        super(new GridBagLayout());
        this.coordinate = coord;

        // Create and add the icon label
        pieceIcon = new JLabel();
        pieceIcon.setHorizontalAlignment(JLabel.CENTER);
        pieceIcon.setVerticalAlignment(JLabel.CENTER);

        this.add(pieceIcon);

        // Set tile preferred size (initially 60x60, but will scale)
        setPreferredSize(new Dimension(BOARD_Configurations.TILE_SIZE, BOARD_Configurations.TILE_SIZE));

        // Assign colors and piece icon
        assignTileColor(chessBoard);
        assignTilePieceIcon(chessBoard);

        // Handle resize to rescale icon
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                assignTilePieceIcon(chessBoard); // Re-assign scaled icon on resize
            }
        });

        // Mouse handling for click/move logic
        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (client.getTeam() != chessBoard.getCurrentPlayer().getTeam()) {
                    return;
                }

                if (!chessBoard.hasChosenTile()) {
                    if (chessBoard.getTile(coordinate).hasPiece()) {
                        if (chessBoard.getCurrentPlayer().getTeam() != chessBoard.getTile(coordinate).getPiece().getTeam()) {
                            return;
                        }
                    }
                    chessBoard.setChosenTile(chessBoard.getTile(coordinate));
                } else {
                    Tile destinationTile = chessBoard.getTile(coordinate);
                    if (MoveUtilities.isValidMove(chessBoard, destinationTile)) {
                        Move move = new Move(chessBoard, chessBoard.getChosenTile(), destinationTile);
                        chessBoard.getCurrentPlayer().makeMove(chessBoard, move);

                        if (move.hasKilledPiece()) {
                            client.game.getBottomGameMenu().killedPiecesListModel.addElement(move.getKilledPiece().toString());
                        }

                        // Send move message to other client
                        Message msg = new Message(Message.MessageTypes.MOVE);
                        MovementMessage movement = new MovementMessage();
                        movement.currentCoordinate = move.getCurrentTile().getCoordinate();
                        movement.destinationCoordinate = move.getDestinationTile().getCoordinate();
                        if (move.getKilledPiece() != null) {
                            movement.isPieceKilled = true;
                        }
                        msg.content = (Object) movement;
                        client.Send(msg);

                        chessBoard.changeCurrentPlayer();
                        client.game.getBottomGameMenu().getTurnLBL().setText("Enemy Turn");
                        client.game.getBottomGameMenu().getTurnLBL().setForeground(Color.RED);

                        if (move.hasKilledPiece() && move.getKilledPiece().getType() == PieceTypes.KING) {
                            Team winnerTeam = (move.getKilledPiece().getTeam() == Team.BLACK) ? Team.WHITE : Team.BLACK;
                            JOptionPane.showMessageDialog(null, "Winner: " + winnerTeam.toString());
                            Message endMsg = new Message(Message.MessageTypes.END);
                            client.Send(endMsg);
                        }

                    } else {
                        if (destinationTile.hasPiece()) {
                            if (chessBoard.getCurrentPlayer().getTeam() != chessBoard.getTile(coordinate).getPiece().getTeam()) {
                                return;
                            }
                        }
                        chessBoard.setChosenTile(destinationTile);
                    }

                    // Check state messages
                    if (MoveUtilities.controlCheckState(chessBoard, Team.BLACK)) {
                        JOptionPane.showMessageDialog(null, "Check state for team : " + Team.BLACK.toString());
                        Message msg = new Message(Message.MessageTypes.CHECK);
                        msg.content = (Object) Team.BLACK;
                        client.Send(msg);
                    } else if (MoveUtilities.controlCheckState(chessBoard, Team.WHITE)) {
                        JOptionPane.showMessageDialog(null, "Check state for team : " + Team.WHITE.toString());
                        Message msg = new Message(Message.MessageTypes.CHECK);
                        msg.content = (Object) Team.WHITE;
                        client.Send(msg);
                    }
                }

                boardPanel.updateBoardGUI(chessBoard); // Refresh GUI
            }

            @Override public void mousePressed(MouseEvent e) {}
            @Override public void mouseReleased(MouseEvent e) {}
            @Override public void mouseEntered(MouseEvent e) {}
            @Override public void mouseExited(MouseEvent e) {}
        });

        validate();
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    // ✅ MODIFIED: Dynamically scale and assign image to match tile size
    public void assignTilePieceIcon(Board board) {
        Tile thisTile = board.getTile(this.coordinate);
        if (thisTile == null) {
            System.out.println("Tile is null");
            return;
        }

        if (thisTile.hasPiece()) {
            ImageIcon rawIcon = BoardUtilities.getImageOfTeamPiece(
                    thisTile.getPiece().getTeam(),
                    thisTile.getPiece().getType()
            );

            // Scale to current tile size
            double scale = 0.7;
            int w = this.getWidth() > 0 ? this.getWidth() : BOARD_Configurations.TILE_SIZE;
            int h = this.getHeight() > 0 ? this.getHeight() : BOARD_Configurations.TILE_SIZE;
            int size = (int) (Math.min(w, h) * scale) ;

            pieceIcon.setIcon(getScaledIcon(rawIcon, size, size));
        } else {
            pieceIcon.setIcon(null);
        }

        pieceIcon.validate();
    }

    // ✅ NEW: Helper method to scale icons
    private ImageIcon getScaledIcon(ImageIcon icon, int width, int height) {
        return new ImageIcon(icon.getImage().getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH));
    }

    public void assignTileColor(Board board) {

        // Default coloring
        if (this.coordinate.getX() % 2 == 0 && this.coordinate.getY() % 2 == 0) {
            this.setBackground(GUI_Configurations.lightColor);
        } else if (this.coordinate.getX() % 2 == 0 && this.coordinate.getY() % 2 == 1) {
            this.setBackground(GUI_Configurations.darkColor);
        } else if (this.coordinate.getX() % 2 == 1 && this.coordinate.getY() % 2 == 0) {
            this.setBackground(GUI_Configurations.darkColor);
        } else if (this.coordinate.getX() % 2 == 1 && this.coordinate.getY() % 2 == 1) {
            this.setBackground(GUI_Configurations.lightColor);
        }

        // Highlight chosen tile
        if (board.hasChosenTile()) {
            if (this.coordinate.equals(board.getChosenTile().getCoordinate())) {
                this.setBackground(Color.GREEN);
            }
        }

        // Highlight checked king's square
        boolean blackInCheck = MoveUtilities.controlCheckState(board, Team.BLACK);
        boolean whiteInCheck = MoveUtilities.controlCheckState(board, Team.WHITE);
        Coordinate blackKingCoord = board.getCoordOfGivenTeamPiece(Team.BLACK, PieceTypes.KING);
        Coordinate whiteKingCoord = board.getCoordOfGivenTeamPiece(Team.WHITE, PieceTypes.KING);
        if (blackInCheck && blackKingCoord != null && this.coordinate.equals(blackKingCoord)) {
            this.setBackground(GUI_Configurations.checkRedColor);
        } else if (whiteInCheck && whiteKingCoord != null && this.coordinate.equals(whiteKingCoord)) {
            this.setBackground(GUI_Configurations.checkRedColor);
        }

        this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    }

    @Override
    public Dimension getPreferredSize() {
        // Ensure square size based on current width or height
        int size = Math.min(getWidth(), getHeight());
        if (size == 0) {
            size = BOARD_Configurations.TILE_SIZE; // fallback default
        }
        return new Dimension(size, size);
    }
}
