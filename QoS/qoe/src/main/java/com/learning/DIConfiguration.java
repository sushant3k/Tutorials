/**
 * 
 */
package com.learning;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.learning.playlist.service.M3U8PlaylistSvc;
import com.learning.playlist.service.M3U8PlaylistSvcImpl;

/**
 * @author Sushant
 *
 */
@Configuration
public class DIConfiguration {

    @Bean
    public M3U8PlaylistSvc getM3U8PlaylistSvc(){
        return new M3U8PlaylistSvcImpl();
    }
}
