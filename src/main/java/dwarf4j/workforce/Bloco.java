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
      String parte1 = Bloco.inverterString(new String(tempBuffer.array()));
      String parte2 = Bloco.inverterString(this.hex2Str(this.getHashBlocoAnterior()));
      String parte3 = Bloco.inverterString(this.hex2Str(merkleBranch));
      StringBuilder parte4 = new StringBuilder();
      tempBuffer = java.nio.ByteBuffer.allocate(8);
      tempBuffer.putInt(new java.math.BigInteger(this.getBits(), 16).intValue());
      tempBuffer.putInt(new java.math.BigInteger(this.getTempo(), 16).intValue());
      for(int i = tempBuffer.array().length - 1; i > -1; i--)
        parte4.append(tempBuffer.array()[i] < 0 ? (char) (tempBuffer.array()[i] + 0x100) : (char) tempBuffer.array()[i]);
      final String prefixoHeader = parte1.concat(parte2.concat(parte3.concat(parte4.toString())));
      // LÓGICA MATADORA - I
      for(int nonce = 10; nonce < Bloco.MAX_NONCE; nonce++) {
        String sufixoHeader = inverterString(new String(java.nio.ByteBuffer.allocate(4).putInt(nonce).array()));
        String header = prefixoHeader.concat(sufixoHeader);
        byte[] hash = SHA256.hash(SHA256.hash(header.getBytes(java.nio.charset.StandardCharsets.ISO_8859_1)));
        final String hashStr = String.format("%040x", new java.math.BigInteger(1, this.inverterArrayBytes(hash))).toLowerCase();
        if(hashStr.length() < alvoStrlen) return this.setHashPremiada(hashStr).setNoncePremiada(nonce);
        break;
      }
      // LÓGICA MATADORA - F

    }

    return this;
  }

  private static final long MAX_NONCE = 0xffffffffL;

  private String hex2Str(String hexStr) {
    StringBuilder saida = new StringBuilder();
    for (int i = 0; i < hexStr.length(); i += 2)
      saida.append((char) Integer.parseInt(hexStr.substring(i, i + 2), 16));
    return saida.toString();
  }

  public static String inverterString(String entrada) {
    StringBuilder saida = new StringBuilder();
    for(int i = entrada.length() - 1; i >= 0; i--)
      saida.append(entrada.charAt(i));
    return saida.toString();
  }

  private byte[] inverterArrayBytes(byte[] arrayByte) {
    byte[] saida = new byte[arrayByte.length];
    for(int i = arrayByte.length - 1, j = 0; i >= 0;)
      saida[j++] = arrayByte[i--];
    return saida;
  }

}

class SHA256 {

  public static byte[] hash(byte[] mensagem) {
    int[] palavra = new int[64];
    int[] temp = new int[8];
    int[] hash = new int[] {
      0x6a09e667, 0xbb67ae85,
      0x3c6ef372, 0xa54ff53a,
      0x510e527f, 0x9b05688c,
      0x1f83d9ab, 0x5be0cd19,
    };
    final int[] CONST = {
      0x428a2f98, 0x71374491, 0xb5c0fbcf, 0xe9b5dba5, 0x3956c25b, 0x59f111f1, 0x923f82a4, 0xab1c5ed5,
      0xd807aa98, 0x12835b01, 0x243185be, 0x550c7dc3, 0x72be5d74, 0x80deb1fe, 0x9bdc06a7, 0xc19bf174,
      0xe49b69c1, 0xefbe4786, 0x0fc19dc6, 0x240ca1cc, 0x2de92c6f, 0x4a7484aa, 0x5cb0a9dc, 0x76f988da,
      0x983e5152, 0xa831c66d, 0xb00327c8, 0xbf597fc7, 0xc6e00bf3, 0xd5a79147, 0x06ca6351, 0x14292967,
      0x27b70a85, 0x2e1b2138, 0x4d2c6dfc, 0x53380d13, 0x650a7354, 0x766a0abb, 0x81c2c92e, 0x92722c85,
      0xa2bfe8a1, 0xa81a664b, 0xc24b8b70, 0xc76c51a3, 0xd192e819, 0xd6990624, 0xf40e3585, 0x106aa070,
      0x19a4c116, 0x1e376c08, 0x2748774c, 0x34b0bcb5, 0x391c0cb3, 0x4ed8aa4a, 0x5b9cca4f, 0x682e6ff3,
      0x748f82ee, 0x78a5636f, 0x84c87814, 0x8cc70208, 0x90befffa, 0xa4506ceb, 0xbef9a3f7, 0xc67178f2,
    };
    int novoTamanhoMensagem = mensagem.length + 9;
    int padBytes = 64 - (novoTamanhoMensagem % 64);
    novoTamanhoMensagem += padBytes;
    byte[] mensagemNormalizada = new byte[novoTamanhoMensagem];
    System.arraycopy(mensagem, 0, mensagemNormalizada, 0, mensagem.length);
    mensagemNormalizada[mensagem.length] = (byte) 0x80;
    java.nio.ByteBuffer.wrap(mensagemNormalizada, mensagem.length + padBytes + 1, 8).putLong(mensagem.length * 8);
    java.nio.ByteBuffer buffer = java.nio.ByteBuffer.wrap(mensagemNormalizada);
    int[] palavras = new int[mensagemNormalizada.length / 4];
    for(int i = 0; i < palavras.length; ++i) palavras[i] = buffer.getInt();
    for(int i = 0, n = palavras.length / 16; i < n; ++i) {
      System.arraycopy(palavras, i * 16, palavra, 0, 16);
      for (int t = 16; t < palavra.length; ++t)
        palavra[t] = (Integer.rotateRight(palavra[t - 2], 17) ^ Integer.rotateRight(palavra[t - 2], 19) ^ (palavra[t - 2] >>> 10)) + palavra[t - 7] + (Integer.rotateRight(palavra[t - 15], 7) ^ Integer.rotateRight(palavra[t - 15], 18) ^ (palavra[t - 15] >>> 3)) + palavra[t - 16];
      System.arraycopy(hash, 0, temp, 0, hash.length);
      for (int t = 0; t < palavra.length; ++t) {
        int t1 = temp[7] + (Integer.rotateRight(temp[4], 6) ^ Integer.rotateRight(temp[4], 11) ^ Integer.rotateRight(temp[4], 25)) + ((temp[4] & temp[5]) | (~temp[4] & temp[6])) + CONST[t] + palavra[t];
        int t2 = (Integer.rotateRight(temp[0], 2) ^ Integer.rotateRight(temp[0], 13) ^ Integer.rotateRight(temp[0], 22)) + ((temp[0] & temp[1]) | (temp[0] & temp[2]) | (temp[1] & temp[2]));
        System.arraycopy(temp, 0, temp, 1, temp.length - 1);
        temp[4] += t1;
        temp[0] = t1 + t2;
      }
      for(int t = 0; t < hash.length; ++t) hash[t] += temp[t];
    }
    buffer = java.nio.ByteBuffer.allocate(hash.length * 4);
    for(int h : hash) buffer.putInt(h);
    return buffer.array();
  }

}
