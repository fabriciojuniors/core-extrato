package cloud.devjunior.consumer;

import cloud.devjunior.service.AtualizacaoSaldoProcessorService;
import io.smallrye.reactive.messaging.annotations.Blocking;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Incoming;

@ApplicationScoped
public class AtualizacaoSaldoConsumer {

    @Inject
    AtualizacaoSaldoProcessorService atualizacaoSaldoProcessorService;

    @Incoming("topic-process-saldo")
    @Blocking
    public void processSaldo(Long contaBancariaId) {
        atualizacaoSaldoProcessorService.processSaldo(contaBancariaId);
    }
}
