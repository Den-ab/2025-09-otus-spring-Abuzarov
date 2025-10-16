package ru.otus.hw.dao;

import lombok.RequiredArgsConstructor;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.domain.Question;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class CsvQuestionDao implements QuestionDao {
    private final TestFileNameProvider fileNameProvider;

    @Override
    public List<Question> findAll() {
        // Использовать CsvToBean
        // https://opencsv.sourceforge.net/#collection_based_bean_fields_one_to_many_mappings
        // Использовать QuestionReadException
        // Про ресурсы: https://mkyong.com/java/java-read-a-file-from-resources-folder/

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
        InputStream is = Thread.currentThread()
            .getContextClassLoader()
            .getResourceAsStream(resourceName);

        if (is == null) {
            throw new FileNotFoundException(
                "Resource not found in classpath: " + resourceName +
                    ". Put the file under src/main/resources and pass just the resource name (e.g., 'questions.csv')."
            );
        }
        return new InputStreamReader(is, StandardCharsets.UTF_8);
    }
}
