package com.example.minigamelauncher.chessgame.engine.piece;

import com.example.minigamelauncher.chessgame.engine.Alliance;
import com.example.minigamelauncher.chessgame.engine.board.Board;
import com.example.minigamelauncher.chessgame.engine.board.Utils;
import com.example.minigamelauncher.chessgame.engine.board.Cell;
import com.example.minigamelauncher.chessgame.engine.board.Move;
import com.example.minigamelauncher.chessgame.engine.board.Move.MajorAttackMove;
import com.example.minigamelauncher.chessgame.engine.board.Move.MajorMove;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Knight extends Piece {

    private final static int[] CANDIDATE_MOVE_COORDINATE = {-17, -15, -10, -6, 6, 10, 15, 17};

    public Knight(final Alliance pieceAlliance, final int piecePosition) {
        super(PieceType.KNIGHT, piecePosition, pieceAlliance, true);
    }

    public Knight(final Alliance pieceAlliance, final int piecePosition, final boolean isFirstMove) {
        super(PieceType.KNIGHT, piecePosition, pieceAlliance, isFirstMove);
    }

    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {

        final List<Move> legalMoves = new ArrayList<>();

        for (final int currentCandidateOffset : CANDIDATE_MOVE_COORDINATE) {

            final int candidateDestinationCoordinate = this.piecePosition + currentCandidateOffset;

            if (Utils.isValidCellCoordinate(candidateDestinationCoordinate)) {

                if (isFirstColumnExclusion(this.piecePosition, currentCandidateOffset) ||
                        isSecondColumnExclusion(this.piecePosition, currentCandidateOffset) ||
                        isSeventhColumnExclusion(this.piecePosition, currentCandidateOffset) ||
                        isEighthColumnExclusion(this.piecePosition, currentCandidateOffset)) {
                    continue;
                }

                final Cell candidateDestinationCell = board.getCell(candidateDestinationCoordinate);

                if (!candidateDestinationCell.isCellOccupied()) {
                    legalMoves.add(new MajorMove(board, this, candidateDestinationCoordinate));
                } else {
                    final Piece pieceAtDestination = candidateDestinationCell.getPiece();
                    final Alliance pieceAlliance = pieceAtDestination.getPieceAlliance();

                    if (this.pieceAlliance != pieceAlliance) {
                        legalMoves.add(new MajorAttackMove(board, this, candidateDestinationCoordinate, pieceAtDestination));
                    }
                }
            }
        }


        return List.copyOf(legalMoves);
    }

    @Override
    public Knight movePiece(final Move move) {
        return new Knight(move.getMovedPiece().getPieceAlliance(), move.getDestinationCoordinate());
    }

    @Override
    public String toString() {
        return PieceType.KNIGHT.toString();
    }

    private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffset) {

        return Utils.FIRST_COLUMN[currentPosition] && ((candidateOffset == -17) || (candidateOffset == -10) ||
                (candidateOffset == 6) || (candidateOffset == 15));
    }

    private static boolean isSecondColumnExclusion(final int currentPosition, final int candidateOffset) {
        return Utils.SECOND_COLUMN[currentPosition] && ((candidateOffset == -10) || (candidateOffset == 6));
    }

    private static boolean isSeventhColumnExclusion(final int currentPosition, final int candidateOffset) {
        return Utils.SEVENTH_COLUMN[currentPosition] && ((candidateOffset == -6) || (candidateOffset == 10));
    }

    private static boolean isEighthColumnExclusion(final int currentPosition, final int candidateOffset) {
        return Utils.EIGHTH_COLUMN[currentPosition] && ((candidateOffset == -15) || (candidateOffset == -6) ||
                (candidateOffset == 10) || (candidateOffset == 17));
    }

}