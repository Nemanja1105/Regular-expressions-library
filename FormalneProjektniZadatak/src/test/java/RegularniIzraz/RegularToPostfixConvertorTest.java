package RegularniIzraz;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RegularToPostfixConvertorTest {

    @Test
    void convertToPostfixTest() {
        RegularExpression regex = new RegularExpression(new Character[]{'a', 'b'});
        regex.setRegex("a.b*.a");
        RegularToPostfixConvertor convertor = new RegularToPostfixConvertor(regex);
        assertEquals(true, "ab*.a.".equals(convertor.convertToPostfix()));

    }
}