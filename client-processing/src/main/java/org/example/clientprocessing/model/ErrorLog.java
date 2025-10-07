package org.example.clientprocessing.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "error_log")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "service_name", length = 64)
    private String serviceName;

    @Column(name = "type", length = 16)
    private String type;

    @Column(name = "method_signature")
    private String methodSignature;

    @Column(name = "exception_text")
    private String exceptionText;

    @Column(name = "stacktrace")
    private String stacktrace;

    @Column(name = "method_args")
    private String methodArgs;
}
