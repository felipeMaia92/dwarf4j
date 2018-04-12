package dwarf4j;

public class App {

  @SuppressWarnings("resource")
  public static void main(String[] args) {
    org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(App.class);
    logger.info("******** Iniciando Dwarf4J ********");

    org.springframework.context.ApplicationContext context = new org.springframework.context.annotation.AnnotationConfigApplicationContext(dwarf4j.database.Dwarf4JSpringConfig.class);
    dwarf4j.workforce.Pool antpool = (dwarf4j.workforce.Pool) context.getBean("antpool");
    try {
      antpool.iniciar();
    }
    catch(Exception e) {
      e.printStackTrace();
      System.exit(-1);
    }


    /*
    dwarf4j.workforce.Bloco b = new dwarf4j.workforce.Bloco();
    b.setIdJob("dummy");
    b.setCoinbaseParte1("a");
    b.setCoinbaseParte2("b");
    b.setVersaoBlocoBitcoin(2);
    b.setHashBlocoAnterior("000000000000000117c80378b8da0e33559b5997f2ad55e2f7d18ec1975b9717");
    b.setMerkleBranches(new String[] {"871714dcbae6c8193a2bb9b2a69fe1c0440399f38d94b3a0f1b447275a29978a"});
    b.setTempo("53058b35");
    b.setBits("19015f53");
    b.setLimparTrabalho(false);
    b.setNomeWorker("teste");
    System.out.println(b.minerar());
    */

    /*
    dwarf4j.workforce.Pool antpool = new dwarf4j.workforce.Pool();
    antpool.setNome("Antpool");
    antpool.setHost("stratum.antpool.com");
    antpool.setPort(3333);
    antpool.setUser("fmaia92");
    antpool.setPassword("xxx");
    antpool.setWorkers(new String[] { "alpha", });
    antpool.iniciar();

    while(true) {
      System.err.println(String.format("Qtd. Blocos: %d", antpool.getBlocos().size()));
      try { Thread.sleep(10000); }
      catch (InterruptedException e) { } // ignore
    }
    */

  }

}
