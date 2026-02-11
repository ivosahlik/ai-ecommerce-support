package cz.ivosahlik.ai_ecommerce_support.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.client.RestClient;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Configuration
public class LLMConfig {

    @Value("${ollama.base-url:http://localhost:11434}")
    private String ollamaBaseUrl;

    @Value("${ollama.model:mistral}")
    private String ollamaModel;

    @Value("${ollama.connect-timeout:30s}")
    private Duration connectTimeout;

    @Value("${ollama.read-timeout:180s}")
    private Duration readTimeout;

    /*
        OllamaApi, it is free, you have to pull ollama model from https://ollama.com/search
     */
    @Bean
    public ChatClient chatClient() {

        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, (int) connectTimeout.toMillis())
                .doOnConnected(conn -> conn
                        .addHandlerLast(new ReadTimeoutHandler(readTimeout.getSeconds(), TimeUnit.SECONDS)));

        WebClient.Builder webClientBuilder = WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient));

        RestClient.Builder restClientBuilder = RestClient.builder();

        OllamaApi ollamaApi = OllamaApi.builder()
                .baseUrl(ollamaBaseUrl)
                .restClientBuilder(restClientBuilder)
                .webClientBuilder(webClientBuilder)
                .build();

        var options = OllamaOptions.builder()
                .model(ollamaModel)
                //        0.0 → deterministic
                //        1.0 → creative / random
                .temperature(0.6)
// maxTokens(50–150) - chat response, API response
// maxTokens(500–2000) - arcticles, document summarize, text generate
//                .maxTokens(200)
                .build();

        OllamaChatModel chatModel = OllamaChatModel.builder()
                .ollamaApi(ollamaApi)
                .defaultOptions(options)
                .build();

        return ChatClient.create(chatModel);
    }

    /*
        OpenAiApi
    */
//  @Bean
//    public ChatClient chatClient(ChatClient.Builder chatClientBuilder) {
//        return chatClientBuilder.build();
//    }
//
// /*Example how you can specify your desired model to work with*/
//   // @Bean
//    public ChatClient chatClient() {
//        OpenAiApi openAiApi = OpenAiApi.builder()
//                .apiKey(System.getenv("OPENAI_API_KEY"))
//                .build();
//
//        OpenAiChatOptions options = OpenAiChatOptions.builder()
//                .model("gpt-4o")
//                .temperature(0.6)
//                .maxTokens(200)
//                .build();
//        OpenAiChatModel chatModel = OpenAiChatModel.builder()
//                .openAiApi(openAiApi)
//                .defaultOptions(options)
//                .build();
//
//        return ChatClient.create(chatModel);
//
//    }
}