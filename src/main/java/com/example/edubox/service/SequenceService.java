package com.example.edubox.service;

public interface SequenceService {
    int getNextSeq(String key, Object... args);
}
