/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chess_game.Utilities;

import chess_game.Boards.Board;
import chess_game.Boards.Tile;
import chess_game.Pieces.Coordinate;
import chess_game.Move.Move;
import chess_game.Pieces.PieceTypes;
import chess_game.Pieces.Team;
import chess_game.Resources.PIECE_Configurations;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Enes Kızılcın <nazifenes.kizilcin@stu.fsm.edu.tr>
 */
public class MoveUtilities {

    public static List<Move> getLegalMoves(Board board, Team team) {
        List<Move> legalMoves = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Tile tile = board.getTile(i, j);
                if (tile.hasPiece() && tile.getPiece().getTeam() == team) {
                    for (Move move : tile.getPiece().availableMoves(board, tile.getCoordinate())) {
                        Board boardCopy = board.deepCopy();
                        Tile fromTile = boardCopy.getTile(tile.getCoordinate());
                        Tile toTile = boardCopy.getTile(move.getDestinationTile().getCoordinate());
                        // Simulate the move
                        boardCopy.getCurrentPlayer().makeMove(boardCopy, new Move(boardCopy, fromTile, toTile));
                        // If king is not in check after the move, it's legal
                        if (!controlCheckState(boardCopy, team)) {
                            legalMoves.add(move);
                        }
                    }
                }
            }
        }
        return legalMoves;
    }

    public static boolean isValidMove(Board board, Tile destinationTile) {
        if (!board.hasChosenTile()) {
            return false;
        }
        Team team = board.getCurrentPlayer().getTeam();
        List<Move> legalMoves = getLegalMoves(board, team);
        for (Move move : legalMoves) {
            if (move.getCurrentTile().getCoordinate().equals(board.getChosenTile().getCoordinate()) &&
                move.getDestinationTile().getCoordinate().equals(destinationTile.getCoordinate())) {
                return true;
            }
        }
        return false;
    }

    public static boolean controlCheckState(Board board, Team team) {

        Tile destinationTile;
        Coordinate currentCoord = board.getCoordOfGivenTeamPiece(team, PieceTypes.KING);
        //control is there a knight as a danger for king ( check state),
        for (Coordinate coord : PIECE_Configurations.KNIGHT_MOVES) {

            if (!BoardUtilities.isValidCoordinate(currentCoord.plus(coord))) {
                continue; // chech if the coord outside of board.
            }
            destinationTile = board.getTile(currentCoord.plus(coord));

            if (!destinationTile.hasPiece()) {
                continue;
            } else {
                if (destinationTile.getPiece().getTeam() != team && destinationTile.getPiece().getType() == PieceTypes.KNIGHT) {
                    return true;
                }
            }
        }
        
        //control is there a rooks, queen etc. (check danger from straight way)
        
        Tile currentTile = board.getTile(currentCoord);
        Coordinate destinationCoordinate;
        for (Coordinate coord : PIECE_Configurations.ROOK_MOVES) {
            destinationCoordinate = currentCoord;
            while (BoardUtilities.isValidCoordinate(destinationCoordinate.plus(coord))) {
                destinationCoordinate = destinationCoordinate.plus(coord);
                destinationTile = board.getTile(destinationCoordinate);
                if (!destinationTile.hasPiece()) {
                    continue;
                } else {
                    if (destinationTile.getPiece().getTeam() == team) {
                        break;
                    }
                    if (destinationTile.getPiece().getTeam() != team && (destinationTile.getPiece().getType() == PieceTypes.ROOK || destinationTile.getPiece().getType() == PieceTypes.QUEEN)) {
                        return true;
                    } else {
                        break;
                    }
                }
            }
        }
        //control is there a bishop, queen ( check danger from cross way)
        for (Coordinate coord : PIECE_Configurations.BISHOP_MOVES) {
            destinationCoordinate = currentCoord;
            while (BoardUtilities.isValidCoordinate(destinationCoordinate.plus(coord))) {
                destinationCoordinate = destinationCoordinate.plus(coord);
                destinationTile = board.getTile(destinationCoordinate);
                if (!destinationTile.hasPiece()) {
                    continue;
                } else {
                    if (destinationTile.getPiece().getTeam() == team) {
                        break;
                    }
                    if (destinationTile.getPiece().getTeam() != team && (destinationTile.getPiece().getType() == PieceTypes.BISHOP || destinationTile.getPiece().getType() == PieceTypes.QUEEN)) {
                        return true;

                    } else {
                        break;
                    }
                }
            }
        }
        
        // control is there a pawn that able to attack king nearby. 
        for (Coordinate coord : (Coordinate[]) PIECE_Configurations.PAWN_MOVES.get(team).get("Attack")) {

            if (!BoardUtilities.isValidCoordinate(currentCoord.plus(coord))) {
                continue;
            }
            destinationTile = board.getTile(currentCoord.plus(coord));

            if (!destinationTile.hasPiece()) {
                continue;
            } else {
                if (destinationTile.getPiece().getTeam() != team && destinationTile.getPiece().getType() == PieceTypes.PAWN) {
                    return true;
                }
            }
        }
        return false;
    }

}
