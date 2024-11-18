package com.github.djoarns.payflow.util;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
public abstract class BaseUnitTest {
    protected <T> T createMock(Class<T> classToMock) {
        return mock(classToMock);
    }
}