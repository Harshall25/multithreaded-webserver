package MultiThreaded;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class LoadTestClient {

    public static void main(String[] args) throws InterruptedException {
        // --- CONFIGURATION ---
        int totalRequests = 1000000;      // Total number of connections to test
        int concurrentThreads = 18000;    // How many threads blast the server at the exact same time
        int port = 8010;
        // ---------------------

        ExecutorService executor = Executors.newFixedThreadPool(concurrentThreads);
        CountDownLatch latch = new CountDownLatch(totalRequests);
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failCount = new AtomicInteger(0);

        System.out.println("Starting load test with " + totalRequests + " requests...");
        long startTime = System.currentTimeMillis();

        for (int i = 0; i < totalRequests; i++) {
            executor.submit(() -> {
                try {
                    InetAddress address = InetAddress.getByName("localhost");
                    // Using try-with-resources to ensure sockets close properly
                    try (Socket socket = new Socket(address, port);
                         PrintWriter toSocket = new PrintWriter(socket.getOutputStream(), true);
                         BufferedReader fromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                        
                        toSocket.println("Hello from the load test client");
                        String response = fromServer.readLine();
                        
                        if (response != null && !response.isEmpty()) {
                            successCount.incrementAndGet();
                        } else {
                            failCount.incrementAndGet();
                        }
                    }
                } catch (Exception e) {
                    failCount.incrementAndGet();
                } finally {
                    latch.countDown(); // Tell the latch this thread is done
                }
            });
        }

        latch.await(); // Pause the main thread until all requests finish
        executor.shutdown();
        long endTime = System.currentTimeMillis();

        // Print Report
        System.out.println("\n====== Load Test Results ======");
        System.out.println("Total time taken: " + (endTime - startTime) + " ms");
        System.out.println("Successful requests: " + successCount.get());
        System.out.println("Failed requests: " + failCount.get());
        
        double seconds = (endTime - startTime) / 1000.0;
        System.out.println("Throughput: " + (int)(totalRequests / seconds) + " requests/sec");
        System.out.println("===============================\n");
    }
}