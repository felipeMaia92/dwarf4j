package dwarf4j.workforce;

import dwarf4j.utils.GSonUtils;

public class Bloco {

  public Bloco() { }

  private String idJob;
  public String getIdJob() { return this.idJob; }
  public Bloco setIdJob(String idJob) { this.idJob = idJob; return this; }

  private String hashBlocoAnterior;
  public String getHashBlocoAnterior() { return this.hashBlocoAnterior; }
  public Bloco setHashBlocoAnterior(String hashBlocoAnterior) { this.hashBlocoAnterior = hashBlocoAnterior; return this; }

  private String coinbaseParte1;
  public String getCoinbaseParte1() { return this.coinbaseParte1; }
  public Bloco setCoinbaseParte1(String coinbaseParte1) { this.coinbaseParte1 = coinbaseParte1; return this; }

  private String coinbaseParte2;
  public String getCoinbaseParte2() { return this.coinbaseParte2; }
  public Bloco setCoinbaseParte2(String coinbaseParte2) { this.coinbaseParte2 = coinbaseParte2; return this; }

  private String[] merkleBranches;
  public String[] getMerkleBranches() { return this.merkleBranches; }
  public Bloco setMerkleBranches(String[] merkleBranches) { this.merkleBranches = merkleBranches; return this; }

  private Integer versaoBlocoBitcoin;
  public Integer getVersaoBlocoBitcoin() { return this.versaoBlocoBitcoin; }
  public Bloco setVersaoBlocoBitcoin(Integer versaoBlocoBitcoin) { this.versaoBlocoBitcoin = versaoBlocoBitcoin; return this; }

  private String bits;
  public String getBits() { return this.bits; }
  public Bloco setBits(String bits) { this.bits = bits; return this; }

  private String tempo;
  public String getTempo() { return this.tempo; }
  public Bloco setTempo(String tempo) { this.tempo = tempo; return this; }
  public java.util.Date getDataHora() {
    java.util.Calendar c = java.util.Calendar.getInstance();
    c.setTimeInMillis(new java.math.BigInteger(this.tempo, 16).longValue() * 1000L);
    return c.getTime();
  }

  private Boolean limparTrabalho;
  public Boolean getLimparTrabalho() { return this.limparTrabalho; }
  public Bloco setLimparTrabalho(Boolean limparTrabalho) { this.limparTrabalho = limparTrabalho; return this; }

  private String nomeWorker;
  public String getNomeWorker() { return this.nomeWorker; }
  public Bloco setNomeWorker(String nomeWorker) { this.nomeWorker = nomeWorker; return this; }

  private String hashPremiada;
  public String getHashPremiada() { return this.hashPremiada; }
  public Bloco setHashPremiada(String hashPremiada) { this.hashPremiada = hashPremiada; return this; }

  private int noncePremiada;
  public int getNoncePremiada() { return this.noncePremiada; }
  public Bloco setNoncePremiada(int noncePremiada) { this.noncePremiada = noncePremiada; return this; }

  @Override
  public String toString() { return GSonUtils.getInstance().obj2Json(this); }

  public Bloco minerar() {

    long bits = new java.math.BigInteger(this.getBits(), 16).longValue();
    long exp = bits >> 24;
    long mant = bits & 0xffffff;
    String alvoHexStr = String.format("%064x", (mant * (1 << (8 * (exp - 3)))));
    alvoHexStr = alvoHexStr.substring(40, 64).concat(alvoHexStr.substring(0, 40));
    final java.math.BigInteger alvo = new java.math.BigInteger(alvoHexStr, 16);
    final int alvoStrlen = new String(alvo.toByteArray()).length() * 2;

    for(String merkleBranch : this.getMerkleBranches()) {
      java.nio.ByteBuffer tempBuffer = java.nio.ByteBuffer.allocate(4);
      tempBuffer.putInt(this.getVersaoBlocoBitcoin());
      String parte1 = dwarf4j.utils.LogicUtils.getInstance().inverterString(new String(tempBuffer.array()));
      String parte2 = dwarf4j.utils.LogicUtils.getInstance().inverterString(dwarf4j.utils.LogicUtils.getInstance().hex2Str(this.getHashBlocoAnterior()));
      String parte3 = dwarf4j.utils.LogicUtils.getInstance().inverterString(dwarf4j.utils.LogicUtils.getInstance().hex2Str(merkleBranch));
      StringBuilder parte4 = new StringBuilder();
      tempBuffer = java.nio.ByteBuffer.allocate(8);
      tempBuffer.putInt(new java.math.BigInteger(this.getBits(), 16).intValue());
      tempBuffer.putInt(new java.math.BigInteger(this.getTempo(), 16).intValue());
      for(int i = tempBuffer.array().length - 1; i > -1; i--)
        parte4.append(tempBuffer.array()[i] < 0 ? (char) (tempBuffer.array()[i] + 0x100) : (char) tempBuffer.array()[i]);
      final String prefixoHeader = parte1.concat(parte2.concat(parte3.concat(parte4.toString())));

      // LÓGICA MATADORA - I
      for(int nonce = 855192327; nonce < dwarf4j.utils.LogicUtils.getInstance().getMaxNonce(); nonce++) {
        String sufixoHeader = dwarf4j.utils.LogicUtils.getInstance().inverterString(new String(java.nio.ByteBuffer.allocate(4).putInt(nonce).array()));
        String header = prefixoHeader.concat(sufixoHeader);
        String duploSHA256 = dwarf4j.utils.LogicUtils.getInstance().str2SHA256(dwarf4j.utils.LogicUtils.getInstance().str2SHA256(header));
        byte[] hash = dwarf4j.utils.LogicUtils.getInstance().inverterString(duploSHA256).getBytes(java.nio.charset.StandardCharsets.ISO_8859_1);
        final String hashStr = String.format("%040x", new java.math.BigInteger(1, hash)).toLowerCase();
        if(hashStr.length() < alvoStrlen) return this.setHashPremiada(hashStr).setNoncePremiada(nonce);
      }
      // LÓGICA MATADORA - F

    }

    return this;
  }

}
