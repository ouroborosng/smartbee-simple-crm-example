package com.smartbee.crm.login.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel(value = "Login Model")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginVO {

    @ApiModelProperty(value = "user name", example = "admin", required = true)
    private String username;
    @ApiModelProperty(value = "password", example = "admin",  required = true)
    private String password;
}
