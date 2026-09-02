package com.gneworks.common.id;
import com.github.ksuid.Ksuid;
import org.springframework.stereotype.Component;

@Component
public class KsuidGenerator {

    private KsuidGenerator() {}

    public static String createId() {
        return Ksuid.newKsuid().toString();
    }
}