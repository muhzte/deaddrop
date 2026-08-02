package io.github.muhzte.deaddrop.capsule;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public record Capsule(
        UUID id,
        UUID owner,
        List<StoredStack> contents,
        String message,
        long sealedAtMillis,
        long durationMillis,
        PrivacyMode privacy,
        UUID recipient
) {

    public Capsule {
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(owner, "owner");
        Objects.requireNonNull(contents, "contents");
        Objects.requireNonNull(message, "message");
        Objects.requireNonNull(privacy, "privacy");
        if (durationMillis <= 0) {
            throw new IllegalArgumentException("durationMillis must be positive");
        }

        if (privacy == PrivacyMode.ADDRESSED && recipient == null) {
            throw new IllegalArgumentException("ADDRESSED capsules must have a recipient");
        }
        if (privacy != PrivacyMode.ADDRESSED && recipient != null) {
            throw new IllegalArgumentException("Only ADDRESSED capsules may carry a recipient");
        }

        contents = List.copyOf(contents);
    }

    public long unlockAtMillis() {
        return sealedAtMillis + durationMillis;
    }
}