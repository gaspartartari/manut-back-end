package com.manut.api.config;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;

import com.manut.api.utils.CustomUserUtil;

public class AuditorAwareImpl implements AuditorAware<String> {

    @Autowired
    private CustomUserUtil customUserUtil;

    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.of(customUserUtil.getLoggedUser());
    }

}