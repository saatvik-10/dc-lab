//client will always give the call
#include <stdio.h>
#include <stdlib.h>
#include <rpc/rpc.h>
#include "rpc.h"
#include <sys/types.h>
#include <sys/time.h>
#include <time.h>
#include <errno.h>
#include <unistd.h>
#include <netinet/in.h>
#include <signal.h>

int
main(int argc, char *argv[])
{
	CLIENT *clnt;
	square_in in;
	square_out *out;
	char *server;

	if (argc != 3) {
		fprintf(stderr, "usage: %s <server-host> <number>\n", argv[0]);
		exit(1);
	}
	server = argv[1];

	clnt = clnt_create(server, SQUARE_PROG, SQUARE_VERS, "udp");
	if (clnt == NULL) {
		clnt_pcreateerror(server);
		exit(1);
	}

	in.arg1 = atol(argv[2]);

	out = square_proc_1(&in, clnt);
	if (out == NULL) {
		clnt_perror(clnt, server);
		exit(1);
	}

	printf("square of %ld = %ld\n", in.arg1, out->res1);

	clnt_destroy(clnt);
	return 0;
}