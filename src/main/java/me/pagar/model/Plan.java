package me.pagar.model;

import java.util.Collection;

import javax.ws.rs.HttpMethod;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import me.pagar.PaymentMethod;
import me.pagar.util.JSONUtils;

public class Plan extends PagarMeModel<String> {

	@Expose
    private Integer amount;

	@Expose
    private Integer days;

	@Expose
    private String name;

	@Expose
	@SerializedName("trial_days")
    private Integer trialDays;

	@Expose
	@SerializedName("payment_methods")
    private Collection<PaymentMethod> paymentMethods;

	@Expose
    private Integer charges;

	@Expose
    private Integer installments;

    public Plan save() throws PagarMeException {
        final Plan saved = super.save(getClass());
        copy(saved);

        return saved;
    }

    public Plan find(String id) throws PagarMeException {

        final PagarMeRequest request = new PagarMeRequest(HttpMethod.GET,
                String.format("/%s/%s", getClassName(), id));

        final Plan other = JSONUtils.getAsObject((JsonObject) request.execute(), Plan.class);
        copy(other);
        flush();

        return other;
    }

    public Plan refresh() throws PagarMeException {
        final Plan other = JSONUtils.getAsObject(refreshModel(), Plan.class);
        copy(other);
        flush();
        return other;
    }

    private void copy(Plan other) {
        setId(other.getId());
	    this.amount = other.amount;
		this.days = other.days;
		this.name = other.name;
		this.trialDays = other.trialDays;
		this.paymentMethods = other.paymentMethods;
		this.charges = other.charges;
		this.installments = other.installments;
    }

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public Integer getDays() {
		return days;
	}

	public void setDays(Integer days) {
		this.days = days;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getTrialDays() {
		return trialDays;
	}

	public void setTrialDays(Integer trialDays) {
		this.trialDays = trialDays;
		addUnsavedProperty("trialDays");
	}

	public Collection<PaymentMethod> getPaymentMethods() {
		return paymentMethods;
	}

	public void setPaymentMethods(Collection<PaymentMethod> paymentMethods) {
		this.paymentMethods = paymentMethods;
		addUnsavedProperty("paymentMethods");
	}

	public Integer getCharges() {
		return charges;
	}

	public void setCharges(Integer charges) {
		this.charges = charges;
		addUnsavedProperty("charges");
	}

	public Integer getInstallments() {
		return installments;
	}

	public void setInstallments(Integer installments) {
		this.installments = installments;
		addUnsavedProperty("installments");
	}

}
