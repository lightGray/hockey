package custom;

import model.Hockeyist;
import model.World;

import static java.lang.StrictMath.hypot;

public class Util {


    public static double getRange(double targetX, double targetY, double x, double y) {
        return hypot(targetX - x, targetY - y);
    }

    public static boolean myTeamHasPuck(World world, Hockeyist self) {
        return world.getPuck().getOwnerPlayerId() == self.getPlayerId();
    }
}
