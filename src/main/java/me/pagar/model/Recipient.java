package me.pagar.model;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import me.pagar.util.JSONUtils;
import org.joda.time.DateTime;

import javax.ws.rs.HttpMethod;
import java.util.Collection;

public class Recipient  extends PagarMeModel<String> {

    @Expose(serialize = false)
    @SerializedName(value = "automatic_anticipation_enabled")
    private Boolean automaticAnticipationEnabled;

    @Expose
    @SerializedName(value = "transfer_enabled")
    private Boolean transferEnabled;

    @Expose(serialize = false)
    @SerializedName(value = "anticipatable_volume_percentage")
    private Integer anticipatableVolumePercentage;

    @Expose
    @SerializedName(value = "transfer_day")
    private Integer transferDay;

    @Expose(deserialize = false)
    @SerializedName(value = "bank_account_id")
    private Integer bankAccountId;

    @Expose
    @SerializedName(value = "bank_account")
    private BankAccount bankAccount;

    @Expose
    @SerializedName(value = "transfer_interval")
    private TransferInterval transferInterval;

    @Expose(serialize = false)
    @SerializedName(value = "lastTransfer")
    private DateTime lastTransfer;

    @Expose(serialize = false)
    @SerializedName("date_updated")
    private DateTime updatedAt;

    public Boolean isAutomaticAnticipationEnabled() {
        return automaticAnticipationEnabled;
    }

    public Boolean isTransferEnabled() {
        return transferEnabled;
    }

    public Integer getAnticipatableVolumePercentage() {
        return anticipatableVolumePercentage;
    }

    public Integer getTransferDay() {
        return transferDay;
    }

    public BankAccount getBankAccount() {
        return bankAccount;
    }

    public TransferInterval getTransferInterval() {
        return transferInterval;
    }

    public DateTime getLastTransfer() {
        return lastTransfer;
    }

    public DateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setTransferEnabled(Boolean transferEnabled) {
        this.transferEnabled = transferEnabled;
        addUnsavedProperty("transferEnabled");
    }

    public void setTransferDay(Integer transferDay) {
        this.transferDay = transferDay;
        addUnsavedProperty("transferDay");
    }

    public void setBankAccountId(Integer bankAccountId) {
        this.bankAccountId = bankAccountId;
        addUnsavedProperty("bankAccountId");
    }

    public void setBankAccount(BankAccount bankAccount) {
        this.bankAccount = bankAccount;
        addUnsavedProperty("bankAccount");
    }

    public void setTransferInterval(TransferInterval transferInterval) {
        this.transferInterval = transferInterval;
        addUnsavedProperty("transferInterval");
    }

    public Recipient save() throws PagarMeException {
        final Recipient saved = super.save(getClass());
        copy(saved);

        return saved;
    }

    public Recipient find(String id) throws PagarMeException {

        final PagarMeRequest request = new PagarMeRequest(HttpMethod.GET,
                String.format("/%s/%s", getClassName(), id));

        final Recipient other = JSONUtils.getAsObject((JsonObject) request.execute(), Recipient.class);
        copy(other);
        flush();

        return other;
    }

    public Collection<Recipient> findCollection(int totalPerPage, int page) throws PagarMeException {
        return JSONUtils.getAsList(super.paginate(totalPerPage, page), new TypeToken<Collection<Recipient>>() {
        }.getType());
    }

    public Balance balance() throws PagarMeException {
        final PagarMeRequest request = new PagarMeRequest(HttpMethod.GET,
                String.format("/%s/%s/%s", getClassName(), getId(), Balance.class.getSimpleName().toLowerCase()));

        return JSONUtils.getAsObject((JsonObject) request.execute(), Balance.class);
    }

    public Recipient refresh() throws PagarMeException {
        final Recipient other = JSONUtils.getAsObject(refreshModel(), Recipient.class);
        copy(other);
        flush();
        return other;
    }

    private void copy(Recipient other) {
        setId(other.getId());
        this.automaticAnticipationEnabled = other.automaticAnticipationEnabled;
        this.transferEnabled = other.transferEnabled;
        this.transferDay = other.transferDay;
        this.anticipatableVolumePercentage = other.anticipatableVolumePercentage;
        this.lastTransfer = other.lastTransfer;
        this.transferInterval = other.transferInterval;
        this.updatedAt = other.updatedAt;
        this.bankAccount = other.bankAccount;
    }

    public enum TransferInterval {
        @SerializedName("daily")
        DAILY,

        @SerializedName("weekly")
        WEEKLY,

        @SerializedName("monthly")
        MONTHLY
    }
}
