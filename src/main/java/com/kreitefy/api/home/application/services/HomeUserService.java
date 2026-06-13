package com.kreitefy.api.home.application.services;

import com.kreitefy.api.home.application.ports.in.GetLimitFilterStyleUseCase;
import com.kreitefy.api.home.application.ports.in.GetRecomendUserUseCase;

public interface HomeUserService extends GetRecomendUserUseCase, GetLimitFilterStyleUseCase {
}
