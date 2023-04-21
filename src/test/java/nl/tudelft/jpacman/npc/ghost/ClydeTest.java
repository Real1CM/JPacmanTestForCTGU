package nl.tudelft.jpacman.npc.ghost;

import nl.tudelft.jpacman.Launcher;
import nl.tudelft.jpacman.board.Board;
import nl.tudelft.jpacman.board.BoardFactory;
import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.level.*;
import nl.tudelft.jpacman.npc.Ghost;
import nl.tudelft.jpacman.points.DefaultPointCalculator;
import nl.tudelft.jpacman.points.PointCalculator;
import nl.tudelft.jpacman.sprite.PacManSprites;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

/**
 * @author Real1CM
 */
public class ClydeTest {

    @BeforeEach
    public MapParser setup() {
        PacManSprites sprites = new PacManSprites();
        LevelFactory levelFactory = new LevelFactory(
            sprites,
            new GhostFactory(sprites),
            mock(PointCalculator.class));
        BoardFactory boardFactory = new BoardFactory(sprites);
        GhostFactory ghostFactory = new GhostFactory(sprites);
        return new GhostMapParser(levelFactory, boardFactory, ghostFactory);
    }

    @Test
    void departLessThanEight(){
        MapParser mapParser = setup();
        List<String> text = Lists.newArrayList(
            "##############",
            "#.#....C.....P",
            "##############");
        Level level = mapParser.parseMap(text);
        Clyde clyde = Navigation.findUnitInBoard(Clyde.class,level.getBoard());
        assertThat(clyde).isNotNull();
        assertThat(clyde.getDirection()).isEqualTo(Direction.valueOf("EAST"));
        Player player = new PlayerFactory(new PacManSprites()).createPacMan();
        player.setDirection(Direction.valueOf("WEST"));
        level.registerPlayer(player);
        Player p = Navigation.findUnitInBoard(Player.class,level.getBoard());
        assertThat(p).isNotNull();
        assertThat(p.getDirection()).isEqualTo(Direction.valueOf("WEST"));
        Optional<Direction> opt = clyde.nextAiMove();
        assertThat(opt.get()).isEqualTo(Direction.valueOf("WEST"));
    }
    @Test
    void departMoreThanEight(){
        MapParser mapParser = setup();
        List<String> text = Lists.newArrayList(
            "##############",
            "#.C..........P",
            "##############");
        Level level = mapParser.parseMap(text);
        Clyde clyde = Navigation.findUnitInBoard(Clyde.class,level.getBoard());
        assertThat(clyde).isNotNull();
        assertThat(clyde.getDirection()).isEqualTo(Direction.valueOf("EAST"));
        Player player = new PlayerFactory(new PacManSprites()).createPacMan();
        player.setDirection(Direction.valueOf("WEST"));
        level.registerPlayer(player);
        Player p = Navigation.findUnitInBoard(Player.class,level.getBoard());
        assertThat(p).isNotNull();
        assertThat(p.getDirection()).isEqualTo(Direction.valueOf("WEST"));
        Optional<Direction> opt = clyde.nextAiMove();
        assertThat(opt.get()).isEqualTo(Direction.valueOf("EAST"));
    }

    @Test
    void departWithoutPlayer(){
        //Arrange
        MapParser mapParser = setup();
        List<String> text = Lists.newArrayList(
            "##############",
            "#.C...........",
            "##############");
        Level level = mapParser.parseMap(text);

        //���������ҵ�ħ�����
        Clyde clyde = Navigation.findUnitInBoard(Clyde.class,level.getBoard());
        assertThat(clyde).isNotNull();
        assertThat(clyde.getDirection()).isEqualTo(Direction.valueOf("EAST"));

        //�ж��������Ƿ�����Ϸ����
        assertThat(level.isAnyPlayerAlive()).isFalse();

        //act:
        Optional<Direction> opt = clyde.nextAiMove();

        //assert:
        assertThat(opt.isPresent()).isFalse();
    }

    @Test
    void withoutPathBetweenClydeAndPlayer(){
        MapParser mapParser = setup();
        List<String> text = Lists.newArrayList(
            "#############P",
            "#.C...........",
            "##############");
        Level level = mapParser.parseMap(text);
        Clyde clyde = Navigation.findUnitInBoard(Clyde.class,level.getBoard());
        assertThat(clyde).isNotNull();
        assertThat(clyde.getDirection()).isEqualTo(Direction.valueOf("EAST"));
        assertThat(level.isAnyPlayerAlive()).isFalse();
        Optional<Direction> opt = clyde.nextAiMove();
        assertThat(opt.isPresent()).isFalse();
    }
}
