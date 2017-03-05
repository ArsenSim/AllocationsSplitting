package allocationssplitting;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

/**
 * Created by Arsen on 3/4/2017
 */
class App {

    List<Group> group(List<Allocation> allocations) {
        return allocations.stream().collect(new GroupByHeaderAndTransportUnits());
    }
}

class Header {
    private final String name;

    Header(String name) {
        this.name = name;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Header transportUnit = (Header) o;
        return name.equals(transportUnit.name);
    }

    public int hashCode() {
        return 31 * name.hashCode();
    }
}

class TransportUnit {
    private final String name;

    TransportUnit(String name) {
        this.name = name;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransportUnit that = (TransportUnit) o;
        return name.equals(that.name);
    }

    public int hashCode() {
        return name.hashCode();
    }
}

class Allocation {
    private final String name;
    private final Header header;
    private final List<TransportUnit> tus;

    Allocation(String name, Header header, List<TransportUnit> tus) {
        this.name = name;
        this.header = header;
        this.tus = tus;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Allocation that = (Allocation) o;
        if (!name.equals(that.name)) return false;
        if (!header.equals(that.header)) return false;
        return tus.equals(that.tus);
    }

    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + header.hashCode();
        result = 31 * result + tus.hashCode();
        return result;
    }

    Header getHeader() {
        return header;
    }

    public List<TransportUnit> getTus() {
        return tus;
    }
}

class Group {
    private final List<Allocation> allocations = new ArrayList<>();

    Group(Allocation allocation) {
        allocations.add(allocation);
    }

    Header getHeader() {
        return allocations.get(0).getHeader();
    }

    List<TransportUnit> getTransportUnits() {
        return allocations.get(0).getTus();
    }

    void include(Allocation allocation) {
        allocations.add(allocation);
    }

    boolean canInclude(Allocation allocation) {
        Allocation thisAllocation = allocations.get(0);
        return thisAllocation.getHeader().equals(allocation.getHeader())
                && thisAllocation.getTus().equals(allocation.getTus());
    }

    List<Group> splitByArticlePackageQuantity() {
        return null; //here will be logic where group can split itself into other groups by article package
    }
}

class GroupByHeaderAndTransportUnits implements Collector<Allocation, List<Group>, List<Group>> {
    @Override
    public Supplier<List<Group>> supplier() {
        return ArrayList::new;
    }

    @Override
    public BiConsumer<List<Group>, Allocation> accumulator() {
        return (gs, a) -> {
            for (Group g : gs) {
                if (g.canInclude(a)) {
                    g.include(a);
                    return;
                }
            }
            gs.add(new Group(a));
        };
    }

    @Override
    public BinaryOperator<List<Group>> combiner() {
        return (left, right) -> {
            left.addAll(right);
            return left;
        };
    }

    @Override
    public Function<List<Group>, List<Group>> finisher() {
        return Function.identity();
    }

    @Override
    public Set<Characteristics> characteristics() {
        return new HashSet<>();
    }
}
