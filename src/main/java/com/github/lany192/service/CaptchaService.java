package com.github.lany192.service;

import com.github.lany192.config.CachesEnum;

public interface CaptchaService {
    boolean saveCaptcha(CachesEnum cachesEnum, String key, Object value);

    String getCaptcha(CachesEnum cachesEnum, String key);

    void removeCaptcha(CachesEnum cachesEnum, String key);

    boolean checkCaptchaTimes(CachesEnum cachesEnum, String key);
}
