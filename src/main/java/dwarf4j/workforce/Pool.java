package dwarf4j.workforce;

public class Pool extends Thread {

  protected org.apache.log4j.Logger logger;

  public Pool() { this.logger = org.apache.log4j.Logger.getLogger(dwarf4j.workforce.Pool.class); }

  private int loop;
  public Integer getLoop() { return this.loop++; }

  private String nome;
  public String getNome() { return this.nome; }
  public Pool setNome(String nome) { this.nome = nome; return this; }

  private String host;
  public String getHost() { return host; }
  public Pool setHost(String host) { this.host = host; return this; }

  private Integer port;
  public Integer getPort() { return this.port; }
  public Pool setPort(Integer port) { this.port = port; return this; }

  private String user;
  public String getUser() { return this.user; }
  public Pool setUser(String user) { this.user = user; return this; }

  private String password;
  public String getPassword() { return this.password; }
  public Pool setPassword(String password) { this.password = password; return this; }

  private String[] workers;
  public String[] getWorkers() { return this.workers; }
  public Pool setWorkers(String[] workers) { this.workers = workers; return this; }

  private String inscricao;
  public String getInscricao() { return this.inscricao; }
  public Pool setInscricao(String inscricao) { this.inscricao = inscricao; return this; }

  private String extraNonce1;
  public String getExtraNonce1() { return this.extraNonce1; }
  public Pool setExtraNonce1(String extraNonce1) { this.extraNonce1 = extraNonce1; return this; }

  private Integer tamanhoExtraNonce2;
  public Integer getTamanhoExtraNonce2() { return this.tamanhoExtraNonce2; }
  public Pool seTamanhoExtraNonce2(Integer tamanhoExtraNonce2) { this.tamanhoExtraNonce2 = tamanhoExtraNonce2; return this; }

  @SuppressWarnings("rawtypes")
  public void iniciar() throws java.net.UnknownHostException, java.io.IOException {
    StringBuilder sb = new StringBuilder();
    sb.append(this.workers != null ? this.workers.length : 0);
    sb.append(String.format(" worker%s encontrados para ", this.workers != null && this.workers.length == 1 ? "" : "s"));
    this.logger.info(sb.append(String.format("'%s'.", this.nome)).toString());
    this.socket = new java.net.Socket(this.host, this.port);
    this.loop = new Integer(1);
    String comandoSubscribe = this.enviarComando(new Comando(this.getLoop(), Metodo.SUBSCRIBE, new String[] { }));
    java.util.ArrayList respostaSubscribe = (java.util.ArrayList) dwarf4j.utils.GSonUtils.getInstance().json2Map(comandoSubscribe).get("result");
    this.inscricao = (String) ((java.util.ArrayList) ((java.util.ArrayList) respostaSubscribe.get(0)).get(0)).get(1);
    this.extraNonce1 = (String) respostaSubscribe.get(1);
    this.tamanhoExtraNonce2 = ((Double) respostaSubscribe.get(2)).intValue();
    this.logger.debug(String.format("Resposta de '%s': %s", this.nome, comandoSubscribe));
    for(String w : this.workers)
      this.logger.debug(String.format("Resposta de '%s': %s", this.nome, this.enviarComando(new Comando(this.getLoop(), Metodo.AUTHORIZE, new String[] { String.format("%s.%s", this.user, w), this.password }))));
    this.blocos = new java.util.ArrayList<Bloco>();
    this.start();
    this.logger.info(String.format("'%s' iniciado com sucesso!", this.nome));
  }

  private java.util.List<dwarf4j.workforce.Bloco> blocos;
  public java.util.List<dwarf4j.workforce.Bloco> getBlocos() { return this.blocos; }

  @SuppressWarnings({ "rawtypes", "unchecked" })
  @Override
  public void run() {
    this.workerAtual = 0;
    while(true) {
      try {
        java.io.BufferedReader inFromServer = new java.io.BufferedReader(new java.io.InputStreamReader(this.socket.getInputStream()));
        String mensagem = inFromServer.readLine();
        this.logger.debug(String.format("Notifica\u00e7\u00e3o (%s) %s", this.nome, mensagem.replaceAll("[\n\r]", "")));
        java.util.Map<String, Object> resposta = dwarf4j.utils.GSonUtils.getInstance().json2Map(mensagem);
        java.util.ArrayList params = (java.util.ArrayList) resposta.get("params");
        if(params != null && Metodo.NOTIFY.metodo().equals(resposta.get("method"))) {
          try {
            java.util.ArrayList merkleBranches = (java.util.ArrayList) params.get(4);
            dwarf4j.workforce.Bloco bloco = new dwarf4j.workforce.Bloco()
            .setIdJob((String) params.get(0))
            .setHashBlocoAnterior((String) params.get(1))
            .setCoinbaseParte1((String) params.get(2))
            .setCoinbaseParte2((String) params.get(3))
            .setVersaoBlocoBitcoin(Integer.valueOf(dwarf4j.utils.LogicUtils.getInstance().inverterString((String) params.get(5))))
            .setBits((String) params.get(6))
            .setTempo((String) params.get(7))
            .setLimparTrabalho((Boolean) params.get(8))
            .setMerkleBranches(new String[merkleBranches.size()])
            .setNomeWorker(String.format("%s.%s", this.user, this.getWorkerAtual()))
            .setNoncePremiada(-1)
            .setHashPremiada("");
            bloco.setMerkleBranches((String[]) merkleBranches.toArray(bloco.getMerkleBranches()));
            if(bloco.getLimparTrabalho()) { this.blocos.clear(); this.logger.info("Limpando trabalho..."); }
            this.blocos.add(bloco);
            this.logger.info(bloco);
          }
          catch(java.lang.ClassCastException e) { this.logger.error(String.format("N\u00e3o foi poss\u00edvel extrair trabalho do '%s' da mensagem '%s'", this.nome, mensagem)); }
        }
      }
      catch (java.io.IOException e) { this.logger.error(String.format("Erro ao ler notifica\u00e7\u00e3o de '%s' %s", this.nome, e.getMessage())); }
      try { Thread.sleep(1); }
      catch (InterruptedException e) { } // ignore
    }
  }

