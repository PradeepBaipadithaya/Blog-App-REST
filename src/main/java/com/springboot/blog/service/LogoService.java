package com.springboot.blog.service;

import com.springboot.blog.entity.Logo;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface LogoService {
    Logo uploadLogo(Long id, String title, MultipartFile logoFile) throws IOException;

    Logo getLogoById(Long id);

    byte[] getLogoImageById(Long id);

    Logo updateLogo(Long id, String title, MultipartFile logoFile) throws IOException;
}