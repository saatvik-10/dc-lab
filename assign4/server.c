//procedure lies in the address space of the server
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

square_out *
square_proc_1_svc(square_in *argp, struct svc_req *rqstp)
{
	static square_out result;

	printf("square_proc called with arg1 = %ld\n", argp->arg1);
	result.res1 = argp->arg1 * argp->arg1;

	return &result;
}