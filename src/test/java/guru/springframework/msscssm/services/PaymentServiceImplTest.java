package guru.springframework.msscssm.services;

import guru.springframework.msscssm.domain.Payment;
import guru.springframework.msscssm.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.sql.SQLOutput;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class PaymentServiceImplTest {

    @Autowired
    PaymentService paymentService;
    @Autowired
    PaymentRepository paymentRepository;

    Payment payment;

    @BeforeEach
    void setUp() {
        payment = payment.builder().amount(new BigDecimal("12.99")).build();
    }

    @Test
    @Transactional
    void preAuth() {
        Payment savePayment = paymentService.newPayment(payment);
        paymentService.preAuth(savePayment.getId());
        Payment preAuthedPayment  = paymentRepository.getOne(savePayment.getId());
        System.out.println(preAuthedPayment);
    }
}