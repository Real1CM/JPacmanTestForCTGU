package nl.tudelft.jpacman;

import nl.tudelft.jpacman.level.CollisionMap;
import nl.tudelft.jpacman.level.Pellet;
import nl.tudelft.jpacman.level.Player;
import nl.tudelft.jpacman.level.PlayerCollisions;
import nl.tudelft.jpacman.npc.Ghost;
import nl.tudelft.jpacman.points.PointCalculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;


public abstract class CollisionMapTest {

    private PointCalculator pointCalculator;
    private Player player;
    private Pellet pellet;
    private Ghost ghost;
    private CollisionMap Collisions;

    public void setPointCalculator(PointCalculator pointCalculator) {
        this.pointCalculator = pointCalculator;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setPellet(Pellet pellet) {
        this.pellet = pellet;
    }

    public void setGhost(Ghost ghost) {
        this.ghost = ghost;
    }

    public void setPlayerCollisions(CollisionMap Collisions) {
        this.Collisions = Collisions;
    }

    public PointCalculator getPointCalculator() {
        return pointCalculator;
    }

    @BeforeEach
    void init() {
        this.setPointCalculator(Mockito.mock(PointCalculator.class));
        this.setPlayer(Mockito.mock(Player.class));
        this.setPellet(Mockito.mock(Pellet.class));
        this.setGhost(Mockito.mock(Ghost.class));
        this.setPlayerCollisions(new PlayerCollisions(this.getPointCalculator()));
    }

    @Test
    void testPlayerPellet() {
        Collisions.collide(player, pellet);
        Mockito.verify(pointCalculator, Mockito.times(1)).consumedAPellet(
            Mockito.eq(player),
            Mockito.eq(pellet)
        );
        Mockito.verify(pellet, Mockito.times(1)).leaveSquare();
        Mockito.verifyNoMoreInteractions(player, pellet);
    }

    @Test
    void testPelletPlayer() {
        Collisions.collide(pellet, player);
        Mockito.verify(pointCalculator, Mockito.times(1)).consumedAPellet(
            Mockito.eq(player),
            Mockito.eq(pellet)
        );
        Mockito.verify(pellet, Mockito.times(1)).leaveSquare();
        Mockito.verifyNoMoreInteractions(pellet, player);
    }

    @Test
    void testPlayerGhost() {
        Collisions.collide(player, ghost);
         Mockito.verify(pointCalculator, Mockito.times(1)).collidedWithAGhost(
            Mockito.eq(player),
            Mockito.eq(ghost)
        );
        Mockito.verify(player, Mockito.times(1)).setAlive(false);
        Mockito.verify(player, Mockito.times(1)).setKiller(Mockito.eq(ghost));
        Mockito.verifyNoMoreInteractions(player, ghost);
    }

    @Test
    void testGhostPlayer() {
        Collisions.collide(ghost, player);
        Mockito.verify(pointCalculator, Mockito.times(1)).collidedWithAGhost(
            Mockito.eq(player),
            Mockito.eq(ghost)
        );
        Mockito.verify(player, Mockito.times(1)).setAlive(false);
        Mockito.verify(player, Mockito.times(1)).setKiller(Mockito.eq(ghost));
        Mockito.verifyNoMoreInteractions(player, ghost);
    }

    @Test
    void testGhostPellet() {
        Collisions.collide(ghost, pellet);
        Mockito.verifyNoMoreInteractions(ghost, pellet);
    }

    @Test
    void testPelletGhost() {
        Collisions.collide(pellet, ghost);
        Mockito.verifyNoMoreInteractions(pellet, ghost);
    }

    @Test
    void testGhostGhost() {
        Ghost ghost1 = Mockito.mock(Ghost.class);
        Collisions.collide(ghost, ghost1);
        Mockito.verifyNoMoreInteractions(ghost, ghost1);
    }

    @Test
    void testPelletPellet() {
        Pellet pellet1 = Mockito.mock(Pellet.class);
        Collisions.collide(pellet, pellet1);
        Mockito.verifyNoMoreInteractions(pellet, pellet1);
    }

    @Test
    void testTwoPlayer() {
        Player player1 = Mockito.mock(Player.class);
        Collisions.collide(player, player1);
        Mockito.verifyNoMoreInteractions(player, player1);
    }
}
