package com.kreitefy.api.users.application.ports.in.auth;

import com.kreitefy.api.users.domain.models.AuthToken;

public interface LoginCaseUse {
    AuthToken login(String username, String password);
}
