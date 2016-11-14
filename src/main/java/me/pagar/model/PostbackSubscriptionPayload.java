package me.pagar.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import me.pagar.SubscriptionStatus;

/**
 * Used in postback url to parse payload data to object. Can't be used know
 * because Postback is not returning payload as JSON, just x-www-form-urlencoded
 */
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

	@Expose(serialize = false)
	private Subscription subscription;

}
