# RPC Assignment 4 - SQUARE Program

Remote Procedure Call (RPC) implementation in C using the ONC RPC / XDR model.
A client sends a number to a server, which computes and returns its square.

## Files

| File        | Description                                             |
|-------------|---------------------------------------------------------|
| `rpc.x`     | RPC specification (program, version, procedures, types) |
| `server.c`  | Server: implements `square_proc_1_svc()` procedure      |
| `client.c`  | Client: calls the remote procedure and prints the result|

The stubs (`rpc.h`, `rpc_clnt.c`, `rpc_svc.c`, `rpc_xdr.c`) are generated from
`rpc.x` with `rpcgen` and should not be edited.

## Prerequisites

```bash
sudo apt update
sudo apt install -y libtirpc-dev rpcbind
sudo systemctl start rpcbind
```

## Build

Generate the stubs and compile:

```bash
rpcgen -C rpc.x
gcc -o server server.c rpc_svc.c rpc_xdr.c -ltirpc
gcc -o client client.c rpc_clnt.c rpc_xdr.c -ltirpc
```

If your system keeps `rpc/rpc.h` under `/usr/include/tirpc`, add
`-I/usr/include/tirpc` to the compile commands above.

## Run

```bash
./server &
./client localhost 12
```

Expected output:

```
square of 12 = 144
```

`rpcbind` (the portmapper) must be running so the server can register and the
client can locate it. Stop the server with `kill %1`.