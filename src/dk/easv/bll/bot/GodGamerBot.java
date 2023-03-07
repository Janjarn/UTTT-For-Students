package dk.easv.bll.bot;

import dk.easv.bll.field.IField;
import dk.easv.bll.game.IGameState;
import dk.easv.bll.move.IMove;
import dk.easv.bll.move.Move;

import java.util.Random;

public class GodGamerBot implements IBot{
    private static final String BOTNAME = "GodGamerBot";

    protected int[][] preferredMoves = {
            {0, 1}, {2, 1}, {1, 0}, {1, 2}, //Outer Middles ordered across
            {0, 0}, {2, 2}, {0, 2}, {2, 0},  //Corners ordered across
            {1, 1}}; //Center

    @Override
    public IMove doMove(IGameState state) {
        for (int[] move : preferredMoves)
        {
            if(state.getField().getMacroboard()[move[0]][move[1]].equals(IField.AVAILABLE_FIELD))
            {
                for (int[] selectedMove : preferredMoves)
                {
                    int x = move[0]*3 + selectedMove[0];
                    int y = move[1]*3 + selectedMove[1];
                    if(state.getField().getBoard()[x][y].equals(IField.EMPTY_FIELD))
                    {
                        return new Move(x,y);
                    }
                }
            }
        }

        return state.getField().getAvailableMoves().get(0);
    }

    /*public boolean playedMoveTwice(){

    }*/

    @Override
    public String getBotName() {
        return BOTNAME;
    }
}
