package pagarme.model;


import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import pagarme.util.JSONUtils;

import javax.ws.rs.HttpMethod;
import java.util.Map;

public class CompaniesTempory extends PagarMeModel<Integer>{

    /**
     * Objeto com dados do telefone do cliente
     */
    @Expose
    @SerializedName("api_key")
    private Map<String, Object> apiKey;

    public String getTemporaryCompanyApiKey() {
        try {
            final PagarMeRequest request = new PagarMeRequest(HttpMethod.POST,"/companies/temporary");
            CompaniesTempory company = JSONUtils.getAsObject((JsonObject) request.execute(), CompaniesTempory.class);

            return company.apiKey.get("test").toString();

        } catch (PagarMeException exception) {
            throw new UnsupportedOperationException(exception);
        }
    }
}