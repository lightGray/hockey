package custom;

import model.*;

import static custom.Util.getRange;
import static custom.Util.myTeamHasPuck;
import static java.lang.StrictMath.PI;
import static java.lang.StrictMath.abs;

public class HockeyistAI {
    private static final double STRIKE_ANGLE = 1.0D * PI / 180.0D;
    private Hockeyist self;

    public HockeyistAI(Hockeyist self) {
        this.self = self;
    }


    public void play(World world, Game game, Move move) {
        defaultPlay(world, game, move);

        //world.getHockeyists() //todo gap add Utils methods getMyHockeyists, getOpponHockeyists
        //todo gap add method isAttackPosition
    }

    private void defaultPlay(World world, Game game, Move move) {
        if (myTeamHasPuck(world, self)) {
            if (selfHasPuck(world)) {
                strikeOnSwingAndRest(move);
                swingIfFaceToNet(turnToNet(world, game, move), move);
            } else {
                goAndStrikeNearestOpponent(world, game, move);
            }
        } else {
            takePuck(world, move);
        }
    }

    private void takePuck(World world, Move move) {
        move.setSpeedUp(1.0D);
        move.setTurn(self.getAngleTo(world.getPuck()));
        move.setAction(ActionType.TAKE_PUCK);
    }

    private void goAndStrikeNearestOpponent(World world, Game game, Move move) {
        Hockeyist nearestOpponent = getNearestOpponent(self.getX(), self.getY(), world);
        if (nearestOpponent != null) {
            if (self.getDistanceTo(nearestOpponent) > game.getStickLength()) {
                move.setSpeedUp(1.0D);
            } else if (abs(self.getAngleTo(nearestOpponent)) < 0.5D * game.getStickSector()) {
                move.setAction(ActionType.STRIKE);
            }
            move.setTurn(self.getAngleTo(nearestOpponent));
        }
    }

    private void swingIfFaceToNet(double angleToNet, Move move) {
        if (abs(angleToNet) < STRIKE_ANGLE) {
            move.setAction(ActionType.SWING);
        }
    }

    private double turnToNet(World world, Game game, Move move) {
        double angleToNet = getAngleToNet(world, game);
        move.setTurn(angleToNet);
        return angleToNet;
    }

    private Hockeyist getNearestOpponent(double x, double y, World world) {
        Hockeyist nearestOpponent = null;
        double nearestOpponentRange = 0.0D;
        for (Hockeyist hockeyist : world.getHockeyists()) {
            if (hockeyist.isTeammate() || hockeyist.getType() == HockeyistType.GOALIE
                    || hockeyist.getState() == HockeyistState.KNOCKED_DOWN
                    || hockeyist.getState() == HockeyistState.RESTING) {
                continue;
            }
            double opponentRange = getRange(x, y, self.getX(), self.getY());
            if (nearestOpponent == null || opponentRange < nearestOpponentRange) {
                nearestOpponent = hockeyist;
                nearestOpponentRange = opponentRange;
            }
        }
        return nearestOpponent;
    }

    private double getAngleToNet(World world, Game game) {
        Player opponentPlayer = world.getOpponentPlayer();
        double netX = 0.5D * (opponentPlayer.getNetBack() + opponentPlayer.getNetFront());
        double netY = 0.5D * (opponentPlayer.getNetBottom() + opponentPlayer.getNetTop());
        netY += (self.getY() < netY ? 0.5D : -0.5D) * game.getGoalNetHeight();
        return self.getAngleTo(netX, netY);
    }

    private void strikeOnSwingAndRest(Move move) {
        if (self.getState() == HockeyistState.SWINGING) {
            move.setAction(ActionType.STRIKE);
            throw new ReturnException();
        }
    }

    private boolean selfHasPuck(World world) {
        return world.getPuck().getOwnerHockeyistId() == self.getId();
    }
}
