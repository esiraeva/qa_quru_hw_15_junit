package guru.qa;

import com.codeborne.selenide.CollectionCondition;
import guru.qa.data.Locale;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.openqa.selenium.By;

import java.util.List;
import java.util.stream.Stream;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;


public class WebTest {

    @ValueSource(strings = {"royal canin", "whiskas"})
    @ParameterizedTest(name = "Проверка наличия названия искомого товара в описании карточек в результатах поиска {0}")
     void aliExpressSearch(String testData)
    {
        open("https://aliexpress.ru/");
        $("#searchInput").setValue(testData).pressEnter();
        $("div.product-snippet_ProductSnippet__name__1ettdy").shouldHave(text(testData));
    }

    @CsvSource(value = {
            "шапка, Бесплатная доставка",
            "шарф, Бесплатная доставка"
    })
    @ParameterizedTest(name = "Проверка фильтра Бесплатная доставка {0}")
    void aliExpressDelivery(String searchQuery, String expectedText) {
        open("https://aliexpress.ru/");
        $("#searchInput").setValue(searchQuery).pressEnter();
        $("div.SnowSearchFilter_SortPanel__view__1rp0i").click();
        $(By.xpath("//span[contains(text(), 'Бесплатная доставка')]")).click();
        $("div.product-snippet_ProductSnippet__name__1ettdy").shouldHave(text(searchQuery));
        $("div.snow-price_SnowPrice__blockFreeDelivery__18x8np").shouldHave(text(expectedText));
    }


    static Stream<Arguments> rtSiteButtonsTextDataProvider() {
        return Stream.of(
                //Arguments.of(List.of("Actualit?", "France", "International",  "Economie", "Tribunes", "Magazines", "Documentaires", "Vid?os", "RT360"), Locale.FR),
                Arguments.of(List.of("Aktuell", "Analyse", "Meinung", "Videos", "In eigener Sache", "Karriere"), Locale.DE)
        );
    }

    @MethodSource("rtSiteButtonsTextDataProvider")
    @ParameterizedTest(name = "Проверка отображения названия кнопок для локали: {1}")
    void rtSiteButtonsText(List<String> buttonsTexts, Locale locale) {
        open("https://www.rt.com/");
        $$(".langs a"  ).find(text(locale.name())).click();
        switchTo().window(1);
        $$(".MainMenu-root a").filter(visible)
                .shouldHave(CollectionCondition.texts(buttonsTexts));
    }

}
