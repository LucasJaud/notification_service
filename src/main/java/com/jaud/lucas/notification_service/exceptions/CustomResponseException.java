package com.jaud.lucas.notification_service.exceptions;

import java.time.Instant;

public record CustomResponseException(String path,String exception, String msg, Instant datetime) {
   
}
