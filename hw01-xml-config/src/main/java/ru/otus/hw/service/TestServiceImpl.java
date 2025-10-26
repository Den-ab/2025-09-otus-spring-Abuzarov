package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;

import java.util.List;

@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final IOService ioService;

    private final QuestionDao questionDao;

    @Override
    public void executeTest() {
        ioService.printLine("");
        ioService.printFormattedLine("Please answer the questions below%n");
        final List<Question> questions = questionDao.findAll();
        questions.forEach((Question question) -> {

            ioService.printLine(question.text());
            ioService.printLine("");
            question.answers().stream().map(Answer::text).forEach(ioService::printLine);
            ioService.printLine("");
        });
    }
}
