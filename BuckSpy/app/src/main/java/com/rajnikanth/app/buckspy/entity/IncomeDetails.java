package com.rajnikanth.app.buckspy.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "IncomeDetails")
public class IncomeDetails {
    public long created_timestamp;
    public long modified_timestamp;
    @PrimaryKey
    @NonNull
    public String income_id;
    public String category_id;
    public String account_id;
    public String income_amount;

    public long getCreated_timestamp() {
        return created_timestamp;
    }

    public void setCreated_timestamp(long created_timestamp) {
        this.created_timestamp = created_timestamp;
    }

    public long getModified_timestamp() {
        return modified_timestamp;
    }

    public void setModified_timestamp(long modified_timestamp) {
        this.modified_timestamp = modified_timestamp;
    }

    public String getIncome_id() {
        return income_id;
    }

    public void setIncome_id(String income_id) {
        this.income_id = income_id;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getAccount_id() {
        return account_id;
    }

    public void setAccount_id(String account_id) {
        this.account_id = account_id;
    }

    public String getIncome_amount() {
        return income_amount;
    }

    public void setIncome_amount(String income_amount) {
        this.income_amount = income_amount;
    }

    public String getIncome_description() {
        return income_description;
    }

    public void setIncome_description(String income_description) {
        this.income_description = income_description;
    }

    public String income_description;

}
