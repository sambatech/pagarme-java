package me.pagar.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import me.pagar.TransactionStatus;

public class PostbackTransactionPayload extends PagarMeModel<String> {

	@Expose(serialize = false)
	@SerializedName("old_status")
    private TransactionStatus oldStatus;

	@Expose(serialize = false)
	@SerializedName("current_status")
	private TransactionStatus currentStatus;

	@Expose(serialize = false)
	@SerializedName("desired_status")
	private TransactionStatus desiredStatus;

	@Expose(serialize = false)
	private String event;

}
