package cloud.devjunior.consumer;

import cloud.devjunior.service.ImportacaoProcessorService;
import cloud.devjunior.utils.AmazonS3Utils;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Incoming;

@ApplicationScoped
public class ImportacaoConsumer extends AmazonS3Utils {

    @Inject
    ImportacaoProcessorService importacaoProcessorService;

    @Incoming("topic-add-movimentacao")
    public void processImportacao(Long importacaoId) {
        importacaoProcessorService.processar(importacaoId);
    }
}
