import org.example.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OrderingStrategyTest {
    private StringRecipe getRecipe() {
        StringInverter si = new StringInverter();
        StringCaseChanger cc = new StringCaseChanger();
        StringReplacer sr = new StringReplacer('A', 'X');
        List<StringTransformer> transformers = new ArrayList<>();
        transformers.add(si);
        transformers.add(cc);
        transformers.add(sr);
        StringRecipe recipe = new StringRecipe(transformers);
        return recipe;
    }



    @Test
    public void impatientStrategy() {
        StringBar stringBar = new StringBar();
        StringDrink drink = new StringDrink("AbCd-aBcD");
        StringRecipe recipe = getRecipe();
        ImpatientStrategy strategy = new ImpatientStrategy();
        HumanClient client = new HumanClient(strategy);
        // Recipe is ordered immediately
        client.wants(drink, recipe, stringBar);
        assertEquals("dCbX-DcBa", drink.getText());
    }

    @Test
    public void smartStrategyStartOpened() {
        StringBar stringBar = new StringBar();
        StringDrink drink = new StringDrink("AbCd-aBcD");
        StringRecipe recipe = getRecipe();
        SmartStrategy strategy = new SmartStrategy();
        HumanClient client = new HumanClient(strategy);

        stringBar.startHappyHour();
        client.wants(drink, recipe, stringBar);
        assertEquals("dCbX-DcBa", drink.getText());
    }

    @Test
    public void smartStrategyStartClosed() {
        StringBar stringBar = new StringBar();
        StringDrink drink = new StringDrink("AbCd-aBcD");
        StringDrink drink2 = new StringDrink("AbCd-aBcD");
        StringRecipe recipe = getRecipe();
        SmartStrategy strategy = new SmartStrategy();
        HumanClient client = new HumanClient(strategy);
        HumanClient client2 = new HumanClient(strategy);

        stringBar.addObserver(client); // este ?? bastante importante
        stringBar.addObserver(client2);

        client.wants(drink, recipe, stringBar);
        client2.wants(drink2, recipe, stringBar);

        assertEquals("AbCd-aBcD", drink.getText());
        assertEquals("AbCd-aBcD", drink2.getText());

        stringBar.startHappyHour();
        assertEquals("dCbX-DcBa", drink.getText());
        assertEquals("dCbX-DcBa", drink2.getText());
    }
}