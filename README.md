# High-Concurrency Java MultiThreaded Server: Scaling to 1 Million Requests

A low-level **multithreaded TCP server built from scratch in Java**, benchmarked with up to **1,000,000 requests** to compare Classic/OS Threads with Java 21 Virtual Threads.

## Benchmark

| Approach             |  Requests | Concurrency |    Time |      Throughput | Failed |
| -------------------- | --------: | ----------: | ------: | --------------: | -----: |
| Classic / OS Threads |     2,000 |       2,000 |  14.19s |      ~140 req/s |      0 |
| Classic / OS Threads |    10,000 |      10,000 |  55.53s |      ~180 req/s |    173 |
| Classic / OS Threads |    20,000 |       5,000 |  73.34s |      ~272 req/s |    587 |
| Virtual Threads      |    20,000 |       5,000 |   6.90s |     2,896 req/s |      0 |
| Virtual Threads      |    50,000 |      10,000 |  12.67s |     3,946 req/s |      0 |
| Virtual Threads      |   100,000 |      10,000 |  28.18s |     3,547 req/s |      0 |
| Virtual Threads      | 1,000,000 |      10,000 | 137.71s | **7,261 req/s** |  **0** |

## Key Result

The server successfully processed **1,000,000 requests with zero failures** using Virtual Threads.

For the 20,000-request benchmark:

**Classic / OS Threads:** ~272 req/s
**Virtual Threads:** ~2,896 req/s

**~10.6× higher throughput** with Virtual Threads in this workload.

## Server Architecture

**Classic / OS Threads**

```java
ServerSocket serverSocket = new ServerSocket(8010);

while (true) {
    Socket client = serverSocket.accept();
    new Thread(() -> handleClient(client)).start();
}
```

**Virtual Threads**

```java
ServerSocket serverSocket = new ServerSocket(8010);

while (true) {
    Socket client = serverSocket.accept();
    Thread.startVirtualThread(() -> handleClient(client));
}
```

## What I Built

* Raw Java TCP server using `ServerSocket` and `Socket`
* Concurrent request handling
* Classic / OS Thread implementation
* Java 21 Virtual Thread implementation
* Custom high-concurrency load tester
* Benchmarks up to **1 million requests**
* Throughput and failure-rate analysis

## Tech Stack

**Java 21+ · TCP Sockets · Multithreading · Virtual Threads · Custom Load Testing**

## Conclusion

For this I/O-bound workload, **Virtual Threads scaled significantly better than Classic / OS Threads**, handling massive concurrency while keeping the server implementation simple.
