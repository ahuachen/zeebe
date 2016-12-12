package org.camunda.tngp.msgpack.jsonpath;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class JsonPathQueryValidationTest
{
    @Parameters
    public static Iterable<Object[]> data()
    {
        return Arrays.asList(new Object[][] {
            { "$..", 1, "Unexpected json-path token RECURSION_OPERATOR" }, // currently not supported
            { "foo", 0, "Unexpected json-path token LITERAL" },
            { "$.foo.$", 6, "Unexpected json-path token ROOT_OBJECT" },
            { "$.[foo", 2, "Unexpected json-path token SUBSCRIPT_OPERATOR_BEGIN" }
        });
    }

    @Parameter(0)
    public String jsonPath;

    @Parameter(1)
    public int expectedInvalidPosition;

    @Parameter(2)
    public String expectedErrorMessage;

    @Test
    public void testCompileInvalidQuery()
    {
        // given
        final JsonPathQueryCompiler compiler = new JsonPathQueryCompiler();

        // when
        final JsonPathQuery jsonPathQuery = compiler.compile(jsonPath);

        // then
        assertThat(jsonPathQuery.isValid()).isFalse(); // as recursion is not yet supported
        assertThat(jsonPathQuery.getInvalidPosition()).isEqualTo(expectedInvalidPosition);
        assertThat(jsonPathQuery.getErrorReason()).isEqualTo(expectedErrorMessage);
    }

}
