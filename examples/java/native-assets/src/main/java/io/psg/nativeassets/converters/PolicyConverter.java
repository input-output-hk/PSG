package io.psg.nativeassets.converters;


import iog.psg.service.nativeassets.native_assets_service.Policy;
import org.springframework.core.convert.converter.Converter;

@ConverterComponent
public class PolicyConverter implements Converter<Policy, io.psg.nativeassets.model.Policy> {

    @Override
    public io.psg.nativeassets.model.Policy convert(Policy policy) {
        return io.psg.nativeassets.model.Policy.builder()
                .policyId(policy.policyId())
                .name(policy.name())
                .paymentAddress(policy.paymentAddress())
                .paymentAddressAmount(policy.paymentAddressAmount())
                .build();
    }
}
