package com.smartbee.crm.login;

import com.smartbee.crm.login.pojo.LoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Api(tags = "Login API")
@Controller
@RequestMapping("/auth")
public class LoginController {

    /**
     * Useless login endpoint. Only for exposing the endpoint on swagger-ui.
     * The login function would be handled by {@link com.smartbee.crm.auth.filter.UserAuthenticationFilter}
     *
     * @param vo with username and password
     * @return ok
     */
    @ApiOperation("login and get the auth token")
    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody LoginVO vo) {
        return ResponseEntity.ok().build();
    }
}
