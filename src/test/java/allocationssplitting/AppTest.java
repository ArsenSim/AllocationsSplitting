package allocationssplitting;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Arsen on 3/4/2017
 */
public class AppTest {

    @Test
    public void testGroupShoudHaveCorrectNumberOfGroups() {
        Header headerOne = new Header("H-1");
        Header headerTwo = new Header("H-2");

        Allocation allocationOne = new Allocation("A-1", headerOne, createTus(3));
        Allocation allocationTwo = new Allocation("A-2", headerOne, createTus(2));
        Allocation allocationThee = new Allocation("A-3", headerOne, createTus(3));
        Allocation allocationFour = new Allocation("A-4", headerTwo, createTus(3));

        List<Allocation> fourAllocationsThreeGroups = Arrays.asList(
                allocationOne,
                allocationTwo,
                allocationThee,
                allocationFour
        );
        List<Allocation> fiveAllocationsThreeGroups = Arrays.asList(
                allocationOne,
                allocationTwo,
                allocationThee,
                allocationThee,
                allocationFour
        );

        App app = new App();
        assertEquals(3, app.group(fourAllocationsThreeGroups).size());
        assertEquals(3, app.group(fiveAllocationsThreeGroups).size());
    }

    private List<TransportUnit> createTus(int nrOfTus) {
        List<TransportUnit> tus = new ArrayList<>();
        for (int i = 0; i < nrOfTus; i++) {
            TransportUnit tu = new TransportUnit("TU-" + i);
            tus.add(tu);
        }
        return tus;
    }
}