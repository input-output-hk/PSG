package io.psg.nativeassets.configiration;

import io.psg.nativeassets.converters.ConverterComponent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ConversionServiceFactoryBean;
import org.springframework.core.convert.converter.Converter;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

@Configuration
@ComponentScan
public class ConversionConfiguration {

    @Bean
    public ConverterRegistry converterRegistry(@ConverterComponent final List<Converter> converters) {
        ConverterRegistry converterRegistry = new ConverterRegistry();
        converterRegistry.setConverters(converters);
        return converterRegistry;
    }

    @Bean
    public ConversionServiceFactoryBean conversionService(ConverterRegistry converterRegistry) {

        ConversionServiceFactoryBean conversionServiceFactoryBean = new ConversionServiceFactoryBean();
        conversionServiceFactoryBean.setConverters(new HashSet<>(converterRegistry.getConverters()));
        return conversionServiceFactoryBean;
    }

    public static class ConverterRegistry {
        private List<Converter> converters;

        public List<Converter> getConverters() {
            return converters;
        }

        public void setConverters(List<Converter> converters) {
            this.converters = converters;
        }

        public void addConverter(Converter converter) {
            converters.add(converter);
        }

        public void removeConverter(Class<? extends Converter> convererClass) {
            Iterator<Converter> iterator = converters.iterator();
            while (iterator.hasNext()) {
                Converter converter = iterator.next();
                if (converter.getClass().equals(convererClass)) {
                    iterator.remove();
                }
            }
        }
    }
}