package com.springboot.blog.service.impl;

import com.springboot.blog.entity.Logo;
import com.springboot.blog.repository.LogoRepository;
import com.springboot.blog.service.LogoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class LogoServiceImpl implements LogoService {

    @Autowired
    private LogoRepository logoRepository;

    @Override
    public Logo uploadLogo(Long id, String title, MultipartFile logoFile) throws IOException {
        Logo existingLogo = logoRepository.findById(id).orElse(null);
        if (existingLogo != null) {
            throw new IllegalArgumentException("Logo with ID " + id + " already exists.");
        }

        Logo logo = new Logo();
        logo.setId(id);
        logo.setTitle(title);
        logo.setLogoData(logoFile.getBytes());
        return logoRepository.save(logo);
    }

    @Override
    public Logo getLogoById(Long id) {
        return logoRepository.findById(id).orElse(null);
    }

    @Override
    public byte[] getLogoImageById(Long id) {
        Logo logo = logoRepository.findById(id).orElse(null);
        return logo != null ? logo.getLogoData() : null;
    }

    @Override
    public Logo updateLogo(Long id, String title, MultipartFile logoFile) throws IOException {
        Logo existingLogo = logoRepository.findById(id).orElse(null);
        if (existingLogo != null) {
            existingLogo.setTitle(title);
            existingLogo.setLogoData(logoFile.getBytes());
            return logoRepository.save(existingLogo);
        }
        return null;
    }
}