package dwarf4j.database;

@org.springframework.context.annotation.Configuration
@org.springframework.context.annotation.ComponentScan("dwarf4j.database")
@org.springframework.data.jpa.repository.config.EnableJpaRepositories("dwarf4j.database")
@org.springframework.context.annotation.Import(dwarf4j.framework.jobmanager.JobManager.class)
public class Dwarf4JSpringConfig extends dwarf4j.framework.orm.generic.GenericSpringConfig {

  @Override
  public dwarf4j.framework.orm.generic.Configuracoes configuracoes() {
    dwarf4j.framework.orm.generic.Configuracoes config = new dwarf4j.framework.orm.generic.Configuracoes();
    // (...)
    return config;
  }

  @org.springframework.context.annotation.Bean
  public dwarf4j.workforce.Pool antpool() {
    return new dwarf4j.workforce.Pool()
      .setNome("Antpool")
      .setHost("stratum.antpool.com")
      .setPort(3333)
      .setUser("fmaia92")
      .setPassword("xxx")
      .setWorkers(new String[] { "alpha", });
  }

}
