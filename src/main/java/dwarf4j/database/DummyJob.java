package dwarf4j.database;

import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

import dwarf4j.framework.jobmanager.GenericJob;

@Component
public class DummyJob extends GenericJob {

  @Override
  protected void executar() throws JobExecutionException {
    this.logger.fatal("XURUVIS");
  }

  @Override
  protected String expressaoCronFrequenciaExecucao() {
    return "0 0/1 * 1/1 * ? *";
  }

}
