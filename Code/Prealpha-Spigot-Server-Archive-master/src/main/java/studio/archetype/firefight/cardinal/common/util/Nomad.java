package studio.archetype.firefight.cardinal.common.util;

import com.google.gson.Gson;
import com.hashicorp.nomad.javasdk.NomadApiClient;
import com.hashicorp.nomad.javasdk.NomadApiConfiguration;
import com.hashicorp.nomad.javasdk.NomadException;
import studio.archetype.firefight.cardinal.server.CardinalConfig;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.IOException;
import java.util.UUID;
import java.util.function.Consumer;

public class Nomad {
    private final NomadApiClient client;

    private Nomad() {
        NomadApiConfiguration config = new NomadApiConfiguration.Builder()
                .setAddress(CardinalConfig.get().getNomadApiUrl())
                .setAuthToken(CardinalConfig.get().getNomadApiToken())
                .build();
        client = new NomadApiClient(config);
    }

    public void startMatchAndWait(String matchJob, MatchParams params, Consumer<MatchParams> callback) throws NomadException, IOException {
        String matchParamsString = new Gson().toJson(params);

        String dispatchedJobEvalId = client
                .getJobsApi()
                .dispatch(matchJob, matchParamsString.getBytes())
                .getValue()
                .getEvalId();

        boolean started = false;

        while (!started) {
            String status = client.getEvaluationsApi().info(dispatchedJobEvalId).getValue().getStatus();
            if (status.equals("started")) started = true; // todo: implement proper status check, will have to mess around
        }

        callback.accept(params);
    }

    public static Nomad instance;

    public static Nomad get() {
        if (instance != null) return instance;
        instance = new Nomad();
        return instance;
    }

    @AllArgsConstructor
    @Getter
    public static class MatchParams {
        private String matchId;
        public MatchParams() {
            this(UUID.randomUUID().toString());
        }
    }
}
