package iog.psg.assets;

import iog.psg.service.nativeassets.TimeBounds;

import java.util.Optional;

public class NativeAssetUtils {
    public static TimeBounds createTimeBounds(Optional<Integer> beforeSlot, Optional<Integer> afterSlot) {
        return TimeBounds.newBuilder()
                .setAfter(afterSlot.orElse(0))
                .setBefore(beforeSlot.orElse(0))
                .build();
    }
}
