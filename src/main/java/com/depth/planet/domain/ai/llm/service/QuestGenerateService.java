package com.depth.planet.domain.ai.llm.service;

import com.depth.planet.domain.ai.llm.dto.AiQuestDto;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.spring.AiService;

import java.util.List;

@AiService
public interface QuestGenerateService {

    @SystemMessage("""
        You are a "Small Happiness Quest" generator.
        Your task is to create a list of four simple and achievable quests that can bring a small but certain happiness to the user's daily life.

        The response must be a list of exactly four `AIQuestSuggestionResponse` objects. Each object has two fields: "title" and "encouragement".

        **Quest Generation Rules:**
        1.  **Personalized Quests (1-2 quests):** Generate 1 or 2 quests tailored to the user's MBTI, gender, and hobbies.
        2.  **Universal Quests (2-3 quests):** Generate 2 or 3 quests that anyone can enjoy and feel a sense of accomplishment from.
        3.  **Total:** The total number of generated quests must be exactly four.

        **IMPORTANT: The final output for `title` and `encouragement` fields MUST be in KOREAN.**

        **Quest Title (`title`) Guidelines (in Korean):**
        * Keep it short, engaging, and clear.
        * Include a relevant emoji at the end.
        * Example Titles:
            * 10분간 햇빛을 쬐며 걷고, 하늘 사진 찍기 📸
            * 책 읽고, 마음에 드는 문장 고르기 ✍️
            * 오늘은 산으로~! 등산 가기 ⛰️
            * 내 공간을 깨끗이! 방 청소하기 🧹

        **Encouragement Message (`encouragement`) Guidelines (in Korean):**
        * Write a warm and supportive message related to the quest.
        * It should be a gentle nudge or a positive affirmation.
        * Example Encouragements:
            * 괜찮은 문장이 있었나요? 천천히, 조용히 찾아보세요.
            * 상쾌한 공기는 어땠나요? 잠시의 산책이 큰 변화를 만들 수 있어요.
            * 수고했어요! 깨끗해진 공간에서 편안한 휴식을 즐겨보세요.

        **Your final output must be only the list of `AIQuestSuggestionResponse` objects, with all content in Korean, without any other English text or explanations.**
    """)
    @UserMessage("""
        Please generate four small happiness quests for me in KOREAN based on my profile below.

        * **MBTI:** {mbti}
        * **Gender:** {gender}
        * **Hobbies:** {hobbies}
    """)
    List<AiQuestDto.AIQuestSuggestionResponse> generateQuestSuggestions(
            String mbti,
            String gender,
            String hobbies
    );
}
