package ru.otus.hw.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ru.otus.hw.config.AppProperties;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.CsvQuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;

import java.util.List;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
class CsvQuestionDAOIntegrationTest {

    @Autowired
    private TestFileNameProvider fileNameProvider;

    @Autowired
    private CsvQuestionDao questionDao;

    @Autowired
    private AppProperties appProperties;

    @DisplayName("Valid file name")
    @Test
    void getTestFileName_validFileName() {

        if (this.appProperties.getLocale().equals(Locale.US)) {

            assertThat(this.fileNameProvider.getTestFileName()).isEqualTo("questions.csv");
        }
        else {

            assertThat(this.fileNameProvider.getTestFileName()).isEqualTo("questions_ru.csv");
        }
    }

    @DisplayName("Not empty and valid question list")
    @Test
    void findAll_validFileName_questionsLoaded() {

        final List<Question> foundQuestions = this.questionDao.findAll();

        final String testFileName = this.fileNameProvider.getTestFileName();
        final boolean enFile = testFileName.equals("questions.csv");

        assertThat(foundQuestions)
            .hasSize(3)
            .extracting(Question::text)
            .containsExactly(
                enFile ? "Is there life on Mars?" : "Есть ли жизнь на Марсе?",
                enFile
                    ? "How should resources be loaded form jar in Java?"
                    : "Как нужно загружать ресурс из jar в Java?",
                enFile
                    ? "Which option is a good way to handle the exception?"
                    : "Какой предпочтительный способ реакции на исключения?"
            );

        assertThat(foundQuestions.get(0).answers())
            .extracting(Answer::text)
            .containsExactly(
                enFile ? "Science doesn't know this yet" : "Это науке пока не известно",
                enFile
                    ? "Certainly. The red UFO is from Mars. And green is from Venus"
                    : "Конечно. Красные (НЛО) это с Марса. А зеленые (НЛО) с Венеры",
                enFile ? "Absolutely not" : "Точно нет"
            );

        assertThat(foundQuestions.get(1).answers())
            .extracting(Answer::text)
            .containsExactly(
                "ClassLoader#geResourceAsStream " + (enFile ? "or" : "или") + " ClassPathResource#getInputStream",
                "ClassLoader#geResource#getFile + FileReader",
                "Wingardium Leviosa"
            );

        assertThat(foundQuestions.get(2).answers())
            .extracting(Answer::text)
            .containsExactly(
                "@SneakyThrow",
                "e.printStackTrace()",
                enFile
                    ? "Rethrow with wrapping in business exception (for example, QuestionReadException)"
                    : "Проброс дальше с предварительным оборачиванием в бизнес-исключение (например, QuestionReadException)",
                enFile ? "Ignoring exception" : "Игнорирование исключения"
            );

        assertThat(foundQuestions.get(0).answers())
            .extracting(Answer::isCorrect)
            .containsExactly(true, false, false);

        assertThat(foundQuestions.get(1).answers())
            .extracting(Answer::isCorrect)
            .containsExactly(true, false, false);

        assertThat(foundQuestions.get(2).answers())
            .extracting(Answer::isCorrect)
            .containsExactly(false, false, true, false);
    }
}
