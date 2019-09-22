package com.rajnikanth.app.buckspy.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "ExpenseDetails")
public class ExpenseDetails {
    public long created_timestamp;
    public long modified_timestamp;
    @PrimaryKey
    @NonNull
    public String expense_id;
    public String category_id;
    public String account_id;
    public String expense_amount;
    public String expense_description;
    public String image_path;
    public String expence_location;

    public String getExpence_location() {
        return expence_location;
    }

    public void setExpence_location(String expence_location) {
        this.expence_location = expence_location;
    }

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

    public String getExpense_id() {
        return expense_id;
    }

    public void setExpense_id(String expense_id) {
        this.expense_id = expense_id;
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

    public String getExpense_amount() {
        return expense_amount;
    }

    public void setExpense_amount(String expense_amount) {
        this.expense_amount = expense_amount;
    }

    public String getExpense_description() {
        return expense_description;
    }

    public void setExpense_description(String expense_description) {
        this.expense_description = expense_description;
    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }
}
