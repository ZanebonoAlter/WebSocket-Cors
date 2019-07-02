package com.zanebono.chart.ServiceImpl;

import com.zanebono.chart.Service.RSAService;
import com.zanebono.chart.Utils.RSAUntils;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;

@Service
public class RSAServiceImpl implements RSAService {
    @Override
    public PublicKey getPublicKey() {
        RSAUntils rsaUntils = new RSAUntils();
        PublicKey rsaPublicKey =null;
        try {
             rsaPublicKey = rsaUntils.LoadPublicKey(RSAUntils.publicPath);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return rsaPublicKey;
    }

    @Override
    public String decrypt(byte[] srcBytes) {
        RSAUntils rsaUntils = new RSAUntils();
        PrivateKey rsaPrivateKey =null;
        String final_result = "";
        try {
            //获取私钥
            rsaPrivateKey = rsaUntils.LoadPrivateKey(RSAUntils.privatePath);
            //解码
            byte[] resultBytes=rsaUntils.decrypt(rsaPrivateKey,srcBytes);
            final_result=new String(resultBytes);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return final_result;
    }
}
