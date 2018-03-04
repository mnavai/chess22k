package nl.s22k.chess.eval;

import nl.s22k.chess.ChessConstants;

public class SchroderUtil {

	public static final int FLAG_PAWN = 1 << (ChessConstants.PAWN - 1);
	public static final int FLAG_NIGHT = 1 << (ChessConstants.NIGHT - 1);
	public static final int FLAG_BISHOP = 1 << (ChessConstants.BISHOP - 1);
	public static final int FLAG_ROOK = 1 << (ChessConstants.ROOK - 1);
	public static final int FLAG_QUEEN = 1 << (ChessConstants.QUEEN - 1);

}
