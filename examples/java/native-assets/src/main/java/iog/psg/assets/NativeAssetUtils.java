package iog.psg.assets;

import iog.psg.service.nativeassets.native_assets.TimeBounds;

import java.util.Optional;

public class NativeAssetUtils {
    public static TimeBounds createTimeBounds(Optional<Integer> beforeSlot, Optional<Integer> afterSlot) {
        return TimeBounds.of(afterSlot.orElse(0), beforeSlot.orElse(0));
    }
}
