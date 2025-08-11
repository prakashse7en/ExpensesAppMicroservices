package com.digital.transactions.expenses.pojo.model;


import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.UUID;


@Data

public class User implements Serializable {


    private UUID userId;

    private String userName;
    @ToString.Exclude
    private String userPhoneNumber;
    @ToString.Exclude
    private String userEmail;


}
