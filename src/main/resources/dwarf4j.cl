#pragma OPENCL EXTENSION cl_amd_printf : enable

int sha256(int *header, int tamanhoHeader) {

	int palavra[64];
	int temp[8];
	int hash[8];

	hash[0] = 0x6a09e667;
	hash[1] = 0xbb67ae85;
	hash[2] = 0x3c6ef372;
	hash[3] = 0xa54ff53a;
	hash[4] = 0x510e527f;
	hash[5] = 0x9b05688c;
	hash[6] = 0x1f83d9ab;
	hash[7] = 0x5be0cd19;

	int CONST[64];
	CONST[0] = 0x428a2f98;
	CONST[1] = 0x71374491;
	CONST[2] = 0xb5c0fbcf;
	CONST[3] = 0xe9b5dba5;
	CONST[4] = 0x3956c25b;
	CONST[5] = 0x59f111f1;
	CONST[6] = 0x923f82a4;
	CONST[7] = 0xab1c5ed5;

	CONST[8] = 0xd807aa98;
	CONST[9] = 0x12835b01;
	CONST[10] = 0x243185be;
	CONST[11] = 0x550c7dc3;
	CONST[12] = 0x72be5d74;
	CONST[13] = 0x80deb1fe;
	CONST[14] = 0x9bdc06a7;
	CONST[15] = 0xc19bf174;

	CONST[16] = 0xe49b69c1;
	CONST[17] = 0xefbe4786;
	CONST[18] = 0x0fc19dc6;
	CONST[19] = 0x240ca1cc;
	CONST[20] = 0x2de92c6f;
	CONST[21] = 0x4a7484aa;
	CONST[22] = 0x5cb0a9dc;
	CONST[23] = 0x76f988da;

	// (...)

	int saida = 0;
	for(int i = 0; i < tamanhoHeader; i++)
		saida += header[i];
	return saida;
}

__kernel void dwarf4j(
		__global const int *prefixoHeader,
		__global const int *tamanhoHeaderEalvo,
		__global int *noncePremiada) {

	int gid = get_global_id(0);
	int tamanhoHeader = tamanhoHeaderEalvo[0] + 4;
	int alvo = tamanhoHeaderEalvo[1];

	int header[16384];

	for(int nonce = 10; nonce < 0xffffffff; nonce++) {
		for(int i = 0; i < tamanhoHeader - 4; i++) header[i] = prefixoHeader[i];
		header[tamanhoHeader - 1] = (nonce >> 24) & 0xff;
		header[tamanhoHeader - 2] = (nonce >> 16) & 0xff;
		header[tamanhoHeader - 3] = (nonce >> 8) & 0xff;
		header[tamanhoHeader - 4] = nonce & 0xff;

		for(int i = 0; i < tamanhoHeader; i++) {
			// printf("%d", header[i]);
		}

		noncePremiada[0] = sha256(header, tamanhoHeader);

		break;
	}

}
