package com.depth.planet.domain.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.depth.planet.domain.user.dto.UserDto;
import com.depth.planet.domain.user.service.UserService;
import com.depth.planet.system.security.model.UserDetails;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "User", description = "사용자 관련 API")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @GetMapping("/{email}")
  @Operation(summary = "사용자 정보 조회", description = "이메일을 사용하여 특정 사용자의 정보를 조회합니다. (관리자 전용)")
  @ApiResponse(responseCode = "200", description = "사용자 정보 조회 성공")
  public ResponseEntity<UserDto.UserResponse> getUserByEmail(
      @PathVariable String email,
      @Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails) {
    UserDto.UserResponse userResponse = userService.findUserByEmail(email, userDetails);
    return ResponseEntity.ok(userResponse);
  }

  @PatchMapping("/{email}")
  @Operation(summary = "사용자 정보 수정", description = "특정 사용자의 정보를 수정합니다. 관리자 또는 본인만 수정할 수 있습니다.")
  @ApiResponse(responseCode = "200", description = "사용자 정보 수정 성공")
  public ResponseEntity<UserDto.UserResponse> updateUser(
      @PathVariable String email,
      @RequestBody @Valid UserDto.UserUpdateRequest request,
      @Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails) {
    UserDto.UserResponse userResponse = userService.updateUser(email, request, userDetails);
    return ResponseEntity.ok(userResponse);
  }
}