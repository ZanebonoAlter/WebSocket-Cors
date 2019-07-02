package com.zanebono.chart.Service;

import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;

public interface RSAService {
    public PublicKey getPublicKey();
    public String decrypt(byte[] srcBytes);
}
