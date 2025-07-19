package cloud.devjunior.utils;

import java.nio.charset.StandardCharsets;

public class StringUtils {

    public static String corrigirEncoding(String textoComErro) {
        byte[] bytes = textoComErro.getBytes(StandardCharsets.ISO_8859_1);
        return new String(bytes, StandardCharsets.UTF_8);
    }
}
