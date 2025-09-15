package com.poc.POC_CHAT.interfaces;

import com.poc.POC_CHAT.dtos.*;

public interface IAuthService {
    AuthResponseDTO login(UserRequestDTO request);
    AuthResponseDTO register(RegisterRequestDTO request);
}
