package ru.otus.hw.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.CsvQuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@SpringBootTest
class CsvQuestionDAOTest {

    @Mock
    private TestFileNameProvider fileNameProvider;

    @InjectMocks
    private CsvQuestionDao questionDao;

    @DisplayName("Not empty and valid test question list")
    @Test
    void findAll_validFileName_questionsLoaded() {
        when(this.fileNameProvider.getTestFileName()).thenReturn("questions-test.csv");
        final List<Question> foundQuestions = this.questionDao.findAll();

        assertThat(foundQuestions)
            .hasSize(2)
            .extracting(Question::text)
            .containsExactly("Question1", "Question2");

        assertThat(foundQuestions.get(0).answers())
            .extracting(Answer::text)
            .containsExactly("Answer1-1", "Answer1-2", "Answer1-3");

        assertThat(foundQuestions.get(1).answers())
            .extracting(Answer::text)
            .containsExactly("Answer2-1", "Answer2-2", "Answer2-3");

        assertThat(foundQuestions.get(0).answers())
            .extracting(Answer::isCorrect)
            .containsExactly(true, false, false);

        assertThat(foundQuestions.get(1).answers())
            .extracting(Answer::isCorrect)
            .containsExactly(false, false, true);
    }
}

