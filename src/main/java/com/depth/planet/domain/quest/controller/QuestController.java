package com.depth.planet.domain.quest.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.depth.planet.domain.quest.dto.QuestDto;
import com.depth.planet.domain.quest.service.QuestService;
import com.depth.planet.system.security.model.UserDetails;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "Quest", description = "퀘스트 관련 API (아직 테스트 안됨. 엔드포인트만 참고)")
@RestController
@RequestMapping("/api/quest")
@RequiredArgsConstructor
public class QuestController {
  private final QuestService questService;

  @GetMapping("/my")
  @Operation(summary = "기간별 내 퀘스트 조회", description = "지정된 기간 내의 내 완료된 퀘스트 목록을 조회합니다.")
  @ApiResponse(responseCode = "200", description = "퀘스트 조회 성공")
  public List<QuestDto.QuestResponse> findMyQuestsBetween(
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
      @Parameter(hidden = true) @AuthenticationPrincipal UserDetails user) {
    return questService.findMyQuestsBetween(startDate, endDate, user);
  }

  @PostMapping("/suggestions/generate")
  @Operation(summary = "퀘스트 제안 생성", description = "AI를 활용하여 개인화된 퀘스트 제안을 생성합니다. (30분동안 캐시됨)")
  @ApiResponse(responseCode = "200", description = "퀘스트 제안 생성 성공")
  public List<QuestDto.QuestSuggestionResponse> generateQuestSuggestions(
      @Parameter(hidden = true) @AuthenticationPrincipal UserDetails user) {
    return questService.generateQuestSuggestions(user);
  }

  @GetMapping("/suggestions")
  @Operation(summary = "캐시된 퀘스트 제안 조회", description = "이전에 캐시된 퀘스트 제안 목록을 조회합니다.")
  @ApiResponse(responseCode = "200", description = "퀘스트 제안 조회 성공")
  public List<QuestDto.QuestSuggestionResponse> findCachedQuestSuggestions(
      @Parameter(hidden = true) @AuthenticationPrincipal UserDetails user) {
    return questService.findCachedQuestSuggestions(user);
  }

  @PostMapping("/suggestions/{uuid}/approve")
  @Operation(summary = "퀘스트 제안 승인", description = "퀘스트 제안을 승인하여 실제 퀘스트로 등록합니다.")
  @ApiResponse(responseCode = "200", description = "퀘스트 제안 승인 성공")
  public QuestDto.QuestResponse approveQuestSuggestion(
      @PathVariable String uuid,
      @Parameter(hidden = true) @AuthenticationPrincipal UserDetails user) {
    return questService.approveQuestSuggestion(uuid, user);
  }

  @GetMapping("/today")
  @Operation(summary = "오늘의 퀘스트 조회", description = "오늘 날짜의 수행해야 하는 내 퀘스트를 조회합니다.")
  @ApiResponse(responseCode = "200", description = "오늘의 퀘스트 조회 성공")
  public QuestDto.QuestResponse findMyQuestToday(
      @Parameter(hidden = true) @AuthenticationPrincipal UserDetails user) {
    return questService.findMyQuestToday(user);
  }

  @PutMapping("/{questId}/complete/without-image")
  @Operation(summary = "이미지 없이 퀘스트 완료", description = "이미지 없이 퀘스트를 완료 처리합니다. 경험치 +20")
  @ApiResponse(responseCode = "200", description = "퀘스트 완료 성공")
  public QuestDto.QuestResponse completeQuestWithoutImage(
      @PathVariable Long questId,
      @Parameter(hidden = true) @AuthenticationPrincipal UserDetails user) {
    return questService.completeQuestWithoutImage(questId, user);
  }

  @PutMapping(value = "/{questId}/complete/with-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @Operation(summary = "이미지와 함께 퀘스트 완료", description = "증거 이미지와 함께 퀘스트를 완료 처리합니다. 경험치 +30")
  @ApiResponse(responseCode = "200", description = "퀘스트 완료 성공")
  public QuestDto.QuestResponse completeQuestWithImage(
      @PathVariable Long questId,
      @ModelAttribute @Valid QuestDto.CompleteQuestWithImageRequest request,
      @Parameter(hidden = true) @AuthenticationPrincipal UserDetails user) {
    return questService.completeQuestWithImage(questId, request, user);
  }

  @DeleteMapping("/my/all")
  @Operation(summary = "내 모든 퀘스트 삭제", description = "현재 로그인한 유저의 모든 퀘스트를 삭제합니다. 연관된 이미지 파일도 함께 삭제됩니다.")
  @ApiResponse(responseCode = "200", description = "모든 퀘스트 삭제 성공")
  public void deleteAllMyQuests(
      @Parameter(hidden = true) @AuthenticationPrincipal UserDetails user) {
    questService.deleteAllQuestsByUser(user);
  }
}
