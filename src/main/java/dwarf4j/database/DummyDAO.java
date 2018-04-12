package dwarf4j.database;

import org.springframework.stereotype.Repository;

import dwarf4j.framework.orm.generic.GenericRepository;

@Repository
public class DummyDAO extends GenericRepository<Dummy, Integer> {

}
