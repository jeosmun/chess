package service.requests;

import chess.ChessGame;

public record JoinGameRequest(String playerColor, double gameID, String username) {}
