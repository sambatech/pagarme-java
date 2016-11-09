package me.pagar.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import me.pagar.SubscriptionStatus;

public class PostbackSubscriptionPayload extends PagarMeModel<String> {

	@Expose(serialize = false)
	@SerializedName("old_status")
    private SubscriptionStatus oldStatus;

	@Expose(serialize = false)
	@SerializedName("current_status")
	private SubscriptionStatus currentStatus;

	@Expose(serialize = false)
	@SerializedName("desired_status")
	private SubscriptionStatus desiredStatus;

	@Expose(serialize = false)
	private String event;

}
