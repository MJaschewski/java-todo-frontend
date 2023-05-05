package de.neuefische.backend.service;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@NoArgsConstructor
public class GenerateUUIDService {

    public String generateUUID(){
        return UUID.randomUUID().toString();
    }
}
