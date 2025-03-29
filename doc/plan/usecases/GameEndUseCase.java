/**
 * Use Case: GAMESTATISTICS-GAME-END-1: Handle Win and Loss Conditions
 * Goal: Handle the winning and losing conditions of the game
 */
class GameEndUseCase {
    public static void main(String[] args) {
        // Step 1: Set up game state
        GameState gameState = new GameState(3, 100);

        // Step 2: Choose a game outcome strategy
        GameOutcomeStrategy winStrategy = new CollectAllDotsStrategy();
        GameOutcomeStrategy loseStrategy = new RunOutOfLivesStrategy();

        // Step 3: Run the game loop
        while (!winStrategy.isGameOver(gameState) && !loseStrategy.isGameOver(gameState)) {
            gameState.loseLife();
            gameState.collectDot();

            System.out.println("Lives: " + gameState.getLives() + ", Remaining Dots: " + gameState.getRemainingDots());
        }

        // Step 4: Determine outcome
        if (loseStrategy.isGameOver(gameState)) {
            System.out.println("Game Over! You lost.");
        } else if (winStrategy.isGameOver(gameState)) {
            System.out.println("Congratulations! You won.");
        }
    }
}