package iog.psg.assets.converters;

import iog.psg.assets.converters.ConverterComponent;
import iog.psg.assets.model.MultisigPolicy;
import iog.psg.service.nativeassets.multisig.proto.v1.native_assets_multisig_service.Policy;
import org.springframework.core.convert.converter.Converter;

@ConverterComponent
public class MutlisigPolicyConverter implements Converter<Policy, MultisigPolicy> {

    @Override
    public MultisigPolicy convert(Policy  policy) {
        return MultisigPolicy.builder()
                .policyId(policy.policyId())
                .name(policy.name())
                .build();
    }
}
