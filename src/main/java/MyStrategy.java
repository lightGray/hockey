import custom.HockeyistAI;
import custom.ReturnException;
import model.Game;
import model.Hockeyist;
import model.Move;
import model.World;

public final class MyStrategy implements Strategy {


    @Override
    public void move(Hockeyist self, World world, Game game, Move move) {
        try {
            new HockeyistAI(self).play(world, game, move);
            System.out.println("Useless string");
        } catch (ReturnException e) {
        }
    }
}