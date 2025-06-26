package com.depth.planet.domain.ai.llm.service;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.depth.planet.domain.file.handler.FileSystemHandler;
import com.depth.planet.domain.quest.entity.Quest;
import com.depth.planet.system.exception.model.ErrorCode;
import com.depth.planet.system.exception.model.RestException;

import dev.langchain4j.data.message.ImageContent;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.TextContent;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;
import lombok.SneakyThrows;

@Service
public class QuestFeedbackGenerateService {

    private final GoogleAiGeminiChatModel chatModel;
    private final FileSystemHandler fileSystemHandler;

    // 썸네일 크기 상수
    private static final int THUMBNAIL_WIDTH = 512;
    private static final int THUMBNAIL_HEIGHT = 512;

    public QuestFeedbackGenerateService(
            @Qualifier("plain-text-model") GoogleAiGeminiChatModel chatModel,
            FileSystemHandler fileSystemHandler) {
        this.chatModel = chatModel;
        this.fileSystemHandler = fileSystemHandler;
    }

    @SneakyThrows
    public String generateQuestFeedback(Quest quest) {
        cannotFeedbackWhenNotQuestCompleted(quest);

        if (quest.getEvidenceImage() != null) {
            Resource imageResource = fileSystemHandler.loadFileAsResource(quest.getEvidenceImage());
            byte[] resourceBytes = imageResource.getContentAsByteArray();

            // 이미지를 썸네일 크기로 리사이징
            byte[] thumbnailBytes = createThumbnail(resourceBytes);
            ImageContent imageContent = ImageContent.from(Base64.getEncoder().encodeToString(thumbnailBytes),
                    "image/jpeg");

            ChatResponse response = chatModel.chat(
                    SystemMessage
                            .from("""
                                    You are a warm and supportive "Quest Companion."
                                            Your primary role is to provide positive and empathetic feedback when a user submits a photo as proof of completing a quest. You will receive the quest title and the user's image.

                                            **Your Task:**
                                            Analyze the context from the `Quest Title` and the contents of the `Image`, then generate a short, heartwarming text response.

                                            **Response Guidelines:**
                                            1.  **Tone:** Your response must be celebratory, empathetic, and encouraging. Make the user feel proud of their accomplishment, no matter how small.
                                            2.  **Length:** The response should be approximately 3 lines long.
                                            3.  **Language:** **Crucially, your entire output must be in KOREAN.**
                                            4.  **Content:** 사진을 언급하는 내용을 포함해야 합니다. 예를 들어, 사진 속의 내용이나 사용자의 행동에 대해 언급하세요.

                                            **Example Scenario:**
                                            * **Input Quest Title:** "책 읽고, 마음에 드는 문장 고르기 ✍️" (Read a book and pick your favorite sentence ✍️)
                                            * **Input Image:** A photo of a book page with a highlighted sentence.
                                            * **Example Korean Output:**
                                                고르신 책 글귀는 저도 마음에 드는데요?
                                                인생을 살아가며 꼭 필요한 문장같아요.
                                                책을 읽으며 힐링이 되셨길 바라요.

                                            **Another Example:**
                                            * **Input Quest Title:** "10분간 햇빛을 쬐며 걷고, 하늘 사진 찍기 📸" (Walk in the sun for 10 minutes and take a picture of the sky 📸)
                                            * **Input Image:** A photo of the sky.
                                            * **Example Korean Output:**
                                                오늘 하늘은 정말 아름다웠네요!
                                                사진을 보니 제 마음까지 맑아지는 기분이에요.
                                                잠깐의 산책이 멋진 선물을 주었네요.

                                            **Your final output must be ONLY the Korean text response, without any other text, titles, or explanations.**

                                            출력 텍스트는 JSON 형식이 아닌 순수한 텍스트로만 작성되어야 합니다. 예를 들어, "{"text": "..." }"와 같은 JSON 형식은 사용하지 마세요.
                                    """),
                    UserMessage.from(
                            TextContent.from("Completed Quest: " + quest.getTitle()),
                            imageContent));

            return response.aiMessage().text();
        } else {
            ChatResponse response = chatModel.chat(
                    SystemMessage
                            .from("""
                                            You are a warm and supportive "Quest Companion."
                                            Your primary role is to provide positive and empathetic feedback when a user reports they have completed a quest. You will receive only the quest title.

                                            **Your Task:**
                                            Based ONLY on the provided `Quest Title`, imagine the user's effort and feelings, then generate a short, heartwarming text response to celebrate their accomplishment. **You will not receive an image.**

                                            **Response Guidelines:**
                                            1.  **Tone:** Your response must be celebratory, empathetic, and encouraging. Focus on the positive experience or effort associated with the quest itself.
                                            2.  **Length:** The response should be approximately 3 lines long.
                                            3.  **Language:** **Crucially, your entire output must be in KOREAN.**

                                            **Example Scenario 1:**
                                            * **Input Quest Title:** "책 읽고, 마음에 드는 문장 고르기 ✍️" (Read a book and pick your favorite sentence ✍️)
                                            * **Example Korean Output:**
                                                마음에 드는 문장을 발견하셨군요!
                                    책 속에서 나만의 보물을 찾은 기분이겠어요.
                                    그 문장이 당신의 하루를 더욱 빛내주길 바라요.

                                            **Example Scenario 2:**
                                            * **Input Quest Title:** "내 공간을 깨끗이! 방 청소하기 🧹" (Clean your personal space! Tidy up your room 🧹)
                                            * **Example Korean Output:**
                                                수고하셨어요! 주변이 깨끗해지니 마음까지 상쾌해지죠?
                                    정돈된 공간에서 보내는 시간이 분명 더 행복할 거예요.
                                    편안한 휴식을 즐기세요!

                                            **Your final output must be ONLY the Korean text response, without any other text, titles, or explanations.**

                                            출력 텍스트는 JSON 형식이 아닌 순수한 텍스트로만 작성되어야 합니다. 예를 들어, "{"text": "..." }"와 같은 JSON 형식은 사용하지 마세요.
                                    """),
                    UserMessage.from(
                            TextContent.from("Completed Quest: " + quest.getTitle())));

            return response.aiMessage().text();
        }
    }

