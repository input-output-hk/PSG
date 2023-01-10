package iog.psg.assets.converters;

import iog.psg.assets.model.NativePolicy;
import iog.psg.service.nativeassets.native_assets_service.Policy;
import org.springframework.core.convert.converter.Converter;

@ConverterComponent
public class NativePolicyConverter implements Converter<Policy, NativePolicy> {

    @Override
    public NativePolicy convert(Policy  policy) {
        return NativePolicy.builder()
                .policyId(policy.policyId())
                .name(policy.name())
                .paymentAddress(policy.paymentAddress())
                .paymentAddressAmount(policy.paymentAddressAmount())
                .build();
    }
}
