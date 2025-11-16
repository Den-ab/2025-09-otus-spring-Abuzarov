package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

import java.util.List;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final LocalizedIOService ioService;

    private final QuestionDao questionDao;

    @Override
    public TestResult executeTestFor(Student student) {
        ioService.printLine("");
        ioService.printLineLocalized("TestService.answer.the.questions");
        ioService.printLine("");

        var questions = questionDao.findAll();
        var testResult = new TestResult(student);

        for (var question: questions) {
            ioService.printLine("\n" + question.text());
            final List<Answer> answers = question.answers();
            IntStream.range(0, answers.size()).forEach(index -> {
                ioService.printLine(String.format("%s. %s", index + 1, answers.get(index).text()));
            });
            var studentAnswer = ioService.readIntForRangeWithPrompt(
                1,
                answers.size(),
                "\nPlease input correct option number",
                "Incorrect option number"
            );
            final int index = studentAnswer - 1;
            var isAnswerValid = index < answers.size() && answers.get(index).isCorrect();

            testResult.applyAnswer(question, isAnswerValid);
        }
        return testResult;
    }

}
