package at.ac.tuwien.qs.movierental;

import java.util.List;

public interface DAO<ENTITY> {

    ENTITY create(ENTITY entity);

    ENTITY read(Long id);

    List<ENTITY> read();

    ENTITY update(ENTITY entity);

    void delete(ENTITY entity);

}
