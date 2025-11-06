package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

import java.util.List;
import java.util.stream.IntStream;

@RequiredArgsConstructor
@Service
public class TestServiceImpl implements TestService {

    private final IOService ioService;

    private final QuestionDao questionDao;

    @Override
    public TestResult executeTestFor(Student student) {
        ioService.printLine("");
        ioService.printFormattedLine("Please answer the questions below%n");
        var questions = questionDao.findAll();
        var testResult = new TestResult(student);

        for (var question: questions) {
            ioService.printLine("\n" + question.text());
            final List<Answer> answers = question.answers();
            IntStream.range(0, answers.size()).forEach(index -> {
                ioService.printLine(String.format("%s. %s", index + 1, answers.get(index).text()));
            });
            var studentAnswer = ioService.readStringWithPrompt("\nPlease input correct option number").trim();
            var isAnswerValid = false;

            if (studentAnswer.matches("[1-9]")) {
                final int index = Integer.parseInt(studentAnswer) - 1;
                isAnswerValid = index < answers.size() && answers.get(index).isCorrect();
            }

            testResult.applyAnswer(question, isAnswerValid);
        }
        return testResult;
    }
}
