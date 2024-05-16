import org.junit.jupiter.api.Test;
import ru.nsu.ccfit.chumak.model.Point;
import ru.nsu.ccfit.chumak.model.Shape;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GeometryTest {
    @Test
    void lineNestingTest(){
        assertEquals(1, Shape.checkNesting(new Point(840,120),new Point(600,120),new Point(744,120),new Point(840,120)));
        assertEquals(1, Shape.checkNesting(new Point(840,120),new Point(600,120),new Point(600,120),new Point(696,120)));
        assertEquals(1, Shape.checkNesting(new Point(840,120),new Point(600,120),new Point(696,120),new Point(744,120)));
    }
}
