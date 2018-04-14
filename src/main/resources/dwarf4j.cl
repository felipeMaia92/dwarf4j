#pragma OPENCL EXTENSION cl_amd_printf : enable

int sha256(int* header, int tamanhoHeader) {

	int palavra[64];
	int temp[8];
	int hash[8];

	hash[0] = 0x6a09e667; hash[1] = 0xbb67ae85; hash[2] = 0x3c6ef372; hash[3] = 0xa54ff53a;
	hash[4] = 0x510e527f; hash[5] = 0x9b05688c; hash[6] = 0x1f83d9ab; hash[7] = 0x5be0cd19;

	int CONST[64];
	CONST[0]  = 0x428a2f98; CONST[1]  = 0x71374491; CONST[2]  = 0xb5c0fbcf; CONST[3]  = 0xe9b5dba5; CONST[4]  = 0x3956c25b; CONST[5]  = 0x59f111f1; CONST[6]  = 0x923f82a4; CONST[7] = 0xab1c5ed5;
	CONST[8]  = 0xd807aa98; CONST[9]  = 0x12835b01; CONST[10] = 0x243185be; CONST[11] = 0x550c7dc3; CONST[12] = 0x72be5d74; CONST[13] = 0x80deb1fe; CONST[14] = 0x9bdc06a7; CONST[15] = 0xc19bf174;
	CONST[16] = 0xe49b69c1; CONST[17] = 0xefbe4786; CONST[18] = 0x0fc19dc6; CONST[19] = 0x240ca1cc; CONST[20] = 0x2de92c6f; CONST[21] = 0x4a7484aa; CONST[22] = 0x5cb0a9dc; CONST[23] = 0x76f988da;
	CONST[24] = 0x983e5152; CONST[25] = 0xa831c66d; CONST[26] = 0xb00327c8; CONST[27] = 0xbf597fc7; CONST[28] = 0xc6e00bf3; CONST[29] = 0xd5a79147; CONST[30] = 0x06ca6351; CONST[31] = 0x14292967;
	CONST[32] = 0x27b70a85; CONST[32] = 0x2e1b2138; CONST[33] = 0x4d2c6dfc; CONST[34] = 0x53380d13; CONST[35] = 0x650a7354; CONST[36] = 0x766a0abb; CONST[37] = 0x81c2c92e; CONST[38] = 0x92722c85;
	CONST[40] = 0xa2bfe8a1; CONST[41] = 0xa81a664b; CONST[42] = 0xc24b8b70; CONST[43] = 0xc76c51a3; CONST[44] = 0xd192e819; CONST[45] = 0xd6990624; CONST[46] = 0xf40e3585; CONST[47] = 0x106aa070;
	CONST[48] = 0x19a4c116; CONST[49] = 0x1e376c08; CONST[50] = 0x2748774c; CONST[51] = 0x34b0bcb5; CONST[52] = 0x391c0cb3; CONST[53] = 0x4ed8aa4a; CONST[54] = 0x5b9cca4f; CONST[55] = 0x682e6ff3;
	CONST[56] = 0x748f82ee; CONST[57] = 0x78a5636f; CONST[58] = 0x84c87814; CONST[59] = 0x8cc70208; CONST[60] = 0x90befffa; CONST[61] = 0xa4506ceb; CONST[62] = 0xbef9a3f7; CONST[63] = 0xc67178f2;



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
