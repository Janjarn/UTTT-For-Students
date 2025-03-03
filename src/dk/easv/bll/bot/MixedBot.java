package dk.easv.bll.bot;

import dk.easv.bll.field.IField;
import dk.easv.bll.game.IGameState;
import dk.easv.bll.move.IMove;
import dk.easv.bll.move.Move;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;


public class MixedBot implements IBot {
    private static final String BOTNAME = "MixedBot";
    private int moveCounter;
    private Random random;

    protected int[][] preferredMoves = {
            {1, 1}, //Center
            {0, 0}, {2, 2}, {0, 2}, {2, 0},  //Corners ordered across
            {0, 1}, {2, 1}, {1, 0}, {1, 2}}; //Outer Middles ordered across

    public MixedBot() {
        this.moveCounter = 0;
        this.random = new Random();
    }

    @Override
    public IMove doMove(IGameState state) {
    List<IMove> winMoves = getWinningMoves(state);
        if(!winMoves.isEmpty())
            return winMoves.get(0);

        return mixedMoves(state);
    }

    // Plays random at odd rounds (1st, 3rd round etc.) and plays preferred moves at even rounds (2nd, 4th...)
    public IMove mixedMoves(IGameState state) {
        List<IMove> moves = state.getField().getAvailableMoves();
        for (int[] move : preferredMoves)
            if (this.moveCounter % 2 == 0) {
                moveCounter++;
                return moves.get(random.nextInt(moves.size()));
            } else if(state.getField().getMacroboard()[move[0]][move[1]].equals(IField.AVAILABLE_FIELD)) {
                for (int[] selectedMove : preferredMoves)
                {
                    int x = move[0]*3 + selectedMove[0];
                    int y = move[1]*3 + selectedMove[1];
                    if(state.getField().getBoard()[x][y].equals(IField.EMPTY_FIELD))
                    {
                        moveCounter++;
                        return new Move(x,y);
                    }
                }
            }
        return state.getField().getAvailableMoves().get(0);
    }

    private boolean isWinningMove(IGameState state, IMove move, String player){
        /*String[][] board = state.getField().getBoard();
        boolean isRowWin = true;
        // Row checking
        int startX = move.getX()-(move.getX()%3);
        int endX = startX + 2;
        for (int x = 0; x < startX; x++) {
            if (x!=move.getX())
                if (!board[x][move.getY()].equals(player))
                    isRowWin = false;
        }

        boolean isColumnWin = true;
        // Column chcking
        int startY = move.getY()-(move.getY()%3);
        int endY = startY + 2;
        for (int y = 0; y < startX; y++) {
            if (y != move.getY())
                if (!board[move.getX()][y].equals(player))
                    isColumnWin = false;
        }

        boolean isDiagWin = true;
        // Diagonal checking left-top to right-bottom
        if (!(move.getX()==startX && move.getY()==startY))
            if (!board[startX][startY].equals(player))
                isDiagWin = false;
        if (!(move.getX()==startX+1 && move.getY()==startY+1))
            if (!board[startX+1][startY+2].equals(player))
                isDiagWin = false;
        if (!(move.getX()==startX+2 && move.getY()==startY+2))
            if (!board[startX+2][startY+2].equals(player))
                isDiagWin = false;

        boolean isOppositeDiagWin = true;
        // Diagonal checking left-top to right-bottom
        if (!(move.getX()==startX && move.getY()==startY+2))
            if (!board[startX][startY+2].equals(player))
                isOppositeDiagWin = false;
        if (!(move.getX()==startX+1 && move.getY()==startY+1))
            if (!board[startX+1][startY+1].equals(player))
                isOppositeDiagWin = false;
        if (!(move.getX()==startX+2 && move.getY()==startY))
            if (!board[startX+2][startY].equals(player))
                isOppositeDiagWin = false;

        return isColumnWin || isDiagWin || isOppositeDiagWin || isRowWin;*/

        // Clones the array and all values to a new array, so we don't mess with the game
        String[][] board = Arrays.stream(state.getField().getBoard()).map(String[]::clone).toArray(String[][]::new);

        //Places the player in the game. Sort of a simulation.
        board[move.getX()][move.getY()] = player;

        int startX = move.getX()-(move.getX()%3);
        if(board[startX][move.getY()]==player)
            if (board[startX][move.getY()] == board[startX+1][move.getY()] &&
                    board[startX+1][move.getY()] == board[startX+2][move.getY()])
                return true;

        int startY = move.getY()-(move.getY()%3);
        if(board[move.getX()][startY]==player)
            if (board[move.getX()][startY] == board[move.getX()][startY+1] &&
                    board[move.getX()][startY+1] == board[move.getX()][startY+2])
                return true;


        if(board[startX][startY]==player)
            if (board[startX][startY] == board[startX+1][startY+1] &&
                    board[startX+1][startY+1] == board[startX+2][startY+2])
                return true;

        if(board[startX][startY+2]==player)
            if (board[startX][startY+2] == board[startX+1][startY+1] &&
                    board[startX+1][startY+1] == board[startX+2][startY])
                return true;

        return false;
    }
    // Compile a list of all available winning moves
    private List<IMove> getWinningMoves(IGameState state){
        String player = "1";
        if(state.getMoveNumber()%2==0)
            player="0";

        List<IMove> avail = state.getField().getAvailableMoves();

        List<IMove> winningMoves = new ArrayList<>();
        for (IMove move:avail) {
            if(isWinningMove(state,move,player))
                winningMoves.add(move);
        }
        return winningMoves;
    }

    @Override
    public String getBotName() {
        return BOTNAME;
    }
}