package msadaka.services;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {


    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        return bCryptPasswordEncoder;
    }


    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> messageConverters) {

        StringHttpMessageConverter stringConverter = new StringHttpMessageConverter();
        //stringConverter.setSupportedMediaTypes(Arrays.asList(new MediaType("text","plain",Charset.defaultCharset())));
        stringConverter.setSupportedMediaTypes(Arrays.asList( //
                MediaType.TEXT_PLAIN, //
                MediaType.TEXT_HTML, //
                MediaType.APPLICATION_JSON));
        MappingJackson2HttpMessageConverter jsonMessageConverter = new MappingJackson2HttpMessageConverter();
        messageConverters.add(jsonMessageConverter);
        messageConverters.add(stringConverter);
        messageConverters.add(new MappingJackson2HttpMessageConverter());
        super.configureMessageConverters(messageConverters);
    }


}