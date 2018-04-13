package dwarf4j;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Calendar;

public class App {

  public static void main(String[] args) throws UnknownHostException, IOException {

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

    long tempo = Calendar.getInstance().getTimeInMillis();
    System.out.println(b.minerar());
    System.out.println(String.format("Tempo para calcular 1m hashes: %f", (Calendar.getInstance().getTimeInMillis() - tempo) / 1000.0f));

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
