package dwarf4j.database;

import org.springframework.stereotype.Service;

import dwarf4j.framework.orm.generic.GenericServiceImpl;

@Service
public class DummyServiceImpl extends GenericServiceImpl<Dummy, Integer, DummyDAO> implements DummyService {

}
