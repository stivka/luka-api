package ee.stivka.luka.service;

import ee.stivka.luka.model.PresetMessage;
import ee.stivka.luka.repository.PresetMessageRepository;
import ee.stivka.luka.sse.SseHub;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

@Service
@Slf4j
@RequiredArgsConstructor
public class RandomTransmissionService {
  private final SseHub hub;
  private final PresetMessageRepository messageRepository;
  private final ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
  private final String transmitterId = resolveTransmitterId();

  private final AtomicLong minMs = new AtomicLong(5 * 60_000);
  private final AtomicLong maxMs = new AtomicLong(25 * 60_000);

  private final AtomicReference<ScheduledFuture<?>> scheduled = new AtomicReference<>();

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
      try {
        sendTransmission();
      } catch (Exception ex) {
        // If a single run fails (e.g. transient DB issue), don't stop the loop.
        log.warn("Random transmission send failed; will retry on next schedule.", ex);
      } finally {
        scheduleNext(); // schedule again after sending (or after failure)
      }
    }, delay, TimeUnit.MILLISECONDS);
    scheduled.set(f);
  }

  private void sendTransmission() {
    String message = pickMessage();

    Map<String, Object> payload = Map.of(
      "type", "TRANSMISSION",
      "message", message,
      "ts", Instant.now().toString(),
      "tx", transmitterId
    );
    hub.broadcast("notification", payload);
  }

  public String getTransmitterId() {
    return transmitterId;
  }

  private static String resolveTransmitterId() {
    // Use RAILWAY_PUBLIC_DOMAIN as the preferred origin info
    String publicDomain = System.getenv("RAILWAY_PUBLIC_DOMAIN");
    if (publicDomain != null && !publicDomain.isBlank()) {
      return publicDomain.trim();
    }

    // Fallback to other Railway metadata or system hostnames
    String[] keys = {
      "RAILWAY_SERVICE_NAME",
      "RAILWAY_PROJECT_NAME",
      "HOSTNAME",
      "COMPUTERNAME"
    };
    for (String key : keys) {
      String v = System.getenv(key);
      if (v != null && !v.isBlank()) return v.trim();
    }
    return "UNKNOWN";
  }

  private long randomBetween(long a, long b) {
    return a + ThreadLocalRandom.current().nextLong(Math.max(1, b - a + 1));
  }

  private String pickMessage() {
    List<PresetMessage> dbMessages = messageRepository.findAll();
    if (!dbMessages.isEmpty()) {
      return dbMessages.get(ThreadLocalRandom.current().nextInt(dbMessages.size())).getContent();
    }

    return "No messages available.";
  }
}

