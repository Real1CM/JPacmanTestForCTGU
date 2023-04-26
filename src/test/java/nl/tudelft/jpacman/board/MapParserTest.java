package nl.tudelft.jpacman.board;

import nl.tudelft.jpacman.PacmanConfigurationException;
import nl.tudelft.jpacman.level.LevelFactory;
import nl.tudelft.jpacman.level.MapParser;
import nl.tudelft.jpacman.level.Pellet;
import nl.tudelft.jpacman.npc.Ghost;
import org.junit.jupiter.api.*;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MapParserTest {

    private MapParser mapParser;
    private final LevelFactory levelCreator = mock(LevelFactory.class);
    private final BoardFactory boardFactory = mock(BoardFactory.class);

    @BeforeEach
    void setup(){
        mapParser = new MapParser(levelCreator,boardFactory);

        when(boardFactory.createGround()).thenReturn(mock(Square.class));
        when(boardFactory.createWall()).thenReturn(mock(Square.class));

        when(levelCreator.createGhost()).thenReturn(mock(Ghost.class));
        when(levelCreator.createPellet()).thenReturn(mock(Pellet.class));
    }

    @Test
    @Order(1)
    @DisplayName("空")
    void nullFile(){
        assertThatThrownBy(()->{
            mapParser.parseMap((String)null);
        }).isInstanceOf(NullPointerException.class);
    }

    @Test
    @Order(2)
    @DisplayName("不存在")
    void notExistFile(){
        String filename = "/notexistmap.txt";
        assertThatThrownBy(()->{
            mapParser.parseMap(filename);
        }).isInstanceOf(PacmanConfigurationException.class)
            .hasMessage("Could not get resource for: "+filename);
    }

    @Test
    @Order(3)
    @DisplayName("正常")
    void existFile() throws IOException {
        String filename = "/simplemap.txt";
        mapParser.parseMap(filename);

        verify(boardFactory,times(4)).createGround();
        verify(boardFactory,times(2)).createWall();
        verify(levelCreator,times(1)).createGhost();
    }

    @Test
    @Order(4)
    @DisplayName("无法识别")
    void unrecognizedMap() throws IOException {
        String filename = "/unrecognizedcharMap.txt";
        assertThatThrownBy(()->{
            mapParser.parseMap(filename);
        }).isInstanceOf(PacmanConfigurationException.class)
            .hasMessage("Invalid character at 0,0: A");
    }

    @Test
    @Order(6)
    @DisplayName("空文件")
    void emptyMap() throws IOException{
        String filename  = "/emptyMap.txt";
        assertThatThrownBy(()->{
            mapParser.parseMap(filename);
        }).isInstanceOf(PacmanConfigurationException.class)
            .hasMessage("Input text must consist of at least 1 row.");
    }

    @Test
    @Order(7)
    @DisplayName("lines为空")
    void nolineMap() throws IOException{
        String filename = "/noline.txt";
        assertThatThrownBy(()->{
            mapParser.parseMap(filename);
        }).isInstanceOf(PacmanConfigurationException.class)
            .hasMessage("Input text lines cannot be empty.");
    }
    @Test
    @Order(8)
    @DisplayName("长宽不等的地图")
    void unequalMap() throws IOException{
        String filename = "/unequal.txt";
        assertThatThrownBy(()->{
            mapParser.parseMap(filename);
        }).isInstanceOf(PacmanConfigurationException.class)
            .hasMessage("Input text lines are not of equal width.");
    }
}
