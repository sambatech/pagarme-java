package me.pagar.model;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.HttpMethod;

import org.joda.time.DateTime;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import me.pagar.model.Transaction.PaymentMethod;
import me.pagar.util.JSONUtils;


public class Payable extends PagarMeModel<Integer> {

    @Expose(serialize = false)
    private Integer amount;

    @Expose(serialize = false)
    private Integer fee;

    @Expose(serialize = false)
    private Integer installment;

    @Expose(serialize = false)
    @SerializedName("transaction_id")
    private Integer transactionId;

    @Expose(serialize = false)
    @SerializedName("split_rule_id")
    private String splitRuleId;

    @Expose(serialize = false)
    @SerializedName("payment_date")
    private DateTime payment;

    @Expose(serialize = false)
    private Status status;

    @Expose(serialize = false)
    private Type type;
    
    @Expose
    @SerializedName("recipient_id")
    private String recipientId;
    
    @Expose
    @SerializedName("anticipation_fee")
    private String anticipationFee;
    
    @Expose
    @SerializedName("bulk_anticipation_id")
    private String bulkAnticipationId;
    
    @Expose
    @SerializedName("original_payment_date")
    private Date originalPaymentDate;
    
    @Expose
    @SerializedName("payment_method")
    private PaymentMethod paymentMethod;
    
    public Payable() {
		super();
	}
    
    public Payable find(Integer id) throws PagarMeException {

        final PagarMeRequest request = new PagarMeRequest(HttpMethod.GET, String.format("/%s/%s", getClassName(), id));

        final Payable other = JSONUtils.getAsObject((JsonObject) request.execute(), Payable.class);
        copy(other);
        flush();

        return other;
    }
    
    public Collection<Payable> findCollection(final Integer totalPerPage, Integer page) throws PagarMeException {
    	return findCollection(totalPerPage, page, null);
    }
    
    public Collection<Payable> findCollection(final Integer totalPerPage, Integer page, final Map<String, Object> filters) 
    		throws PagarMeException {
    	String path = String.format("/%s", getClassName());
    	return _findCollection(path, totalPerPage, page, filters);
    }
    
    public Collection<Payable> findCollection(Transaction transaction, final Integer totalPerPage, Integer page) 
    		throws PagarMeException {
    	String path = String.format("/%s/%s/%s", transaction.getClassName() , transaction.getId(), getClassName());
    	return _findCollection(path, totalPerPage, page, null);
    }
    
    private Collection<Payable> _findCollection(String path, final Integer totalPerPage, Integer page, final Map<String, Object> filters) 
    		throws PagarMeException {
    	final Map<String, Object> parameters = new HashMap<String, Object>();

        if (null != totalPerPage && 0 != totalPerPage) {
            parameters.put("count", totalPerPage);
        }

        if (null == page || 0 >= page) {
            page = 1;
        }

        parameters.put("page", page);

        final PagarMeRequest request = new PagarMeRequest(HttpMethod.GET, path);
        
        request.getParameters().putAll(parameters);
        
        if(filters != null) {
        	request.getParameters().putAll(filters);
        }

        JsonArray payables= request.execute();
        
        return JSONUtils.getAsList(payables, new TypeToken<Collection<Payable>>() {
        }.getType());
    }
    
    private void copy(Payable other) {
    	
    }
    
    public Integer getAmount() {
        return amount;
    }

    public Integer getFee() {
        return fee;
    }

    public Integer getInstallment() {
        return installment;
    }

    public Integer getTransactionId() {
        return transactionId;
    }

    public String getSplitRuleId() {
        return splitRuleId;
    }

    public DateTime getPayment() {
        return payment;
    }

    public Status getStatus() {
        return status;
    }

    public Type getType() {
        return type;
    }
    
    public String getRecipientId() {
		return recipientId;
	}

	public String getAnticipationFee() {
		return anticipationFee;
	}

	public String getBulkAnticipationId() {
		return bulkAnticipationId;
	}

	public Date getOriginalPaymentDate() {
		return originalPaymentDate;
	}

	public PaymentMethod getPaymentMethod() {
		return paymentMethod;
	}

	@Override
    public void setId(Integer id) {
        throw new UnsupportedOperationException("Not allowed.");
    }

    @Override
    public void setClassName(String className) {
        throw new UnsupportedOperationException("Not allowed.");
    }

    public enum Status {

        @SerializedName("paid")
        PAID,

        @SerializedName("waiting_funds")
        WAITING_FUNDS

    }

    public enum Type {

        @SerializedName("chargeback")
        CHARGEBACK,

        @SerializedName("credit")
        CREDIT,

        @SerializedName("refund")
        REFUND

    }
}
