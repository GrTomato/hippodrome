import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class HorseTest {

    @ParameterizedTest
    @NullSource
    public void WhenNameIsNull_ThenThrowException_WithMessage(String name){
        assertAll(
                "Tests if name is null",
                () -> assertThrows(
                        IllegalArgumentException.class,
                        () -> { new Horse(name, 10.0, 10.0) ; }
                ),
                () -> {
                    try{
                        new Horse(name, 10.0, 10.0);
                    } catch (IllegalArgumentException e){
                        assertEquals("Name cannot be null.", e.getMessage());
                    }
                }
        );
    }

    @ParameterizedTest
    @EmptySource
    @ValueSource(strings = {"  ", "\t", "\n"})
    public void WhenNameIsBlank_ThenThrowExceptionWithMessage(String name){
        assertAll(
                "Test if name is blank",
                () -> assertThrows(
                        IllegalArgumentException.class,
                        () -> { new Horse(name, 10.0, 10.0) ; }
                ),
                () -> {
                    try{
                        new Horse(name, 10.0, 10.0);
                    } catch (IllegalArgumentException e){
                        assertEquals("Name cannot be blank.", e.getMessage());
                    }
                }
        );
    }

    @ParameterizedTest
    @ValueSource(doubles = {-2.0, -4.0, -9.0})
    public void WhenSpeedIsNegative_ThenThrowExceptionWithMessage(double speed){
        assertAll(
                "Test if speed is negative",
                () -> assertThrows(
                        IllegalArgumentException.class,
                        () -> {new Horse("Hugo", speed, 10.0) ; }
                ),
                () -> {
                    try{
                        new Horse("Hugo", speed, 10.0);
                    } catch (IllegalArgumentException e){
                        assertEquals("Speed cannot be negative.", e.getMessage());
                    }
                }
        );
    }

    @ParameterizedTest
    @ValueSource(doubles = {-2.0, -4.0, -9.0})
    public void WhenDistanceIsNegative_ThenThrowExceptionWithMessage(double distance){
        assertAll(
                "Test if speed is negative",
                () -> assertThrows(
                        IllegalArgumentException.class,
                        () -> {new Horse("Hugo", 10.0, distance) ; }
                ),
                () -> {
                    try{
                        new Horse("Hugo", 10.0, distance);
                    } catch (IllegalArgumentException e){
                        assertEquals("Distance cannot be negative.", e.getMessage());
                    }
                }
        );
    }

    @Test
    public void getName(){
        Horse horse = new Horse("Hugo", 10.0, 12.0);
        String actual = horse.getName();
        assertEquals("Hugo", actual);
    }

    @Test
    public void getSpeed(){
        Horse horse = new Horse("Hugo", 10.0, 12.0);
        double actual = horse.getSpeed();
        assertEquals(10.0, actual);
    }

    @Test
    public void getDistance(){
        Horse horse = new Horse("Hugo", 10.0, 12.0);
        double actual = horse.getDistance();
        assertEquals(12.0, actual);
    }

    @Test
    public void getZeroDistance() throws NoSuchFieldException, IllegalAccessException {
        Horse horse = new Horse("Hugo", 10.0);

        // constructor check
        Field distance = Horse.class.getDeclaredField("distance");
        distance.setAccessible(true);
        double actualDistance = (double) distance.get(horse);
        assertEquals(0, actualDistance);

        assertEquals(0, horse.getDistance());
    }

    @Test
    public void getRandomDoubleIsUsed(){
        Horse horse = new Horse("Hugo", 10.0);
        try ( MockedStatic<Horse> dummyMock = Mockito.mockStatic(Horse.class) ) {
            horse.move();
            dummyMock.verify(()->Horse.getRandomDouble(0.2, 0.9));
        }
    }

    @ParameterizedTest
    @CsvSource(value = {"3.0","5.0"}, delimiter = ',')
    public void moveMethodCorrect(double result){
        try ( MockedStatic<Horse> dummyMock = Mockito.mockStatic(Horse.class) ) {
            dummyMock.when(()-> Horse.getRandomDouble(0.2, 0.9)).thenReturn(result);
            Horse horse = new Horse("Hugo", 0.2, 0.9);
            horse.move();
            double expected = 0.9 + (0.2 * result);
            assertEquals(expected, horse.getDistance());
        }
    }

}
