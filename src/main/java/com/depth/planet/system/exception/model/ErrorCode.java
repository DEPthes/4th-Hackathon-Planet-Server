package com.depth.planet.system.exception.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    // Global
    GLOBAL_BAD_REQUEST(400, "올바르지 않은 요청입니다."),
    GLOBAL_NOT_FOUND(404, "요청한 사항을 찾을 수 없습니다."),
    GLOBAL_ALREADY_EXIST(400, "요청의 대상이 이미 존재합니다."),
    GLOBAL_METHOD_NOT_ALLOWED(405, "허용되지 않는 Method 입니다."),
    GLOBAL_INVALID_PARAMETER(400, "올바르지 않은 파라미터입니다."),
    INVALID_INPUT_VALUE(400, "유효하지 않은 입력 값입니다."),

    // Auth
    AUTH_PASSWORD_NOT_MATCH(401, "비밀번호가 올바르지 않습니다."),

    // User
    USER_ALREADY_JOINED(400, "이미 가입된 그룹입니다."),
    USER_NOT_FOUND(404, "존재하지 않는 사용자입니다."),
    USER_PASSWORD_NOT_MATCH(400, "올바른 비밀번호가 아닙니다."),

    // Invite
    INVITE_ALREADY_MEMBER(400, "이미 등록된 멤버입니다."),
    INVITE_CODE_EXPIRED(400, "만료된 초대 코드입니다."),
    INVITE_SELF_INVITATION_NOT_ALLOWED(400, "스스로를 초대할 수 없습니다."),
    INVITE_ALREADY_SENT(400, "이미 초대장이 발송되었습니다."),

    // jwt
    AUTH_TOKEN_NOT_FOUND(401, "인증 토큰을 찾을 수 없습니다."),
    AUTH_TOKEN_EXPIRED(401, "토큰이 만료되었습니다."),
    AUTH_TOKEN_INVALID(401, "올바른 토큰이 아닙니다."),
    AUTH_TOKEN_MALFORMED(401, "토큰 형식이 올바르지 않습니다."),
    AUTH_AUTHENTICATION_FAILED(401, "인증에 실패했습니다."),
    AUTH_USER_NOT_FOUND(404, "등록된 유저를 찾을 수 없습니다."),
    AUTH_FORBIDDEN(403, "접근 권한이 없습니다."),
    AUTH_CANNOT_GENERATE_TOKEN(400, "인증키를 생성 할 수 없습니다."),

    // File
    FILE_ALREADY_EXISTS(400, "파일이 이미 존재합니다."),
    FILE_NOT_FOUND(404, "파일을 찾을 수 없습니다."),
    FILE_NOT_IMAGE(400, "이미지 파일이 아닙니다."),

    // Quest
    QUEST_ALREADY_COMPLETED(400, "이미 완료된 퀘스트입니다."),
    QUEST_ALREADY_APPROVED_TODAY(400, "오늘 이미 승인한 퀘스트가 있습니다."),
    QUEST_SUGGESTION_NOT_FOUND(404, "퀘스트 제안을 찾을 수 없습니다."),
    QUEST_SUGGESTION_SIZE_MISMATCH(400, "퀘스트 제안의 개수가 일치하지 않습니다."),
    QUEST_NOT_COMPLETED(400, "퀘스트가 완료되지 않았습니다."),
    // Other
    INTERNAL_SERVER_ERROR(500, "오류가 발생했습니다."),
    ;

    private final int statusCode;
    private final String message;
}