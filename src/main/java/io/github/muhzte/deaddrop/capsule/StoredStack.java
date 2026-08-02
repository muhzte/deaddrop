package io.github.muhzte.deaddrop.capsule;

import net.minecraft.nbt.CompoundTag;

import java.util.Objects;

public record StoredStack(String itemId, int count, CompoundTag extraData) {

    public StoredStack {
        Objects.requireNonNull(itemId, "itemId");
        if (count <= 0) {
            throw new IllegalArgumentException("count must be positive, got" + count);
        }

        extraData = extraData == null ? new CompoundTag() : extraData.copy();
    }
}