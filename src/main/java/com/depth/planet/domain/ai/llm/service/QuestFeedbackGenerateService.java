package com.depth.planet.domain.ai.llm.service;

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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
public class QuestFeedbackGenerateService {


    private final GoogleAiGeminiChatModel chatModel;
    private final FileSystemHandler fileSystemHandler;

    public QuestFeedbackGenerateService(
            @Qualifier("plain-text-model")
            GoogleAiGeminiChatModel chatModel,
            FileSystemHandler fileSystemHandler
    ) {
        this.chatModel = chatModel;
        this.fileSystemHandler = fileSystemHandler;
    }

    @SneakyThrows
    public String generateQuestFeedback(Quest quest) {
        cannotFeedbackWhenNotQuestCompleted(quest);

        if(quest.getEvidenceImage() != null) {
            Resource imageResource = fileSystemHandler.loadFileAsResource(quest.getEvidenceImage());
            byte[] resouceBytes = imageResource.getContentAsByteArray();
            ImageContent imageContent = ImageContent.from(Base64.getMimeEncoder().encodeToString(resouceBytes), "image/png");

            ChatResponse response = chatModel.chat(
                    SystemMessage.from("""
                            You are a warm and supportive "Quest Companion."
                                    Your primary role is to provide positive and empathetic feedback when a user submits a photo as proof of completing a quest. You will receive the quest title and the user's image.
                            
                                    **Your Task:**
                                    Analyze the context from the `Quest Title` and the contents of the `Image`, then generate a short, heartwarming text response.
                            
                                    **Response Guidelines:**
                                    1.  **Tone:** Your response must be celebratory, empathetic, and encouraging. Make the user feel proud of their accomplishment, no matter how small.
                                    2.  **Length:** The response should be approximately 3 lines long.
                                    3.  **Language:** **Crucially, your entire output must be in KOREAN.**
                            
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
                            imageContent
                    )
            );

            return response.aiMessage().text();
        } else {
            ChatResponse response = chatModel.chat(
                    SystemMessage.from("""
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
                            TextContent.from("Completed Quest: " + quest.getTitle())
                    )
            );

            return response.aiMessage().text();
        }
    }

    private static void cannotFeedbackWhenNotQuestCompleted(Quest quest) {
        if(!quest.getIsCompleted()) {
            throw new RestException(ErrorCode.QUEST_NOT_COMPLETED);
        }
    }
}
