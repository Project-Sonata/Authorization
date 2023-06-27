package com.odeyalo.sonata.authorization.testing.asserts;

import com.odeyalo.sonata.common.shared.ErrorDetails;
import org.assertj.core.api.AbstractAssert;

public class ErrorDetailsAssert extends AbstractAssert<ErrorDetailsAssert, ErrorDetails> {

    public ErrorDetailsAssert(ErrorDetails actual) {
        super(actual, ErrorDetailsAssert.class);
    }

    protected ErrorDetailsAssert(ErrorDetails errorDetails, Class<?> selfType) {
        super(errorDetails, selfType);
    }

    public static ErrorDetailsAssert from(ErrorDetails actual) {
        return new ErrorDetailsAssert(actual);
    }

    public ErrorDetailsAssert equalToNull() {
        isNull();
        return this;
    }

    public ErrorDetailsAssert codeEqualTo(String expectedCodeValue) {
        if (actual.getCode() == null && expectedCodeValue == null) {
            return this;
        }
        if (!actual.getCode().equals(expectedCodeValue)) {
            failWithMessage("Error codes are not equal! Expected: <%s>,\n actual: <%s>", expectedCodeValue, actual.getCode());
        }
        return this;
    }

    public ErrorDetailsAssert descriptionEqualTo(String expectedDescription) {
        if (actual.getDescription() == null && expectedDescription == null) {
            return this;
        }
        if (!actual.getDescription().equals(expectedDescription)) {
            failWithMessage("Error descriptions are not equal! Expected: <%s>,\n actual: <%s>", expectedDescription, actual.getDescription());
        }
        return this;
    }

    public ErrorDetailsAssert solutionEqualTo(String expectedSolution) {
        if (actual.getPossibleSolution() == null && expectedSolution == null) {
            return this;
        }
        if (!actual.getPossibleSolution().equals(expectedSolution)) {
            failWithMessage("Error solutions are not equal! Expected: <%s>,\n actual: <%s>", expectedSolution, actual.getPossibleSolution());
        }
        return this;
    }
}
