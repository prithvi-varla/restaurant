package com.midtier.bonmunch.security.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 *
 * @author prithvi
 */
@Data @NoArgsConstructor @AllArgsConstructor @ToString
public class  AuthRequest {

    private String username;

    private String password;

}
