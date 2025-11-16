package ru.otus.hw.dao;

import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.dto.QuestionDto;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;
import ru.otus.hw.service.IOService;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Component
public class CsvQuestionDao implements QuestionDao {
    private final TestFileNameProvider fileNameProvider;

    private final IOService ioService;

    @Override
    public List<Question> findAll() {
        final String questionsFileName = this.fileNameProvider.getTestFileName();
        try (Reader reader = this.openResourceReader(questionsFileName)) {
            CSVReader csvReader = this.buildCSVReader(reader, ';', false, 1);
            List<QuestionDto> questionDtos = new CsvToBeanBuilder<QuestionDto>(csvReader)
                .withType(QuestionDto.class)
                .withIgnoreLeadingWhiteSpace(true)
                .build()
                .parse();

            return questionDtos.stream()
                .map(QuestionDto::toDomainObject)
                .toList();
        } catch (IOException e) {
            this.ioService.printFormattedLine("Couldn't find the questions.");
        } catch (RuntimeException e) {
            throw new QuestionReadException("CSV parse error: " + e.getMessage(), e);
        }

        return new ArrayList<>();
    }

    private CSVReader buildCSVReader(Reader reader, char separator, boolean ignoreQuotations, int skipLinesCount) {
        return new CSVReaderBuilder(reader)
            .withSkipLines(skipLinesCount)
            .withCSVParser(new CSVParserBuilder()
                .withSeparator(separator)
                .withIgnoreQuotations(ignoreQuotations)
                .build())
            .build();
    }

    private Reader openResourceReader(String resourceName) throws IOException {
        var resource = new ClassPathResource(resourceName);

        if (!resource.exists()) {
            throw new FileNotFoundException(
                "Resource not found in classpath: " + resourceName +
                    ". Put the file under src/main/resources and pass just the resource name (e.g., 'questions.csv')."
            );
        }
        return new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8);
    }
}
