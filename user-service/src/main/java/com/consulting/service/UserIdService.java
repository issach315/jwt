package com.consulting.service;

import com.consulting.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserIdService {

    private final UserRepository userRepository;

    public UserIdService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public String generateNextUserId() {
        String prefix = "USER";

        // Find the last ID
        String lastId = userRepository.findLastUserId();

        int nextId = 1;
        if (lastId != null && lastId.startsWith(prefix)) {
            String numericPart = lastId.substring(prefix.length());
            nextId = Integer.parseInt(numericPart) + 1;
        }

        return prefix + String.format("%04d", nextId);
    }
}