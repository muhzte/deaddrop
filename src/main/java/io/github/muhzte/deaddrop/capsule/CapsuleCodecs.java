package io.github.muhzte.deaddrop.capsule;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.UUIDUtil;
import net.minecraft.nbt.CompoundTag;

import java.util.Optional;

public final class CapsuleCodecs {

    private CapsuleCodecs() {
    }

    public static final Codec<PrivacyMode> PRIVACY_MODE_CODEC =
            Codec.STRING.xmap(PrivacyMode::valueOf, Enum::name);

    public static final Codec<StoredStack> STORED_STACK_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("item").forGetter(StoredStack::itemId),
            Codec.INT.fieldOf("count").forGetter(StoredStack::count),
            CompoundTag.CODEC.optionalFieldOf("extra", new CompoundTag()).forGetter(StoredStack::extraData)
    ).apply(instance, StoredStack::new));

    public static final Codec<Capsule> CAPSULE_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            UUIDUtil.CODEC.fieldOf("id").forGetter(Capsule::id),
            UUIDUtil.CODEC.fieldOf("owner").forGetter(Capsule::owner),
            STORED_STACK_CODEC.listOf().fieldOf("contents").forGetter(Capsule::contents),
            Codec.STRING.optionalFieldOf("message", "").forGetter(Capsule::message),
            Codec.LONG.fieldOf("sealed_at").forGetter(Capsule::sealedAtMillis),
            Codec.LONG.fieldOf("duration").forGetter(Capsule::durationMillis),
            PRIVACY_MODE_CODEC.fieldOf("privacy").forGetter(Capsule::privacy),
            UUIDUtil.CODEC.optionalFieldOf("recipient").forGetter(c -> Optional.ofNullable(c.recipient()))
    ).apply(instance, (id, owner, contents, message, sealedAt, duration, privacy, recipient) ->
            new Capsule(id, owner, contents, message, sealedAt, duration, privacy, recipient.orElse(null))
    ));
}