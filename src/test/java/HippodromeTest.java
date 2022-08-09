import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class HippodromeTest {

    @ParameterizedTest
    @NullSource
    public void constructorIsNull(List<Horse> horses){
        IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class,
                ()-> new Hippodrome(horses)
        );

        assertEquals("Horses cannot be null.", e.getMessage());
    }

    @Test
    public void constructorListEmpty(){
        IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class,
                () -> new Hippodrome(new ArrayList<>())
        );

        assertEquals("Horses cannot be empty.", e.getMessage());
    }

    @Test
    public void getHorsesTest(){
        List<Horse> expectedHorses = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            expectedHorses.add(new Horse("Horse_"+1, i+2, i*2.3));
        }
        Hippodrome hippodrome = new Hippodrome(expectedHorses);
        List<Horse> actualHorses = hippodrome.getHorses();
        for (int i = 0; i < 30; i++) {
            assertEquals(expectedHorses.get(i), actualHorses.get(i));
        }
    }

    @Test
    public void moveTest(){
        List<Horse> expectedHorses = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            Horse mockHorse = Mockito.spy(new Horse("Horse_"+1, i+2, i*2.3));
            expectedHorses.add(mockHorse);
        }
        Hippodrome hippodrome = new Hippodrome(expectedHorses);
        hippodrome.move();

        for (Horse horse: hippodrome.getHorses()) {
            Mockito.verify(horse).move();
        }
    }

    @Test
    public void getWinnerTest(){
        List<Horse> expectedHorses = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            Horse mockHorse = Mockito.spy(new Horse("Horse_"+1, i+2, i*2.3));
            expectedHorses.add(mockHorse);
        }
        Hippodrome hippodrome = new Hippodrome(expectedHorses);
        Horse winner = hippodrome.getWinner();

        for (Horse horse: hippodrome.getHorses()) {
            if (horse.getDistance() > winner.getDistance()){
                fail();
            }
        }

    }
}
