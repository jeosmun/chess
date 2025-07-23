package service;

import chess.ChessGame;
import dataaccess.*;
import model.GameData;
import service.requests.CreateGameRequest;
import service.requests.JoinGameRequest;
import service.requests.ListGamesRequest;
import service.results.CreateGameResult;
import service.results.ListGamesResult;

import java.util.ArrayList;
import java.util.Objects;

public class GameService {
    private final GameDAO gameDAO = new SQLGameDAO();

    public GameService() throws DataAccessException {

    }

    public ListGamesResult listGames(ListGamesRequest listGamesRequest) throws DataAccessException {
        ArrayList<GameData> gameList = gameDAO.listGames();
        return new ListGamesResult(gameList);
    }

    public CreateGameResult createGame(CreateGameRequest createGameRequest) throws DataAccessException {
        if (createGameRequest.gameName() == null) {
            throw new RequestException("Error: bad request");
        }
        GameData gameData = gameDAO.createGame(createGameRequest.gameName());
        return new CreateGameResult(gameData.gameID());
    }

    public void joinGame(JoinGameRequest joinGameRequest) throws DataAccessException {
        GameData oldGame = gameDAO.getGame((int) Math.round(joinGameRequest.gameID()));
        if (oldGame == null) {
            throw new RequestException("Error: bad request");
        }
        if (!Objects.equals(joinGameRequest.playerColor(), "WHITE") && !Objects.equals(joinGameRequest.playerColor(), "BLACK")) {
            throw new RequestException("Error: bad request");
        }
        if (Objects.equals(joinGameRequest.playerColor(), "WHITE")) {
            if (oldGame.whiteUsername() != null) {
                throw new RequestConflictException("Error: already taken");
            }
            GameData newGame = new GameData(oldGame.gameID(), joinGameRequest.username(), oldGame.blackUsername(),
                    oldGame.gameName(), oldGame.game());
            gameDAO.updateGame(newGame);
        }
        if (Objects.equals(joinGameRequest.playerColor(), "BLACK")) {
            if (oldGame.blackUsername() != null) {
                throw new RequestConflictException("Error: already taken");
            }
            GameData newGame = new GameData(oldGame.gameID(), oldGame.whiteUsername(), joinGameRequest.username(),
                    oldGame.gameName(), oldGame.game());
            gameDAO.updateGame(newGame);
        }
    }

    public void clear() {
        try {
            gameDAO.clear();
        }
        catch (Exception ex) {

        }
    }
}
