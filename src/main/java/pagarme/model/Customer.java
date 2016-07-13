package pagarme.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Collection;
import org.joda.time.LocalDate;

public class Customer extends PagarMeModel<Integer>{

    @Expose
    @SerializedName("document_number")
    private String documentNumber;

    @Expose(serialize = false)
    @SerializedName("document_type")
    private String documentType;

    @Expose
    private String name;

    @Expose
    private String email;

    @Expose
    private String gender;

    @Expose
    @SerializedName("born_at")
    private LocalDate bornAt;

    @Expose
    private Phone phone;

    @Expose
    private Address address;

    public Customer() {
        super();
    }

    public Customer(final String name, final String email) {
        this();
        this.name = name;
        this.email = email;
        this.address = new Address();
        this.phone = new Phone();
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public String getDocumentType() {
        return documentType;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getGender() {
        return gender;
    }

    public LocalDate getBornAt() {
        return bornAt;
    }

    public Phone getPhone() {
        return phone;
    }

    public Address getAddress() {
        return address;
    }

    public void setDocumentNumber(final String documentNumber) {
        this.documentNumber = documentNumber;
        addUnsavedProperty("documentNumber");
    }

    public void setDocumentType(final String documentType) {
        this.documentType = documentType;
        addUnsavedProperty("documentType");
    }

    public void setName(final String name) {
        this.name = name;
        addUnsavedProperty("name");
    }

    public void setEmail(final String email) {
        this.email = email;
        addUnsavedProperty("email");
    }

    public void setGender(final String gender) {
        this.gender = gender;
        addUnsavedProperty("gender");
    }

    public void setBornAt(final LocalDate bornAt) {
        this.bornAt = bornAt;

        System.out.println(this.bornAt);

        if (bornAt != null) {
            addUnsavedProperty("bornAt");
        }
    }

    public void setPhone(final Phone phone) {
        this.phone = phone;
        addUnsavedProperty("phone");
    }

    public void setAddress(final Address address) {
        this.address = address;
        addUnsavedProperty("address");
    }

    @Override
    public void setId(Integer id) {
        throw new UnsupportedOperationException("Not allowed.");
    }

    @Override
    public void setClassName(String className) {
        throw new UnsupportedOperationException("Not allowed.");
    }
}