package service;

import dataaccess.DataAccessException;

public class ClearService {
    private final UserService userService;
    private final GameService gameService;

    public ClearService(UserService userService, GameService gameService) {
        this.userService = userService;
        this.gameService = gameService;
    }

    public void clear() throws DataAccessException {
        userService.clear();
        gameService.clear();
    }
}
