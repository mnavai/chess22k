package nl.s22k.chess.move;

import java.util.Arrays;

import nl.s22k.chess.ChessBoard;
import nl.s22k.chess.ChessConstants.ScoreType;
import nl.s22k.chess.Util;
import nl.s22k.chess.search.TTUtil;

public class PV {

	private static final int MOVES_LENGTH = 8;

	private static int[] moves = new int[MOVES_LENGTH];
	private static int flag;
	private static int score;

	public static void init(ChessBoard cb) {
		final long ttValue = TTUtil.getValue(cb.zobristKey);
		if (ttValue == 0) {
			Arrays.fill(moves, 0);
		} else {
			PV.set(TTUtil.getMove(ttValue), Util.SHORT_MIN, Util.SHORT_MAX, TTUtil.getScore(ttValue, 0), cb);
		}
	}

	public static void set(int bestMove, int alpha, int beta, int score, ChessBoard cb) {

		PV.score = score;

		if (score <= alpha) {
			flag = TTUtil.FLAG_UPPER;
			return;
		} else if (score >= beta) {
			flag = TTUtil.FLAG_LOWER;
		} else {
			flag = TTUtil.FLAG_EXACT;
		}

		Arrays.fill(moves, 0);
		moves[0] = bestMove;
		cb.doMove(bestMove);

		for (int i = 1; i < MOVES_LENGTH; i++) {
			long ttValue = TTUtil.getValue(cb.zobristKey);
			if (ttValue == 0) {
				break;
			}
			int move = TTUtil.getMove(ttValue);
			moves[i] = move;
			cb.doMove(move);
		}
		for (int i = MOVES_LENGTH - 1; i >= 0; i--) {
			if (moves[i] == 0) {
				continue;
			}
			cb.undoMove(moves[i]);
		}
	}

	public static ScoreType getScoreType() {
		switch (flag) {
		case TTUtil.FLAG_EXACT:
			return ScoreType.EXACT;
		case TTUtil.FLAG_LOWER:
			return ScoreType.LOWER;
		case TTUtil.FLAG_UPPER:
			return ScoreType.UPPER;
		}
		throw new RuntimeException("Unknown flag " + flag);
	}

	public static int getPonderMove() {
		return moves[1];
	}

	public static int getBestMove() {
		return moves[0];
	}

	public static int getScore() {
		return score;
	}

	public static String asString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < MOVES_LENGTH; i++) {
			int move = moves[i];
			if (move == 0) {
				break;
			}
			sb.append(new MoveWrapper(move)).append(" ");
		}
		return sb.toString();
	}

}
