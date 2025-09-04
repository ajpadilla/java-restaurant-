package restaurant.order.order.infrastructure.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/simulate")
public class SimulationController {

    @GetMapping("/cpu")
    public ResponseEntity<String> simulateCpuLoad() {
        long start = System.currentTimeMillis();
        long sum = 0;

        // Simulate CPU-bound task
        for (int i = 0; i < 100_000_000; i++) {
            sum += Math.sqrt(i);
        }

        long end = System.currentTimeMillis();
        String threadName = Thread.currentThread().getName();

        return ResponseEntity.ok("Thread: " + threadName +
                " handled the request in " + (end - start) + " ms. Sum: " + sum);
    }

    @GetMapping("/memory")
    public ResponseEntity<String> simulateMemoryAllocation() {
        List<byte[]> memoryHog = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            memoryHog.add(new byte[1_000_000]); // 1MB each
        }

        String threadName = Thread.currentThread().getName();

        return ResponseEntity.ok("Thread: " + threadName + " allocated ~100MB of memory");
    }

    @GetMapping("/gc")
    public ResponseEntity<String> simulateGcPressure() {
        List<Object> garbage = new ArrayList<>();

        for (int i = 0; i < 10_000_000; i++) {
            garbage.add(new String("garbage" + i));
        }

        String threadName = Thread.currentThread().getName();

        return ResponseEntity.ok("Thread: " + threadName + " created 10 million temporary strings");
    }
}
