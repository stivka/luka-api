package ee.stivka.luka.service;

import ee.stivka.luka.SseHub;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class RandomTransmissionService {
    private final SseHub hub;
    private final ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();

    private final AtomicLong minMs = new AtomicLong(5 * 60_000);
    private final AtomicLong maxMs = new AtomicLong(25 * 60_000);

    private final AtomicReference<ScheduledFuture<?>> scheduled = new AtomicReference<>();

    public RandomTransmissionService(SseHub hub) {
        this.hub = hub;
    }

    @PostConstruct
    public void start() {
        scheduleNext();
    }

    public void updateInterval(long newMinMs, long newMaxMs) {
        minMs.set(Math.min(newMinMs, newMaxMs));
        maxMs.set(Math.max(newMinMs, newMaxMs));
        reschedule();
    }

    private void reschedule() {
        ScheduledFuture<?> prev = scheduled.getAndSet(null);
        if (prev != null) prev.cancel(false);
        scheduleNext();
    }

    private void scheduleNext() {
        long delay = randomBetween(minMs.get(), maxMs.get());
        ScheduledFuture<?> f = exec.schedule(() -> {
            sendTransmission();
            scheduleNext(); // schedule again after sending
        }, delay, TimeUnit.MILLISECONDS);
        scheduled.set(f);
    }

    private void sendTransmission() {
        Map<String, Object> payload = Map.of(
                "type", "TRANSMISSION",
                "message", pickMessage(),
                "ts", Instant.now().toString()
        );
        hub.broadcast("notification", payload);
    }

    private long randomBetween(long a, long b) {
        return a + ThreadLocalRandom.current().nextLong(Math.max(1, b - a + 1));
    }

    private String pickMessage() {
        String[] msgs = {
                "📡 Transmission received… ‘Don’t trust the monitor.’",
                "🕯️ A candle flickered somewhere.",
                "👁️ Blink twice if you saw Luka.",
                "🎚️ New frequency detected. Try again soon."
        };
        return msgs[ThreadLocalRandom.current().nextInt(msgs.length)];
    }
}

