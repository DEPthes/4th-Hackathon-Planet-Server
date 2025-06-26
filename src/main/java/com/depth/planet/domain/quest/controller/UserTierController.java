package com.depth.planet.domain.quest.controller;

import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.depth.planet.domain.quest.dto.TierDto;
import com.depth.planet.domain.quest.service.UserTierService;
import com.depth.planet.system.security.model.UserDetails;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "UserTier", description = "사용자 티어 관련 API")
@RestController
@RequestMapping("/api/tier")
@RequiredArgsConstructor
public class UserTierController {
  private final UserTierService userTierService;

  @PostMapping(value = "/exp", consumes = MediaType.APPLICATION_JSON_VALUE)
  @Operation(summary = "경험치 추가", description = "현재 로그인한 사용자에게 경험치를 추가합니다.")
  @ApiResponse(responseCode = "200", description = "경험치 추가 성공")
  public TierDto.TierResponse addExp(
      @RequestBody @Valid TierDto.AddExpRequest request,
      @Parameter(hidden = true) @AuthenticationPrincipal UserDetails user) {
    return userTierService.addExpToUser(user.getKey(), request.getExp());
  }

  @GetMapping("/current")
  @Operation(summary = "현재 월 티어 조회", description = "현재 로그인한 사용자의 이번 달 티어 정보를 조회합니다.")
  @ApiResponse(responseCode = "200", description = "티어 조회 성공")
  public TierDto.TierResponse getCurrentMonthTier(
      @Parameter(hidden = true) @AuthenticationPrincipal UserDetails user) {
    return userTierService.findThisMonthTier(user.getKey());
  }

  @GetMapping("/specific")
  @Operation(summary = "특정 월 티어 조회", description = "현재 로그인한 사용자의 특정 년월 티어 정보를 조회합니다.")
  @ApiResponse(responseCode = "200", description = "티어 조회 성공")
  public TierDto.TierResponse getSpecificMonthTier(
      @RequestParam Integer year,
      @RequestParam Integer month,
      @Parameter(hidden = true) @AuthenticationPrincipal UserDetails user) {
    return userTierService.findSpecificMonthTier(user.getKey(), year, month);
  }
}