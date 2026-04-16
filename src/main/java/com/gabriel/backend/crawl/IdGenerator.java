package com.gabriel.backend.crawl;

import java.util.UUID;

public class IdGenerator {

    public String createId() {
        return UUID.randomUUID().toString().replaceAll("-", "").substring(0, 8);
    }

}