  private int workerAtual;
  private String getWorkerAtual() {
    if(workerAtual >= this.workers.length) this.workerAtual = 0;
    return this.workers[workerAtual++];
  }

  // Client: {"params": ["slush.miner1", "bf", "00000001", "504e86ed", "b2957c02"], "id": 4, "method": "mining.submit"}\n
  /*
    params[0] = Worker Name
    params[1] = Job ID
    params[2] = ExtraNonce 2
    params[3] = nTime
    params[4] = nonce
   */
  public void enviarSubmission(Bloco bloco) {
    try {
      String submit = this.enviarComando(
        new Comando(
          this.getLoop(),
          Metodo.SUBMIT,
          new String[] {
            bloco.getNomeWorker(),
            bloco.getIdJob(),
            "00000000",
            String.format("%x", java.util.Calendar.getInstance().getTimeInMillis() / 1000),
            String.format("%x", bloco.getNoncePremiada())
          }
        )
      );
      this.logger.warn(String.format("Resposta ao Submit de '%s': %s", this.nome, submit));
    }
    catch (java.io.IOException e) {
      e.printStackTrace();
      System.exit(-1);
    }
  }

  private java.net.Socket socket;
  public java.net.Socket getSocket() { return this.socket; }
  public Pool setSocket(java.net.Socket socket) { this.socket = socket; return this; }

  private String enviarComando(Comando comando) throws java.io.IOException {
    this.logger.debug(String.format("Comando enviado a '%s': %s", this.nome, comando.toString().replaceAll(String.format("(%s)", this.password), "*****")));
    java.io.DataOutputStream outToServer = new java.io.DataOutputStream(this.socket.getOutputStream());
    outToServer.writeBytes(comando.toString().concat(System.getProperty("line.separator")));
    java.io.BufferedReader inFromServer = new java.io.BufferedReader(new java.io.InputStreamReader(this.socket.getInputStream()));
    return inFromServer.readLine();
  }

  private final String CONST_MASCARA_2STR = "{\"rodando\"=%s,\"nome\"=\"%s\",\"host\"=\"%s\",\"port\"=%d,\"user\"=\"%s\",\"password\"=\"%s\",\"workers\"=%s,\"inscricao\"=\"%s\",\"extraNonce1\"=\"%s\",\"tamanhoExtraNonce2\"=%d}";
  @Override
  public String toString() {
    return dwarf4j.utils.GSonUtils.getInstance()
        .obj2Json(dwarf4j.utils.GSonUtils.getInstance()
            .json2Map(
                String.format(
                  this.CONST_MASCARA_2STR,
                    this.isAlive(),
                    this.nome,
                    this.host,
                    this.port,
                    this.user,
                    "*****",
                    dwarf4j.utils.GSonUtils.getInstance().obj2Json(this.workers),
                    this.inscricao,
                    this.extraNonce1,
                    this.tamanhoExtraNonce2
                )
            )
        ).replaceAll("(.0)", "");
  }

}

class Comando {

  public Comando(Integer id, Metodo metodo, String[] params) {
    this.id = id;
    this.method = metodo.metodo();
    this.params = params;
  }

  private Integer id;
  public Integer getId() { return this.id; }

  private String method;
  public String getMethod() { return this.method; }

  private String[] params;
  public String[] getParams() { return this.params; }

  @Override
  public String toString() { return dwarf4j.utils.GSonUtils.getInstance().obj2Json(this).replaceAll("[\n]", ""); }

}

enum Metodo {
  SUBSCRIBE("mining.subscribe"),
  NOTIFY("mining.notify"),
  AUTHORIZE("mining.authorize"),
  SUBMIT("mining.submit"),
  SET_DIFFICULTY("mining.set_difficulty");
  private String metodo;
  private Metodo(String metodo) { this.metodo = metodo; }
  public String metodo() { return this.metodo; }
}