    /**
     * 이미지를 썸네일 크기로 리사이징하는 메서드
     * 
     * @param originalImageBytes 원본 이미지 바이트 배열
     * @return 썸네일로 변환된 이미지 바이트 배열
     * @throws IOException 이미지 처리 중 오류 발생 시
     */
    private byte[] createThumbnail(byte[] originalImageBytes) throws IOException {
        // 원본 이미지 로드
        BufferedImage originalImage = ImageIO.read(new ByteArrayInputStream(originalImageBytes));

        if (originalImage == null) {
            throw new RestException(ErrorCode.INTERNAL_SERVER_ERROR, "이미지를 읽을 수 없습니다.");
        }

        // 원본 이미지 크기
        int originalWidth = originalImage.getWidth();
        int originalHeight = originalImage.getHeight();

        // 썸네일 크기 계산 (비율 유지하면서 크롭)
        double scaleX = (double) THUMBNAIL_WIDTH / originalWidth;
        double scaleY = (double) THUMBNAIL_HEIGHT / originalHeight;
        double scale = Math.max(scaleX, scaleY); // 더 큰 스케일 사용하여 크롭

        int scaledWidth = (int) (originalWidth * scale);
        int scaledHeight = (int) (originalHeight * scale);

        // 스케일된 이미지 생성
        BufferedImage scaledImage = new BufferedImage(scaledWidth, scaledHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = scaledImage.createGraphics();

        // 고품질 렌더링 설정
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.drawImage(originalImage, 0, 0, scaledWidth, scaledHeight, null);
        g2d.dispose();

        // 중앙에서 썸네일 크기로 크롭
        int cropX = Math.max(0, (scaledWidth - THUMBNAIL_WIDTH) / 2);
        int cropY = Math.max(0, (scaledHeight - THUMBNAIL_HEIGHT) / 2);

        BufferedImage thumbnail = scaledImage.getSubimage(cropX, cropY,
                Math.min(THUMBNAIL_WIDTH, scaledWidth),
                Math.min(THUMBNAIL_HEIGHT, scaledHeight));

        // 최종 썸네일 이미지 생성 (정확한 크기로)
        BufferedImage finalThumbnail = new BufferedImage(THUMBNAIL_WIDTH, THUMBNAIL_HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D finalG2d = finalThumbnail.createGraphics();
        finalG2d.setColor(Color.WHITE); // 빈 공간은 흰색으로 채움
        finalG2d.fillRect(0, 0, THUMBNAIL_WIDTH, THUMBNAIL_HEIGHT);

        // 중앙에 이미지 배치
        int x = (THUMBNAIL_WIDTH - thumbnail.getWidth()) / 2;
        int y = (THUMBNAIL_HEIGHT - thumbnail.getHeight()) / 2;
        finalG2d.drawImage(thumbnail, x, y, null);
        finalG2d.dispose();

        // JPEG로 변환하여 바이트 배열 반환
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(finalThumbnail, "jpeg", baos);
        return baos.toByteArray();
    }

    private static void cannotFeedbackWhenNotQuestCompleted(Quest quest) {
        if (!quest.getIsCompleted()) {
            throw new RestException(ErrorCode.QUEST_NOT_COMPLETED);
        }
    }
}
