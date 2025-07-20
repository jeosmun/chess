package service;

public class ClearService {
    private final UserService userService;
    private final GameService gameService;

    public ClearService(UserService userService, GameService gameService) {
        this.userService = userService;
        this.gameService = gameService;
    }

    public void clear() {
        userService.clear();
        gameService.clear();
    }
}
