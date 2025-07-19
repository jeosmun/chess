package service;

import dataaccess.GameDAO;
import dataaccess.MemoryGameDAO;
import service.requests.CreateGameRequest;
import service.requests.JoinGameRequest;
import service.requests.ListGamesRequest;
import service.results.CreateGameResult;
import service.results.ListGamesResult;

public class GameService {
    private final GameDAO gameDAO = new MemoryGameDAO();

    public ListGamesResult listGames(ListGamesRequest listGamesRequest) {
        return null;
    }

    public CreateGameResult createGame(CreateGameRequest createGameRequest) {
        return null;
    }

    void joinGame(JoinGameRequest joinGameRequest) {

    }
}
