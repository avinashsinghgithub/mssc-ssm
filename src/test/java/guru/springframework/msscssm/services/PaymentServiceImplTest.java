package guru.springframework.msscssm.services;

import guru.springframework.msscssm.domain.Payment;
import guru.springframework.msscssm.domain.PaymentEvent;
import guru.springframework.msscssm.domain.PaymentState;
import guru.springframework.msscssm.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.statemachine.StateMachine;

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

    @Test
    @Transactional
    @RepeatedTest(10)
    void testAuth(){
        Payment savePayment = paymentService.newPayment(payment);
        StateMachine<PaymentState, PaymentEvent> preAuthSM  = paymentService.preAuth(savePayment.getId());
        if(preAuthSM.getState().getId() == PaymentState.PRE_AUTH){
            System.out.println("payment is pre authorized");
            StateMachine<PaymentState, PaymentEvent> authSM = paymentService.authorizePayment(savePayment.getId());
            System.out.println("result of auth: "+authSM.getState().getId());
        }else {
            System.out.println("payment failed pre-auth");
        }

    }
}