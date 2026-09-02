package com.gneworks.api.controller.base;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 서버 헬스체크용 컨트롤러
 *
 * @author y_ha
 */
@RestController
@RequestMapping("/health-check")
public class HealthCheckController {

    @GetMapping
    public ResponseEntity healthCheck() {
        return ResponseEntity.ok().build();
    }
}
